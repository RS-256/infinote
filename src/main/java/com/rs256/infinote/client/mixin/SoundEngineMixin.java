package com.rs256.infinote.client.mixin;

import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundEngine;
import net.minecraft.util.Mth;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SoundEngine.class)
public abstract class SoundEngineMixin {

    @Inject(
            method = "calculatePitch",
            at = @At("HEAD"),
            cancellable = true
    )
    private void infinote_unclampPitch(SoundInstance soundInstance, CallbackInfoReturnable<Float> cir) {
        cir.setReturnValue(Mth.clamp(soundInstance.getPitch(), 0.0625f, 16.0f));
    }
}