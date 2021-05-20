package me.mrfunnny.plugins.spigot.discordminecraft.minecraft.commands;

import me.mrfunnny.plugins.spigot.discordminecraft.DiscordMinecraft;
import me.mrfunnny.plugins.spigot.discordminecraft.bot.Bot;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static net.dv8tion.jda.api.requests.ErrorResponse.CANNOT_SEND_TO_USER;

public class DiscordCommand implements CommandExecutor, TabCompleter {

    public DiscordMinecraft plugin;
    public DiscordCommand(DiscordMinecraft plugin){
        this.plugin = plugin;
        plugin.getCommand("discord").setExecutor(this);
        plugin.getCommand("discord").setTabCompleter(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(sender instanceof Player){
            Player p = (Player)sender;
            try {
                if (args.length == 2) {
                    if (args[0].equalsIgnoreCase("link")) {
                        if (!DiscordMinecraft.authPlugin.sql.isDiscordLinked(p.getUniqueId()) && DiscordMinecraft.authPlugin.getPlayerData(p).isFrozen()) {
                            String[] data = args[1].split("#");
                            User linkUser;
                            if (data.length == 2) {
                                linkUser = DiscordMinecraft.jda.getUserByTag(data[0], data[1]);
                            } else {
                                if (DiscordMinecraft.jda.getUserById(args[1]) != null) {
                                    linkUser = DiscordMinecraft.jda.getUserById(args[1]);
                                } else {
                                    linkUser = null;
                                }
                            }
                            if(linkUser == null){
                                p.sendMessage("You must join");
                                return false;
                            }
                            if (Bot.guild.getMember(linkUser) != null) {
                                DiscordMinecraft.waiting.put(linkUser, p.getUniqueId());
                                linkUser.openPrivateChannel().flatMap((channel) -> {
                                    try {
                                        return channel.sendMessage(new EmbedBuilder()
                                                .setAuthor(DiscordMinecraft.jda.getSelfUser().getName(), null, DiscordMinecraft.jda.getSelfUser().getEffectiveAvatarUrl())
                                                .addField("Привязка аккаунта", "Игрок " + p.getName() + " запросил привязку к вашему аккаунту", false)
                                                .setColor(Color.GRAY)
                                                .build());
                                    } catch (Exception ex){
                                        ex.printStackTrace();
                                    } finally {
                                        channel.close().queue();
                                    }
                                    return null;
                                }).onErrorFlatMap(CANNOT_SEND_TO_USER::test, error -> {
                                    DiscordMinecraft.waiting.remove(linkUser);
                                    Bukkit.getScheduler().runTask(plugin, () -> p.sendMessage(ChatColor.RED + "Пожалуйста, откройте лс на время привязки"));
                                    return null;
                                }).queue(message -> {
                                    message.addReaction("\u2705").queue();
                                    message.addReaction("\u274c").queue();
                                });
                            } else {
                                p.sendMessage(ChatColor.RED + "Вы должны быть на дискорд сервере https://discord.gg/ne9nrKbxHd");
                            }
                        } else if(DiscordMinecraft.authPlugin.sql.isDiscordLinked(p.getUniqueId()) && !DiscordMinecraft.authPlugin.getPlayerData(p).isFrozen()){
                            String[] data = args[1].split("#");
                            User linkUser;
                            if (data.length == 2) {
                                linkUser = DiscordMinecraft.jda.getUserByTag(data[0], data[1]);

                            } else {
                                if (DiscordMinecraft.jda.getUserById(args[1]) != null) {
                                    linkUser = DiscordMinecraft.jda.getUserById(args[1]);
                                } else {
                                    linkUser = null;
                                }
                            }

                            if (Bot.guild.getMember(linkUser) != null) {
                                DiscordMinecraft.waiting.put(linkUser, p.getUniqueId());
                                linkUser.openPrivateChannel().queue((channel) -> channel.sendMessage(new EmbedBuilder()
                                        .setAuthor(DiscordMinecraft.jda.getSelfUser().getName(), null, DiscordMinecraft.jda.getSelfUser().getEffectiveAvatarUrl())
                                        .addField("Привязка аккаунта", "Игрок " + p.getName() + " запросил привязку к вашему аккаунту", false)
                                        .setColor(Color.GRAY)
                                        .build()).queue((message) -> {
                                    message.addReaction("\u2705").queue();
                                    message.addReaction("\u274c").queue();
                                }));
                            } else {
                                p.sendMessage(ChatColor.RED + "Вы должны быть на дискорд сервере https://discord.gg/UbgeFSPTCY");
                            }
                        }
                        else {
                            p.sendMessage(ChatColor.RED + "Вам нужно подтвердить свой старый аккаунт, чтобы привязать новый!");
                        }
                    } else {
                        sender.sendMessage(ChatColor.RED + "Ошибка в агрументах(длинна)");
                    }
                    return true;
                }
            } catch(NullPointerException ex){
                p.sendMessage(ChatColor.RED + "null");
                ex.printStackTrace();
            }

        }

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if(args.length == 0){
            return sender.hasPermission("integration.forcelink") ? Arrays.asList("link", "forcelink") : Arrays.asList("link");
        } else if(args.length == 1){
            if(args[0].equalsIgnoreCase("link")){
                return Arrays.asList();
            }
        }
         return sender.hasPermission("integration.forcelink") ? Arrays.asList("link", "forcelink") : Collections.singletonList("link");
    }
}
