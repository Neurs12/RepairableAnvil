package dev.neurs.repairableanvil;

import org.bukkit.plugin.java.JavaPlugin;

public final class RepairableAnvil extends JavaPlugin {

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new AnvilEvents(), this);
    }

    @Override
    public void onDisable() {
    }
}
