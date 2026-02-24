package com.rs256.infinote.client;

import com.rs256.infinote.network.NetworkCompat;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.util.RandomSource;

public class InfinoteClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        NetworkCompat.registerClient();
    }

    public static void playClientSound(ResourceLocation soundId,
                                       SoundSource category,
                                       float volume,
                                       float pitch,
                                       BlockPos pos) {

        Minecraft mc = Minecraft.getInstance();

        if (mc.level == null) return;

        SimpleSoundInstance sound = new SimpleSoundInstance(
                soundId,
                category,
                volume,
                pitch,
                RandomSource.create(),
                false,
                0,
                SoundInstance.Attenuation.LINEAR,
                pos.getX() + 0.5,
                pos.getY() + 0.5,
                pos.getZ() + 0.5,
                false
        );

        mc.getSoundManager().play(sound);
    }
}
