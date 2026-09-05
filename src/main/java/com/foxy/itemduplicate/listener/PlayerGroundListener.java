package com.foxy.itemduplicate.listener;

import com.foxy.itemduplicate.service.GroundItemService;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public final class PlayerGroundListener implements Listener {

    private final GroundItemService groundItemService;

    public PlayerGroundListener(GroundItemService groundItemService) {
        this.groundItemService = groundItemService;
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerMove(PlayerMoveEvent event) {
        if (event.getTo() == null || hasNotChangedBlock(event)) {
            return;
        }

        groundItemService.scheduleGroundAction(event.getPlayer());
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerItemHeld(PlayerItemHeldEvent event) {
        groundItemService.scheduleGroundAction(event.getPlayer());
    }

    private boolean hasNotChangedBlock(PlayerMoveEvent event) {
        return event.getFrom().getBlockX() == event.getTo().getBlockX()
                && event.getFrom().getBlockY() == event.getTo().getBlockY()
                && event.getFrom().getBlockZ() == event.getTo().getBlockZ()
                && event.getFrom().getWorld().equals(event.getTo().getWorld());
    }
}