package com.rs256.infinote.config;

import com.rs256.infinote.Infinote;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rs256.infinote.compat.IdCompat;
import net.fabricmc.loader.api.FabricLoader;

import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.rs256.infinote.config.InfinoteConfig.*;
import static net.minecraft.sounds.SoundSource.RECORDS;

public class ImportConfig {
    private static final class NotebetterFabricRoot {
        List<NotebetterFabricMapping> mappings;
    }

    private static final class NotebetterRoot {
        List<NotebetterBlock> blocks;
    }

    private static final class NotebetterFabricMapping {
        String block;
        NotebetterFabricSound sound;
    }

    private static final class NotebetterBlock {
        String block;
        NotebetterSound sound;
    }

    private static final class NotebetterFabricSound {
        String sound;
        double volume;
        double pitch;
    }

    private static final class NotebetterSound {
        String name;
        double volume;
    }

    public static int fromNotebetterfabric(String sourceFileName) {
        Path source = CONFIG_DIR.resolve(sourceFileName);

        if (!Files.exists(source)) {
            Infinote.LOGGER.warn("Import file not found: {}", source);
            return 0;
        }

        Map<String, BlockSoundConfig> imported = new LinkedHashMap<>();
        int importedCount = 0;

        try (Reader r = Files.newBufferedReader(source)) {
            Gson gson = new Gson();
            NotebetterFabricRoot root = gson.fromJson(r, NotebetterFabricRoot.class);
            if (root == null || root.mappings == null) {
                Infinote.LOGGER.warn("Import file has no mappings: {}", source);
                return 0;
            }

            for (NotebetterFabricMapping m : root.mappings) {
                if (m == null || m.block == null || m.sound == null || m.sound.sound == null) {
                    continue;
                }

                String blockId = IdCompat.normalize(m.block);
                String soundId = IdCompat.normalize(m.sound.sound);
                if (blockId == null || soundId == null) {
                    continue;
                }

                BlockSoundConfig config = new BlockSoundConfig();
                config.sound = soundId;
                config.category = RECORDS;
                config.volume = (float) m.sound.volume;

                double p = m.sound.pitch;
                if (p <= 0.0) {
                    config.pitchShift = 0.0f;
                } else {
                    config.pitchShift = (float) (12.0 * (Math.log(p) / Math.log(2.0)));
                }

                imported.put(blockId, config);
                importedCount++;
            }

        } catch (Exception e) {
            Infinote.LOGGER.error("Failed to read import file: {}", source, e);
            return 0;
        }

        return replaceConfig(sourceFileName, imported, importedCount);
    }

    public static int fromNotebetter(String sourceFileName) {
        Path source = CONFIG_DIR.resolve(sourceFileName);

        if (!Files.exists(source)) {
            Infinote.LOGGER.warn("Import file not found: {}", source);
            return 0;
        }

        Map<String, BlockSoundConfig> imported = new LinkedHashMap<>();
        int importedCount = 0;

        try (Reader r = Files.newBufferedReader(source)) {
            Gson gson = new Gson();
            NotebetterRoot root = gson.fromJson(r, NotebetterRoot.class);
            if (root == null || root.blocks == null) {
                Infinote.LOGGER.warn("Import file has no blocks: {}", source);
                return 0;
            }

            for (NotebetterBlock block : root.blocks) {
                if (block == null || block.block == null || block.sound == null || block.sound.name == null) {
                    continue;
                }

                String blockId = IdCompat.normalize(block.block);
                String soundId = IdCompat.normalize(block.sound.name);
                if (blockId == null || soundId == null) {
                    continue;
                }

                BlockSoundConfig config = new BlockSoundConfig();
                config.sound = soundId;
                config.category = RECORDS;
                config.pitchShift = 0.0f;
                config.volume = (float) block.sound.volume;

                imported.put(blockId, config);
                importedCount++;
            }

        } catch (Exception e) {
            Infinote.LOGGER.error("Failed to read import file: {}", source, e);
            return 0;
        }

        return replaceConfig(sourceFileName, imported, importedCount);
    }

    private static int replaceConfig(String sourceFileName, Map<String, BlockSoundConfig> imported, int importedCount) {
        if (importedCount == 0) {
            Infinote.LOGGER.warn("Import file has zero valid entries: {}", sourceFileName);
            return 0;
        }

        try {
            if (Files.exists(CONFIG_PATH)) {
                createBackup(CONFIG_PATH);
            }

            Gson gsonPretty = new GsonBuilder().setPrettyPrinting().create();
            try (Writer w = Files.newBufferedWriter(CONFIG_PATH)) {
                gsonPretty.toJson(imported, w);
            }

            BLOCK_SOUNDS.clear();
            BLOCK_SOUNDS.putAll(imported);

            InfinoteConfig.load();

            Infinote.LOGGER.info("Imported {} mappings from {} and replaced infinote.json (backup: infinote_old.json)", importedCount, sourceFileName);

            return importedCount;

        } catch (Exception e) {
            Infinote.LOGGER.error("Failed to replace infinote.json during import", e);
            return 0;
        }
    }
}
