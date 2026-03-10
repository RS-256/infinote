package com.rs256.infinote.commands;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.rs256.infinote.compat.ComponentCompat;
import com.rs256.infinote.compat.CommandCompat;
import com.rs256.infinote.compat.IdCompat;
import com.rs256.infinote.compat.RegistryCompat;
import com.rs256.infinote.config.ImportConfig;
import com.rs256.infinote.config.InfinoteConfig;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.*;
import net.minecraft.commands.arguments.blocks.BlockStateArgument;
import net.minecraft.network.chat.*;
import net.minecraft.sounds.SoundSource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class InfinoteCommand {
//? if <=1.18.2 {
    /*public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("infinote")
                        .then(Commands.literal("add")
                                .then(Commands.argument("block", BlockStateArgument.block())
                                        .then(IdCompat.commandArgument("sound")
                                                .suggests(CommandCompat.soundSuggestionProviders())
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
                                                                            String blockId = RegistryCompat.getKey(BlockStateArgument.getBlock(commandContext, "block").getState().getBlock());
                                                                            String soundId = IdCompat.normalize(IdCompat.iDArgumentGetIdString(commandContext, "sound"));
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
                                .then(Commands.argument("block", BlockStateArgument.block())
                                        .suggests((commandContext, builder) -> {
                                            for (String key : InfinoteConfig.BLOCK_SOUNDS.keySet()) {
                                                builder.suggest(key);
                                            }
                                            return builder.buildFuture();
                                        })
                                        .executes(commandContext -> {
                                            String blockId = RegistryCompat.getKey(BlockStateArgument.getBlock(commandContext, "block").getState().getBlock());
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
                                .then(Commands.argument("block", BlockStateArgument.block())
                                        .executes(commandContext -> {
                                            String blockId = RegistryCompat.getKey(BlockStateArgument.getBlock(commandContext, "block").getState().getBlock());
                                            return executeGet(commandContext.getSource(), blockId);
                                        })
                                )
                        )
        );
    }
*///?} else {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext commandBuildContext) {
        dispatcher.register(
                Commands.literal("infinote")
                        .then(Commands.literal("add")
                                .then(Commands.argument("block", BlockStateArgument.block(commandBuildContext))
                                        .then(IdCompat.commandArgument("sound")
                                                .suggests(CommandCompat.soundSuggestionProviders())
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
                                                                            String blockId = RegistryCompat.getKey(BlockStateArgument.getBlock(commandContext, "block").getState().getBlock());
                                                                            String soundId = IdCompat.normalize(IdCompat.iDArgumentGetIdString(commandContext, "sound"));
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
                                .then(Commands.argument("block", BlockStateArgument.block(commandBuildContext))
                                        .suggests((commandContext, builder) -> {
                                            for (String key : InfinoteConfig.BLOCK_SOUNDS.keySet()) {
                                                builder.suggest(key);
                                            }
                                            return builder.buildFuture();
                                        })
                                        .executes(commandContext -> {
                                            String blockId = RegistryCompat.getKey(BlockStateArgument.getBlock(commandContext, "block").getState().getBlock());
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
                                .then(Commands.argument("block", BlockStateArgument.block(commandBuildContext))
                                        .executes(commandContext -> {
                                            String blockId = RegistryCompat.getKey(BlockStateArgument.getBlock(commandContext, "block").getState().getBlock());
                                            return executeGet(commandContext.getSource(), blockId);
                                        })
                                )
                        )
        );
    }
//?}

    private static int executeAdd(CommandSourceStack source, String blockId, String soundId, String rawCategory, float pitchShift, float volume) {
        SoundSource category;
        try {
            category = SoundSource.valueOf(rawCategory.toUpperCase());
        } catch (IllegalArgumentException e) {
            source.sendFailure(ComponentCompat.literal("Invalid category: " + rawCategory));
            return 0;
        }

        InfinoteConfig.add(blockId, soundId, category, pitchShift, volume);

        if (InfinoteConfig.isInConfig(blockId)) {
            CommandCompat.sourceSendSuccess(source, ComponentCompat.literal("Added note for " + blockId + " with " + soundId), true);
        }
        return 1;
    }

    private static int executeRemove(CommandSourceStack source, String blockId) {
        if (!InfinoteConfig.isInConfig(blockId)) {
            source.sendFailure(ComponentCompat.literal("No such note about " + blockId));
            return 0;
        }

        InfinoteConfig.remove(blockId);

        if (!InfinoteConfig.isInConfig(blockId)) {
            CommandCompat.sourceSendSuccess(source, ComponentCompat.literal("Removed note for " + blockId), true);
        }
        return 1;
    }

    private static int executeReload(CommandSourceStack source) {
        InfinoteConfig.load();
        CommandCompat.sourceSendSuccess(source, ComponentCompat.literal("Infinote reloaded!"), true);
        return 1;
    }

    private static int executeImport(CommandSourceStack source, String fileName) {
        int count = ImportConfig.fromNotebetterfabric(fileName);
        if (count == 0) {
            source.sendFailure(ComponentCompat.literal("The file: " + fileName + " does not exist, or is not in the correct notebetterfabric.json format, or has zero entries."));
            return 0;
        }
        CommandCompat.sourceSendSuccess(source, ComponentCompat.literal("Imported " + count + " mappings from " + fileName), true);
        return 1;
    }

    private static int executeList(CommandSourceStack source, int page, int pageSize) {
        List<String> keys = new ArrayList<>(InfinoteConfig.BLOCK_SOUNDS_COMPILED.keySet());
        Collections.sort(keys);

        int total = keys.size();
        if (total == 0) {
            CommandCompat.sourceSendSuccess(source, ComponentCompat.literal("You have no mappings."), false);
            return 1;
        }

        int totalPages = Math.max(1, (total + pageSize - 1) / pageSize);
        int p = Math.max(1, Math.min(page, totalPages));
        int from = (p - 1) * pageSize;
        int to = Math.min(total, from + pageSize);

        MutableComponent records = ComponentCompat.empty();
        for (int i = from; i < to; i++) {
            String blockId = keys.get(i);
            var config = InfinoteConfig.BLOCK_SOUNDS_COMPILED.get(blockId);
            if (config == null) continue;

            records.append(
                    ComponentCompat.literal("\n")
                            .append(ComponentCompat.literal("[X] ")
                                    .withStyle(style -> style
                                            .withColor(ChatFormatting.RED)
                                            .withClickEvent(ComponentCompat.withClickSuggestCommand("/infinote remove " + blockId))))
                            .append(blockId)
                            .withStyle(ChatFormatting.AQUA)
                            .append(ComponentCompat.literal(" -> ").withStyle(ChatFormatting.GRAY))
                            .append(ComponentCompat.literal(config.sound).withStyle(ChatFormatting.GREEN))
            );
        }

        Component prev = ComponentCompat.literal("<<<  ")
                .withStyle(style -> style
                        .withColor(ChatFormatting.GRAY)
                        .withClickEvent(ComponentCompat.withClickRunCommand("/infinote list " + (p - 1) + " " + pageSize))
                        .withHoverEvent(ComponentCompat.withHoverShowText(ComponentCompat.literal("Go to page " + (p - 1)))));

        Component center = ComponentCompat.literal(
                " " + total + " entries  |  page " + p + "/" + totalPages + "  (" + pageSize + "/page)"
        ).withStyle(ChatFormatting.GOLD);

        Component next = ComponentCompat.literal("  >>>")
                .withStyle(style -> style
                        .withColor(ChatFormatting.GRAY)
                        .withClickEvent(ComponentCompat.withClickRunCommand("/infinote list " + (p + 1) + " " + pageSize))
                        .withHoverEvent(ComponentCompat.withHoverShowText(ComponentCompat.literal("Go to page " + (p + 1)))));

        MutableComponent footer = ComponentCompat.empty();
        footer.append(p > 1 ? prev : ComponentCompat.literal("<<<  ").withStyle(ChatFormatting.DARK_GRAY));
        footer.append(center);
        footer.append(p < totalPages ? next : ComponentCompat.literal("  >>>").withStyle(ChatFormatting.DARK_GRAY));

        Component list = records.append("\n").append(footer);

        CommandCompat.sourceSendSuccess(source, list, false);
        return 1;
    }

    private static int executeGet(CommandSourceStack source, String blockId) {
        String key = IdCompat.normalize(blockId);
        if (key == null) {
            source.sendFailure(ComponentCompat.literal("Invalid block id: " + blockId));
            return 0;
        }

        var config = InfinoteConfig.BLOCK_SOUNDS_COMPILED.get(key);
        if (config == null) {
            source.sendFailure(ComponentCompat.literal("No mapping for " + key));
            return 0;
        }

        Component component = ComponentCompat.empty()
                .append(ComponentCompat.literal("Infinote mapping for ").withStyle(ChatFormatting.GOLD))
                .append(ComponentCompat.literal(key).withStyle(ChatFormatting.AQUA))
                .append(ComponentCompat.literal("\n├─sound: ").withStyle(ChatFormatting.GRAY))      .append(ComponentCompat.literal(config.sound).withStyle(ChatFormatting.GREEN))
                .append(ComponentCompat.literal("\n├─category: ").withStyle(ChatFormatting.GRAY))   .append(ComponentCompat.literal(config.category.toString()).withStyle(ChatFormatting.LIGHT_PURPLE))
                .append(ComponentCompat.literal("\n├─pitchShift: ").withStyle(ChatFormatting.GRAY)) .append(ComponentCompat.literal(String.valueOf(config.pitchShift)).withStyle(ChatFormatting.YELLOW))
                .append(ComponentCompat.literal("\n└─volume: ").withStyle(ChatFormatting.GRAY))     .append(ComponentCompat.literal(String.valueOf(config.volume)).withStyle(ChatFormatting.YELLOW));

        CommandCompat.sourceSendSuccess(source, component, false);

        return 1;
    }
}