package com.nullpointercoding.zdeathradio.Events;



import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import com.nullpointercoding.zdeathradio.Main;
import com.nullpointercoding.zdeathradio.Utils.ZombieConfigManager;

import io.papermc.paper.event.entity.EntityMoveEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;

public class ZombieExplodeEvent implements Listener{


    private Main pl = Main.getInstance();
    private ZombieConfigManager zCM = pl.getZombieConfigManager();
    private BukkitTask bombTimer;
    /**
     *
     */
    

    @EventHandler
    public void sbMoving(EntityMoveEvent ev){
        if(!(ev.getEntity() instanceof Zombie) || ev.getEntity().customName() == null) return;

        Zombie z = (Zombie) ev.getEntity();
        final Component zName = (Component) z.customName();
        String zNameString = PlainTextComponentSerializer.plainText().serialize(z.customName());
        if(!(zNameString.equalsIgnoreCase(zCM.getSuicideBomberConfig().getString("SuicideBomber.Name")))) return;

        for(Entity et : z.getNearbyEntities(5, 10, 5)){
            if(!(et instanceof Player)) return;
            Player p = (Player) et;
            //Check if a player is nearby
            if(p.getLocation().distance(z.getLocation()) <= 6){
                p.playSound(p.getLocation(), Sound.ENTITY_CREEPER_PRIMED, SoundCategory.AMBIENT, 0.5f,12.0f);
            }

            if(p.getLocation().distance(z.getLocation()) <= 3){
                bombTimer = new BukkitRunnable(){
                    @Override
                    public void run(){
                        if(p.getLocation().distance(z.getLocation()) > 3){
                            bombTimer.cancel();
                            z.customName(zName);
                            return;
                        }
                        p.playSound(p,Sound.ENTITY_GENERIC_EXPLODE, SoundCategory.AMBIENT, 0.5f, 1.0f);
                        z.getLocation().createExplosion(z, 6, true, false);
                        z.setLastDamageCause(new EntityDamageEvent(z, DamageCause.ENTITY_EXPLOSION, 70.0D));
                        z.setHealth(0.0D);
                        
                        p.damage(p.getHealth() /6.88);
                    }
                }.runTaskLater(pl, 32);
            }

        }
    }
}
