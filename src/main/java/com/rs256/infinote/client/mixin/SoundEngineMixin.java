package com.rs256.infinote.client.mixin;

import com.rs256.infinote.Infinote;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundEngine;
import net.minecraft.util.Mth;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(SoundEngine.class)
public abstract class SoundEngineMixin {

    @Redirect(
            method = "play(Lnet/minecraft/client/resources/sounds/SoundInstance;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/sounds/SoundEngine;calculatePitch(Lnet/minecraft/client/resources/sounds/SoundInstance;)F"
            )
    )
    private float infinote_unclamp(SoundEngine instance, SoundInstance soundInstance) {
        float pitch = soundInstance.getPitch();
        return Mth.clamp(pitch, 0.03125f, 32.0f);
    }
}