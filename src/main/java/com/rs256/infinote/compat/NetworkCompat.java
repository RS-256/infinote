package com.rs256.infinote.compat;

import com.rs256.infinote.Infinote;
import com.rs256.infinote.client.InfinoteClient;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
//? if <1.21 {

 /*import net.minecraft.resources.ResourceLocation;
*///?} else {
import net.minecraft.resources.Identifier;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.codec.StreamCodec;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
//?}
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;


public class NetworkCompat {
    //? if <1.21 {
    
     /*public static final ResourceLocation PLAY_CUSTOM_SOUND = IdCompat.idFromNamespaceAndPath(Infinote.MOD_ID, "play_custom_sound");

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

        ServerPlayNetworking.send(player, PLAY_CUSTOM_SOUND, buf);
    }
    *///?} else {

    public static final Identifier PLAY_CUSTOM_SOUND_ID = IdCompat.idFromNamespaceAndPath(Infinote.MOD_ID, "play_custom_sound");

    public static final CustomPacketPayload.Type<PlayCustomSoundS2CPayload> PLAY_CUSTOM_SOUND = new CustomPacketPayload.Type<>(PLAY_CUSTOM_SOUND_ID);

    public record PlayCustomSoundS2CPayload(
            String soundId,
            SoundSource category,
            float pitch,
            float volume,
            BlockPos pos
    ) implements CustomPacketPayload {

        public static final StreamCodec<FriendlyByteBuf, PlayCustomSoundS2CPayload> CODEC =
                StreamCodec.of(
                        // encoder
                        (buf, p) -> {
                            buf.writeUtf(p.soundId);
                            buf.writeEnum(p.category);
                            buf.writeFloat(p.pitch);
                            buf.writeFloat(p.volume);
                            buf.writeBlockPos(p.pos);
                        },
                        // decoder
                        buf -> new PlayCustomSoundS2CPayload(
                                buf.readUtf(),
                                buf.readEnum(SoundSource.class),
                                buf.readFloat(),
                                buf.readFloat(),
                                buf.readBlockPos()
                        )
                );

        @Override
        public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
            return PLAY_CUSTOM_SOUND;
        }
    }

    public static void registerPayloadTypes() {
        PayloadTypeRegistry.playS2C().register(PLAY_CUSTOM_SOUND, PlayCustomSoundS2CPayload.CODEC);
    }

    public static void registerClient() {
        ClientPlayNetworking.registerGlobalReceiver(
                PLAY_CUSTOM_SOUND,
                (payload, context) -> {
                    context.client().execute(() ->
                            InfinoteClient.playClientSound(
                                    payload.soundId(),
                                    payload.category(),
                                    payload.pitch(),
                                    payload.volume(),
                                    payload.pos()
                            )
                    );
                }
        );
    }

    public static void sendPlayCustomSound(ServerPlayer player, String soundId, SoundSource category, float pitch, float volume, BlockPos pos) {
        ServerPlayNetworking.send(
                player,
                new PlayCustomSoundS2CPayload(soundId, category, pitch, volume, pos)
        );
    }



    //?}

}
