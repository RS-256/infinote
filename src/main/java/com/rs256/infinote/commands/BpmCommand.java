//? if >=1.20.3 {
package com.rs256.infinote.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.rs256.infinote.compat.CommandCompat;
import com.rs256.infinote.compat.ComponentCompat;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.MutableComponent;

import java.util.Locale;

public class BpmCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("bpm")
                        .then(Commands.literal("set")
                                .then(Commands.argument("bpm", FloatArgumentType.floatArg(0.0F))
                                        .then(Commands.argument("tickPerQuarter", IntegerArgumentType.integer(1))
                                                .executes(context -> {
                                                    float bpm = FloatArgumentType.getFloat(context, "bpm");
                                                    int tickPerQuarter = IntegerArgumentType.getInteger(context, "tickPerQuarter");
                                                    return executeSet(context.getSource(), bpm, tickPerQuarter);
                                                })
                                        )
                                )
                        )
        );
    }

    private static int executeSet(CommandSourceStack source, float bpm, int tickPerQuarter) {
        double tps = (double) bpm * tickPerQuarter / 60.0D;
        if (tps == 0.0F) {
            source.sendFailure(ComponentCompat.literal("Calculated tps must not be 0"));
            return 0;
        }

        String tpsString = format(tps);
        source.getServer().getCommands().performPrefixedCommand(source, "tick rate " + tpsString);

        MutableComponent result = ComponentCompat.buildWithBreak(
                ComponentCompat.literal("BPM settings").withStyle(ChatFormatting.GOLD),
                ComponentCompat.literal("bpm: ").withStyle(ChatFormatting.GRAY)             .append(ComponentCompat.literal(format(bpm)).withStyle(ChatFormatting.YELLOW)),
                ComponentCompat.literal("tickPerQuarter: ").withStyle(ChatFormatting.GRAY)  .append(ComponentCompat.literal(String.valueOf(tickPerQuarter)).withStyle(ChatFormatting.YELLOW)),
                ComponentCompat.literal("tps: ").withStyle(ChatFormatting.GRAY)             .append(ComponentCompat.literal(tpsString).withStyle(ChatFormatting.GREEN))
        );

        CommandCompat.sourceSendSuccess(source, result, false);
        return 1;
    }

    private static String format(double value) {
        String formatted = String.format(Locale.ROOT, "%.6f", value);
        return formatted.indexOf('.') < 0 ? formatted : formatted.replaceAll("0+$", "").replaceAll("\\.$", "");
    }
}
//?}
