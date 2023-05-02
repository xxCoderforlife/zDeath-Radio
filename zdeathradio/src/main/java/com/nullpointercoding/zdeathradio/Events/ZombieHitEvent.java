package com.nullpointercoding.zdeathradio.Events;

import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import com.nullpointercoding.zdeathradio.Main;
import com.nullpointercoding.zdeathradio.Utils.Messages;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.title.TitlePart;

public class ZombieHitEvent implements Listener {

    private Main pl = Main.getInstance();

    public ZombieHitEvent() {

    }

    private ArrayList<UUID> enragedList = new ArrayList<UUID>();
    private ArrayList<UUID> infectedPlayers = new ArrayList<UUID>();
    private static final Random r = new Random();
    private Messages m = new Messages();

    private static final Random getRandom() {
        return r;
    }

    @EventHandler
    public void onPlayerHitEvent(EntityDamageByEntityEvent ev) {
        if(ev.getDamager() instanceof Zombie && ev.getEntity() instanceof Zombie){
            if(ev.getCause() == DamageCause.CRAMMING){
                Zombie z = (Zombie) ev.getDamager();
                Zombie z2 = (Zombie) ev.getEntity();
                if(z == null || z2 == null) return;
                z.remove();
                z2.remove();
                m.sendConsoleMessage("Zombie Cramming");
            }

        }
        if (ev.getDamager() instanceof Zombie && ev.getEntity() instanceof Player) {
            final Player p = (Player) ev.getEntity();
            Zombie z = (Zombie) ev.getDamager();
            if(infectedPlayers.contains(p.getUniqueId())){
                p.getWorld().spawnParticle(Particle.CRIT, z.getLocation().add(0, 1, 0), 4);
                p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ITEM_BREAK, (float)10.0, (float)0.3);
                return;
            }
            if (getRandom().nextInt(100) < pl.getConfig().getInt("zDeath-Radio.chanceOfInfection")) {
               final TextComponent infectedMessageTitle = Component.text("Infected!").color(TextColor.color(193,12,12));
               final TextComponent infectedMessageSubTitle = Component.text("Infected by a ").color(TextColor.color(246,16,16)).append(z.customName());

                p.sendTitlePart(TitlePart.SUBTITLE, infectedMessageSubTitle);
                p.sendTitlePart(TitlePart.TITLE, infectedMessageTitle);
                p.getWorld().playSound(p.getLocation(), Sound.ENTITY_PLAYER_BREATH, (float) 10.0, (float) 1.3);
                p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20 * 60 * 3, 2));
                p.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 20 * 60 * 3, 2));
                p.addPotionEffect(new PotionEffect(PotionEffectType.DARKNESS, 20 * 60 * 3, 2));
                p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20 * 60 * 3, 3));
                infectedPlayers.add(p.getUniqueId());
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if(infectedPlayers.contains(p.getUniqueId())){
                            for(PotionEffect pe : p.getActivePotionEffects()){
                               p.removePotionEffect(pe.getType());
                            }
                            p.getWorld().playSound(p.getLocation(), Sound.ENTITY_WITCH_DEATH, (float) 6.0, (float) 2.0);
                            infectedPlayers.remove(p.getUniqueId());                          
                        }
                    }
                }.runTaskLater(pl, 100);
            }
        }

        if (ev.getDamager() instanceof Player && ev.getEntity() instanceof Zombie) {
            Player p = (Player) ev.getDamager();
            final Zombie z = (Zombie) ev.getEntity();
            final Component name = z.customName();
            if(enragedList.contains(z.getUniqueId())){
                z.getWorld().spawnParticle(Particle.CRIT, z.getLocation().add(0, 1, 0), 4);
                z.getWorld().playSound(z.getLocation(), Sound.ENTITY_ZOMBIE_INFECT, (float)2.0, (float)1.0);
                return;
            }
            if (getRandom().nextInt(100) < 3) {
                final TextComponent en = Component.text("enraged ",TextColor.color(223, 24, 24),TextDecoration.ITALIC).toBuilder().build();
                p.sendMessage(m.getPrefix().append(Component.text("You have ").append(en).append(Component.text("a ").append(z.customName()))));

                z.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 999999, 2));
                z.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 2999999, 2));

                final TextComponent enragedName = Component.text("ENRAGED ", TextColor.color(203, 31, 31), TextDecoration.BOLD).append(z.customName());
                z.customName(enragedName);
                enragedList.add(z.getUniqueId());
                for (int i = 0; i < 4; i++) {
                    z.getWorld().spawnEntity(z.getLocation(), EntityType.ZOMBIE);
                }

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if(enragedList.contains(z.getUniqueId())){
                            for(PotionEffect pe : z.getActivePotionEffects()){
                                z.removePotionEffect(pe.getType());
                            }
                            enragedList.remove(z.getUniqueId());
                            z.customName(name);
                        }
                    }
                }.runTaskLater(pl, 1200);
            }

        }

    }

    @EventHandler
    public void zombieDeathType(EntityDamageByBlockEvent ev) {
        if (ev.getEntity() instanceof Zombie) {
            if (ev.getCause() == DamageCause.SUFFOCATION) {
                Zombie z = (Zombie) ev.getEntity();
                z.remove();
                Bukkit.getConsoleSender()
                        .sendMessage("Zombie " + z.getName() + " has been removed before it suffocated");
            }
        }

    }


}
