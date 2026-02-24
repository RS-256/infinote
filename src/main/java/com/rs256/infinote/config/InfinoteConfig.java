package com.rs256.infinote.config;

import com.ibm.icu.impl.UResource;
import com.rs256.infinote.Infinote;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resources.ResourceLocation;
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

public class InfinoteConfig {

    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    public static Map<String, BlockSoundConfig> BLOCK_SOUNDS = new HashMap<>();

    public static void load() {
        Path configDir = FabricLoader.getInstance().getConfigDir();
        Path path = configDir.resolve("infinote.json");
        Path backupPath = configDir.resolve("infinote_old.json");

        Infinote.LOGGER.info("Infinote config path = {}", path.toAbsolutePath());

        try {
            if (Files.notExists(path)) {
                Infinote.LOGGER.info("could not found config. generating default...");

                createDefault(path);
                return;
            } else {
                Infinote.LOGGER.info("Config found!");
            }

            Reader reader = Files.newBufferedReader(path);
            Type type = new TypeToken<Map<String, BlockSoundConfig>>(){}.getType();
            BLOCK_SOUNDS = GSON.fromJson(reader, type);
            reader.close();

            if (BLOCK_SOUNDS == null) {
                throw new JsonParseException("[Infinote] Config not found.");
            }

        } catch (Exception e) {
            Infinote.LOGGER.warn("Config corrupted, backing up...");

            try {
                if (Files.exists(path)) {
                    Files.move(path, backupPath, StandardCopyOption.REPLACE_EXISTING);
                }

                createDefault(path);

            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }

    private static void createDefault(Path path) throws IOException {
        Map<String, BlockSoundConfig> emptyMap = new HashMap<>();

        try (Writer writer = Files.newBufferedWriter(path)) {
            GSON.toJson(emptyMap, writer);
        }
    }
    public static void save() {
        Path path = FabricLoader.getInstance()
                .getConfigDir()
                .resolve("infinote.json");

        try (Writer writer = Files.newBufferedWriter(path)) {
            GSON.toJson(BLOCK_SOUNDS, writer);
            Infinote.LOGGER.info("Config saved successfully.");
        } catch (Exception e) {
            Infinote.LOGGER.error("Failed to save config!", e);
        }
    }

    public static void remove(String blockId) {
        BLOCK_SOUNDS.remove(blockId);
        Infinote.LOGGER.info("smth removed in infinote conf");
        save();
    }

    public static void add(ResourceLocation blockId, ResourceLocation soundId, SoundSource soundCategory, float pitch_shift, float volume) {
        BlockSoundConfig config = new BlockSoundConfig();
        config.sound = soundId.toString();
        config.category = soundCategory;
        config.pitch_shift = pitch_shift;
        config.volume = volume;

        BLOCK_SOUNDS.put(blockId.toString(), config);

        Infinote.LOGGER.info("smth added in infinote conf");
        save();
    }

    public static boolean isInConfig(ResourceLocation blockId) {
        return BLOCK_SOUNDS.containsKey(blockId.toString());
    }
}

