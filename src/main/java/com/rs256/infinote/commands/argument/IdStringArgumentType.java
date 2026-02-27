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

/**
 * namespace:path 形式の ID をクオート無しで受け取るための ArgumentType。
 * 戻り値は「正規化済みの String」（minecraft: 省略対応）。
 *
 * - 許容文字: [a-z0-9_.:]（= Identifier/ResourceLocation で一般的な範囲）
 * - 末尾まで読み進め、空白で終了
 */
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

        // 「空白」までを読む（ただし許可した文字以外が来たら止める）
        while (reader.canRead() && isAllowed(reader.peek())) {
            reader.skip();
        }

        String raw = reader.getString().substring(start, reader.getCursor());
        String normalized = IdCompat.normalize(raw);
        if (normalized == null) {
            reader.setCursor(start);
            throw INVALID_ID.createWithContext(reader);
        }

        // 実際にIDとして構築できるかチェック（ここで弾けば後段で try-catch 不要）
        if (IdCompat.idFromString(normalized) == null) {
            reader.setCursor(start);
            throw INVALID_ID.createWithContext(reader);
        }

        return normalized;
    }

    private static boolean isAllowed(char c) {
        // MinecraftのIDで一般的に使われる範囲
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
