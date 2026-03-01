package com.rs256.infinote.compat;

//? if <=1.21.10 {
/*import net.minecraft.resources.ResourceLocation;
 *///?} else
import net.minecraft.resources.Identifier;

public final class IdCompat {
    private IdCompat() {
    }

    // "path" → "minecraft:path" にする等、最低限の正規化
    public static String normalize(String raw) {
        if (raw == null) return null;
        String s = raw.trim();
        if (s.isEmpty()) return null;
        if (!s.contains(":")) s = "minecraft:" + s;
        return s;
    }


    //? if <=1.21.10 {
    /*public static ResourceLocation idFromString(String raw) {
        String s = normalize(raw);
        if (s == null) return null;

        int i = s.indexOf(':');
        if (i <= 0 || i >= s.length() - 1) return null;

        String namespace = s.substring(0, i);
        String path = s.substring(i + 1);
        return idFromNamespaceAndPath(namespace, path);
    }

    *///?} else {
    public static Identifier idFromString(String raw) {
        String s = normalize(raw);
        if (s == null) return null;

        int i = s.indexOf(':');
        if (i <= 0 || i >= s.length() - 1) return null;

        String namespace = s.substring(0, i);
        String path = s.substring(i + 1);
        return idFromNamespaceAndPath(namespace, path);
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
