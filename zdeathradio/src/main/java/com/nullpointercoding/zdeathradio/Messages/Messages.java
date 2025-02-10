package com.nullpointercoding.zdeathradio.Messages;



import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.EntityType;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;

public class Messages {

    private final TextComponent deathAdminPrefix = Component.text("[").decorate(TextDecoration.BOLD).color(NamedTextColor.WHITE)
        .append(Component.text("DeathAdmin").color(NamedTextColor.DARK_RED).decorate(TextDecoration.BOLD))
        .append(Component.text("] ").decorate(TextDecoration.BOLD).color(NamedTextColor.WHITE));
    private final TextComponent deathPlayerPrefix = Component.text("[").decorate(TextDecoration.BOLD).color(NamedTextColor.WHITE)
        .append(Component.text("DeathPlayer").color(NamedTextColor.GREEN).decorate(TextDecoration.BOLD))
        .append(Component.text("] ").decorate(TextDecoration.BOLD).color(NamedTextColor.WHITE));
    private final TextComponent deathPlayerPrefixShort = Component.text("[").decorate(TextDecoration.BOLD).color(NamedTextColor.WHITE)
        .append(Component.text("DP").color(NamedTextColor.GREEN).decorate(TextDecoration.BOLD))
        .append(Component.text("] ").decorate(TextDecoration.BOLD).color(NamedTextColor.WHITE));
    private final TextComponent deathAdminPrefixShort = Component.text("[").decorate(TextDecoration.BOLD).color(NamedTextColor.WHITE)
        .append(Component.text("DA").color(NamedTextColor.DARK_RED).decorate(TextDecoration.BOLD))
        .append(Component.text("] ").decorate(TextDecoration.BOLD).color(NamedTextColor.WHITE));
    private ConsoleCommandSender console = Bukkit.getConsoleSender();
    
    public void logZombieSpawn(EntityType et, Location loc){
        Component zombieSpawnText = Component.text("Zombie Spawn: ")
            .color(NamedTextColor.DARK_RED)
            .decorate(TextDecoration.BOLD);
        Component message = zombieSpawnText.append(Component.text(et + " at " + "X: " + loc.getBlockX()
            + " Y: " + loc.getBlockY() + " Z: " + loc.getBlockZ() + " in " + loc.getWorld().getName()));
        console.sendMessage(message);
    }

    public void broadcastMessage(Component message){
        Bukkit.broadcast(message);
    }

    public void sendConsoleMessage(Component message){
        console.sendMessage(message);
    }

    public final TextComponent getDeathAdminPrefix(){
        return deathAdminPrefix;
    }
    public final TextComponent getDeathPlayerPrefix(){
        return deathPlayerPrefix;
    }
    public final TextComponent getDeathPlayerPrefixShort(){
        return deathPlayerPrefixShort;
    }
    public final TextComponent getDeathAdminPrefixShort(){
        return deathAdminPrefixShort;
    }
}
