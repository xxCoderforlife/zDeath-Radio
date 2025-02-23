package com.nullpointercoding.zdeathradio.Commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;

import com.nullpointercoding.zdeathradio.Main;
import net.kyori.adventure.text.Component;

public class SysCommands implements TabExecutor {

    private final List<String> COMMANDS = Arrays.asList("reload", "help", "killall", "stopgc", "startgc");

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (sender instanceof ConsoleCommandSender) {
            ConsoleCommandSender console = (ConsoleCommandSender) sender;
            console.sendMessage(Component.text("Error: You must be a player to use this command"));
            console.sendMessage(Component.text("Error: Adding in Console Commands in later updates"));
            return false;
        }

        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("zdeathradio.admin")) {
                if (args.length == 0) {
                    player.sendMessage(Component.text("Error: Please provide a command"));
                }
                if (args[0].equalsIgnoreCase("reload")) {
                    player.sendMessage(Component.text("Reloading Config"));
                }
                if (args[0].equalsIgnoreCase("help")) {
                    player.sendMessage(Component.text("Help:"));
                    player.sendMessage(Component.text("/zdr reload - Reloads the config"));
                }
                if (args[0].equalsIgnoreCase("killall")) {
                    for (Monster m : player.getWorld().getEntitiesByClass(Monster.class)) {
                        m.remove();
                    }
                    player.sendMessage(Component.text("All monsters have been removed"));
                }
                if (args[0].equalsIgnoreCase("stopgc")) {
                    Main.getZombieGC().stopGC();
                    player.sendMessage(Component.text("Stopping Zombie Garbage Collection"));
                }
                if (args[0].equalsIgnoreCase("startgc")) {
                    Main.getZombieGC().startGC();
                    player.sendMessage(Component.text("Starting Zombie Garbage Collection"));
                }
            }
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (args.length == 1) {
            List<String> completions = new ArrayList<>();
            for (String command : COMMANDS) {
                if (command.toLowerCase().startsWith(args[0].toLowerCase())) {
                    completions.add(command);
                }
            }
            return completions;
        }
        return null;
    }
}
