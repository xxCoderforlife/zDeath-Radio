package com.nullpointercoding.zdeathradio.Commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

public class TabCommands implements TabCompleter {
    @Override
    public List<String> onTabComplete(org.bukkit.command.CommandSender sender, org.bukkit.command.Command cmd,
            String label, String[] args) {
        List<String> tab = new ArrayList<>();
        if (cmd.getName().equalsIgnoreCase("zdeathradio")) {
            if (args.length == 1) {
                tab.add("spawn");
                tab.add("shootarrow");
                tab.add("give");
                tab.add("version");
                tab.add("reload");
                tab.add("killzombies");
            } else if (args.length == 2) {
                if (args[0].equalsIgnoreCase("spawn")) {
                    tab.add("zombie");
                    tab.add("warrior");
                    tab.add("brute");
                    tab.add("henchmen");
                    tab.add("athlete");
                    tab.add("assassin");
                    tab.add("suicidebomber");
                }
                if(args[0].equalsIgnoreCase("give")){
                    tab.add("meat");
                }

            }else if(args.length == 3){
                if(args[0].equalsIgnoreCase("give")){
                    if(args[1].equalsIgnoreCase("meat")){
                        tab.add("1");
                        tab.add("5");
                        tab.add("10");
                        tab.add("30");
                        tab.add("64");
                    }
                    
                }

            }else if(args.length == 4){
                if(args[0].equalsIgnoreCase("give")){
                    if(args[1].equalsIgnoreCase("meat")){
                            for(Player p : Bukkit.getOnlinePlayers()){
                                tab.add(p.getName());

                        }
                    }
                }
            }
            
        }
        return tab;
    }
}
