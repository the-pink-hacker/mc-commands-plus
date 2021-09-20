package com.ryangar46.commandsplus;

import com.ryangar46.commandsplus.commands.HealthCommand;
import com.ryangar46.commandsplus.commands.HungerCommand;
import com.ryangar46.commandsplus.commands.NameCommand;
import com.ryangar46.commandsplus.commands.SetOwnerCommand;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;

public class Main implements ModInitializer {
	@Override
	public void onInitialize() {
		CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
			HealthCommand.register(dispatcher);
			HungerCommand.register(dispatcher);
			NameCommand.register(dispatcher);
			SetOwnerCommand.register(dispatcher);
		});
	}
}
