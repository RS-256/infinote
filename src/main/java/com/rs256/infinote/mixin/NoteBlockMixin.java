package com.rs256.infinote.mixin;

import com.rs256.infinote.compat.IdCompat;
import com.rs256.infinote.compat.RegistryCompat;
import com.rs256.infinote.config.BlockSoundConfigCompiled;
import com.rs256.infinote.config.InfinoteConfig;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
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
    //? if <=1.19.2 {
    /*private void infinote_onPlayNote(Entity entity, Level level, BlockPos blockPos, CallbackInfo ci) {
        int note = level.getBlockState(blockPos).getValue(NoteBlock.NOTE);

        BlockPos belowPos = blockPos.below(1);

        String belowBlock = RegistryCompat.getKey(level.getBlockState(belowPos).getBlock());
        BlockSoundConfigCompiled c = InfinoteConfig.BLOCK_SOUNDS_COMPILED.get(belowBlock);

        if (c != null) {
            var soundId = IdCompat.idFromString(c.sound);

            if (soundId == null) {
                LOGGER.warn("cant cast!: {}, ignored", c.sound);
                return;
            }

            SoundEvent soundEvent = new SoundEvent(soundId);

            float shiftedNote = note + c.pitchShift;
            float pitch = (float) Math.pow(2.0D, (shiftedNote - 12) / 12.0D);
            ServerLevel serverWorld = (ServerLevel) level;

            level.playSound(null, blockPos, soundEvent, c.category, c.volume, pitch);

            double d = (double) note / 24.0D;
            serverWorld.sendParticles(ParticleTypes.NOTE, blockPos.getX() + 0.5D, blockPos.getY() + 1.2D, blockPos.getZ() + 0.5D, 0, d, 0.0D, 0.0D, 1.0D);

            ci.cancel();
        }
    *///?} else {
    private void infinote_onPlayNote(Entity entity, BlockState state, Level world, BlockPos pos, CallbackInfo ci) {
        int note = state.getValue(NoteBlock.NOTE);

        BlockPos belowPos = pos.below(1);

        String belowBlock = RegistryCompat.getKey(world.getBlockState(belowPos).getBlock());
        BlockSoundConfigCompiled c = InfinoteConfig.BLOCK_SOUNDS_COMPILED.get(belowBlock);

        if (c != null) {
            var soundId = IdCompat.idFromString(c.sound);

            if (soundId == null) {
                LOGGER.warn("cant cast!: {}, ignored", c.sound);
                return;
            }

            SoundEvent soundEvent = SoundEvent.createVariableRangeEvent(soundId);

            float shiftedNote = note + c.pitchShift;
            float pitch = (float) Math.pow(2.0D, (shiftedNote - 12) / 12.0D);
            ServerLevel serverWorld = (ServerLevel) world;

            world.playSound(null, pos, soundEvent, c.category, c.volume, pitch);

            double d = (double) note / 24.0D;
            serverWorld.sendParticles(ParticleTypes.NOTE, pos.getX() + 0.5D, pos.getY() + 1.2D, pos.getZ() + 0.5D, 0, d, 0.0D, 0.0D, 1.0D);

            ci.cancel();
        }
     //?}
    }
}