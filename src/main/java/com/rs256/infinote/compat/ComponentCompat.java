package com.rs256.infinote.compat;

import net.minecraft.network.chat.*;

public class ComponentCompat {
    public static MutableComponent literal(String string) {
    //? if <=1.18.2 {
    /*return new TextComponent(string);
    *///?} else
    return Component.literal(string);
}

    public static MutableComponent empty() {
        return literal("");
    }

    //? if <=1.21.4 {
    /*public static ClickEvent withClickRunCommand(String string) {
        return new ClickEvent(ClickEvent.Action.RUN_COMMAND, string);
    }

    public static ClickEvent withClickSuggestCommand(String string) {
        return new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, string);
    }

    public static HoverEvent withHoverShowText(Component component) {
        return new HoverEvent(HoverEvent.Action.SHOW_TEXT, component);
    }

    *///?} else {
    public static ClickEvent.RunCommand withClickRunCommand(String string) {
        return new ClickEvent.RunCommand(string);
    }

    public static ClickEvent.SuggestCommand withClickSuggestCommand(String string) {
        return new ClickEvent.SuggestCommand(string);
    }

    public static HoverEvent.ShowText withHoverShowText(Component component) {
        return new HoverEvent.ShowText(component);
    }
    //?}

    public static MutableComponent build(MutableComponent mutableComponent, String[] strings) {
        return null;
    }

    public static MutableComponent buildWithBreak(MutableComponent mutableComponent, String[] strings) {
        return null;
    }
}
