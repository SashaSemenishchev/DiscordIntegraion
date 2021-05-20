package me.mrfunnny.plugins.spigot.discordminecraft.bot;

import me.mrfunnny.plugins.spigot.discordminecraft.DiscordMinecraft;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.restaction.WebhookAction;

import javax.annotation.Nonnull;

public class CommandListener extends ListenerAdapter {

    public static final String PREFIX = "!!";
    private DiscordMinecraft plugin;

    public CommandListener(DiscordMinecraft plugin){
        this.plugin = plugin;
    }

    @Override
    public void onMessageReceived(@Nonnull MessageReceivedEvent event) {
        String message = event.getMessage().getContentRaw();
        if(message.startsWith(PREFIX)){
            String[] args = message.substring(PREFIX.length()).split(" ");
            switch(args[0]){
                case "setupchannel":
                    if(args.length == 2){
                        WebhookAction wa = event.getTextChannel().createWebhook(args[1]);
                    }
                    break;
                case "принято":
                    if(args.length == 3){
                        Member ment = event.getMessage().getMentionedMembers().get(0);
                        Bot.guild.addRoleToMember(ment, Bot.guild.getRoleById(plugin.getConfig().getString("default-role-id"))).queue();
                        DiscordMinecraft.link(DiscordMinecraft.getOfflinePlayerByName(args[2]).getUniqueId(), ment.getUser());
                        event.getTextChannel().sendMessage("Успешно");
                    }
                    break;
                default:
                    event.getChannel().sendMessage("**Unknown command: " + args[0] + "**").queue();
                    break;
            }
        }
    }
}
