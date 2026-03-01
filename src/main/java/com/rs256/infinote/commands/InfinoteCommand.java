package com.rs256.infinote.commands;

import com.rs256.infinote.Infinote;
import com.rs256.infinote.compat.IdCompat;
import com.rs256.infinote.config.ImportConfig;
import com.rs256.infinote.config.InfinoteConfig;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.commands.*;
//? if <=1.21.10 {
/*import net.minecraft.commands.arguments.ResourceLocationArgument;
*///?} else {
import net.minecraft.commands.arguments.IdentifierArgument;
//?}
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
                                        //? if <=1.21.10 {
                                        
                                        /*.then(Commands.argument("sound", ResourceLocationArgument.id())
                                        *///?} else {
                                        .then(Commands.argument("sound", IdentifierArgument.id())
                                        //?}

                                                //? if <=1.21.5 {
                                                /*.suggests(SuggestionProviders.AVAILABLE_SOUNDS)
                                                *///?} else {
                                                .suggests(SuggestionProviders.cast(SuggestionProviders.AVAILABLE_SOUNDS))
                                                //?}
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
                                                                            //? if <=1.21.10 {
                                                                            
                                                                             /*String soundId = IdCompat.normalize(ResourceLocationArgument.getId(context, "sound").toString());
                                                                            *///?} else {
                                                                            String soundId = IdCompat.normalize(IdentifierArgument.getId(context, "sound").toString());
                                                                            //?}

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
                        .then(Commands.literal("import")
                                .then(Commands.literal("notebetterfabric")
                                        .then(Commands.argument("json", StringArgumentType.word())
                                                .executes(context -> {
                                                    String fileName = StringArgumentType.getString(context, "json");
                                                    int count = ImportConfig.FromNotebetterfabric(fileName); // 追加する
                                                    if (count == 0) {
                                                        context.getSource().sendFailure(Component.literal("The file: " + fileName + " does not exist, or is not in the correct notebetterfabric.json format, or has zero entries."));
                                                    }
                                                    context.getSource().sendSuccess(() -> Component.literal("Imported " + count + " mappings from " + fileName), true);
                                                    return 1;
                                                })
                                        )
                                )
                        )
        );
    }
}