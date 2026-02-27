package com.rs256.infinote.client;

import com.rs256.infinote.Infinote;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.util.RandomSource;

public class InfinoteClient implements ClientModInitializer {

    private static final RandomSource RNG = RandomSource.create();

    @Override
    public void onInitializeClient() {
    }
}
