package com.rs256.infinote.compat;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.NoteBlock;
import net.minecraft.world.level.block.state.BlockState;
//? if >=1.19 {
import net.minecraft.world.level.gameevent.GameEvent;
//?}

public class NoteBlockCompat {
    /**
     * should play as above sound, currently mob head only
     * must before bypass
     */
    //? if <=1.19.2 {
    /*public static boolean worksAboveNoteBlock(BlockState state) {
        // mob head inst is introduced at 1.19.4
        return false;
    }
    *///?} else if <=1.19.4 {
    /*public static boolean worksAboveNoteBlock(BlockState state) {
        return !state.getValue(NoteBlock.INSTRUMENT).requiresAirAbove();
    }
    *///?} else {
    public static boolean worksAboveNoteBlock(BlockState state) {
        return state.getValue(NoteBlock.INSTRUMENT).worksAboveNoteBlock();
    }
    //?}

    /**
     * vanilla gameEvent caller; sculk and allay should observe this
     */
    //? if <=1.18.2 {
    /*public static void notePlayedGameEvent(Level level, Entity entity, BlockPos blockPos) {
        // NOTE_BLOCK_PLAY is introduced at 1.19
    }
    *///?} else {
    public static void notePlayedGameEvent(Level level, Entity entity, BlockPos blockPos) {
        level.gameEvent(entity, GameEvent.NOTE_BLOCK_PLAY, blockPos);
    }
    //?}
}
