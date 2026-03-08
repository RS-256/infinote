package com.rs256.infinote.compat;

import net.minecraft.world.level.block.Block;
//? if <=1.19.2 {
/*import net.minecraft.core.Registry;
 *///?} else {
import net.minecraft.core.registries.BuiltInRegistries;
//?}

public class RegistryCompat {
    //? if <=1.19.2 {
    /*public static String getKey(Block block) {
        return Registry.BLOCK.getKey(block).toString();
    }
     *///?} else {
    public static String getKey(Block block) {
        return BuiltInRegistries.BLOCK.getKey(block).toString();
    }
    //?}
}
