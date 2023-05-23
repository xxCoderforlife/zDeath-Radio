package com.nullpointercoding.zdeathradio.Events;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Spider;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;

import com.nullpointercoding.zdeathradio.Main;
import com.nullpointercoding.zdeathradio.Utils.Messages;
import com.nullpointercoding.zdeathradio.Utils.ZombieConfigManager;
import com.nullpointercoding.zdeathradio.ZombieTypes.assassinHandler;
import com.nullpointercoding.zdeathradio.ZombieTypes.athleteHandler;
import com.nullpointercoding.zdeathradio.ZombieTypes.bruteHandler;
import com.nullpointercoding.zdeathradio.ZombieTypes.henchmenHandler;
import com.nullpointercoding.zdeathradio.ZombieTypes.suicideBomberHandler;
import com.nullpointercoding.zdeathradio.ZombieTypes.warriorHandler;
import com.nullpointercoding.zdeathradio.ZombieTypes.zombieHandler;

public class ZombieSpawnEvent implements Listener {

    private Main pl = Main.getInstance();

    Messages m = new Messages();

    private static final Random r = new Random();

    protected final Integer spawnLimit = 2500;

    private Integer spawnCount = 0;

    private static final Random getRandom() {
        return r;
    }

    ZombieConfigManager zCM = pl.getZombieConfigManager();

    @EventHandler(priority = org.bukkit.event.EventPriority.HIGHEST)
    public void onZombieSpawn(CreatureSpawnEvent e) {
        if (e.getEntity() instanceof Zombie) {
            if (spawnLimit >= e.getEntity().getWorld().getEntitiesByClass(Zombie.class).size()) {
                e.setCancelled(true);
                return;
            }

            Zombie z = (Zombie) e.getEntity();

            if (getRandom().nextInt(100) < zCM.getZombieConfig().getInt("Zombie.spawnChance")) {
                zombieHandler zH = new zombieHandler();
                zH.convertToZombie(z);
            } else if (getRandom().nextInt(100) < zCM.getWarriorConfig().getInt("Warrior.spawnChance")) {
                warriorHandler wH = new warriorHandler();
                wH.convertToWarrior(z);
            } else if (getRandom().nextInt(100) < zCM.getAthleteConfig().getInt("Athlete.spawnChance")) {
                athleteHandler aH = new athleteHandler();
                aH.convertToAthlete(z);
                z.getWorld().spawnParticle(Particle.COMPOSTER, z.getEyeLocation().add(0, 1, 0), 3);
                return;
            } else if (getRandom().nextInt(100) < zCM.getHenchmenConfig().getInt("Henchmen.spawnChance")) {
                henchmenHandler hH = new henchmenHandler();
                hH.convertToHenchmen(z);
                z.getWorld().spawnParticle(Particle.CRIT, z.getEyeLocation().add(0, 1, 0), 3);
                return;
            } else if (getRandom().nextInt(100) < zCM.getBruteConfig().getInt("Brute.spawnChance")) {
                bruteHandler bH = new bruteHandler();
                bH.convertToBrute(z);
            } else if (getRandom().nextInt(100) < zCM.getBruteConfig().getInt("Assassin.spawnChance")) {
                assassinHandler aH = new assassinHandler();
                aH.convertToAssassin(z);
            } else if (getRandom().nextInt(100) < zCM.getSuicideBomberConfig().getInt("SuicideBomber.spawnChance")) {
                suicideBomberHandler sH = new suicideBomberHandler();
                sH.convertToSuicideBomber(z);
                z.getWorld().spawnParticle(Particle.FLAME, z.getEyeLocation().add(0, 1, 0), 3);
                return;

            }
            z.getWorld().spawnParticle(Particle.VILLAGER_ANGRY, z.getEyeLocation().add(0, 1, 0), 3);
            spawnCount++;

        } else if (e.getEntity() instanceof Creeper) {
            Creeper cr = (Creeper) e.getEntity();
            cr.getWorld().spawnEntity(cr.getLocation(), EntityType.ZOMBIE);
            cr.remove();
        } else if (e.getEntity() instanceof Skeleton) {
            Skeleton cr = (Skeleton) e.getEntity();
            cr.getWorld().spawnEntity(cr.getLocation(), EntityType.ZOMBIE);
            cr.remove();
        } else if (e.getEntity() instanceof Spider) {
            Spider cr = (Spider) e.getEntity();
            cr.getWorld().spawnEntity(cr.getLocation(), EntityType.ZOMBIE);
            cr.remove();
        }
        spawnCount++;

    }

}
