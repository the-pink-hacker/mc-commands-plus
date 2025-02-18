package com.thepinkhacker.decree.data;

import com.thepinkhacker.decree.Decree;
import com.thepinkhacker.decree.data.lang.LanguageProvider;
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
        return Decree.MOD_ID;
    }
}
