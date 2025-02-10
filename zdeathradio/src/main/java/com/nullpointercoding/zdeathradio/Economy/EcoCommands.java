package com.nullpointercoding.zdeathradio.Economy;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.nullpointercoding.zdeathradio.Main;
import com.nullpointercoding.zdeathradio.FileManager.PlayerConfigManager;

import net.milkbowl.vault.economy.Economy;

public class EcoCommands implements TabExecutor {

    Economy econ = new VaultHook();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String arg2,
            @NotNull String[] args) {
        if (cmd.getName().equalsIgnoreCase("economy")) {
            if (args.length == 0) {
                sender.sendMessage("§cUsage: /economy <add|remove|set> <player> <amount>");
                return true;
            }
            if (args.length == 1) {
                sender.sendMessage("§cUsage: /economy <add|remove|set> <player> <amount>");
                return true;
            }
            if (args.length == 2) {
                sender.sendMessage("§cUsage: /economy <add|remove|set> <player> <amount>");
                return true;
            }
            if (args.length == 3) {
                if (args[0].equalsIgnoreCase("add")) {
                    Player target = Main.getInstance().getServer().getPlayer(args[1]);
                    if (target == null) {
                        sender.sendMessage("§cPlayer not found!");
                        return true;
                    }
                    double amount = Double.parseDouble(args[2]);
                    econ.depositPlayer(target, amount);
                    sender.sendMessage("§aAdded " + amount + " to " + target.getName() + "'s balance!");
                    return true;
                }
                if (args[0].equalsIgnoreCase("remove")) {
                    Player target = Main.getInstance().getServer().getPlayer(args[1]);
                    if (target == null) {
                        sender.sendMessage("§cPlayer not found!");
                        return true;
                    }
                    double amount = Double.parseDouble(args[2]);
                    econ.withdrawPlayer(target, amount);
                    sender.sendMessage("§aRemoved " + amount + " from " + target.getName() + "'s balance!");
                    return true;
                }
                if (args[0].equalsIgnoreCase("set")) {
                    Player target = Main.getInstance().getServer().getPlayer(args[1]);
                    if (target == null) {
                        sender.sendMessage("§cPlayer not found!");
                        return true;
                    }
                    double amount = Double.parseDouble(args[2]);
                    econ.withdrawPlayer(target, econ.getBalance(target));
                    econ.depositPlayer(target, amount);
                    sender.sendMessage("§aSet " + target.getName() + "'s balance to " + amount + "!");
                    return true;
                }
            }
        }
        if (cmd.getName().equalsIgnoreCase("token")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (args.length == 0) {
                    player.sendMessage("§cUsage: /token <add|remove|set> <player> <amount>");
                    return true;
                }
                if (args.length == 1) {
                    player.sendMessage("§cUsage: /token <add|remove|set> <player> <amount>");
                    return true;
                }
                if (args.length == 2) {
                    player.sendMessage("§cUsage: /token <add|remove|set> <player> <amount>");
                    return true;
                }
                if (args.length == 3) {
                    if (args[0].equalsIgnoreCase("add")) {
                        Player target = Main.getInstance().getServer().getPlayer(args[1]);
                        if (target == null) {
                            player.sendMessage("§cPlayer not found!");
                            return true;
                        }
                        PlayerConfigManager pcm = new PlayerConfigManager(target.getUniqueId().toString());
                        int amount = Integer.parseInt(args[2]);
                        pcm.setTokens((double) amount + pcm.getTokens());
                        player.sendMessage("§aAdded " + amount + " to " + target.getName() + "'s tokens!");
                        return true;
                    }
                    if (args[0].equalsIgnoreCase("remove")) {
                        Player target = Main.getInstance().getServer().getPlayer(args[1]);
                        if (target == null) {
                            player.sendMessage("§cPlayer not found!");
                            return true;
                        }
                        PlayerConfigManager pcm = new PlayerConfigManager(target.getUniqueId().toString());
                        int amount = Integer.parseInt(args[2]);
                        pcm.setTokens(pcm.getTokens() - (double) amount);
                        player.sendMessage("§aRemoved " + amount + " from " + target.getName() + "'s tokens!");
                        return true;
                    }
                    if (args[0].equalsIgnoreCase("set")) {
                        Player target = Main.getInstance().getServer().getPlayer(args[1]);
                        if (target == null) {
                            player.sendMessage("§cPlayer not found!");
                            return true;
                        }
                        PlayerConfigManager pcm = new PlayerConfigManager(target.getUniqueId().toString());
                        int amount = Integer.parseInt(args[2]);
                        pcm.setTokens((double) amount);
                        player.sendMessage("§aSet " + target.getName() + "'s tokens to " + amount + "!");
                        return true;
                    }
                }
            }
        }
        return true;
    }
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd,
            @NotNull String arg2, @NotNull String[] args) {

        List<String> tab = new ArrayList<String>();
        if (cmd.getName().equalsIgnoreCase("economy") || cmd.getName().equalsIgnoreCase("token")) {
            if (args.length == 0) {

            }
            if (args.length == 1) {
                tab.add("add");
                tab.add("remove");
                tab.add("set");
            }
            if (args.length == 2) {
                if (args[0].equalsIgnoreCase("give") || args[0].equalsIgnoreCase("take")
                        || args[0].equalsIgnoreCase("set")) {
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        tab.add(p.getName());
                    }
                }
                if (args[0].equalsIgnoreCase("bank")) {
                    tab.add("create");
                    tab.add("delete");
                    tab.add("info");
                }

            }
            if (args.length == 3) {
                if (args[0].equalsIgnoreCase("bank")) {
                    if (args[1].equalsIgnoreCase("delete")) {
                        tab.add("Still need to add the bank file system.");
                    }
                }
            }
        }
        return tab;
    }

}
