package com.nullpointercoding.zdeathradio.Zombies;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import com.nullpointercoding.zdeathradio.Main;
import com.nullpointercoding.zdeathradio.Messages.Messages;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;

import java.util.ArrayList;
import java.util.List;

public class ZombieGC {

    private Main plugin = Main.getInstance();
    private BukkitTask gcTask;
    private final int removeZombiesThreshold = 200;
    private Messages messages;
    private Listener spawnListener;
    private BukkitTask checkCount;

    public ZombieGC() {
        messages = plugin.getMessages();
        messages.sendConsoleMessage(messages.getDeathAdminPrefix().append(
                Component.text("Zombie GC initialized.").color(NamedTextColor.GREEN)));
        startGC();
    }

    public void startGC() {
        if (gcTask != null && !gcTask.isCancelled()) {
            messages.sendConsoleMessage(messages.getDeathAdminPrefix().append(
                    Component.text("Zombie GC already running.").color(NamedTextColor.RED)));
            return;
        }
        checkZombieCount();

        gcTask = new BukkitRunnable() {
            @Override
            public void run() {
                messages.sendConsoleMessage(messages.getDeathAdminPrefix().append(
                        Component.text("Running Zombie GC.").color(NamedTextColor.GREEN)));
                for (World world : Bukkit.getWorlds()) {
                    int zombieCount = world.getEntitiesByClass(Zombie.class).size();
                    if (zombieCount >= removeZombiesThreshold) {
                        removeExcessZombies(world, (int) (zombieCount * 0.6)); // Remove 60% of zombies
                    } else {
                        messages.sendConsoleMessage(messages.getDeathAdminPrefix().append(
                                Component.text("Zombie count is below threshold for " + world.getName())
                                        .color(NamedTextColor.GREEN)));
                    }
                }
            }
        }.runTaskTimer(plugin, 0L, 20L * 60 * 15); // Runs every 15 minutes
    }

    public void stopGC() {
        if (gcTask != null && !gcTask.isCancelled()) {
            gcTask.cancel();
            messages.sendConsoleMessage(messages.getDeathAdminPrefix().append(
                    Component.text("Zombie GC stopped.").color(NamedTextColor.RED)));
        } else {
            messages.sendConsoleMessage(messages.getDeathAdminPrefix().append(
                    Component.text("Zombie GC not running.").color(NamedTextColor.RED)));
        }
    }

    public void emergencyGC() {
        boolean isListenerRegistered = HandlerList.getRegisteredListeners(plugin).stream()
                .anyMatch(registeredListener -> registeredListener.getListener().equals(spawnListener));
        if (isListenerRegistered) {
            messages.sendConsoleMessage(messages.getDeathAdminPrefix().append(
                Component.text("Emergency GC already running.").color(NamedTextColor.AQUA)));
            return;
        }
        for (World world : Bukkit.getWorlds()) {
            removeExcessZombies(world, (int) (world.getEntitiesByClass(Zombie.class).size() * 0.6)); // Remove 60% of zombies
        }
        messages.sendConsoleMessage(messages.getDeathAdminPrefix().append(
                Component.text("Emergency GC initiated. Disabling Mob Spawns.").color(NamedTextColor.RED)));
        // Disable mob spawning
        Bukkit.getPluginManager().registerEvents(
                spawnListener = new Listener() {
                    @EventHandler
                    public void onCreatureSpawn(EntitySpawnEvent event) {
                        if (event.getEntity() instanceof Zombie) {
                            event.setCancelled(true);
                        }
                    }
                }, plugin);

        // Re-enable mob spawning after 15 seconds
        new BukkitRunnable() {
            @Override
            public void run() {
                HandlerList.unregisterAll(spawnListener);
                HandlerList.bakeAll();
                messages.sendConsoleMessage(messages.getDeathAdminPrefix().append(
                        Component.text("Emergency GC complete. Mob Spawning Re-Enabled.").color(NamedTextColor.GREEN)));
            }
        }.runTaskLater(plugin, 20L * 15); // 15 seconds later
    }

    private void removeExcessZombies(World world, int amount) {
        List<Zombie> zombies = new ArrayList<>(world.getEntitiesByClass(Zombie.class));
        for (int i = 0; i < amount && i < zombies.size(); i++) {
            zombies.get(i).remove();
        }
        messages.sendConsoleMessage(messages.getDeathAdminPrefix().append(
                Component.text("Removed " + amount + " zombies from " + world.getName()).color(NamedTextColor.RED)));
    }
    

    public EntitySpawnEvent getSpawnListener() {
        return (EntitySpawnEvent) spawnListener;
    }

    private void checkZombieCount() {
        checkCount = Bukkit.getScheduler().runTaskTimer(plugin, new Runnable() {
            @Override
            public void run() {
                for (World world : Bukkit.getWorlds()) {
                    int zombieCount = world.getEntitiesByClass(Zombie.class).size();
                    messages.broadcastMessage(messages.getDeathAdminPrefix()
                            .append(Component.text("Zombie Count: " + zombieCount)
                                    .decorate(TextDecoration.ITALIC).color(NamedTextColor.GREEN)
                                    .append(Component.text(" in " + world.getName()).decorate(TextDecoration.BOLD)
                                            .color(NamedTextColor.GOLD))));
                    if (zombieCount > removeZombiesThreshold * 1.5) {
                        emergencyGC();
                    }
                }
            }
        }, 0L, 20L * 60 * 2); // Runs every 120 seconds
    }

    public BukkitTask getCheckCount() {
        return checkCount;
    }
}