package com.rs256.infinote;

import com.rs256.infinote.commands.InfinoteCommand;
import com.rs256.infinote.commands.argument.IdStringArgumentType;
import com.rs256.infinote.config.InfinoteConfig;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Infinote implements ModInitializer {
	public static final String MOD_ID = "infinote";
	public static final String VERSION = /*$ mod_version*/ "0.3.0";
	public static final String MINECRAFT = /*$ minecraft*/ "1.21.11";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);


	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		LOGGER.info("He said the sky is the limit!");
		InfinoteConfig.load();
		registerCommands();
		IdStringArgumentType.registerArgumentTypes();
	}

	public void registerCommands() {
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> InfinoteCommand.register(dispatcher, registryAccess));
	}
}