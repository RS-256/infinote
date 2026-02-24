package com.rs256.infinote.mixin;

import com.rs256.infinote.Infinote;
import com.rs256.infinote.config.BlockSoundConfig;
import com.rs256.infinote.config.InfinoteConfig;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
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

import static com.rs256.infinote.Infinote.PLAY_CUSTOM_SOUND;

@Mixin(NoteBlock.class)
public class NoteBlockMixin {
    @Inject(method = "playNote", at = @At("HEAD"), cancellable = true)
    private void infinote_onPlayNote(Entity entity, BlockState state, Level world, BlockPos pos, CallbackInfo ci) {

        int note = state.getValue(NoteBlock.NOTE);

        BlockPos belowPos = pos.below(1);
        ResourceLocation blockId = BuiltInRegistries.BLOCK.getKey(world.getBlockState(belowPos).getBlock());
        BlockSoundConfig config = InfinoteConfig.BLOCK_SOUNDS.get(blockId.toString());

        if (config != null) {
            ResourceLocation soundId = new ResourceLocation(config.sound);

            if (BuiltInRegistries.SOUND_EVENT.containsKey(soundId)) {
                SoundEvent soundEvent = BuiltInRegistries.SOUND_EVENT.get(soundId);
                float shiftedNote = note + config.pitch_shift;
                float pitch = (float) Math.pow(2.0D, (shiftedNote - 12) / 12.0D);
                world.playSound(null, pos, soundEvent, config.category, config.volume, pitch);
            } else {
                float shiftedNote = note + config.pitch_shift;
                float pitch = (float) Math.pow(2.0D, (shiftedNote - 12) / 12.0D);
                ServerLevel serverWorld = (ServerLevel) world;
                FriendlyByteBuf buf = PacketByteBufs.create();
                buf.writeResourceLocation(soundId);
                buf.writeEnum(config.category);
                buf.writeFloat(config.volume);
                buf.writeFloat(pitch);
                buf.writeBlockPos(pos);

                for (ServerPlayer p : serverWorld.players()) {
                    if (p.blockPosition().closerThan(pos, config.volume * 16)) { // 適当に範囲
                        ServerPlayNetworking.send(p, PLAY_CUSTOM_SOUND, buf);
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
