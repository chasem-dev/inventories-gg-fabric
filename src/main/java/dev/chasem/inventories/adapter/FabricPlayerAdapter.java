package dev.chasem.inventories.adapter;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import gg.inventories.InventoriesCore;
import gg.inventories.adapters.player.PlayerAdapter;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;

import static dev.chasem.inventories.adapter.FabricItemAdapter.airJson;

public class FabricPlayerAdapter extends PlayerAdapter<ServerPlayerEntity, FabricItemAdapter> {

    @Override
    public JsonObject toJson(ServerPlayerEntity player) {
        JsonObject playerInfo = new JsonObject();

        playerInfo.addProperty("username", player.getName().getString().replace("[", "").replace("]", ""));
        playerInfo.addProperty("displayName", player.getDisplayName().getString());
        playerInfo.addProperty("ping", player.pingMilliseconds); // TODO
        playerInfo.addProperty("uuid", player.getUuid().toString());

        playerInfo.addProperty("level", player.experienceLevel);

        playerInfo.addProperty("exp", player.experienceProgress);
        playerInfo.addProperty("totalExp", player.totalExperience);
        playerInfo.addProperty("expToLevel", player.experienceProgress);

        playerInfo.addProperty("health", player.getHealth());
        playerInfo.addProperty("hunger", player.getHungerManager().getFoodLevel());

        JsonArray inventoryJson = new JsonArray();

        for (int i = 0; i < player.getInventory().armor.size(); i++) {
            ItemStack item = player.getInventory().armor.get(i);

            if (!item.isEmpty() && item.getItem() != Items.AIR) {
                inventoryJson.add(this.getItemAdapter().toJson(item));
            } else {
                inventoryJson.add(airJson);
            }
        }
        InventoriesCore.getLogger().fine("Armor of " + player.getName() + " logged.");

        for (int i = 0; i < player.getInventory().main.size(); i++) {
            ItemStack item = player.getInventory().main.get(i);

            if (!item.isEmpty() && item.getItem() != Items.AIR) {
                inventoryJson.add(this.getItemAdapter().toJson(item));
            } else {
                inventoryJson.add(airJson);
            }
        }
        InventoriesCore.getLogger().fine("Inventory of " + player.getName() + " logged.");

        if (!player.getInventory().offHand.isEmpty() && player.getInventory().offHand.get(0).getItem() != Items.AIR) {
            inventoryJson.add(getItemAdapter().toJson(player.getInventory().offHand.get(0)));
        } else {
            inventoryJson.add(airJson);
        }
        InventoriesCore.getLogger().fine("Offhand of " + player.getName() + " logged.");

        JsonArray enderInventoryJson = new JsonArray();

        for (int i = 0; i < player.getEnderChestInventory().size(); i++) {
            ItemStack item = player.getEnderChestInventory().getStack(i);

            if (!item.isEmpty() && item.getItem() != Items.AIR) {
                enderInventoryJson.add(this.getItemAdapter().toJson(item));
            } else {
                enderInventoryJson.add(airJson);
            }
        }
        InventoriesCore.getLogger().fine("Enderchest of " + player.getName() + " logged.");

        playerInfo.add("inventory", inventoryJson);

        playerInfo.add("enderChest", enderInventoryJson);
        InventoriesCore.getLogger().info("Syncing " + player.getName().getString());
        return playerInfo;
    }

    @Override
    public FabricItemAdapter getItemAdapter() {
        return new FabricItemAdapter();
    }
}
