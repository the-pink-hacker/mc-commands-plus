package com.ryangar46.commandsplus.world;

import com.google.gson.*;
import com.ryangar46.commandsplus.CommandsPlus;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Objects;

public class GameRulePreset {
    public static final Path GAMERULE_PRESET_PATH = Path.of(FabricLoader.getInstance().getGameDir().toString(), CommandsPlus.MOD_ID, "gamerulepresets");

    public static void save(Path path, World world) {
        JsonObject root = new JsonObject();
        JsonObject gamerules = new JsonObject();

        Map<GameRules.Key<?>, GameRules.Rule<?>> rules = world.getGameRules().rules;

        rules.forEach((key, rule) -> {
            if (rule instanceof GameRules.BooleanRule booleanRule) gamerules.addProperty(key.getName(), booleanRule.get());
            else if (rule instanceof GameRules.IntRule intRule) gamerules.addProperty(key.getName(), intRule.get());
            else gamerules.add(key.getName(), JsonNull.INSTANCE);
        });

        root.add("gamerules", gamerules);

        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        createDirectory(path);

        try {
            Files.writeString(path, gson.toJson(root), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean load(Path path, ServerWorld world) {
        if (!Files.exists(path)) return false;

        Gson gson = new Gson();

        String data;

        try {
            data = Files.readString(path, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (data != null) {
            JsonObject root = gson.fromJson(data, JsonObject.class);

            JsonElement element = root.get("gamerules");

            if (element instanceof JsonObject presetRules) {
                Map<GameRules.Key<?>, GameRules.Rule<?>> rules = world.getGameRules().rules;

                rules.forEach(((ruleKey, rule) -> {
                    String ruleName = ruleKey.getName();

                    for (Map.Entry<String, JsonElement> entry : presetRules.entrySet()) {
                        String jsonKey = entry.getKey();

                        if (Objects.equals(ruleName, jsonKey)) {
                            JsonElement jsonValue = entry.getValue();
                            if (jsonValue instanceof JsonPrimitive primitive) {
                                if (primitive.isBoolean()) {
                                    boolean value = primitive.getAsBoolean();
                                    if (rule instanceof GameRules.BooleanRule booleanRule) booleanRule.set(value, world.getServer());
                                } else if (primitive.isNumber()) {
                                    int value = primitive.getAsInt();
                                    if (rule instanceof GameRules.IntRule intRule) intRule.set(value, world.getServer());
                                }
                            }
                        }
                    }
                }));
            }
        }

        return true;
    }

    private static void createDirectory(Path path) {
        Path directory = Files.isDirectory(path) ? path : path.getParent();
        directory.toFile().mkdirs();
    }
}
