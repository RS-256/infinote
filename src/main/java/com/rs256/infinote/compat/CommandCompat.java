package com.rs256.infinote.compat;

import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.synchronization.SuggestionProviders;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;

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

    //? if <=1.21.4 {
    /*public static ClickEvent clickRunCommand(String string) {
        return new ClickEvent(ClickEvent.Action.RUN_COMMAND, string);
    }

    public static ClickEvent clickSuggestCommand(String string) {
        return new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, string);
    }

    public static HoverEvent hoverShowText(Component component) {
        return new HoverEvent(HoverEvent.Action.SHOW_TEXT, component);
    }

     *///?} else {
    public static ClickEvent.RunCommand clickRunCommand(String string) {
        return new ClickEvent.RunCommand(string);
    }

    public static ClickEvent.SuggestCommand clickSuggestCommand(String string) {
        return new ClickEvent.SuggestCommand(string);
    }

    public static HoverEvent.ShowText hoverShowText(Component component) {
        return new HoverEvent.ShowText(component);
    }
    //?}
}
