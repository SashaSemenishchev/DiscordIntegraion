package me.mrfunnny.plugins.spigot.discordminecraft.minecraft;

import me.mrfunnny.plugins.spigot.discordminecraft.DiscordMinecraft;
import me.mrfunnny.plugins.spigot.discordminecraft.minecraft.data.PlayerData;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class Listeners implements Listener {

    private DiscordMinecraft plugin;
    public Listeners(DiscordMinecraft plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        DiscordMinecraft.USERS.put(e.getPlayer().getUniqueId(), new PlayerData(e.getPlayer().getUniqueId(), plugin));
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e){
        DiscordMinecraft.USERS.remove(e.getPlayer().getUniqueId());
    }

}
