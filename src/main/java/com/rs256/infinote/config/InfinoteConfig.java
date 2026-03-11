package com.rs256.infinote.config;

import com.rs256.infinote.Infinote;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.rs256.infinote.compat.IdCompat;
import com.rs256.infinote.compat.JsonCompat;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.sounds.SoundSource;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;

import static net.minecraft.sounds.SoundSource.RECORDS;

public class InfinoteConfig {

    public static final int CURRENT_SCHEMA = 1;
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static Map<String, BlockSoundConfig> BLOCK_SOUNDS = new HashMap<>();
    public static final Map<String, BlockSoundConfigCompiled> BLOCK_SOUNDS_COMPILED = new HashMap<>();
    public static final Path CONFIG_DIR = FabricLoader.getInstance().getConfigDir();
    public static final Path CONFIG_PATH = CONFIG_DIR.resolve("infinote.json");

    public static void load() {
        Infinote.LOGGER.info("Infinote config path = {}", CONFIG_PATH.toAbsolutePath());

        if (Files.notExists(CONFIG_PATH)) {
            Infinote.LOGGER.info("Config not found. Generating default...");
            generateDefaultConfig();
            return;
        }

        Infinote.LOGGER.info("Config found. Loading...");

        try {
            readConfigFile();
            LoadReport report = rebuildCache();
            Infinote.LOGGER.info("Config: {} entries, compiled {}, badBlock {}, badSound {}, badCat {}",
                    report.rawTotal(), report.compiledOk(), report.invalidBlockId(), report.invalidSoundId(), report.invalidCategory());
        } catch (Exception e) {
            Infinote.LOGGER.warn("Config corrupted or unreadable: {}. Backing up and regenerating...", e.getMessage());
            recoverFromCorruptConfig();
        }
    }

    public static void save() {
        try (Writer writer = Files.newBufferedWriter(CONFIG_PATH)) {
            GSON.toJson(buildJsonObject(), writer);
            Infinote.LOGGER.info("Config saved successfully.");
        } catch (Exception e) {
            Infinote.LOGGER.error("Failed to save config!", e);
        }
    }

    public static void add(String blockId, String soundId, SoundSource soundCategory, float pitchShift, float volume) {
        BlockSoundConfig config = new BlockSoundConfig();
        config.sound = soundId;
        config.category = soundCategory;
        config.pitchShift = pitchShift;
        config.volume = volume;

        BLOCK_SOUNDS.put(blockId, config);
        save();
        rebuildCache();
    }

    public static void remove(String blockId) {
        BLOCK_SOUNDS.remove(blockId);
        save();
        rebuildCache();
    }

    public static boolean isInConfig(String blockId) {
        return BLOCK_SOUNDS.containsKey(blockId);
    }

    /**
     * BLOCK_SOUNDS からコンパイル済みキャッシュを再構築
     */
    public static LoadReport rebuildCache() {
        BLOCK_SOUNDS_COMPILED.clear();

        int compiledOk = 0;
        int invalidBlockId = 0;
        int invalidSoundId = 0;
        int invalidCategory = 0;

        for (Map.Entry<String, BlockSoundConfig> entry : BLOCK_SOUNDS.entrySet()) {
            String rawBlockKey = entry.getKey();
            BlockSoundConfig config = entry.getValue();
            if (config == null) {
                invalidBlockId++;
                continue;
            }

            String blockKey = IdCompat.normalize(rawBlockKey);
            if (blockKey == null) {
                Infinote.LOGGER.warn("Skipping invalid block id: {}", rawBlockKey);
                invalidBlockId++;
                continue;
            }

            String soundKey = IdCompat.normalize(config.sound);
            if (soundKey == null) {
                Infinote.LOGGER.warn("Skipping invalid sound id: {} (block={})", config.sound, rawBlockKey);
                invalidSoundId++;
                continue;
            }

            SoundSource category = config.category;
            if (category == null) {
                Infinote.LOGGER.warn("Skipping null category (block={})", rawBlockKey);
                invalidCategory++;
                continue;
            }

            BLOCK_SOUNDS_COMPILED.put(blockKey, new BlockSoundConfigCompiled(blockKey, soundKey, category, config.pitchShift, config.volume));
            compiledOk++;
        }

        return new LoadReport(BLOCK_SOUNDS.size(), compiledOk, invalidBlockId, invalidSoundId, invalidCategory);
    }

    private static void readConfigFile() throws IOException {
        try (Reader reader = Files.newBufferedReader(CONFIG_PATH)) {
            JsonElement root = JsonCompat.read(reader);

            if (!root.isJsonObject()) {
                throw new JsonParseException("Config root is not a JSON object.");
            }
            JsonObject rootObject = root.getAsJsonObject();

            // schema キーがない -> old = schema 0
            int schema = rootObject.has("schema")
                    ? rootObject.get("schema").getAsInt()
                    : 0;

            if (schema > CURRENT_SCHEMA) {
                throw new JsonParseException("Config schema " + schema + " is newer than supported (" + CURRENT_SCHEMA + "). Please update the mod.");
            }

            if (schema < CURRENT_SCHEMA) {
                Infinote.LOGGER.warn("Config schema {} is outdated (current: {}). Migrating...", schema, CURRENT_SCHEMA);
                rootObject = migrateToCurrentSchema(schema, rootObject);
                applyCurrentFormat(rootObject);
                // マイグレーション後の内容を即保存
                save();
                return;
            }
            // schema == CURRENT_SCHEMA
            applyCurrentFormat(rootObject);
        }
    }

    /**
     * schemaかわったらmigration書く、case増やすだけ
     */
    private static JsonObject migrateToCurrentSchema(int fromSchema, JsonObject rootObject) throws IOException {
        int schema = fromSchema;
        while (schema < CURRENT_SCHEMA) {
            Infinote.LOGGER.info("Applying migration: schema {} -> {}", schema, schema + 1);
            rootObject = applyMigrationStep(schema, rootObject);
            schema++;
        }
        return rootObject;
    }

    private static JsonObject applyMigrationStep(int from, JsonObject rootObject) {
        if (from == 0) {
            // schema なし -> 1: ルート直下のマッピングをmappingsに
            JsonObject migrated = new JsonObject();
            migrated.addProperty("schema", 1);
            migrated.add("mappings", rootObject);
            return migrated;
        } else {
            // schema 1 -> 2; light_level?
            throw new JsonParseException("No migration step defined from schema " + from + ".");
        }
    }

    /** schema: 1 の現行フォーマットをパースする */
    private static void applyCurrentFormat(JsonObject rootObject) {
        // mappings
        if (!rootObject.has("mappings")) {
            throw new JsonParseException("schema 1: missing 'mappings' field.");
        }
        Type type = new TypeToken<Map<String, BlockSoundConfig>>() {
        }.getType();
        Map<String, BlockSoundConfig> parsed = GSON.fromJson(rootObject.get("mappings"), type);
        if (parsed == null) {
            throw new JsonParseException("schema 1: 'mappings' could not be parsed.");
        }
        BLOCK_SOUNDS = parsed;
    }

    private static void generateDefaultConfig() {
        try {
            writeDefaultConfigFile();
            rebuildCache();
        } catch (IOException e) {
            Infinote.LOGGER.error("Failed to generate default config!", e);
        }
    }

    private static void recoverFromCorruptConfig() {
        try {
            if (Files.exists(CONFIG_PATH)) {
                createBackup(CONFIG_PATH);
            }
            writeDefaultConfigFile();
            rebuildCache();
        } catch (IOException e) {
            Infinote.LOGGER.error("Failed to recover from corrupt config!", e);
        }
    }

    private static void writeDefaultConfigFile() throws IOException {
        BlockSoundConfig defaultConfig = new BlockSoundConfig();
        defaultConfig.sound = "minecraft:block.note_block.harp";
        defaultConfig.category = RECORDS;
        defaultConfig.pitchShift = 0;
        defaultConfig.volume = 3;

        BLOCK_SOUNDS = new HashMap<>();
        BLOCK_SOUNDS.put("minecraft:air", defaultConfig);

        try (Writer writer = Files.newBufferedWriter(CONFIG_PATH)) {
            GSON.toJson(buildJsonObject(), writer);
        }
    }

    /** { "schema": N, "mappings": { ... } } のJsonObjectをビルド */
    private static JsonObject buildJsonObject() {
        JsonObject root = new JsonObject();
        root.addProperty("schema", CURRENT_SCHEMA);
        root.add("mappings", GSON.toJsonTree(BLOCK_SOUNDS));
        return root;
    }

    public static void createBackup(Path path) throws IOException {
        String timestamp = java.time.LocalDateTime.now()
                .format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss"));
        Path backupPath = CONFIG_DIR.resolve("infinote_old_" + timestamp + ".json");
        Files.move(path, backupPath, StandardCopyOption.REPLACE_EXISTING);
        Infinote.LOGGER.info("Backed up config to {}", backupPath.getFileName());
    }
}