package com.nullpointercoding.zdeathradio.ZombieTypes;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import com.nullpointercoding.zdeathradio.Main;
import com.nullpointercoding.zdeathradio.Utils.ZombieConfigManager;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

public class assassinHandler {

    private Main pl = Main.getInstance();
    private Zombie assassin;

    ZombieConfigManager zCM = pl.getZombieConfigManager();

    public LivingEntity convertToAssassin(final Zombie z) {
        String name = zCM.getAssassinConfig().getString("Assassin.Name");
        String color = zCM.getAssassinConfig().getString("Assassin.NameColor");
        if (z.getLocation().getChunk().isEntitiesLoaded()) {
            z.customName(Component.text(name, TextColor.color(Integer.parseInt(color.split(",")[0]),
                    Integer.parseInt(color.split(",")[1]), Integer.parseInt(color.split(",")[2]))));
            z.setCustomNameVisible((boolean) zCM.getAssassinConfig().getBoolean("Assassin.isCustomNameVisible"));
            z.getAttribute(Attribute.MAX_HEALTH)
                    .setBaseValue((double) zCM.getAssassinConfig().getDouble("Assassin.MaxHealth"));
            z.getAttribute(Attribute.SPAWN_REINFORCEMENTS).setBaseValue((double) 0.03);
            z.getAttribute(Attribute.FOLLOW_RANGE)
                    .setBaseValue((double) zCM.getAssassinConfig().getDouble("Assassin.FollowRange"));
            z.getAttribute(Attribute.ATTACK_DAMAGE)
                    .setBaseValue((double) zCM.getAssassinConfig().getDouble("Assassin.AttackDamage"));
            z.getAttribute(Attribute.MOVEMENT_SPEED)
                    .setBaseValue((double) zCM.getAssassinConfig().getDouble("Assassin.MovementSpeed"));
            z.setRemoveWhenFarAway((boolean) zCM.getAssassinConfig().getBoolean("Assassin.RemoveWhenFarAway"));
            z.setCanBreakDoors(true);
            z.setShouldBurnInDay(false);
            z.getEquipment().clear();
            z.setCanPickupItems((boolean) zCM.getAssassinConfig().getBoolean("Assassin.CanPickupItems"));
            ItemStack hel = new ItemStack(Material.getMaterial(zCM.getAssassinConfig().getString("Assassin.Helmet")));
            ItemStack chest = new ItemStack(
                    Material.getMaterial(zCM.getAssassinConfig().getString("Assassin.Chestplate")));
            ItemStack legs = new ItemStack(
                    Material.getMaterial(zCM.getAssassinConfig().getString("Assassin.Leggings")));
            ItemStack boots = new ItemStack(Material.getMaterial(zCM.getAssassinConfig().getString("Assassin.Boots")));
            ItemStack mainHand = new ItemStack(
                    Material.getMaterial(zCM.getAssassinConfig().getString("Assassin.MainHand")));
            // ItemStack offHand = new
            // ItemStack(Material.getMaterial(zCM.getassassinConfig().getString("assassin.OffHand")));
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
            for (String s : zCM.getAssassinConfig().getStringList("assassin.PotionEffects")) {
                String[] split = s.split(":");
                z.addPotionEffect(new PotionEffect(Registry.POTION_EFFECT_TYPE.get(new NamespacedKey(pl, split[0])),
                        Integer.parseInt(split[1]), Integer.parseInt(split[2])));
            }
        } else {
            Bukkit.getConsoleSender().sendMessage("Tried to spawn a assassin in a unloaded chunk!");
        }
        this.assassin = z;
        return z;
    }

    public Zombie getAssassin() {
        return assassin;
    }

    public void spawnAssassin(World w, Location loc) {
        assassin = (Zombie) w.spawnEntity(loc, EntityType.ZOMBIE);
        convertToAssassin(assassin);

    }
}
