package com.rs256.infinote.network;

import com.rs256.infinote.Infinote;
import com.rs256.infinote.client.InfinoteClient;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public class NetworkCompat {
    public static final ResourceLocation PLAY_CUSTOM_SOUND = Infinote.id(Infinote.MOD_ID, "play_custom_sound");

    public static void registerClient(){
        ClientPlayNetworking.registerGlobalReceiver(
                PLAY_CUSTOM_SOUND, (client, handler, buf, responseSender) -> {
                    ResourceLocation soundId = buf.readResourceLocation();
                    SoundSource category = buf.readEnum(SoundSource.class);
                    float volume = buf.readFloat();
                    float pitch = buf.readFloat();
                    BlockPos pos = buf.readBlockPos();
                    client.execute(() ->
                            InfinoteClient.playClientSound(soundId, category, volume, pitch, pos)
                            );
                }
        );
    }

    public static void sendPlayCustomSound(ServerPlayer player, ResourceLocation soundId, SoundSource soundSource, float pitch, float volume, BlockPos pos) {

    }
}
