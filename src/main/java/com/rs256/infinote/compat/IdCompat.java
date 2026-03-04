package com.rs256.infinote.compat;

//? if <=1.21.10 {
/*import net.minecraft.resources.ResourceLocation;
 *///?} else {
import com.rs256.infinote.Infinote;
//?}
import net.minecraft.resources.Identifier;

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

    *///?} else {
    public static Identifier idFromString(String raw) {
        return Identifier.tryParse(raw);
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
