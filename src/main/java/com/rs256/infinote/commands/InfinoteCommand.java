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
                                                                            float pitchShift = FloatArgumentType.getFloat(commandContext, "pitchShift");
                                                                            float volume = FloatArgumentType.getFloat(commandContext, "volume");
                                                                            return executeAdd(commandContext.getSource(), blockId, soundId, rawCategory, pitchShift, volume);
                                                                        })
                                                                )
                                                        )
                                                )
                                        )
                                )
                        )
                        .then(Commands.literal("remove")
                                .then(Commands.argument("block", BlockStateArgument.block(registryAccess))
                                        .suggests((commandContext, builder) -> {
                                            for (String key : InfinoteConfig.BLOCK_SOUNDS.keySet()) {
                                                builder.suggest(key);
                                            }
                                            return builder.buildFuture();
                                        })
                                        .executes(commandContext -> {
                                            String blockId = BuiltInRegistries.BLOCK.getKey(BlockStateArgument.getBlock(commandContext, "block").getState().getBlock()).toString();
                                            return executeRemove(commandContext.getSource(), blockId);
                                        })
                                )
                        )
                        .then(Commands.literal("reload")
                                .executes(commandContext -> executeReload(commandContext.getSource()))
                        )
                        .then(Commands.literal("import")
                                .then(Commands.literal("notebetterfabric")
                                        .then(Commands.argument("json", StringArgumentType.word())
                                                .executes(commandContext -> {
                                                    String fileName = StringArgumentType.getString(commandContext, "json");
                                                    return executeImport(commandContext.getSource(), fileName);
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

    private static int executeAdd(CommandSourceStack source, String blockId, String soundId, String rawCategory, float pitchShift, float volume) {
        SoundSource category;
        try {
            category = SoundSource.valueOf(rawCategory.toUpperCase());
        } catch (IllegalArgumentException e) {
            source.sendFailure(Component.literal("Invalid category: " + rawCategory));
            return 0;
        }

        InfinoteConfig.add(blockId, soundId, category, pitchShift, volume);

        if (InfinoteConfig.isInConfig(blockId)) {
            source.sendSuccess(() -> Component.literal("Added note for " + blockId + " with " + soundId), true);
        }
        return 1;
    }

    private static int executeRemove(CommandSourceStack source, String blockId) {
        if (!InfinoteConfig.isInConfig(blockId)) {
            source.sendFailure(Component.literal("No such note about " + blockId));
            return 0;
        }

        InfinoteConfig.remove(blockId);

        if (!InfinoteConfig.isInConfig(blockId)) {
            source.sendSuccess(() -> Component.literal("Removed note for " + blockId), true);
        }
        return 1;
    }

    private static int executeReload(CommandSourceStack source) {
        InfinoteConfig.load();
        source.sendSuccess(() -> Component.literal("Infinote reloaded!"), true);
        return 1;
    }

    private static int executeImport(CommandSourceStack source, String fileName) {
        int count = ImportConfig.fromNotebetterfabric(fileName);
        if (count == 0) {
            source.sendFailure(Component.literal(
                    "The file: " + fileName + " does not exist, or is not in the correct notebetterfabric.json format, or has zero entries."
            ));
            return 0;
        }
        source.sendSuccess(() -> Component.literal("Imported " + count + " mappings from " + fileName), true);
        return 1;
    }

    private static int executeList(CommandSourceStack source, int page, int pageSize) {
        List<String> keys = new ArrayList<>(InfinoteConfig.BLOCK_SOUNDS_COMPILED.keySet());
        Collections.sort(keys);

        int total = keys.size();
        if (total == 0) {
            source.sendSuccess(() -> Component.literal("You have no mappings."), false);
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
            if (config == null) continue;

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
                        .withHoverEvent(new HoverEvent.ShowText(Component.literal("Go to page " + (p - 1)))));

        Component center = Component.literal(
                " " + total + " entries  |  page " + p + "/" + totalPages + "  (" + pageSize + "/page)"
        ).withStyle(ChatFormatting.GOLD);

        Component next = Component.literal("  >>>")
                .withStyle(style -> style
                        .withColor(ChatFormatting.GRAY)
                        .withClickEvent(new ClickEvent.RunCommand("/infinote list " + (p + 1) + " " + pageSize))
                        .withHoverEvent(new HoverEvent.ShowText(Component.literal("Go to page " + (p + 1)))));

        MutableComponent footer = Component.empty();
        footer.append(p > 1 ? prev : Component.literal("<<< ").withStyle(ChatFormatting.DARK_GRAY));
        footer.append(center);
        footer.append(p < totalPages ? next : Component.literal(" >>>").withStyle(ChatFormatting.DARK_GRAY));

        source.sendSuccess(() -> records, false);
        source.sendSuccess(() -> footer, false);
        return 1;
    }

    private static int executeGet(CommandSourceStack source, String blockId) {
        String key = IdCompat.normalize(blockId);
        if (key == null) {
            source.sendFailure(Component.literal("Invalid block id: " + blockId));
            return 0;
        }

        var config = InfinoteConfig.BLOCK_SOUNDS_COMPILED.get(key);
        if (config == null) {
            source.sendFailure(Component.literal("No mapping for " + key));
            return 0;
        }

        source.sendSuccess(() -> Component.literal("Infinote mapping for " + key + ":"), false);
        source.sendSuccess(() -> Component.literal("├─sound: " + config.sound), false);
        source.sendSuccess(() -> Component.literal("├─category: " + config.category), false);
        source.sendSuccess(() -> Component.literal("├─pitchShift: " + config.pitchShift), false);
        source.sendSuccess(() -> Component.literal("└─volume: " + config.volume), false);
        return 1;
    }
}