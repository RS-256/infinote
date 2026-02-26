package com.rs256.infinote.config;

import net.minecraft.sounds.SoundSource;

public final class BlockSoundConfigCompiled {
    public final String block;      // "minecraft:diamond_block"
    public final String sound;      // 正規化済み "namespace:path"
    public final SoundSource category; // enum
    public final float pitchShift;
    public final float volume;

    public BlockSoundConfigCompiled(String block, String sound, SoundSource category, float pitchShift, float volume) {
        this.block = block;
        this.sound = sound;
        this.category = category;
        this.pitchShift = pitchShift;
        this.volume = volume;
    }
}
