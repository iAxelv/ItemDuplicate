package com.foxy.itemduplicate.service;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class GroundItemService {

    private final JavaPlugin plugin;
    private final Map<UUID, BukkitTask> pendingActions = new HashMap<>();
    private final long delayTicks;

    public GroundItemService(JavaPlugin plugin) {
        this.plugin = plugin;
        this.delayTicks = Math.max(0L, plugin.getConfig().getLong("action-delay-ticks", 10L));
    }

    public void scheduleGroundAction(Player player) {
        UUID playerId = player.getUniqueId();
        if (pendingActions.containsKey(playerId)) {
            return;
        }

        Location targetLocation = player.getLocation().getBlock().getLocation();
        ItemStack heldItem = player.getInventory().getItemInMainHand().clone();
        BukkitTask task = Bukkit.getScheduler().runTaskLater(plugin, () -> {
            pendingActions.remove(playerId);
            applyGroundAction(player, targetLocation, heldItem);
        }, delayTicks);
        pendingActions.put(playerId, task);
    }

    private void applyGroundAction(Player player, Location targetLocation, ItemStack heldItem) {
        if (!player.isOnline()) {
            return;
        }

        Block groundBlock = targetLocation.clone().subtract(0, 1, 0).getBlock();
        if (groundBlock.getType().isAir()) {
            return;
        }

        Material heldMaterial = heldItem.getType();

        if (heldMaterial.isBlock()) {
            if (groundBlock.getType() != heldMaterial) {
                groundBlock.setType(heldMaterial, false);
            }
            return;
        }

        groundBlock.setType(Material.AIR, false);
        if (!heldItem.getType().isAir()) {
            dropSingleItem(groundBlock, heldItem);
        }
    }

    private void dropSingleItem(Block groundBlock, ItemStack heldItem) {
        ItemStack droppedItem = heldItem.clone();
        droppedItem.setAmount(1);
        Location dropLocation = groundBlock.getLocation().add(0.5, 0.5, 0.5);
        groundBlock.getWorld().dropItemNaturally(dropLocation, droppedItem);
    }
}