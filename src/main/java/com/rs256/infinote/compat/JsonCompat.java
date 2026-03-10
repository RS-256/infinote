package com.rs256.infinote.compat;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.Reader;

public class JsonCompat {
    public static JsonElement read(Reader reader) {
        //? if <=1.17.1 {
        /*return new JsonParser().parse(reader);
         *///?} else {
        return JsonParser.parseReader(reader);
        //?}
    }
}
