package com.rs256.infinote.compat;

import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.blocks.BlockInput;
import net.minecraft.commands.arguments.blocks.BlockStateArgument;
import net.minecraft.commands.arguments.coordinates.Coordinates;
import net.minecraft.commands.synchronization.SuggestionProviders;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
//? if >=1.19 {
import net.minecraft.commands.CommandBuildContext;
//?}

public class CommandCompat {
    //? if <=1.21.5 {
    /*public static SuggestionProvider<CommandSourceStack> soundSuggestionProviders() {
        return SuggestionProviders.AVAILABLE_SOUNDS;
    }
     *///?} else {
    public static SuggestionProvider<CommandSourceStack> soundSuggestionProviders() {
        return SuggestionProviders.cast(SuggestionProviders.AVAILABLE_SOUNDS);
    }
    //?}

    // Holds the command build context, which only exists since 1.19, so command trees stay version agnostic.
    public static final class BuildContext {
        //? if <=1.18.2 {
        /*public BuildContext() {
        }
         *///?} else {
        private final CommandBuildContext context;

        public BuildContext(CommandBuildContext context) {
            this.context = context;
        }
        //?}
    }

    public static RequiredArgumentBuilder<CommandSourceStack, BlockInput> blockArgument(String name, BuildContext buildContext) {
        //? if <=1.18.2 {
        /*return Commands.argument(name, BlockStateArgument.block());
         *///?} else {
        return Commands.argument(name, BlockStateArgument.block(buildContext.context));
        //?}
    }

    public static void sourceSendSuccess(CommandSourceStack commandSourceStack, Component component, boolean broadcast){
        //? if <=1.19.4 {
        /*commandSourceStack.sendSuccess(component, broadcast);
         *///?} else {
        commandSourceStack.sendSuccess(() -> component, broadcast);
        //?}
    }

    public static BlockPos getBlockPos(CommandContext<CommandSourceStack> commandContext, String string) {
        return commandContext.getArgument(string, Coordinates.class).getBlockPos(commandContext.getSource());
        //return BlockPosArgument.getBlockPos(commandContext, string);
    }
}
