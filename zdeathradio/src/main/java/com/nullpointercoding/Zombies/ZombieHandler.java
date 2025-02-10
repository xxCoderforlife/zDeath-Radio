package com.nullpointercoding.Zombies;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.scheduler.BukkitTask;

import com.nullpointercoding.zdeathradio.Main;
import com.nullpointercoding.zdeathradio.Messages.Messages;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;

public class ZombieHandler implements Listener {

    private HashMap<EntityType, Location> possibleSpawns = new HashMap<>();
    private Boolean saveSpawns = true;
    private Main plugin = Main.getInstance();
    private Random random = new Random();
    private BukkitTask spawnTask;
    private Messages messages = new Messages();

    private final List<String> passiveMobs = Arrays.asList(
        "COW", "SHEEP", "PIG", "CHICKEN", "RABBIT", "HORSE", "DONKEY", "MULE", "LLAMA", "PARROT", "TURTLE", "CAT", "FOX", "PANDA", "BEE", "GOAT", "AXOLOTL", "GLOW_SQUID", "STRIDER", "SLIME"
    );

    @EventHandler
    public void disableAllOtherMobs(EntitySpawnEvent e) {

        if (e.getEntity().getType() == EntityType.ZOMBIE) {
            if(e.getEntity().getWorld().getLivingEntities().stream().filter(entity -> entity instanceof Zombie).count() >= 400){
                e.setCancelled(true);
            }
            //messages.logZombieSpawn(e.getEntity().getType(), e.getLocation());
            new UndeadMinion(e.getEntity());
            e.setCancelled(true);
        }
        //TODO: Fix pool spawning for zombies can't figure out why the handler is not being called.
        if (saveSpawns) {
            //possibleSpawns.put(e.getEntity().getType(), e.getLocation());
            //TODO: Create Counter to see how many locs are saved.
            //messages.logOtherMobSpawn(e.getEntity().getType(), e.getLocation());
        } else {
            e.getEntity().remove();
            messages.sendConsoleMessage(Component.text("Save Spawns is False AND spawns are not being saved."));
        }
        e.setCancelled(true);
    }

    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent e) {
        if (e.getSpawnReason() == CreatureSpawnEvent.SpawnReason.NATURAL && isDayTime(e.getLocation().getWorld())) {
            for (String mobName : passiveMobs) {
                try {
                    if (e.getEntity().getType().name().equalsIgnoreCase(mobName)) {
                        e.setCancelled(true);
                        spawnZombieAtLocation(e.getLocation());
                        break;
                    }
                } catch (IllegalArgumentException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }
        }
    }

    private boolean isDayTime(World world) {
        long time = world.getTime();
        return time < 12300 || time > 23850; // Daytime in Minecraft is from 0 to 12300 and from 23850 to 24000
    }

    private void spawnZombieAtLocation(Location location) {
        World world = location.getWorld();
        if (world != null) {
            if (random.nextDouble() < 0.333) {
                new UndeadMinion((Zombie) world.spawnEntity(location, EntityType.ZOMBIE));
            } else {
                Zombie z = (Zombie) world.spawnEntity(location, EntityType.ZOMBIE);
                z.customName(Component.text("I'm Not Him"));
                z.setCustomNameVisible(true);
            }
        }
    }

    private void spawnCustomZombie() {
        if (spawnTask != null && !spawnTask.isCancelled()) {
            Bukkit.getConsoleSender().sendMessage("Wave Spawn task is already running.");
            return;
        }

        spawnTask = Bukkit.getScheduler().runTaskTimer(plugin, new Runnable() {
            @Override
            public void run() {
                Bukkit.broadcast(Component.text("Spawning a Wave of Zombies!"));
                messages.sendConsoleMessage(Component.text("Spawning a Wave of Zombies!")
                .decorate(TextDecoration.BOLD).color(NamedTextColor.DARK_RED));
                for (Location loc : possibleSpawns.values()) {
                    World world = loc.getWorld();
                    if (world != null) {
                        if (random.nextDouble() < 0.333) {
                            new UndeadMinion((Zombie) world.spawnEntity(loc, EntityType.ZOMBIE));
                        } else {
                            Zombie z = (Zombie) world.spawnEntity(loc, EntityType.ZOMBIE);
                            z.customName(Component.text("I'm Not Him"));
                            z.setCustomNameVisible(true);
                        }
                    }
                }
                possibleSpawns.clear();
                saveSpawns = true;
                spawnTask.cancel();
            }
        }, 20L * 45, 20L * 45);
    }

    @EventHandler
    public void onChunkGen(ChunkLoadEvent chunk) {
        for (Entity entity : chunk.getChunk().getEntities()) {
            if (entity instanceof Zombie) {
                entity.remove();
            }
        }
    }

    @EventHandler
    public void onWorldLoad(WorldLoadEvent world) {
        // Additional logic for world load event if needed
    }

    public static Integer checkZombieSpawnCount(){
        return Bukkit.getWorlds().stream().mapToInt(world -> (int) world.getEntitiesByClass(Zombie.class).size()).sum();
    }
}
