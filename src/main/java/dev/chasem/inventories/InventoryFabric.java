package dev.chasem.inventories;

import dev.chasem.inventories.adapter.FabricPlayerAdapter;
import gg.inventories.InventoriesCore;
import net.fabricmc.api.ModInitializer;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.concurrent.CompletableFuture;

public class InventoryFabric implements ModInitializer {

    private static InventoryFabric instance;
    private static ModConfig config = new ModConfig();

    private final FabricPlayerAdapter playerAdapter = new FabricPlayerAdapter();

    public static InventoryFabric getInstance() {
        return instance;
    }

    public static ModConfig getConfig() {
        return config;
    }

    @Override
    public void onInitialize() {
        instance = this;

        getConfig().readConfigFromFile();
        getConfig().saveConfigToFile();

        InventoriesCore.getLogger().info("Inventories.gg Fabric Mod Enabled.");
        //        InventoriesCore.API_URL = "http://localhost:3000/api";
        if (getClientSecret() == null) {
            try {
                throw (new Exception("Failed to Start Inventories.gg... Missing clientSecret in config."));
            } catch (Exception exception) {
                exception.printStackTrace();
                return;
            }
        } else {
            InventoriesCore.setClientSecret(getClientSecret());
        }

//        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> new ConfigCommand(getConfig()).register(dispatcher, true));


    }

    private String getClientSecret() {
        return getConfig().getValue("main.clientSecret", String.class);
    }

    public void syncPlayer(ServerPlayerEntity player) {
        CompletableFuture.runAsync(() -> InventoriesCore.sendUpdateRequest(playerAdapter.toJson(player)));
    }

}
