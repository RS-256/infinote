package com.rs256.infinote.compat;

import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.synchronization.SuggestionProviders;
import net.minecraft.network.chat.Component;

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

    public static void sourceSendSuccess(CommandSourceStack commandSourceStack, Component component, boolean broadcast){
        //? if <=1.19.4 {
        /*commandSourceStack.sendSuccess(component, broadcast);
         *///?} else {
        commandSourceStack.sendSuccess(() -> component, broadcast);
        //?}
    }
}
