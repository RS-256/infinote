package com.rs256.infinote.compat;

import net.minecraft.sounds.SoundEvent;

public class SoundCompat {
    /**
     * create soundEvent from String id, the value is null if invalid
     */
    //? if <=1.19.2 {
    /*public static SoundEvent soundEventFromString(String raw) {
        return IdCompat.idFromString(raw) == null ? null : new SoundEvent(IdCompat.idFromString(raw));
    }
    *///?} else {
    public static SoundEvent soundEventFromString(String raw) {
        var id = IdCompat.idFromString(raw);
        return id == null ? null : SoundEvent.createVariableRangeEvent(id);
    }
    //?}
}
