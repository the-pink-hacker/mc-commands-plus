package com.thepinkhacker.decree.data.lang;

import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.thepinkhacker.decree.server.command.NameCommand;
import com.thepinkhacker.decree.world.DecreeGameRules;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.world.GameRules;

import java.util.concurrent.CompletableFuture;

public class LanguageProvider extends FabricLanguageProvider {
    public LanguageProvider(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(dataOutput, registryLookup);
    }

    @Override
    public void generateTranslations(RegistryWrapper.WrapperLookup lookup, TranslationBuilder builder) {
        add(
                builder,
                DecreeGameRules.DO_ENDERMAN_PICKUP,
                "Do Enderman Pickup",
                "Allow Endermen to pickup blocks."
        );
        add(
                builder,
                DecreeGameRules.DO_ENDERMAN_PLACE,
                "Do Enderman Place",
                "Allow Endermen to place blocks."
        );
        add(
                builder,
                DecreeGameRules.ITEM_DESPAWN_AGE,
                "Item Despawn Age",
                "Controls how long it takes for an item to despawn."
        );

        add(builder, NameCommand.ENTITY_FAILED, "Failed to name entity");
        add(builder, NameCommand.ENTITY_REMOVE_FAILED, "Failed to remove entity name");
        add(builder, NameCommand.ITEM_FAILED, "Failed to name item");
        add(builder, NameCommand.ITEM_REMOVE_FAILED, "Failed to remove item name");

        GenericTranslationBuilder.of(builder)
                .child(GenericTranslationBuilder.Node.of("commands")
                        .child(GenericTranslationBuilder.Node.of("clearspawnpoint")
                                .child("failed", "Failed to clear spawnpoint")
                                .child("success", "Cleared spawnpoint of %s player(s)")
                        )
                        .child(GenericTranslationBuilder.Node.of("cpride")
                                .child(GenericTranslationBuilder.Node.of("evict_riders")
                                        .child("failed", "Failed to evict rider(s)")
                                        .child("success", "Evicted rider(s)")
                                )
                                .child(GenericTranslationBuilder.Node.of("start_riding")
                                        .child("failed", "Failed to ride the entity")
                                        .child("success", "Mounted entities")
                                )
                                .child(GenericTranslationBuilder.Node.of("stop_riding")
                                        .child("failed", "Failed to dismount the entity")
                                        .child("success", "Dismounted entities")
                                )
                                .child(GenericTranslationBuilder.Node.of("summon_ride")
                                        .child("failed", "Failed to summon a ride")
                                        .child("success", "Summoned and mounted to a %s")
                                )
                                .child(GenericTranslationBuilder.Node.of("summon_rider")
                                        .child("failed", "Failed to summon a rider")
                                        .child("success", "Summoned and mounted a %s")
                                )
                        )
                        .child(GenericTranslationBuilder.Node.of("cpstop")
                                .child(GenericTranslationBuilder.Node.of("cancel")
                                        .child("failed", "Failed to cancel server stop")
                                        .child("success", "Canceled server stop")
                                )
                                .child("immediate", "Stopping server now")
                                .child("stop", "Stopped server")
                                .child("time", "Stopping server in %s second(s)...")
                        )
                        .child(GenericTranslationBuilder.Node.of("daylock")
                                .child("enabled", "Enabled daylock")
                                .child("disabled", "Disabled daylock")
                        )
                        .child(GenericTranslationBuilder.Node.of("gamerulepreset")
                                .child(GenericTranslationBuilder.Node.of("load")
                                        .child("changed", "Changed gamerule %s: %s -> %s")
                                        .child("failed", "Failed to load gamerule preset: %s")
                                        .child("unchanged", "No gamerules were affected by: %s")
                                        .child("success", "Loaded gamerule preset: %s")
                                )
                                .child(GenericTranslationBuilder.Node.of("save")
                                        .child("success", "Saved gamerule preset: %s")
                                )
                        )
                        .child(GenericTranslationBuilder.Node.of("head")
                                .child(GenericTranslationBuilder.Node.of("give")
                                        .child("failed", "Failed to give head to player")
                                        .child("success", "Gave head to player")
                                )
                                .child(GenericTranslationBuilder.Node.of("query")
                                        .child(GenericTranslationBuilder.Node.of("name")
                                                .child("success", "The head belongs to %s")
                                        )
                                        .child(GenericTranslationBuilder.Node.of("uuid")
                                                .child("success", "The head's owner's UUID is %s")
                                        )
                                )
                        )
                        .child(GenericTranslationBuilder.Node.of("health")
                                .child(GenericTranslationBuilder.Node.of("add")
                                        .child("failed", "Failed to add health")
                                        .child("success", "Added %s health")
                                )
                                .child(GenericTranslationBuilder.Node.of("query")
                                        .child("failed", "Failed to query health")
                                        .child("success", "Health is %s")
                                )
                                .child(GenericTranslationBuilder.Node.of("set")
                                        .child("failed", "Failed to set health")
                                        .child("success", "Set health to %s")
                                )
                        )
                        .child(GenericTranslationBuilder.Node.of("hunger")
                                .child(GenericTranslationBuilder.Node.of("add")
                                        .child(GenericTranslationBuilder.Node.of("exhaustion")
                                                .child("failed", "Failed to add exhaustion")
                                                .child("success", "Added %s exhaustion")
                                        )
                                        .child(GenericTranslationBuilder.Node.of("food")
                                                .child("failed", "Failed to add food")
                                                .child("success", "Added %s food")
                                        )
                                        .child(GenericTranslationBuilder.Node.of("saturation")
                                                .child("failed", "Failed to add saturation")
                                                .child("success", "Added %s saturation")
                                        )
                                )
                                .child(GenericTranslationBuilder.Node.of("query")
                                        .child(GenericTranslationBuilder.Node.of("exhaustion")
                                                .child("failed", "Failed to query exhaustion")
                                                .child("success", "Player's exhaustion is %s")
                                        )
                                        .child(GenericTranslationBuilder.Node.of("food")
                                                .child("failed", "Failed to query food")
                                                .child("success", "Player's food is %s")
                                        )
                                        .child(GenericTranslationBuilder.Node.of("saturation")
                                                .child("failed", "Failed to query saturation")
                                                .child("success", "Player's saturation is %s")
                                        )
                                )
                                .child(GenericTranslationBuilder.Node.of("set")
                                        .child(GenericTranslationBuilder.Node.of("exhaustion")
                                                .child("failed", "Failed to set exhaustion")
                                                .child("success", "Set exhaustion to %s")
                                        )
                                        .child(GenericTranslationBuilder.Node.of("food")
                                                .child("failed", "Failed to set food")
                                                .child("success", "Set food to %s")
                                        )
                                        .child(GenericTranslationBuilder.Node.of("saturation")
                                                .child("failed", "Failed to set saturation")
                                                .child("success", "Set saturation to %s")
                                        )
                                )
                        )
                        .child(GenericTranslationBuilder.Node.of("name")
                                .child(GenericTranslationBuilder.Node.of("entity")
                                        // TODO: Color name
                                        .child("name.success", "Named entity to \"%s\"")
                                        .child("remove.success", "Removed entity name")
                                )
                                .child(GenericTranslationBuilder.Node.of("item")
                                        // TODO: Color name
                                        .child("name.success", "Named item to \"%s\"")
                                        .child("remove.success", "Removed item name")
                                )
                        )
                        .child(GenericTranslationBuilder.Node.of("setowner")
                                .child("failed", "Failed to set pet's owner")
                                .child("success", "Set pet's owner to %s")
                        )
                        .child(GenericTranslationBuilder.Node.of("toggledownfall")
                                .child("success", "Toggled downfall")
                        )
                )
                .build();
    }

    private static void add(
            TranslationBuilder builder,
            GameRules.Key<?> gamerule,
            String title,
            String description
    ) {
        add(builder, gamerule, title);
        builder.add(gamerule.getTranslationKey() + ".description", description);
    }
    
    private static void add(TranslationBuilder builder, GameRules.Key<?> gamerule, String title) {
        builder.add(gamerule.getTranslationKey(), title);
    }

    private static void add(TranslationBuilder builder, SimpleCommandExceptionType exception, String value) {
        builder.add(exception.toString(), value);
    }
}
