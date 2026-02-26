package com.rs256.infinote.compat;

import net.minecraft.resources.ResourceLocation;

public final class IdCompat {
    private IdCompat() {}

    /** "path" → "minecraft:path" にする等、最低限の正規化 */
    public static String normalize(String raw) {
        if (raw == null) return null;
        String s = raw.trim();
        if (s.isEmpty()) return null;
        if (!s.contains(":")) s = "minecraft:" + s;
        return s;
    }

    /** "namespace:path" を分解して作る（namespace省略はminecraft扱い） */
    public static ResourceLocation idFromString(String raw) {
        String s = normalize(raw);
        if (s == null) {
            return null;
        }
        int i = s.indexOf(":");
        if (i<=0 || i>=s.length() - 1) {
            return null;
        }

        String namespace = s.substring(0, i);
        String path = s.substring(i + 1);

        return new ResourceLocation(namespace, path);
        /*
        1.21+
        return ResourceLocation.of(ns, path);
         */
    }
    /** namespace/path から作る */
    public static ResourceLocation idFromSpaceAndPath(String namespace, String path){
        return new ResourceLocation(namespace, path);
        /*
        1.21+
        return ResourceLocation.of(namespace, path);
         */
    }
}
