package com.foxy.itemduplicate.listener;

import com.foxy.itemduplicate.service.GroundItemService;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class PlayerGroundListener implements Listener {

    private final GroundItemService groundItemService;
    private final Map<UUID, Location> previousBlocks = new HashMap<>();

    public PlayerGroundListener(GroundItemService groundItemService) {
        this.groundItemService = groundItemService;
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerMove(PlayerMoveEvent event) {
        if (event.getTo() == null || hasNotChangedBlock(event)) {
            return;
        }

        Location previousBlock = event.getFrom().getBlock().getLocation();
        previousBlocks.put(event.getPlayer().getUniqueId(), previousBlock);
        groundItemService.scheduleGroundAction(event.getPlayer(), previousBlock);
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerItemHeld(PlayerItemHeldEvent event) {
        Location previousBlock = previousBlocks.get(event.getPlayer().getUniqueId());
        if (previousBlock != null) {
            groundItemService.scheduleGroundAction(event.getPlayer(), previousBlock);
        }
    }

    private boolean hasNotChangedBlock(PlayerMoveEvent event) {
        return event.getFrom().getBlockX() == event.getTo().getBlockX()
                && event.getFrom().getBlockY() == event.getTo().getBlockY()
                && event.getFrom().getBlockZ() == event.getTo().getBlockZ()
                && event.getFrom().getWorld().equals(event.getTo().getWorld());
    }
}