package com.rs256.infinote.mixin;

import com.rs256.infinote.compat.IdCompat;
import com.rs256.infinote.compat.RegistryCompat;
import com.rs256.infinote.config.BlockSoundConfigCompiled;
import com.rs256.infinote.config.InfinoteConfig;

import com.rs256.infinote.compat.NetworkCompat;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.NoteBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.rs256.infinote.Infinote.LOGGER;

@Mixin(NoteBlock.class)
public class NoteBlockMixin {
    @Inject(method = "playNote", at = @At("HEAD"), cancellable = true)
    private void infinote_onPlayNote(Entity entity, BlockState state, Level world, BlockPos pos, CallbackInfo ci) {

        int note = state.getValue(NoteBlock.NOTE);

        BlockPos belowPos = pos.below(1);

        String belowBlock = RegistryCompat.blockIdString(world.getBlockState(belowPos).getBlock()); // これも後でcompat化
        BlockSoundConfigCompiled c = InfinoteConfig.BLOCK_SOUNDS_COMPILED.get(belowBlock);

        if (c != null) {
            var soundId = IdCompat.idFromString(c.sound);

            if (soundId == null) {
                LOGGER.warn("cant cast!: {}, ignored", c.sound);
                return;
            }

            if (RegistryCompat.isSoundEventRegistered(c.sound)) {
                SoundEvent soundEvent = RegistryCompat.getRegisteredSoundEvent(c.sound);
                float shiftedNote = note + c.pitchShift;
                float pitch = (float) Math.pow(2.0D, (shiftedNote - 12) / 12.0D);

                world.playSound(null, pos, soundEvent, c.category, c.volume, pitch);
            } else {
                float shiftedNote = note + c.pitchShift;
                float pitch = (float) Math.pow(2.0D, (shiftedNote - 12) / 12.0D);
                ServerLevel serverWorld = (ServerLevel) world;

                for (ServerPlayer p : serverWorld.players()) {
                    if (p.blockPosition().closerThan(pos, c.volume * 16)) {
                        NetworkCompat.sendPlayCustomSound(p, c.sound, c.category, pitch, c.volume, pos);
                    }
                }
            }

            if (world instanceof ServerLevel serverWorld) {
                double d = (double) note / 24.0D;
                serverWorld.sendParticles(ParticleTypes.NOTE, pos.getX() + 0.5D, pos.getY() + 1.2D, pos.getZ() + 0.5D, 0, d, 0.0D, 0.0D, 1.0D);
            }

            ci.cancel();
        }
    }
}