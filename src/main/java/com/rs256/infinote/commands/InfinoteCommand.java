package com.rs256.infinote.commands;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.rs256.infinote.compat.IdCompat;
import com.rs256.infinote.config.ImportConfig;
import com.rs256.infinote.config.InfinoteConfig;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.*;
//? if <=1.21.10 {
/*import net.minecraft.commands.arguments.ResourceLocationArgument;
*///?} else {
import net.minecraft.commands.arguments.IdentifierArgument;
//?}
import net.minecraft.commands.arguments.blocks.BlockStateArgument;
import net.minecraft.commands.synchronization.SuggestionProviders;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.sounds.SoundSource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
                                                        .suggests((commandContext, builder) -> {
                                                            for (SoundSource cat : SoundSource.values()) {
                                                                builder.suggest(cat.name().toLowerCase());
                                                            }
                                                            return builder.buildFuture();
                                                        })
                                                        .then(Commands.argument("pitchShift", FloatArgumentType.floatArg(-48))
                                                                .then(Commands.argument("volume", FloatArgumentType.floatArg(0))
                                                                        .executes(commandContext -> {
                                                                            String blockId = BuiltInRegistries.BLOCK.getKey(BlockStateArgument.getBlock(commandContext, "block").getState().getBlock()).toString();
                                                                            //? if <=1.21.10 {

                                                                             /*String soundId = IdCompat.normalize(ResourceLocationArgument.getId(commandContext, "sound").toString());
                                                                            *///?} else {
                                                                            String soundId = IdCompat.normalize(IdentifierArgument.getId(commandContext, "sound").toString());
                                                                            //?}

                                                                            String rawCategory = StringArgumentType.getString(commandContext, "category");
                                                                            SoundSource category;
                                                                            try {
                                                                                category = SoundSource.valueOf(rawCategory.toUpperCase());
                                                                            } catch (IllegalArgumentException e) {
                                                                                commandContext.getSource().sendFailure(Component.literal("Invalid category: " + rawCategory));
                                                                                return 0;
                                                                            }
                                                                            float pitch_shift = FloatArgumentType.getFloat(commandContext, "pitchShift");
                                                                            float volume = FloatArgumentType.getFloat(commandContext, "volume");

                                                                            InfinoteConfig.add(blockId, soundId, category, pitch_shift, volume);

                                                                            if (InfinoteConfig.isInConfig(blockId)) {
                                                                                commandContext.getSource().sendSuccess(
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
                                        .executes(commandContext -> {
                                            String blockId = BuiltInRegistries.BLOCK.getKey(BlockStateArgument.getBlock(commandContext, "block").getState().getBlock()).toString();

                                            if (InfinoteConfig.isInConfig(blockId)) {
                                                InfinoteConfig.remove(blockId);
                                                if (! InfinoteConfig.isInConfig(blockId)) {
                                                    commandContext.getSource().sendSuccess(() -> Component.literal("Removed note for " + blockId), true);
                                                }
                                            } else {
                                                commandContext.getSource().sendFailure(Component.literal("No Such a note about " + blockId));
                                            }
                                            return 1;
                                        })
                                )
                        )
                        .then(Commands.literal("reload")
                                .executes(commandContext -> {
                                    InfinoteConfig.load();
                                    commandContext.getSource().sendSuccess(()->Component.literal("infinote reloaded!"), true);
                                    return 1;
                                })
                        )
                        .then(Commands.literal("import")
                                .then(Commands.literal("notebetterfabric")
                                        .then(Commands.argument("json", StringArgumentType.word())
                                                .executes(commandContext -> {
                                                    String fileName = StringArgumentType.getString(commandContext, "json");
                                                    int count = ImportConfig.FromNotebetterfabric(fileName); // 追加する
                                                    if (count == 0) {
                                                        commandContext.getSource().sendFailure(Component.literal("The file: " + fileName + " does not exist, or is not in the correct notebetterfabric.json format, or has zero entries."));
                                                    }
                                                    commandContext.getSource().sendSuccess(() -> Component.literal("Imported " + count + " mappings from " + fileName), true);
                                                    return 1;
                                                })
                                        )
                                )
                        )
                        .then(Commands.literal("list")
                                .executes(commandContext -> executeList(commandContext.getSource(), 1, 16))
                                .then(Commands.argument("page", IntegerArgumentType.integer(1))
                                        .executes(commandContext -> executeList(commandContext.getSource(), IntegerArgumentType.getInteger(commandContext, "page"), 16))
                                        .then(Commands.argument("pageSize", IntegerArgumentType.integer(1, 20))
                                                .executes(commandContext -> executeList(
                                                        commandContext.getSource(),
                                                        IntegerArgumentType.getInteger(commandContext, "page"),
                                                        IntegerArgumentType.getInteger(commandContext, "pageSize")
                                                ))
                                        )
                                )
                        )

                        .then(Commands.literal("get")
                                .then(Commands.argument("block", BlockStateArgument.block(registryAccess))
                                        .suggests((context, builder) -> {
                                            for (String key : InfinoteConfig.BLOCK_SOUNDS.keySet()) {
                                                builder.suggest(key);
                                            }
                                            return builder.buildFuture();
                                        })
                                        .executes(commandContext -> {
                                            String blockId = BuiltInRegistries.BLOCK
                                                    .getKey(BlockStateArgument.getBlock(commandContext, "block").getState().getBlock())
                                                    .toString();
                                            return executeGet(commandContext.getSource(), blockId);
                                        })
                                )
                        )
        );
    }

    private static int executeList(CommandSourceStack commandSourceStack, int page, int pageSize) {
        List<String> keys = new ArrayList<>(InfinoteConfig.BLOCK_SOUNDS_COMPILED.keySet());
        Collections.sort(keys);

        int total = keys.size();
        if (total == 0) {
            commandSourceStack.sendSuccess(() -> Component.literal("you have no mappings."), false);
            return 1;
        }

        int totalPages = Math.max(1, (total + pageSize - 1) / pageSize);
        int p = Math.max(1, Math.min(page, totalPages));

        int from = (p - 1) * pageSize;
        int to = Math.min(total, from + pageSize);

        MutableComponent records = Component.empty();

        for (int i = from; i < to; i++) {
            String blockId = keys.get(i);
            var config = InfinoteConfig.BLOCK_SOUNDS_COMPILED.get(blockId);
            if (config == null) {
                continue;
            }

            records.append(
                    Component.literal("\n")
                            .append(Component.literal("[X] ")
                                    .withStyle(style -> style
                                                    .withColor(ChatFormatting.RED)
                                                    .withClickEvent(new ClickEvent.SuggestCommand("/infinote remove " + blockId))))
                            .append(blockId)
                            .withStyle(ChatFormatting.AQUA)
                            .append(Component.literal(" -> ").withStyle(ChatFormatting.GRAY))
                            .append(Component.literal(config.sound).withStyle(ChatFormatting.GREEN))
            );
        }

        Component prev = Component.literal("<<<  ")
                .withStyle(style -> style
                        .withColor(ChatFormatting.GRAY)
                        .withClickEvent(new ClickEvent.RunCommand("/infinote list " + (p - 1) + " " + pageSize))
                        .withHoverEvent(new HoverEvent.ShowText(Component.literal("Go to page " + (p - 1))))
                );

        Component center = Component.literal(
                " " + total + " entries  |  page " + p + "/" + totalPages + "  (" + pageSize + "/page) "
        ).withStyle(ChatFormatting.GOLD);

        Component next = Component.literal("  >>>")
                .withStyle(style -> style
                        .withColor(ChatFormatting.GRAY)
                        .withClickEvent(new ClickEvent.RunCommand("/infinote list " + (p + 1) + " " + pageSize))
                        .withHoverEvent(new HoverEvent.ShowText(Component.literal("Go to page " + (p + 1))))
                );

        MutableComponent footer = Component.empty();

        if (p > 1) {
            footer.append(prev);
        } else {
            footer.append(Component.literal("<<<  ").withStyle(ChatFormatting.DARK_GRAY));
        }

        footer.append(center);

        if (p < totalPages) {
            footer.append(next);
        } else {
            footer.append(Component.literal("  >>>").withStyle(ChatFormatting.DARK_GRAY));
        }
        commandSourceStack.sendSuccess(() -> records, false);
        commandSourceStack.sendSuccess(() -> footer, false);

        return 1;
    }

    private static int executeGet(CommandSourceStack commandSourceStack, String blockId) {
        String key = IdCompat.normalize(blockId);
        if (key == null) {
            commandSourceStack.sendFailure(Component.literal("Invalid block id: " + blockId));
            return 0;
        }

        var config = InfinoteConfig.BLOCK_SOUNDS_COMPILED.get(key);
        if (config == null) {
            commandSourceStack.sendFailure(Component.literal("No mapping for " + key));
            return 0;
        }

        commandSourceStack.sendSuccess(() -> Component.literal("Infinote mapping for " + key + ":"), false);
        commandSourceStack.sendSuccess(() -> Component.literal("├─sound: " + config.sound), false);
        commandSourceStack.sendSuccess(() -> Component.literal("├─category: " + config.category), false);
        commandSourceStack.sendSuccess(() -> Component.literal("├─pitchShift: " + config.pitchShift), false);
        commandSourceStack.sendSuccess(() -> Component.literal("└─volume: " + config.volume), false);
        return 1;
    }
}