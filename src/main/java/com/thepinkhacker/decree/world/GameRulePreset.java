package com.thepinkhacker.decree.world;

import com.google.gson.*;
import com.thepinkhacker.decree.Decree;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.world.GameRules;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

// TODO: Add the ability to save and load presets from world creation
public class GameRulePreset {
    public static final Path GAMERULE_PRESET_PATH = Path.of(FabricLoader.getInstance().getGameDir().toString(), Decree.MOD_ID, "gamerulepresets");

    public static void save(Path path, ServerWorld world) {
        JsonObject root = new JsonObject();
        JsonObject gamerules = new JsonObject();

        Map<GameRules.Key<?>, GameRules.Rule<?>> rules = world.getServer().getGameRules().rules;

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

    public static int load(Path path, ServerCommandSource source) {
        AtomicInteger i = new AtomicInteger();

        Gson gson = new Gson();

        String data;

        try {
            data = Files.readString(path, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        JsonObject root = gson.fromJson(data, JsonObject.class);
        JsonElement element = root.get("gamerules");

        if (element instanceof JsonObject presetRules) {
            ServerWorld world = source.getWorld();
            Map<GameRules.Key<?>, GameRules.Rule<?>> rules = world.getGameRules().rules;

            rules.forEach(((ruleKey, rule) -> {
                String ruleName = ruleKey.getName();

                for (Map.Entry<String, JsonElement> entry : presetRules.entrySet()) {
                    String jsonKey = entry.getKey();

                    if (!Objects.equals(ruleName, jsonKey)) continue;

                    JsonElement jsonValue = entry.getValue();

                    if (jsonValue instanceof JsonPrimitive primitive) {
                        if (primitive.isBoolean()) {
                            boolean value = primitive.getAsBoolean();

                            if (rule instanceof GameRules.BooleanRule booleanRule) {
                                if (booleanRule.get() == value) continue;

                                source.sendFeedback(
                                        () -> Text.translatable(
                                                "commands.gamerulepreset.load.change",
                                                ruleName,
                                                booleanRule.get(),
                                                value
                                        ),
                                        true
                                );
                                booleanRule.set(value, world.getServer());
                                i.getAndIncrement();
                            }
                        } else if (primitive.isNumber()) {
                            int value = primitive.getAsInt();

                            if (rule instanceof GameRules.IntRule intRule) {
                                if (intRule.get() == value) continue;

                                source.sendFeedback(
                                        () -> Text.translatable(
                                                "commands.gamerulepreset.load.change",
                                                ruleName,
                                                intRule.get(),
                                                value
                                        ),
                                        true
                                );
                                intRule.set(value, world.getServer());
                                i.getAndIncrement();
                            }
                        }
                    }
                }
            }));
        }

        return i.get();
    }

    private static void createDirectory(Path path) {
        Path directory = Files.isDirectory(path) ? path : path.getParent();
        directory.toFile().mkdirs();
    }
}
