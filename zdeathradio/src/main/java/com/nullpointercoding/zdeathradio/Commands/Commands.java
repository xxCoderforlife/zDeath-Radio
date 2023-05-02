package com.nullpointercoding.zdeathradio.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import com.nullpointercoding.zdeathradio.Main;
import com.nullpointercoding.zdeathradio.Utils.CustomRecipes;
import com.nullpointercoding.zdeathradio.Utils.Messages;
import com.nullpointercoding.zdeathradio.Utils.ZombieConfigManager;
import com.nullpointercoding.zdeathradio.ZombieTypes.assassinHandler;
import com.nullpointercoding.zdeathradio.ZombieTypes.athleteHandler;
import com.nullpointercoding.zdeathradio.ZombieTypes.bruteHandler;
import com.nullpointercoding.zdeathradio.ZombieTypes.henchmenHandler;
import com.nullpointercoding.zdeathradio.ZombieTypes.suicideBomberHandler;
import com.nullpointercoding.zdeathradio.ZombieTypes.warriorHandler;
import com.nullpointercoding.zdeathradio.ZombieTypes.zombieHandler;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;

public class Commands implements CommandExecutor {

    private Main pl = Main.getInstance();
    private Messages m = new Messages();
    private CustomRecipes cr = pl.getCustomRecipes();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (args.length == 0) {
                if (!p.hasPermission("zdeathradio.admin")) {
                    p.sendMessage(
                            m.getPrefix().append(Component.text("You do not have permission to use this command!")
                                    .decorate(TextDecoration.ITALIC).color(TextColor.color(202, 4, 4))));
                    return true;
                }
                p.sendMessage(m.getPrefix().append(
                        Component.text("A Zombie plugin made by xxCoderforlife for PaperMC servers ONLY").color(
                                TextColor.color(243, 0, 252))));
            } else if (args.length == 1) {
                if (args[0].equalsIgnoreCase("spawn")) {
                    p.sendMessage("Almost there. You need to specify a zombie type.");
                } else if (args[0].equalsIgnoreCase("version")) {
                    p.sendMessage(m.getPrefix()
                            .append(Component.text("zDeathRadio version: " + pl.getPluginMeta().getVersion())
                                    .color(TextColor.color(229, 224, 80))));
                } else if (args[0].equalsIgnoreCase("reload")) {
                    ZombieConfigManager zcm = pl.getZombieConfigManager();
                    pl.reloadConfig();
                    zcm.reloadConfigs();
                    p.sendMessage(m.getPrefix()
                            .append(Component.text("Reloaded config files!").color(TextColor.color(46, 228, 10))));
                } else if (args[0].equalsIgnoreCase("killzombies")) {
                    for (Zombie z : p.getWorld().getEntitiesByClass(Zombie.class)) {
                        z.remove();
                    }
                    p.sendMessage(m.getPrefix().append(
                            Component.text("Killed all zombies in the world!").color(TextColor.color(206, 50, 50))));
                } else if (args[0].equalsIgnoreCase("give")) {
                    p.sendMessage(m.getDash().append(Component.text("give meat <player>")));
                } else if (args[0].equalsIgnoreCase("shootarrow")) {
                    Vector v = p.getLocation().getDirection();
                    Arrow a = p.launchProjectile(Arrow.class, v);
                    a.customName(Component.text(p.customName() + " Arrow"));
                    a.setCustomNameVisible(true);
                    p.sendMessage("Shot an arrow");

                }

            } else if (args.length == 2) {
                if (args[0].equalsIgnoreCase("spawn")) {
                    if (args[1].equalsIgnoreCase("zombie")) {
                        zombieHandler zH = new zombieHandler();
                        zH.spawnZombie(p.getWorld(), p.getLocation());
                        p.sendMessage(m.getPrefix().append(Component.text("Spawned a ")
                                .color(TextColor.color(46, 228, 10)).append(zH.getZombie().customName())));
                    } else if (args[1].equalsIgnoreCase("brute")) {
                        bruteHandler bH = new bruteHandler();
                        bH.spawnBrute(p.getWorld(), p.getLocation());
                        p.sendMessage(m.getPrefix().append(Component.text("Spawned a ")
                                .color(TextColor.color(46, 228, 10)).append(bH.getBrute().customName())));
                    } else if (args[1].equalsIgnoreCase("warrior")) {
                        warriorHandler wH = new warriorHandler();
                        wH.spawnWarrior(p.getWorld(), p.getLocation());
                        p.sendMessage(m.getPrefix().append(Component.text("Spawned a ")
                                .color(TextColor.color(46, 228, 10)).append(wH.getWarrior().customName())));
                    } else if (args[1].equalsIgnoreCase("henchmen")) {
                        henchmenHandler hH = new henchmenHandler();
                        hH.spawnHenchmen(p.getWorld(), p.getLocation());
                        p.sendMessage(m.getPrefix().append(Component.text("Spawned a ")
                                .color(TextColor.color(46, 228, 10)).append(hH.getHenchmen().customName())));
                    } else if (args[1].equalsIgnoreCase("athlete")) {
                        athleteHandler ah = new athleteHandler();
                        ah.spawnAthlete(p.getWorld(), p.getLocation());
                        p.sendMessage(m.getPrefix().append(Component.text("Spawned a ")
                                .color(TextColor.color(46, 228, 10)).append(ah.getAthlete().customName())));
                    } else if (args[1].equalsIgnoreCase("assassin")) {
                        assassinHandler asH = new assassinHandler();
                        asH.spawnAssassin(p.getWorld(), p.getLocation());
                        p.sendMessage(m.getPrefix().append(Component.text("Spawned a ")
                                .color(TextColor.color(46, 228, 10)).append(asH.getAssassin().customName())));
                    }else if(args[1].equalsIgnoreCase("suicidebomber")){
                        suicideBomberHandler sbH = new suicideBomberHandler();
                        sbH.spawnSuicideBomber(p.getWorld(), p.getLocation());
                        p.sendMessage(m.getPrefix().append(Component.text("Spawned a ")
                                .color(TextColor.color(46, 228, 10)).append(sbH.getSuicideBomber().customName())));
                    }

                } else if (args[0].equalsIgnoreCase("give")) {
                    if (args[1].equalsIgnoreCase("meat")) {
                        p.sendMessage(Component.text("You need to specify an amount"));
                    }

                }

            } else if (args.length == 3) {
                if (args[0].equalsIgnoreCase("give")) {
                    if (args[1].equalsIgnoreCase("meat")) {
                        ItemStack rottenFlesh = cr.getCookedRottenFlesh();
                        rottenFlesh.setAmount(Integer.parseInt(args[2]));
                        p.getInventory().addItem(rottenFlesh);
                        p.sendMessage(m.getPrefix()
                                .append(Component.text("You now have " + args[2] + " cooked rotten flesh!")));
                    }
                }
            } else if (args.length == 4) {
                if (args[0].equalsIgnoreCase("give")) {
                    if (args[1].equalsIgnoreCase("meat")) {
                        if (Integer.parseInt(args[2]) > 64) {
                            p.sendMessage(m.getPrefix().append(Component.text("You can only give 64 at a time!")));
                            return true;

                        }
                        Player tar = Bukkit.getPlayer(args[3]);
                        if (tar == null) {
                            p.sendMessage(m.getPrefix().append(Component.text("That player is not online!")));
                        }
                        ItemStack rottenFlesh = cr.getCookedRottenFlesh();
                        rottenFlesh.setAmount(Integer.parseInt(args[2]));
                        p.getInventory().addItem(rottenFlesh);
                    }

                }
            }
        }
        return true;

    }
}