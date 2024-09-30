package com.nullpointercoding.zdeathradio.Events;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Random;

import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Sound;

import com.nullpointercoding.zdeathradio.Main;
import com.nullpointercoding.zdeathradio.Utils.Messages;
import com.nullpointercoding.zdeathradio.Utils.ZombieConfigManager;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.title.TitlePart;

public class ZombieDeathEvent implements Listener{
    
    private Main pl = Main.getInstance();
	Messages m = new Messages();
    ZombieConfigManager zCM = pl.getZombieConfigManager();
    ItemStack totem = new ItemStack(Material.TOTEM_OF_UNDYING);
	ItemStack head = new ItemStack(Material.ZOMBIE_HEAD);
	ItemStack shard = new ItemStack(Material.AMETHYST_SHARD);
	ItemStack box = new ItemStack(Material.SHULKER_BOX);
	ItemStack bottle = new ItemStack(Material.EXPERIENCE_BOTTLE);
	ItemStack fox = new ItemStack(Material.FOX_SPAWN_EGG);
	ItemStack rbed = new ItemStack(Material.RED_BED);


	protected HashMap<ItemStack,String> itemList = new HashMap<>();


	private static final Random r = new Random();

	public ZombieDeathEvent(){
		itemList.put(totem, "totem");
		itemList.put(head, "head");
		itemList.put(shard, "shard");
		itemList.put(box, "box");
		itemList.put(bottle, "bottle");
		itemList.put(fox, "fox");
		itemList.put(rbed, "rbed");

	}

	private static final Random getRandom() {
		return r;
	}

	@EventHandler
	public void onPlayerRespawnEvent(PlayerRespawnEvent e) {
		Player p = (Player) e.getPlayer();
		if(p.getLastDamageCause() instanceof EntityDamageByEntityEvent){
			EntityDamageByEntityEvent eEvent = (EntityDamageByEntityEvent) p.getLastDamageCause();
			if(eEvent.getDamager() instanceof Zombie){
		Zombie z = (Zombie) eEvent.getDamager();
		final TextComponent title = Component.text("You have Died!",TextColor.color(255, 14, 14),TextDecoration.BOLD);
		final TextComponent subtitle = Component.text("You have been killed by a ",TextColor.color(75, 77, 85),TextDecoration.BOLD);
		p.sendTitlePart(TitlePart.TITLE, title);
		p.sendTitlePart(TitlePart.SUBTITLE, subtitle.append(z.customName()));
			}
		}

	}

	@EventHandler
	public void onPlayerDeathEvent(PlayerDeathEvent e) {
		Player p = (Player) e.getPlayer();
		if(p.getLastDamageCause() instanceof EntityDamageByEntityEvent){
			EntityDamageByEntityEvent eEvent = (EntityDamageByEntityEvent) p.getLastDamageCause();
			if(eEvent.getDamager() instanceof Zombie){
			Zombie z = (Zombie) eEvent.getDamager();
			final Component name = p.displayName().style(Style.style(TextColor.color(255, 255, 255), TextDecoration.ITALIC));
			final Component message = name.append(Component.text(" has been killed by a ",TextColor.color(141, 138, 138),TextDecoration.BOLD)).append(z.customName());
			e.deathMessage(m.getPrefix().append(message));
			}
		}
	}

    @EventHandler(priority = org.bukkit.event.EventPriority.HIGHEST)
    public void onZombieDeath(EntityDeathEvent e){
        if(e.getEntity() instanceof Zombie){
			if(e.getEntity().getLastDamageCause() == null){
				EntityDamageEvent ede = new EntityDamageEvent(e.getEntity(), DamageCause.ENTITY_EXPLOSION, 0);
				e.getEntity().setLastDamageCause(ede);
			
			}
			DamageCause cause = e.getEntity().getLastDamageCause().getCause();
            Zombie z = (Zombie) e.getEntity();
			z.getWorld().playEffect(z.getEyeLocation(), Effect.BLAZE_SHOOT, 4);
			if(r.nextInt(100) < 4){
				for(int i = 0; i < 5; i++){
					z.getWorld().spawnEntity(z.getLocation(), EntityType.ZOMBIE);
				}
			}

			if(cause == DamageCause.ENTITY_ATTACK || cause == DamageCause.ENTITY_EXPLOSION){
				if(z.getKiller() instanceof Player){
					Player p = (Player) z.getKiller();
					if(p.hasPermission("zdeathradio.doublexp")){
						Entity orb = z.getWorld().spawn(z.getLocation(), ExperienceOrb.class);
						((ExperienceOrb) orb).setExperience(e.getDroppedExp() * 2);
					}
					p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ITEM_PICKUP, (float) 10.0, (float) 2.0);
					p.sendActionBar(Component.text("You just killed a ",NamedTextColor.WHITE,TextDecoration.ITALIC).append(z.customName()));
				}
				
				if (getRandom().nextInt(100) < 90) {
					z.getWorld().dropItemNaturally(z.getLocation(), head);
				} else if (getRandom().nextInt(100) < 82) {
					z.getWorld().dropItemNaturally(z.getLocation(), shard);
				} else if (getRandom().nextInt(100) < 74) {
					z.getWorld().dropItemNaturally(z.getLocation(), bottle);
				} else if (getRandom().nextInt(100) < 66) {
					z.getWorld().dropItemNaturally(z.getLocation(), box);
				} else if (getRandom().nextInt(100) < 58) {
					z.getWorld().dropItemNaturally(z.getLocation(), fox);
				} else if (getRandom().nextInt(100) < 42) {
					z.getWorld().dropItemNaturally(z.getLocation(), rbed);
				}else if (getRandom().nextInt(100) < 22) {
					z.getWorld().dropItemNaturally(z.getLocation(), totem);
				}
			}else{
				e.getDrops().clear();
			}

        }
      }
    }
