package com.rs256.infinote.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.rs256.infinote.config.InfinoteConfig;
import net.minecraft.commands.*;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.commands.arguments.blocks.BlockInput;
import net.minecraft.commands.arguments.blocks.BlockStateArgument;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;

public class InfinoteCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext registryAccess) {

        dispatcher.register(
                Commands.literal("infinote")
                        .then(Commands.literal("add")
                                .then(Commands.argument("block", BlockStateArgument.block(registryAccess))
                                        .then(Commands.argument("sound", ResourceLocationArgument.id())
                                                .suggests(SOUND_ID_SUGGESTIONS)
                                                .then(Commands.argument("category", StringArgumentType.word())
                                                        .suggests((context, builder) -> {
                                                            for (SoundSource cat : SoundSource.values()) {
                                                                builder.suggest(cat.name().toLowerCase());
                                                            }
                                                            return builder.buildFuture();
                                                        })
                                                        .then(Commands.argument("pitch_shift", FloatArgumentType.floatArg(-48))
                                                                .then(Commands.argument("volume", FloatArgumentType.floatArg(0))
                                                                        .executes(context -> {

                                                                            BlockInput blockArg = BlockStateArgument.getBlock(context, "block");
                                                                            ResourceLocation blockId = BuiltInRegistries.BLOCK.getKey(blockArg.getState().getBlock());
                                                                            ResourceLocation soundId = ResourceLocationArgument.getId(context, "sound");
                                                                            String rawCategory = StringArgumentType.getString(context, "category");
                                                                            SoundSource category;
                                                                            try {
                                                                                category = SoundSource.valueOf(rawCategory.toUpperCase());
                                                                            } catch (IllegalArgumentException e) {
                                                                                context.getSource().sendFailure(Component.literal("Invalid category: " + rawCategory));
                                                                                return 0;
                                                                            }
                                                                            float pitch_shift = FloatArgumentType.getFloat(context, "pitch_shift");
                                                                            float volume = FloatArgumentType.getFloat(context, "volume");

                                                                            InfinoteConfig.add(blockId, soundId, category, pitch_shift, volume);

                                                                            if (InfinoteConfig.isInConfig(blockId)) {
                                                                                context.getSource().sendSuccess(
                                                                                        () -> Component.literal("Added note for " + blockId + " with " + soundId), true
                                                                                );
                                                                            }
                                                                            return 1;
                                                                        })
                                                                )
                                                        )
                                                )
                                        )
                                )
                        )
                        .then(Commands.literal("remove")
                                .then(Commands.argument("block", ResourceLocationArgument.id())
                                        .suggests((context, builder) -> {
                                            for (String key : InfinoteConfig.BLOCK_SOUNDS.keySet()) {
                                                builder.suggest(key);
                                            }
                                            return builder.buildFuture();
                                        })
                                        .executes(context -> {
                                            ResourceLocation blockId = ResourceLocationArgument.getId(context, "block");

                                            if (InfinoteConfig.isInConfig(blockId)) {
                                                InfinoteConfig.remove(blockId.toString());
                                                if (! InfinoteConfig.isInConfig(blockId)) {
                                                    context.getSource().sendSuccess(() -> Component.literal("Removed note for " + blockId), true);
                                                }
                                            } else {
                                                context.getSource().sendFailure(Component.literal("No Such a note about " + blockId));
                                            }
                                            return 1;
                                        })
                                )
                        )
        );
    }

    private static final SuggestionProvider<CommandSourceStack> SOUND_ID_SUGGESTIONS =
            (context, builder) -> SharedSuggestionProvider.suggestResource(
                    BuiltInRegistries.SOUND_EVENT.keySet(),
                    builder
            );
}