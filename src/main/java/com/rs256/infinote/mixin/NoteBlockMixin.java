package com.rs256.infinote.mixin;

import com.rs256.infinote.compat.NoteBlockCompat;
import com.rs256.infinote.compat.RegistryCompat;
import com.rs256.infinote.compat.SoundCompat;
import com.rs256.infinote.config.BlockSoundConfigCompiled;
import com.rs256.infinote.config.InfinoteConfig;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.NoteBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.rs256.infinote.Infinote.LOGGER;

@Mixin(NoteBlock.class)
public abstract class NoteBlockMixin {
    @Inject(method = "playNote", at = @At("HEAD"), cancellable = true)
    //? if <=1.18.2 {
    /*private void infinote$onPlayNote(Level level, BlockPos blockPos, CallbackInfo ci) {
        infinote$handlePlayNote(null, level.getBlockState(blockPos), level, blockPos, ci);
    }
    *///?} else if <=1.19.2 {
    /*private void infinote$onPlayNote(Entity entity, Level level, BlockPos blockPos, CallbackInfo ci) {
        infinote$handlePlayNote(entity, level.getBlockState(blockPos), level, blockPos, ci);
    }
    *///?} else {
    private void infinote$onPlayNote(Entity entity, BlockState state, Level level, BlockPos blockPos, CallbackInfo ci) {
        infinote$handlePlayNote(entity, state, level, blockPos, ci);
    }
    //?}

    @Unique
    private void infinote$handlePlayNote(Entity entity, BlockState state, Level level, BlockPos blockPos, CallbackInfo ci) {
        // for mob head
        if (NoteBlockCompat.worksAboveNoteBlock(state)) {
            return;
        }

        BlockState aboveState = level.getBlockState(blockPos.above(1));
        boolean airAbove = aboveState.isAir();
        // bypass; must after mob head
        boolean bypassed = !airAbove && InfinoteConfig.BYPASS_BLOCKS_COMPILED.contains(RegistryCompat.getKey(aboveState.getBlock()));

        if (!airAbove && !bypassed) {
            // should vanilla sound
            return;
        }

        BlockPos belowPos = blockPos.below(1);

        String belowBlock = RegistryCompat.getKey(level.getBlockState(belowPos).getBlock());
        BlockSoundConfigCompiled config = InfinoteConfig.BLOCK_SOUNDS_COMPILED.get(belowBlock);

        if (config == null) {
            if (!bypassed) {
                return;
            }
            // vanilla event
            level.blockEvent(blockPos, (Block) (Object) this, 0, 0);

            ci.cancel();
            return;
        }

        SoundEvent soundEvent = SoundCompat.soundEventFromString(config.sound);

        if (soundEvent == null) {
            LOGGER.warn("cant cast!: {}, ignored", config.sound);
            return;
        }

        infinote$playNote(level, blockPos, soundEvent, config, state.getValue(NoteBlock.NOTE));

        if (!bypassed) {
            NoteBlockCompat.notePlayedGameEvent(level, entity, blockPos);
        }

        ci.cancel();
    }

    @Unique
    private static void infinote$playNote(Level level, BlockPos blockPos, SoundEvent soundEvent, BlockSoundConfigCompiled config, int note) {
        float shiftedNote = note + config.pitchShift;
        float pitch = (float) Math.pow(2.0D, (shiftedNote - 12) / 12.0D);
        ServerLevel serverLevel = (ServerLevel) level;

        level.playSound(null, blockPos, soundEvent, config.category, config.volume, pitch);

        double d = (double) note / 24.0D;
        serverLevel.sendParticles(ParticleTypes.NOTE, blockPos.getX() + 0.5D, blockPos.getY() + 1.2D, blockPos.getZ() + 0.5D, 0, d, 0.0D, 0.0D, 1.0D);
    }
}