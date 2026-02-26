package com.rs256.infinote.compat;


import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.block.Block;


public final class RegistryCompat {
    private RegistryCompat() {}

    /** Block -> "namespace:path"（常にStringで返す） */
    public static String blockIdString(Block block) {
        ResourceLocation id = BuiltInRegistries.BLOCK.getKey(block);
        return id.toString();
    }

    /** Sound id が「登録済み（=バニラ/既存）」かどうか */
    public static boolean isSoundEventRegistered(String soundIdString) {
        String normalized = IdCompat.normalize(soundIdString);
        if (normalized == null) return false;

        ResourceLocation id = IdCompat.idFromString(normalized);
        if (id == null) return false;
        return BuiltInRegistries.SOUND_EVENT.containsKey(id);
    }

    public static SoundEvent getRegisteredSoundEvent(String soundKeyRaw) {
        String key = IdCompat.normalize(soundKeyRaw);
        if (key == null) {
            return null;
        }

        ResourceLocation id = IdCompat.idFromString(key);
        if (id == null) {
            return null;
        }
        if (!BuiltInRegistries.SOUND_EVENT.containsKey(id)) {
            return null;
        }
        return BuiltInRegistries.SOUND_EVENT.get(id);


//        Identifier id = IdCompat.idFromString(key);
//        if (id == null) return null;
//        if (!Registries.SOUND_EVENT.containsId(id)) return null;
//        return Registries.SOUND_EVENT.get(id);
    }
}
