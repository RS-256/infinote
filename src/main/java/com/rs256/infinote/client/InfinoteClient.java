package com.rs256.infinote.client;

import com.rs256.infinote.Infinote;
import com.rs256.infinote.compat.IdCompat;
import com.rs256.infinote.compat.NetworkCompat;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.util.RandomSource;

public class InfinoteClient implements ClientModInitializer {

    private static final RandomSource RNG = RandomSource.create();

    @Override
    public void onInitializeClient() {
        NetworkCompat.registerClient();
    }

    public static void playClientSound(String soundId, SoundSource category, float pitch, float volume, BlockPos pos) {

        Minecraft mc = Minecraft.getInstance();

        if (mc.level == null) {
            return;
        }
        if (IdCompat.idFromString(soundId) == null) {
            Infinote.LOGGER.warn("Invalid sound id (client): {}", soundId);
            return;
        }

        SimpleSoundInstance sound = new SimpleSoundInstance(
                IdCompat.idFromString(soundId),
                category,
                volume,
                pitch,
                RNG,
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
