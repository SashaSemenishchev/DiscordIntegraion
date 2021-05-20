package me.mrfunnny.plugins.spigot.discordminecraft.minecraft.data;

import me.mrfunnny.plugins.spigot.discordminecraft.DiscordMinecraft;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.UUID;

public class PlayerData {
    private Player player;
    public PersistentDataContainer data;

    public PlayerData(UUID uuid, DiscordMinecraft plugin){
        this.player = Bukkit.getPlayer(uuid);
        this.data = this.player.getPersistentDataContainer();
        if(!data.has(new NamespacedKey(plugin, "discordConfirmed"), PersistentDataType.INTEGER)){
            data.set(new NamespacedKey(plugin, "discordConfirmed"), PersistentDataType.INTEGER, 0);
        }
    }

    public Player getPlayer() {
        return player;
    }
}
