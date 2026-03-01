package com.rs256.infinote.commands.argument;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.rs256.infinote.Infinote;
import com.rs256.infinote.compat.IdCompat;
import net.fabricmc.fabric.api.command.v2.ArgumentTypeRegistry;
import net.minecraft.commands.synchronization.SingletonArgumentInfo;
import net.minecraft.network.chat.Component;

public final class IdStringArgumentType implements ArgumentType<String> {

    private static final SimpleCommandExceptionType INVALID_ID = new SimpleCommandExceptionType(Component.literal("Invalid id"));

    private IdStringArgumentType() {}

    public static IdStringArgumentType id() {
        return new IdStringArgumentType();
    }

    public static String getId(CommandContext<?> ctx, String name) {
        return ctx.getArgument(name, String.class);
    }

    @Override
    public String parse(StringReader reader) throws CommandSyntaxException {
        int start = reader.getCursor();

        while (reader.canRead() && isAllowed(reader.peek())) {
            reader.skip();
        }

        String raw = reader.getString().substring(start, reader.getCursor());
        String normalized = IdCompat.normalize(raw);
        if (normalized == null) {
            reader.setCursor(start);
            throw INVALID_ID.createWithContext(reader);
        }

        if (IdCompat.idFromString(normalized) == null) {
            reader.setCursor(start);
            throw INVALID_ID.createWithContext(reader);
        }

        return normalized;
    }

    private static boolean isAllowed(char c) {
        // MinecraftのIDで一般的に使われる範囲 許容文字: [a-z0-9_.:]
        return (c >= 'a' && c <= 'z')
                || (c >= '0' && c <= '9')
                || c == '_' || c == ':' || c == '.';
    }

    public static void registerArgumentTypes() {
        ArgumentTypeRegistry.registerArgumentType(
                IdCompat.idFromNamespaceAndPath(Infinote.MOD_ID, "id_string"),
                IdStringArgumentType.class,
                SingletonArgumentInfo.contextFree(IdStringArgumentType::id)
        );
    }
}
