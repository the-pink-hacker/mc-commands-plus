package com.thepinkhacker.commandsplus.data;

import com.thepinkhacker.commandsplus.CommandsPlus;
import com.thepinkhacker.commandsplus.data.lang.LanguageProvider;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class DataGenerator implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
        pack.addProvider(LanguageProvider::new);
    }

    @Override
    public String getEffectiveModId() {
        return CommandsPlus.MOD_ID;
    }
}
