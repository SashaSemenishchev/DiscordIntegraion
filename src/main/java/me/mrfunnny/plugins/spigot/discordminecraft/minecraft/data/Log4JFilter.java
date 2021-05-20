package me.mrfunnny.plugins.spigot.discordminecraft.minecraft.data;

import club.minnced.discord.webhook.WebhookClientBuilder;
import club.minnced.discord.webhook.send.WebhookMessageBuilder;
import me.mrfunnny.plugins.spigot.discordminecraft.DiscordMinecraft;
import me.mrfunnny.plugins.spigot.discordminecraft.bot.Bot;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.message.Message;

public class Log4JFilter implements Filter {

    private Bot bot;

    public Log4JFilter(Bot bot){
        this.bot = bot;
    }

    public Result filter(LogEvent record) {
        WebhookMessageBuilder wmb = new WebhookMessageBuilder();
        wmb.setUsername("Console");
        wmb.setAvatarUrl("https://d1nhio0ox7pgb.cloudfront.net/_img/g_collection_png/standard/512x512/console.png");
        wmb.setContent("[" +  record.getLevel() + "] " + record.getMessage().getFormattedMessage());

        bot.getClient().send(wmb.build());
        return null;
    }

    @Override
    public Result getOnMismatch() {
        return null;
    }

    @Override
    public Result getOnMatch() {
        return null;
    }

    public Result filter(Logger arg0, Level arg1, Marker arg2, String message, Object... arg4) {
//        DiscordMinecraft.jda.getTextChannelById(console_id).sendMessage("[" +  arg1 + "] " + message).queue();
        WebhookMessageBuilder wmb = new WebhookMessageBuilder();
        wmb.setUsername("Console");
        wmb.setAvatarUrl("https://d1nhio0ox7pgb.cloudfront.net/_img/g_collection_png/standard/512x512/console.png");
        wmb.setContent("[" +  arg1 + "] " + message);

        bot.getClient().send(wmb.build());
        return null;
    }

    @Override
    public Result filter(Logger logger, Level level, Marker marker, String message, Object p0) {
        WebhookMessageBuilder wmb = new WebhookMessageBuilder();
        wmb.setUsername("Console");
        wmb.setAvatarUrl("https://d1nhio0ox7pgb.cloudfront.net/_img/g_collection_png/standard/512x512/console.png");
        wmb.setContent("[" +  level + "] " + message);

        bot.getClient().send(wmb.build());
        return null;
    }

    @Override
    public Result filter(Logger logger, Level level, Marker marker, String message, Object p0, Object p1) {
        return null;
    }

    @Override
    public Result filter(Logger logger, Level level, Marker marker, String message, Object p0, Object p1, Object p2) {
        return null;
    }

    @Override
    public Result filter(Logger logger, Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3) {
        return null;
    }

    @Override
    public Result filter(Logger logger, Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4) {
        return null;
    }

    @Override
    public Result filter(Logger logger, Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5) {
        return null;
    }

    @Override
    public Result filter(Logger logger, Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6) {
        return null;
    }

    @Override
    public Result filter(Logger logger, Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7) {
        return null;
    }

    @Override
    public Result filter(Logger logger, Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8) {
        return null;
    }

    @Override
    public Result filter(Logger logger, Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8, Object p9) {
        return null;
    }

    public Result filter(Logger arg0, Level arg1, Marker arg2, Object message, Throwable arg4) {
        WebhookMessageBuilder wmb = new WebhookMessageBuilder();
        wmb.setUsername("Console");
        wmb.setAvatarUrl("https://d1nhio0ox7pgb.cloudfront.net/_img/g_collection_png/standard/512x512/console.png");
        wmb.setContent("[" +  arg1 + "] " + message);

        bot.getClient().send(wmb.build());
        return null;
    }

    public Result filter(Logger arg0, Level arg1, Marker arg2, Message message, Throwable arg4) {
        WebhookMessageBuilder wmb = new WebhookMessageBuilder();
        wmb.setUsername("Console");
        wmb.setAvatarUrl("https://d1nhio0ox7pgb.cloudfront.net/_img/g_collection_png/standard/512x512/console.png");
        wmb.setContent("[" +  arg1 + "] " + message);

        bot.getClient().send(wmb.build());
        return null;
    }


    @Override
    public State getState() {
        return null;
    }

    @Override
    public void initialize() {

    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public boolean isStarted() {
        return false;
    }

    @Override
    public boolean isStopped() {
        return false;
    }
}

