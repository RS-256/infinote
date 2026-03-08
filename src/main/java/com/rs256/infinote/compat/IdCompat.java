package com.rs256.infinote.compat;

import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
//? if <=1.21.10 {
/*import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.resources.ResourceLocation;
 *///?} else {
import net.minecraft.commands.arguments.IdentifierArgument;
import net.minecraft.resources.Identifier;
//?}

public final class IdCompat {
    private IdCompat() {
    }

    // "path" -> "minecraft:path" にする等、最低限の正規化 <- Identifierにあるので、いつかけす
    public static String normalize(String raw) {
        if (raw == null) return null;
        String s = raw.trim();
        if (s.isEmpty()) return null;
        return s;
    }

    //? if <=1.21.10 {
    /*public static ResourceLocation idFromString(String raw) {
        return ResourceLocation.tryParse(raw);
    }

    public static RequiredArgumentBuilder<CommandSourceStack, ResourceLocation> commandArgument(String key) {
        return Commands.argument(key, ResourceLocationArgument.id());
    }

    public static String iDArgumentGetIdString(CommandContext<CommandSourceStack> commandContext, String name) {
        return ResourceLocationArgument.getId(commandContext, name).toString();
    }
    *///?} else {
    public static Identifier idFromString(String raw) {
        return Identifier.tryParse(raw);
    }

    public static RequiredArgumentBuilder<CommandSourceStack, Identifier> commandArgument(String key) {
        return Commands.argument(key, IdentifierArgument.id());
    }

    public static String iDArgumentGetIdString(CommandContext<CommandSourceStack> commandContext, String name) {
        return IdentifierArgument.getId(commandContext, name).toString();
    }
    //?}

    //?if <=1.20.6 {
    /*public static ResourceLocation idFromNamespaceAndPath(String namespace, String path) {
        return new ResourceLocation(namespace, path);
    }
    
    *///?} else if <=1.21.10 {
    /*public static ResourceLocation idFromNamespaceAndPath(String namespace, String path) {
        return ResourceLocation.fromNamespaceAndPath(namespace, path);
    }
     *///?} else {
    public static Identifier idFromNamespaceAndPath(String namespace, String path) {
        return Identifier.fromNamespaceAndPath(namespace, path);
    }
     //?}
}
