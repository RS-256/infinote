package com.rs256.infinote.commands;

import com.rs256.infinote.commands.argument.IdStringArgumentType;
import com.rs256.infinote.compat.IdCompat;
import com.rs256.infinote.config.InfinoteConfig;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.commands.*;
import net.minecraft.commands.arguments.blocks.BlockStateArgument;
import net.minecraft.commands.synchronization.SuggestionProviders;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;

public class InfinoteCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext registryAccess) {

        dispatcher.register(
                Commands.literal("infinote")
                        .then(Commands.literal("add")
                                .then(Commands.argument("block", BlockStateArgument.block(registryAccess))
                                        .then(Commands.argument("sound", IdStringArgumentType.id())
                                                //? if <1.21 {
                                                /*.suggests(SuggestionProviders.AVAILABLE_SOUNDS)
                                                *///?} else
                                                .suggests(SuggestionProviders.cast(SuggestionProviders.AVAILABLE_SOUNDS))

                                                .then(Commands.argument("category", StringArgumentType.word())
                                                        .suggests((context, builder) -> {
                                                            for (SoundSource cat : SoundSource.values()) {
                                                                builder.suggest(cat.name().toLowerCase());
                                                            }
                                                            return builder.buildFuture();
                                                        })
                                                        .then(Commands.argument("pitchShift", FloatArgumentType.floatArg(-48))
                                                                .then(Commands.argument("volume", FloatArgumentType.floatArg(0))
                                                                        .executes(context -> {
                                                                            String blockId = BuiltInRegistries.BLOCK.getKey(BlockStateArgument.getBlock(context, "block").getState().getBlock()).toString();
                                                                            String soundId = IdCompat.normalize(IdStringArgumentType.getId(context, "sound"));
                                                                            String rawCategory = StringArgumentType.getString(context, "category");
                                                                            SoundSource category;
                                                                            try {
                                                                                category = SoundSource.valueOf(rawCategory.toUpperCase());
                                                                            } catch (IllegalArgumentException e) {
                                                                                context.getSource().sendFailure(Component.literal("Invalid category: " + rawCategory));
                                                                                return 0;
                                                                            }
                                                                            float pitch_shift = FloatArgumentType.getFloat(context, "pitchShift");
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

                                .then(Commands.argument("block",  BlockStateArgument.block(registryAccess))
                                        .suggests((context, builder) -> {
                                            for (String key : InfinoteConfig.BLOCK_SOUNDS.keySet()) {
                                                builder.suggest(key);
                                            }
                                            return builder.buildFuture();
                                        })
                                        .executes(context -> {
                                            String blockId = BuiltInRegistries.BLOCK.getKey(BlockStateArgument.getBlock(context, "block").getState().getBlock()).toString();

                                            if (InfinoteConfig.isInConfig(blockId)) {
                                                InfinoteConfig.remove(blockId);
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
                        .then(Commands.literal("reload")
                                .executes(context -> {
                                    InfinoteConfig.load();
                                    context.getSource().sendSuccess(()->Component.literal("infinote reloaded!"), true);
                                    return 1;
                                })
                        )
        );
    }
}