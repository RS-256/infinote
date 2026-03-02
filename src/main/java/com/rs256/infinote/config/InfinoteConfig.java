package com.rs256.infinote.config;

import com.rs256.infinote.Infinote;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.rs256.infinote.compat.IdCompat;
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

    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    public static Map<String, BlockSoundConfig> BLOCK_SOUNDS = new HashMap<>();
    public static final Map<String, BlockSoundConfigCompiled> BLOCK_SOUNDS_COMPILED = new HashMap<>();

    public static final Path CONFIG_DIR = FabricLoader.getInstance().getConfigDir();
    public  static final Path CONFIG_PATH = CONFIG_DIR.resolve("infinote.json");

    public static void load() {

        Infinote.LOGGER.info("Infinote config path = {}", CONFIG_PATH.toAbsolutePath());

        try {
            if (Files.notExists(CONFIG_PATH)) {
                Infinote.LOGGER.info("could not found config. generating default...");

                createDefault();
                rebuildCache();
                return;
            } else {
                Infinote.LOGGER.info("Config found!");
            }

            Reader reader = Files.newBufferedReader(CONFIG_PATH);
            Type type = new TypeToken<Map<String, BlockSoundConfig>>(){}.getType();
            BLOCK_SOUNDS = GSON.fromJson(reader, type);
            reader.close();
            rebuildCache();

            if (BLOCK_SOUNDS == null) {
                throw new JsonParseException("[Infinote] Config not found.");
            }

        } catch (Exception e) {
            Infinote.LOGGER.warn("Config corrupted, backing up...");

            try {
                if (Files.exists(CONFIG_PATH)) {
                    createBackup(CONFIG_PATH);
                }

                createDefault();

            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }

    public static void createBackup(Path path) throws IOException {
        String ts = java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss"));
        Path backupPath = CONFIG_DIR.resolve("infinote_old_" + ts + ".json");
        try {
            Files.move(path, backupPath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    private static void createDefault() throws IOException {
        Map<String, BlockSoundConfig> defaultMap = new HashMap<>();

        String defaultBlockId = "minecraft:air";
        BlockSoundConfig defaultConfig = new BlockSoundConfig();
        defaultConfig.sound = "minecraft:block.note_block.harp";
        defaultConfig.category = RECORDS;
        defaultConfig.pitchShift = 0;
        defaultConfig.volume = 3;

        defaultMap.put(defaultBlockId, defaultConfig);

        try (Writer writer = Files.newBufferedWriter(CONFIG_PATH)) {
            GSON.toJson(defaultMap, writer);
        }
    }

    public static void save() {
        try (Writer writer = Files.newBufferedWriter(CONFIG_PATH)) {
            GSON.toJson(BLOCK_SOUNDS, writer);
            Infinote.LOGGER.info("Config saved successfully.");
        } catch (Exception e) {
            Infinote.LOGGER.error("Failed to save config!", e);
        }
    }

    public static void remove(String blockId) {
        BLOCK_SOUNDS.remove(blockId);
        save();
        rebuildCache();
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

    public static boolean isInConfig(String blockId) {
        return BLOCK_SOUNDS.containsKey(blockId);
    }

    public static void rebuildCache() {
        BLOCK_SOUNDS_COMPILED.clear();

        for (var entry : BLOCK_SOUNDS.entrySet()) {
            String blockKeyRaw = entry.getKey();
            BlockSoundConfig config = entry.getValue();
            if (config == null) continue;

            String blockKey = IdCompat.normalize(blockKeyRaw);
            String soundKey = IdCompat.normalize(config.sound);

            if (blockKey == null || soundKey == null) {
                Infinote.LOGGER.warn("Invalid config entry: block={}, sound={}", blockKeyRaw, config.sound);
                continue;
            }

            SoundSource category;
            try {
                category = SoundSource.valueOf(config.category.toString().toUpperCase()); // 例
            } catch (Exception e) {
                Infinote.LOGGER.warn("Invalid category: {} (block={})", config.category, blockKeyRaw);
                continue;
            }

            float pitchShift = config.pitchShift;
            float volume = config.volume;

            BLOCK_SOUNDS_COMPILED.put(blockKey, new BlockSoundConfigCompiled(blockKey, soundKey, category, pitchShift, volume));
        }

        Infinote.LOGGER.info("Compiled {} entries (raw={})", BLOCK_SOUNDS_COMPILED.size(), BLOCK_SOUNDS.size());
    }
}

