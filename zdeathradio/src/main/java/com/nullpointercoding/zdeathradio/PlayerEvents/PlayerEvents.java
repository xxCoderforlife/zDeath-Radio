package com.nullpointercoding.zdeathradio.PlayerEvents;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.scheduler.BukkitTask;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.nullpointercoding.zdeathradio.Main;
import com.nullpointercoding.zdeathradio.AutoBroadcaster.AutoBroadcast;
import com.nullpointercoding.zdeathradio.Economy.VaultHook;
import com.nullpointercoding.zdeathradio.FileManager.PlayerConfigManager;
import com.nullpointercoding.zdeathradio.FileManager.Bank.BankAccountGUI;
import com.nullpointercoding.zdeathradio.Messages.Messages;
import com.nullpointercoding.zdeathradio.Utils.CustomInvFunctions;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.title.TitlePart;

public class PlayerEvents implements Listener {

    private Main plugin = Main.getInstance();
    private Messages messages = plugin.getMessages();
    private BukkitTask stopSpawning;
    private AutoBroadcast autoBroadcast = new AutoBroadcast();
    private HashMap<Player, Inventory> playerInv = new HashMap<Player, Inventory>();
    PlayerConfigManager PCM;


    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();

        autoBroadcast.startBroadcast();

        if (Bukkit.getOnlinePlayers().size() == 1) {
            p.sendMessage(Component.text("Welcome to the server! You are the only one here!"));
        }

        // Check if Player config exists

        if (Main.getEconomy() != null) { // add null check here
            Main.getEconomy().createPlayerAccount(p);
        } else {
            Bukkit.getConsoleSender().sendMessage("Economy is null.");
        }
        // Check if the Player who joined has a bounty
        final TextComponent joinMessage = Component.text().content("{").color(TextColor.color(0x00FF00))
                .append(Component.text('✔').color(TextColor.color(0xFF0000)))
                .append(Component.text().content("} ").color(TextColor.color(0x00FF00)))
                .append(p.displayName().color(NamedTextColor.GREEN)).build();
        e.joinMessage(joinMessage.hoverEvent(randomJoinMessageLore(p)));
        joinedWithBounty(p);
        // ✘
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent e) {
        Player p = e.getPlayer();

        autoBroadcast.stopBroadcast();
        
        if (Bukkit.getOnlinePlayers().size() == 1) {
            p.sendMessage(Component.text("You are the only one left on the server!"));
        }
        stopSpawning = Bukkit.getScheduler().runTaskLater(plugin, () -> {
            if (Bukkit.getOnlinePlayers().size() == 0) {
                messages.sendConsoleMessage(messages.getDeathAdminPrefix().append(Component.text("No players online. Stopping Zombie Spawning.")));
                for(World world : Bukkit.getWorlds()){
                    world.getEntitiesByClass(Zombie.class).forEach(zombie -> {
                        zombie.remove();
                    });

                }
            }
        }, 20 * 3);
        final TextComponent leaveMessage = Component.text().content("{").color(TextColor.color(0x00FF00))
                .append(Component.text('✘').color(TextColor.color(0xFF0000)))
                .append(Component.text().content("} ").color(TextColor.color(0x00FF00)))
                .append(p.displayName().color(NamedTextColor.RED)).build();
        e.quitMessage(leaveMessage);
        leftWithBounty(p);
        setLastLoginPlayer(p);
    }
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        Player p = (Player) e.getPlayer();
        if (p.getLastDamageCause() instanceof EntityDamageByEntityEvent) {
            EntityDamageByEntityEvent playerKiller = (EntityDamageByEntityEvent) p.getLastDamageCause();
            PlayerConfigManager PCM = new PlayerConfigManager(p.getUniqueId().toString());
            if (playerKiller.getDamager() instanceof Zombie) {
                PCM.setDeaths(PCM.getDeaths() + 1);
                PCM.saveConfig();
                final Component deathMessage = Component
                        .text("§c§l" + p.getName() + " §7was killed by §c§l" + playerKiller.getDamager().getName()
                                + " and has died " + "§e§o" + PCM.getDeaths().intValue() + " times.");
                playerInv.put(p, p.getInventory());
                e.deathMessage(deathMessage);
            }
            if (playerKiller.getDamager() instanceof Player) {
                PCM.setDeaths(PCM.getDeaths() + 1);
                PCM.saveConfig();
                final Component deathMessage = Component
                        .text("§c§l" + p.getName() + " §7was killed by §c§l" + playerKiller.getDamager().getName()
                                + " and has died " + "§e§o" + PCM.getDeaths().intValue() + " times.");
                playerInv.put(p, p.getInventory());
                e.deathMessage(deathMessage);
                checkForBounites(p);
                List<ItemStack> d = e.getDrops();
                d.clear();
            }
        }
    }

    @EventHandler
    public void onPlayerReSpawn(PlayerRespawnEvent e) {
        Player p = (Player) e.getPlayer();
        Double toTake = Main.getEconomy().getBalance(p) * 0.08;
        VaultHook.round(toTake, 2);
        if (playerInv.containsKey(p)) {
            Main.getEconomy().withdrawPlayer(p, toTake);
            p.sendTitlePart(TitlePart.TITLE, Component.text("§4§lYOU DIED"));
            p.sendTitlePart(TitlePart.SUBTITLE, Component.text("You lost §e§o" + toTake.toString() + "§4§l$"));
           // p.teleportAsync(p.getWorld().getSpawnLocation(), TeleportCause.PLUGIN);
            p.getInventory().clear();
            // TODO: Remove all loot and ammo but keep guns
            p.getInventory().setContents(playerInv.get(p).getContents());
            playerInv.remove(p);
        }
    }

    public BukkitTask getStopSpawning() {
        return stopSpawning;


    }
    private void checkForBounites(Player p) {
        for (Entry<Player, HashMap<Player, Double>> m : BankAccountGUI.getBountyList().entrySet()) {
            if (m.getValue().containsKey(p)) {
                Main.getEconomy().withdrawPlayer(m.getKey(), m.getValue().get(p).doubleValue());
                m.getValue().remove(p);
                Bukkit.broadcast(Component.text(p.getKiller() + " has claimed the bounty on " + p.getName()
                        + " and has been rewarded with " + m.getValue().get(p).doubleValue() + "$"));
                Main.getEconomy().depositPlayer(p.getKiller(), m.getValue().get(p).doubleValue());
                p.getKiller().getInventory().addItem(bountyHead());
            }
        }
    }

    private void setLastLoginPlayer(Player player) {
        PlayerConfigManager pcm = new PlayerConfigManager(player.getUniqueId().toString());
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        pcm.setLastLogin(myFormatObj.format(now));
        pcm.setClientBrand(player.getClientBrandName());
        pcm.saveConfig();
    }

    private void joinedWithBounty(Player playerWithBounty) {
        PlayerConfigManager pcm = new PlayerConfigManager(playerWithBounty.getUniqueId().toString());
        if (pcm.hasBounty()) {
            Bukkit.broadcast(Component.text(playerWithBounty.getName() + " has joined and still has a bounty of "
                    + pcm.getBounty() + "$"));

        }
    }

    private void leftWithBounty(Player playerWithBounty) {
        PlayerConfigManager pcm = new PlayerConfigManager(playerWithBounty.getUniqueId().toString());
        if (pcm.hasBounty()) {
            Bukkit.broadcast(Component.text(playerWithBounty.getName() + " has left and still has a bounty of "
                    + pcm.getBounty() + "$"));
        }
    }

    private ItemStack bountyHead() {
        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) head.getItemMeta();
        meta.displayName(Component.text("§c§lBounty Head"));
        List<Component> lore = new ArrayList<Component>();
        lore.add(Component.text("Sell this at the Bounty Board"));
        PlayerProfile profile = CustomInvFunctions.getProfile(
                "https://textures.minecraft.net/texture/2c57e391e36801da12714cf7bcaed71e2c57fde4815afb692445f2b1393cd520");
        meta.setPlayerProfile(profile);
        head.setItemMeta(meta);
        return head;
    }

    private Component randomJoinMessageLore(Player player) {
        Component lore;
        int random = (int) (Math.random() * 5) + 1;
        switch (random) {
            case 1:
                lore = Component.text("Oh no, not this guy again...", NamedTextColor.RED, TextDecoration.BOLD);
                player.getWorld().spawnParticle(Particle.HAPPY_VILLAGER, player.getEyeLocation().add(0, 1, 0), 20, 2.0, 0.0, 2.0);
                return lore;
            case 2:
                lore = Component.text("Just one more login...", NamedTextColor.YELLOW, TextDecoration.BOLD);
                player.getWorld().spawnParticle(Particle.HAPPY_VILLAGER, player.getEyeLocation().add(0, 1, 0), 20, 2.0, 0.0, 2.0);
                return lore;
            case 3:
                lore = Component.text("The Choosen One has Returned!",NamedTextColor.LIGHT_PURPLE, TextDecoration.ITALIC);
                player.getWorld().spawnParticle(Particle.HAPPY_VILLAGER, player.getEyeLocation().add(0, 1, 0), 20, 2.0, 0.0, 2.0);
                return lore;
            case 4:
                lore = Component.text("yo yo welcome back", NamedTextColor.GOLD, TextDecoration.BOLD);
                player.getWorld().spawnParticle(Particle.HAPPY_VILLAGER, player.getEyeLocation().add(0, 1, 0), 20, 2.0, 0.0, 2.0);
                return lore;
            case 5:
                lore = Component.text("THE ONE AND ONLY", NamedTextColor.DARK_RED,
                        TextDecoration.BOLD);
                player.getWorld().spawnParticle(Particle.HAPPY_VILLAGER, player.getEyeLocation().add(0, 1, 0), 20, 2.0, 0.0, 2.0);
                return lore;
        }
        return null;
    }

}
