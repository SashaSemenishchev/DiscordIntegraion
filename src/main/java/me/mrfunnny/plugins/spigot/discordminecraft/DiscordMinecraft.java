package me.mrfunnny.plugins.spigot.discordminecraft;

import me.mrfunnny.plugins.spigot.discordminecraft.bot.Bot;
import me.mrfunnny.plugins.spigot.discordminecraft.bot.CommandListener;
import me.mrfunnny.plugins.spigot.discordminecraft.minecraft.Listeners;
import me.mrfunnny.plugins.spigot.discordminecraft.minecraft.commands.DiscordCommand;
import me.mrfunnny.plugins.spigot.discordminecraft.minecraft.data.PlayerData;
import me.mrfunny.discordauth.spigotdiscordauth.SpigotDiscordAuth;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.User;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.java.JavaPlugin;

import javax.security.auth.login.LoginException;
import java.util.HashMap;
import java.util.UUID;

public final class DiscordMinecraft extends JavaPlugin {

    public static JDA jda;
    public static HashMap<UUID, PlayerData> USERS = new HashMap<>();
    public static DiscordMinecraft instance;
    public static HashMap<User, UUID> waiting = new HashMap<>();

    public static SpigotDiscordAuth authPlugin;

    @Override
    public void onEnable() {
        instance = this;

        try {
            jda = new JDABuilder(getConfig().getString("bot-token")).setActivity(Activity.playing(Bukkit.getOnlinePlayers().size() + "/" + Bukkit.getMaxPlayers())).build();
        } catch (LoginException e) {
            e.printStackTrace();
        }

        jda.addEventListener(new Bot());
        jda.addEventListener(new CommandListener(this));
        for(Player p : Bukkit.getOnlinePlayers()) {
            USERS.put(p.getUniqueId(), new PlayerData(p.getUniqueId(), this));
        }
        Bukkit.getPluginManager().registerEvents(new Listeners(this), this);

        new DiscordCommand(this);

        Bukkit.getPluginManager().addPermission(new Permission("integration.forcelink"));

        if(Bukkit.getPluginManager().getPlugin("SpigotDiscordAuth") != null){
            authPlugin = (SpigotDiscordAuth) Bukkit.getPluginManager().getPlugin("SpigotDiscordAuth");
        } else {
            authPlugin = null;
        }

        getConfig().options().copyDefaults(true);
        saveConfig();
    }

    public boolean isUsingAuth(){
        return authPlugin != null;
    }

    @Override
    public void onDisable() {
        jda.shutdown();
    }

    public static String getDiscordByMinecraft(UUID uuid){
        try {
//            String data = instance.getConfig().getString("discords." + uuid);
//            if(data != null){
//                return data;
//            } else {
//                return null;
//            }
            return authPlugin.sql.getDiscord(uuid);
        }catch (NullPointerException e){
            return null;
        }

    }

    public static String getMinecraftByDiscord(String name){
        /*for(String key : instance.getConfig().getConfigurationSection("discords").getKeys(false)){
            String value = instance.getConfig().getString("discords." + key);
            if (value !=null && value.equals(name)) {
                return key;
            }
        }
        return null;*/

        return authPlugin.sql.getMinecraft(name);
    }

    public static PlayerData getPlayerData(Player player){
        return USERS.get(player.getUniqueId());
    }

    public static void link(UUID uuid, User user){
        authPlugin.sql.setDiscord(uuid, user.getId());
    }

//    public static void link(UUID uuid, String data){
//        instance.getConfig().set("discords." + uuid, data);
//        instance.saveConfig();
//    }
//
//    public static void link(UUID uuid, String name, String discriminator){
//        instance.getConfig().set("discords." + uuid, name + "#" + discriminator);
//        instance.saveConfig();
//    }

    public static String getPlayerAvatar(String name){
        return "https://minotar.net/avatar/" + name + "/512.png";
    }

    public static OfflinePlayer getOfflinePlayerByName(String name){
        for(OfflinePlayer op : Bukkit.getOfflinePlayers()){
            if(op.getName().equals(name)){
                return op;
            }
        }
        return null;
    }

}
