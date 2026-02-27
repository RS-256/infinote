package com.rs256.infinote.compat;

//? if <1.21 {
import net.minecraft.resources.ResourceLocation;
//?} else
//import net.minecraft.resources.Identifier;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.block.Block;


public final class RegistryCompat {
    private RegistryCompat() {}

    /** Block -> "namespace:path"（常にStringで返す） */
    public static String blockIdString(Block block) {
        //? if <1.21 {
        return BuiltInRegistries.BLOCK.getKey(block).toString();
        //?} else
        //return BuiltInRegistries.BLOCK.getKey(block).toString();
    }

    /** Sound id が「登録済み（=バニラ/既存）」かどうか */
    public static boolean isSoundEventRegistered(String soundIdString) {
        String normalized = IdCompat.normalize(soundIdString);
        if (normalized == null) {
            return false;
        }

        //? if <1.21 {
        ResourceLocation id = IdCompat.idFromString(normalized);
        //?} else
        //Identifier id = IdCompat.idFromString(normalized);

        if (id == null) {
            return false;
        }
        return BuiltInRegistries.SOUND_EVENT.containsKey(id);

    }

    public static SoundEvent getRegisteredSoundEvent(String soundKeyRaw) {
        String key = IdCompat.normalize(soundKeyRaw);
        if (key == null) {
            return null;
        }

        //? if <1.21 {
        ResourceLocation id = IdCompat.idFromString(key);
        if (id == null) {
            return null;
        }
        if (!BuiltInRegistries.SOUND_EVENT.containsKey(id)) {
            return null;
        }
        return BuiltInRegistries.SOUND_EVENT.get(id);
        //?} else {
        /*Identifier id = IdCompat.idFromString(key);
        if (id == null) {
            return null;
        }
        if (!BuiltInRegistries.SOUND_EVENT.containsKey(id)) {
            return null;
        }
        return BuiltInRegistries.SOUND_EVENT.getValue(id);
        *///?}
    }
}
