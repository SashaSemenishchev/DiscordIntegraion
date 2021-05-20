package me.mrfunnny.plugins.spigot.discordminecraft.bot;

import club.minnced.discord.webhook.WebhookClient;
import club.minnced.discord.webhook.WebhookClientBuilder;
import club.minnced.discord.webhook.send.WebhookEmbed;
import club.minnced.discord.webhook.send.WebhookEmbedBuilder;
import club.minnced.discord.webhook.send.WebhookMessageBuilder;
import me.mrfunnny.plugins.spigot.discordminecraft.DiscordMinecraft;
import me.mrfunnny.plugins.spigot.discordminecraft.minecraft.data.Log4JFilter;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.priv.react.PrivateMessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;

import javax.annotation.Nonnull;
import java.awt.*;
import java.util.UUID;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class Bot extends ListenerAdapter implements Listener {

    private static DiscordMinecraft plugin = DiscordMinecraft.instance;
    public static Guild guild;

    private WebhookClient client;
    private WebhookClient consoleClient;

    public Bot(){
        WebhookClientBuilder wcb = new WebhookClientBuilder(DiscordMinecraft.instance.getConfig().getString("main-channel.webhook-url"));
        wcb.setThreadFactory((job) -> {
            Thread thread = new Thread(job);
            thread.setName("Integrator-Thread");

            thread.setDaemon(true);
            return thread;
        });
        this.client = wcb.build();

        WebhookClientBuilder consoleBuilder = new WebhookClientBuilder("https://discordapp.com/api/webhooks/792300495888056351/cjA0qzUVZ_tEEe5TZSMHZTXSWvO2mBueoUFKgNwlYnMzIy1na0P3OElIaZpY19wAkFbE");
        consoleBuilder.setThreadFactory((job) -> {
            Thread thread = new Thread(job);
            thread.setName("Console-Thread");

            thread.setDaemon(true);
            return thread;
        });
        this.consoleClient = consoleBuilder.build();
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @Override
    public void onReady(@Nonnull ReadyEvent event) {
        guild = DiscordMinecraft.jda.getGuildById("778248042331242506");
        Bukkit.getLogger().info(guild.getName());
        Logger coreLogger = (Logger) LogManager.getRootLogger();
        coreLogger.addFilter(new Log4JFilter(this));
        Bukkit.getLogger().addHandler(new Handler() {
            @Override
            public void publish(LogRecord record) {
                WebhookMessageBuilder wmb = new WebhookMessageBuilder();
                wmb.setUsername("Console");
                wmb.setAvatarUrl("https://d1nhio0ox7pgb.cloudfront.net/_img/g_collection_png/standard/512x512/console.png");
                wmb.setContent("[" +  record.getLevel() + "] " + record.getMessage());

                consoleClient.send(wmb.build());
            }

            @Override
            public void flush() {

            }

            @Override
            public void close() throws SecurityException {

            }
        });
    }

    public WebhookClient getClient() {
        return consoleClient;
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e){
        if(e.getMessage().startsWith(plugin.getConfig().getString("main-channel.prefix"))){
            WebhookMessageBuilder builder = new WebhookMessageBuilder();
            String content = e.getMessage().substring(1);
            if(DiscordMinecraft.getDiscordByMinecraft(e.getPlayer().getUniqueId())!=null){
                User sender = DiscordMinecraft.jda.getUserById(DiscordMinecraft.getDiscordByMinecraft(e.getPlayer().getUniqueId()));
                if(guild.getMember(sender) != null){
                    builder.setUsername(guild.getMember(sender).getEffectiveName());
                } else {
                    builder.setUsername(sender.getName());
                }
                builder.setAvatarUrl(sender.getEffectiveAvatarUrl().replaceFirst("gif", "png" + "?size=512"));
            } else {
                builder.setAvatarUrl(DiscordMinecraft.getPlayerAvatar(e.getPlayer().getName()));
                builder.setUsername(e.getPlayer().getName());
            }
            builder.setContent(content);
            client.send(builder.build());
        }

    }

    @Override
    public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event) {
        String searchData = event.getAuthor().getId();
        if(!event.getAuthor().isBot()){
            if(event.getChannel().getId().equalsIgnoreCase(plugin.getConfig().getString("main-channel.id"))){
                if(DiscordMinecraft.getMinecraftByDiscord(searchData) != null){
    //
                    net.md_5.bungee.api.chat.TextComponent name = new net.md_5.bungee.api.chat.TextComponent(Bukkit.getOfflinePlayer(UUID.fromString(DiscordMinecraft.getMinecraftByDiscord(searchData))).getName());
                    name.setColor(net.md_5.bungee.api.ChatColor.BLUE);
                    User user = DiscordMinecraft.jda.getUserById(searchData);
                    name.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Пользователь Discord: " + user.getName() + "#" + user.getDiscriminator())));
                    net.md_5.bungee.api.chat.TextComponent beforeName = new net.md_5.bungee.api.chat.TextComponent("[" + ChatColor.AQUA + "G" + ChatColor.RESET + "] | ");
                    net.md_5.bungee.api.chat.TextComponent content = new TextComponent(": " + event.getMessage().getContentRaw());
                    content.setColor(net.md_5.bungee.api.ChatColor.WHITE);
                    Bukkit.getServer().spigot().broadcast(beforeName, name, content);
                    //, name, TextComponent.fromLegacyText(ChatColor.WHITE + " ")
    //                Bukkit.broadcastMessage( + ChatColor.BLUE + name + ChatColor.WHITE + ": " + event.getMessage().getContentRaw());
                }
            } else if(event.getChannel().getId().equalsIgnoreCase("792296974283440129")){
                if(event.getMessage().getContentRaw().startsWith("&")){
                    String cmd = event.getMessage().getContentRaw().substring(1);
                    Bukkit.getScheduler().runTask(plugin, () -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd));
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onJoin(PlayerJoinEvent e){
        DiscordMinecraft.jda.getPresence().setActivity(Activity.playing(Bukkit.getOnlinePlayers().size() + "/" + Bukkit.getMaxPlayers()));
        WebhookMessageBuilder builder = new WebhookMessageBuilder();
        WebhookEmbedBuilder eb = new WebhookEmbedBuilder();
        if(DiscordMinecraft.getDiscordByMinecraft(e.getPlayer().getUniqueId())!=null){
            User sender = DiscordMinecraft.jda.getUserById(DiscordMinecraft.getDiscordByMinecraft(e.getPlayer().getUniqueId()));
            eb.setAuthor(new WebhookEmbed.EmbedAuthor(sender.getName() + " подключился", sender.getEffectiveAvatarUrl().replaceFirst("gif", "png" + "?size=512"), null));
        } else {
            eb.setAuthor(new WebhookEmbed.EmbedAuthor(e.getPlayer().getName() + " подключился", DiscordMinecraft.getPlayerAvatar(e.getPlayer().getName()), null));
        }
        eb.setColor(5635925);
        builder.addEmbeds(eb.build());
        builder.setAvatarUrl(DiscordMinecraft.jda.getSelfUser().getEffectiveAvatarUrl());
        builder.setUsername(DiscordMinecraft.jda.getSelfUser().getName());
        this.client.send(builder.build());
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e){
        DiscordMinecraft.jda.getPresence().setActivity(Activity.playing(Bukkit.getOnlinePlayers().size() - 1 + "/" + Bukkit.getMaxPlayers()));
        WebhookMessageBuilder builder = new WebhookMessageBuilder();
        WebhookEmbedBuilder eb = new WebhookEmbedBuilder();
        if(DiscordMinecraft.getDiscordByMinecraft(e.getPlayer().getUniqueId())!=null){
            User sender = DiscordMinecraft.jda.getUserById(DiscordMinecraft.getDiscordByMinecraft(e.getPlayer().getUniqueId()));
            eb.setAuthor(new WebhookEmbed.EmbedAuthor(sender.getName() + " отключился", sender.getEffectiveAvatarUrl().replaceFirst("gif", "png" + "?size=512"), null));
        } else {
            eb.setAuthor(new WebhookEmbed.EmbedAuthor(e.getPlayer().getName() + " отключился", DiscordMinecraft.getPlayerAvatar(e.getPlayer().getName()), null));
        }
        eb.setColor(16733525);
        builder.setAvatarUrl(DiscordMinecraft.jda.getSelfUser().getEffectiveAvatarUrl().replaceFirst("gif", "png" + "?size=512"));
        builder.setUsername(DiscordMinecraft.jda.getSelfUser().getName());
        builder.addEmbeds(eb.build());
        this.client.send(builder.build());
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e){
        WebhookMessageBuilder builder = new WebhookMessageBuilder();
        WebhookEmbedBuilder eb = new WebhookEmbedBuilder();
        if(DiscordMinecraft.getDiscordByMinecraft(e.getEntity().getUniqueId())!=null){
            User sender = getUser(DiscordMinecraft.getDiscordByMinecraft(e.getEntity().getUniqueId()));
            eb.setAuthor(new WebhookEmbed.EmbedAuthor(e.getDeathMessage(), sender.getEffectiveAvatarUrl().replaceFirst("gif", "png" + "?size=512"), null));
        }
        builder.setAvatarUrl(DiscordMinecraft.jda.getSelfUser().getEffectiveAvatarUrl());
        builder.setUsername(DiscordMinecraft.jda.getSelfUser().getName());
        builder.addEmbeds(eb.build());
        this.client.send(builder.build());
    }

    @Override
    public void onPrivateMessageReactionAdd(@Nonnull PrivateMessageReactionAddEvent event) {
        System.out.println("called"); // /discord link MrFunny#8972
        try{
            if(!event.getUser().isBot()){
                boolean result;
                if(event.getReaction().getReactionEmote().equals(MessageReaction.ReactionEmote.fromUnicode("\u2705", DiscordMinecraft.jda))){
                    if(DiscordMinecraft.waiting.containsKey(event.getUser())){
                        System.out.println("Trying to link");
                        DiscordMinecraft.link(DiscordMinecraft.waiting.get(event.getUser()), event.getUser());
                        event.getChannel().editMessageById(event.getMessageId(), new EmbedBuilder()
                                .setColor(Color.GREEN)
                                .setAuthor(DiscordMinecraft.jda.getSelfUser().getName(), null, DiscordMinecraft.jda.getSelfUser().getEffectiveAvatarUrl())
                                .addField("Привязка аккаунта", "Вы успешно привязяли свой аккаунт к " + Bukkit.getOfflinePlayer(DiscordMinecraft.waiting.get(event.getUser())).getName(), false)
                                .build()).queue((message) -> {
                            message.removeReaction("\u2705").queue();
                            message.removeReaction("\u274c").queue();
                        });
                    }
                    guild.removeRoleFromMember(event.getUserId(), DiscordMinecraft.jda.getRoleById(DiscordMinecraft.instance.getConfig().getString("guest-role-id"))).queue();
                    guild.addRoleToMember(event.getUserId(), DiscordMinecraft.jda.getRoleById(DiscordMinecraft.instance.getConfig().getString("linked-role-id"))).queue();
                    DiscordMinecraft.authPlugin.getPlayerData(Bukkit.getOfflinePlayer(DiscordMinecraft.waiting.get(event.getUser())).getUniqueId()).setFrozen(false);
                    DiscordMinecraft.authPlugin.sql.setDiscord(Bukkit.getOfflinePlayer(DiscordMinecraft.waiting.get(event.getUser())).getUniqueId(), event.getUserId());
                    Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&f[&9+&f] &9" + Bukkit.getPlayer(DiscordMinecraft.waiting.get(event.getUser())).getName() + "&f вперые подключился"));

                    result = true;
                } else {
                    event.getChannel().editMessageById(event.getMessageId(), new EmbedBuilder()
                            .setColor(Color.RED)
                            .setAuthor(DiscordMinecraft.jda.getSelfUser().getName(), null, DiscordMinecraft.jda.getSelfUser().getEffectiveAvatarUrl())
                            .addField("Привязка аккаунта", "Вы отклонили привязку аккаунта к " + Bukkit.getOfflinePlayer(DiscordMinecraft.waiting.get(event.getUser())).getName(), false)
                            .build()).queue((message) -> {
                        message.removeReaction("\u2705").queue();
                        message.removeReaction("\u274c").queue();
                    });
                    result = false;
                }
                if(Bukkit.getPlayer(DiscordMinecraft.waiting.get(event.getUser())) != null){
                    Bukkit.getPlayer(DiscordMinecraft.waiting.get(event.getUser())).sendMessage(result ? ChatColor.GREEN + "Вы успешно привязяли свой дискорд" : ChatColor.RED + "Запрос на привязку отклонен!");
                }
                DiscordMinecraft.waiting.remove(event.getUser());
            }

        } catch (IllegalArgumentException ignored){}

    }

    @EventHandler
    public void onAdv(PlayerAdvancementDoneEvent event){
        String[] path = event.getAdvancement().getKey().getKey().split("/");
        String advancement = path[path.length - 1];
        if(event.getAdvancement().getKey().getKey().contains("recipe") || event.getAdvancement().getKey().getKey().contains("root")) return;
        WebhookMessageBuilder builder = new WebhookMessageBuilder();
        WebhookEmbedBuilder eb = new WebhookEmbedBuilder();
        if(DiscordMinecraft.getDiscordByMinecraft(event.getPlayer().getUniqueId())!=null){
            User sender = getUser(DiscordMinecraft.getDiscordByMinecraft(event.getPlayer().getUniqueId()));
            eb.setAuthor(new WebhookEmbed.EmbedAuthor(sender.getName() + " получил достижение " + event.getAdvancement().getKey().getNamespace(), sender.getEffectiveAvatarUrl().replaceFirst("gif", "png" + "?size=512"), null));
        }
        eb.setColor(16766720);
        builder.setAvatarUrl(DiscordMinecraft.jda.getSelfUser().getEffectiveAvatarUrl());
        builder.setUsername(DiscordMinecraft.jda.getSelfUser().getName());
        builder.addEmbeds(eb.build());
        this.client.send(builder.build());
    }

    public User getUser(String id){
        for(Member member : guild.getMembers()){
            if(member.getUser().getId().equals(id)){
                return member.getUser();
            }
        }
        return null;
    }
}
