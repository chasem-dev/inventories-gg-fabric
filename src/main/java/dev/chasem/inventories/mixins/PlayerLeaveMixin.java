package dev.chasem.inventories.mixins;

import dev.chasem.inventories.InventoryFabric;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerManager.class)
public class PlayerLeaveMixin {
    @Inject(at = @At("TAIL"), method = "remove(Lnet/minecraft/server/network/ServerPlayerEntity;)V")
    private void onPlayerConnect(ServerPlayerEntity playerEntity, CallbackInfo info) {
        InventoryFabric.getInstance().syncPlayer(playerEntity);
    }
}