package com.nullpointercoding.zdeathradio.ZombieTypes;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.nullpointercoding.zdeathradio.Main;
import com.nullpointercoding.zdeathradio.Utils.Messages;
import com.nullpointercoding.zdeathradio.Utils.ZombieConfigManager;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

public class suicideBomberHandler {

        private Main pl = Main.getInstance();
        private Zombie zombie;
        Messages m = new Messages();

        ZombieConfigManager zCM = pl.getZombieConfigManager();

        public LivingEntity convertToSuicideBomber(final Zombie z) {
                String name = zCM.getSuicideBomberConfig().getString("SuicideBomber.Name");
                String color = zCM.getSuicideBomberConfig().getString("SuicideBomber.NameColor");
                z.getAttribute(Attribute.MAX_HEALTH)
                                .setBaseValue((double) zCM.getSuicideBomberConfig()
                                                .getDouble("SuicideBomber.MaxHealth"));
                z.customName(Component.text(name, TextColor.color(Integer.parseInt(color.split(",")[0]),
                                Integer.parseInt(color.split(",")[1]), Integer.parseInt(color.split(",")[2]))));
                z.setHealth((double) zCM.getSuicideBomberConfig().getDouble("SuicideBomber.MaxHealth"));
                z.setCustomNameVisible(
                                (boolean) zCM.getSuicideBomberConfig().getBoolean("SuicideBomber.isCustomNameVisible"));
                z.getAttribute(Attribute.SPAWN_REINFORCEMENTS).setBaseValue((double) 0.03);
                z.getAttribute(Attribute.FOLLOW_RANGE)
                                .setBaseValue((double) zCM.getSuicideBomberConfig()
                                                .getDouble("SuicideBomber.FollowRange"));
                z.getAttribute(Attribute.ATTACK_DAMAGE)
                                .setBaseValue((double) zCM.getSuicideBomberConfig()
                                                .getDouble("SuicideBomber.AttackDamage"));
                z.getAttribute(Attribute.MOVEMENT_SPEED)
                                .setBaseValue((double) zCM.getSuicideBomberConfig()
                                                .getDouble("SuicideBomber.MovementSpeed"));
                z.setRemoveWhenFarAway(
                                (boolean) zCM.getSuicideBomberConfig().getBoolean("SuicideBomber.RemoveWhenFarAway"));
                z.getEquipment().clear();
                z.setCanBreakDoors(true);
                z.setShouldBurnInDay(false);
                z.setCanPickupItems((boolean) zCM.getSuicideBomberConfig().getBoolean("SuicideBomber.CanPickupItems"));
                ItemStack hel = new ItemStack(
                                Material.getMaterial(zCM.getSuicideBomberConfig().getString("SuicideBomber.Helmet")));
                ItemStack chest = new ItemStack(
                                Material.getMaterial(
                                                zCM.getSuicideBomberConfig().getString("SuicideBomber.Chestplate")));
                ItemStack legs = new ItemStack(
                                Material.getMaterial(zCM.getSuicideBomberConfig().getString("SuicideBomber.Leggings")));
                ItemStack boots = new ItemStack(
                                Material.getMaterial(zCM.getSuicideBomberConfig().getString("SuicideBomber.Boots")));
                ItemStack mainHand = new ItemStack(
                                Material.getMaterial(zCM.getSuicideBomberConfig().getString("SuicideBomber.MainHand")));
                // ItemStack offHand = new
                // ItemStack(Material.getMaterial(zCM.getSuicideBomberConfig().getString("SuicideBomber.OffHand")));
                LeatherArmorMeta lam = (LeatherArmorMeta) hel.getItemMeta();
                LeatherArmorMeta lam2 = (LeatherArmorMeta) chest.getItemMeta();
                LeatherArmorMeta lam3 = (LeatherArmorMeta) legs.getItemMeta();
                LeatherArmorMeta lam4 = (LeatherArmorMeta) boots.getItemMeta();
                lam.setColor(Color.fromRGB(255, 0, 0));
                lam2.setColor(Color.fromRGB(255, 0, 0));
                lam3.setColor(Color.fromRGB(255, 0, 0));
                lam4.setColor(Color.fromRGB(255, 0, 0));
                hel.setItemMeta(lam);
                chest.setItemMeta(lam2);
                legs.setItemMeta(lam3);
                boots.setItemMeta(lam4);
                z.getEquipment().setHelmet(hel);
                z.getEquipment().setChestplate(chest);
                z.getEquipment().setLeggings(legs);
                z.getEquipment().setBoots(boots);
                z.getEquipment().setItemInMainHand(mainHand);
                // z.getEquipment().setItemInOffHand(offHand);
                z.setAI(true);
                for (PotionEffect pe : z.getActivePotionEffects()) {
                        z.removePotionEffect(pe.getType());
                }
                for (String s : zCM.getSuicideBomberConfig().getStringList("SuicideBomber.PotionEffects")) {
                        if (s == null) {
                                zCM.getSuicideBomberConfig().set("SuicideBomber.PotionEffects", "STRENGTH:1:1");
                                z.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, 1, 1));

                        } else {
                                String[] split = s.split(":");
                                z.addPotionEffect(new PotionEffect(
                                                Registry.POTION_EFFECT_TYPE.get(new NamespacedKey(pl, split[0])),
                                                Integer.parseInt(split[1]), Integer.parseInt(split[2])));
                        }
                }
                this.zombie = z;
                return z;
        }

        public Zombie getSuicideBomber() {
                return zombie;
        }

        public void spawnSuicideBomber(World w, Location loc) {
                zombie = (Zombie) w.spawnEntity(loc, org.bukkit.entity.EntityType.ZOMBIE);
                convertToSuicideBomber(zombie);

        }
}
