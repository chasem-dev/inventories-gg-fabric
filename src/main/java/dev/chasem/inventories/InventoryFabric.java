package dev.chasem.inventories;

import dev.chasem.inventories.adapter.FabricPlayerAdapter;
import gg.inventories.InventoriesCore;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.MinecraftDedicatedServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;

import java.util.concurrent.*;

public class InventoryFabric implements ModInitializer {

    private static InventoryFabric instance;
    private static ModConfig config = new ModConfig();

    private final FabricPlayerAdapter playerAdapter = new FabricPlayerAdapter();
    private static MinecraftServer mcServer;

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
        ServerLifecycleEvents.SERVER_STARTING.register(server -> InventoryFabric.mcServer = server);

        ServerPlayConnectionEvents.JOIN.register((ServerPlayNetworkHandler handler, PacketSender sender, MinecraftServer server)
                -> syncPlayer(handler.player));

        ServerPlayConnectionEvents.DISCONNECT.register((ServerPlayNetworkHandler handler, MinecraftServer server)
                -> syncPlayer(handler.player));

        Runnable syncAllPlayersTask = () -> {
            if(mcServer == null) { return; }

            for (ServerPlayerEntity player : mcServer.getOverworld().getPlayers()) {
                syncPlayer(player);
            }
        };

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(syncAllPlayersTask, 2, 2, TimeUnit.MINUTES);
    }

    private String getClientSecret() {
        return getConfig().getValue("main.clientSecret", String.class);
    }

    public void syncPlayer(ServerPlayerEntity player) {
        CompletableFuture.runAsync(() -> InventoriesCore.sendUpdateRequest(playerAdapter.toJson(player)));
    }

}
