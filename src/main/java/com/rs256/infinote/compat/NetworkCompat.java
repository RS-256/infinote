package com.rs256.infinote.compat;

import com.rs256.infinote.Infinote;
import com.rs256.infinote.client.InfinoteClient;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public class NetworkCompat {
    public static final ResourceLocation PLAY_CUSTOM_SOUND = Infinote.id(Infinote.MOD_ID, "play_custom_sound");

    public static void registerClient(){
        ClientPlayNetworking.registerGlobalReceiver(
                PLAY_CUSTOM_SOUND, (client, handler, buf, responseSender) -> {
                    String soundId = buf.readUtf();
                    SoundSource category = buf.readEnum(SoundSource.class);
                    float pitch = buf.readFloat();
                    float volume = buf.readFloat();
                    BlockPos pos = buf.readBlockPos();
                    client.execute(() ->
                            InfinoteClient.playClientSound(soundId, category, pitch, volume, pos)
                            );
                }
        );
    }

    public static void sendPlayCustomSound(ServerPlayer player, String soundId, SoundSource category, float pitch, float volume, BlockPos pos) {
        FriendlyByteBuf buf = PacketByteBufs.create();
        buf.writeUtf(soundId);
        buf.writeEnum(category);
        buf.writeFloat(pitch);
        buf.writeFloat(volume);
        buf.writeBlockPos(pos);

        ServerPlayNetworking.send(player, Infinote.PLAY_CUSTOM_SOUND, buf);
    }
}
