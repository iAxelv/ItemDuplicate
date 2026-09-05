package com.foxy.itemduplicate;

import com.foxy.itemduplicate.listener.PlayerGroundListener;
import com.foxy.itemduplicate.service.GroundItemService;
import org.bukkit.plugin.java.JavaPlugin;

public final class ItemDuplicate extends JavaPlugin {

    @Override
    public void onEnable() {
        saveDefaultConfig();

        GroundItemService groundItemService = new GroundItemService(this);
        getServer().getPluginManager().registerEvents(
                new PlayerGroundListener(groundItemService),
                this
        );
    }

    @Override
    public void onDisable() {
    }
}
