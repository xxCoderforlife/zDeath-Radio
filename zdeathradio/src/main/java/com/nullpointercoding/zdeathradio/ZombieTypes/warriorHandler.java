package com.nullpointercoding.zdeathradio.ZombieTypes;

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

public class warriorHandler {

    private Main pl = Main.getInstance();
    private Zombie zombie;

    public warriorHandler() {
    }

    ZombieConfigManager zCM = pl.getZombieConfigManager();

    public LivingEntity convertToWarrior(final Zombie z) {
        String name = zCM.getWarriorConfig().getString("Warrior.Name");
        String color = zCM.getWarriorConfig().getString("Warrior.NameColor");
        z.getAttribute(Attribute.MAX_HEALTH)
                .setBaseValue((double) zCM.getWarriorConfig().getDouble("Warrior.MaxHealth"));
        z.setHealth((double) zCM.getWarriorConfig().getDouble("Warrior.MaxHealth"));
        z.customName(Component.text(name, TextColor.color(Integer.parseInt(color.split(",")[0]),
                Integer.parseInt(color.split(",")[1]), Integer.parseInt(color.split(",")[2]))));
        z.setCustomNameVisible((boolean) zCM.getWarriorConfig().getBoolean("Warrior.isCustomNameVisible"));
        z.getAttribute(Attribute.SPAWN_REINFORCEMENTS).setBaseValue((double) 0.03);
        z.getAttribute(Attribute.FOLLOW_RANGE)
                .setBaseValue((double) zCM.getWarriorConfig().getDouble("Warrior.FollowRange"));
        z.getAttribute(Attribute.ATTACK_DAMAGE)
                .setBaseValue((double) zCM.getWarriorConfig().getDouble("Warrior.AttackDamage"));
        z.getAttribute(Attribute.MOVEMENT_SPEED)
                .setBaseValue((double) zCM.getWarriorConfig().getDouble("Warrior.MovementSpeed"));
        z.setRemoveWhenFarAway((boolean) zCM.getWarriorConfig().getBoolean("Warrior.RemoveWhenFarAway"));
        z.getEquipment().clear();
        z.setCanBreakDoors(true);
        z.setShouldBurnInDay(false);
        z.setCanPickupItems((boolean) zCM.getWarriorConfig().getBoolean("Warrior.CanPickupItems"));
        ItemStack hel = new ItemStack(Material.getMaterial(zCM.getWarriorConfig().getString("Warrior.Helmet")));
        ItemStack chest = new ItemStack(Material.getMaterial(zCM.getWarriorConfig().getString("Warrior.Chestplate")));
        ItemStack legs = new ItemStack(Material.getMaterial(zCM.getWarriorConfig().getString("Warrior.Leggings")));
        ItemStack boots = new ItemStack(Material.getMaterial(zCM.getWarriorConfig().getString("Warrior.Boots")));
        ItemStack mainHand = new ItemStack(Material.getMaterial(zCM.getWarriorConfig().getString("Warrior.MainHand")));
        // ItemStack offHand = new
        // ItemStack(Material.getMaterial(zCM.getWarriorConfig().getString("Warrior.OffHand")));
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

        for (String s : zCM.getWarriorConfig().getStringList("Warrior.PotionEffects")) {
            String[] split = s.split(":");
            z.addPotionEffect(new PotionEffect(Registry.POTION_EFFECT_TYPE.get(new NamespacedKey(pl, split[0])),
                    Integer.parseInt(split[1]), Integer.parseInt(split[2])));
        }

        return z;
    }

    public Zombie getWarrior() {
        return zombie;
    }

    public void spawnWarrior(World w, Location loc) {
        zombie = (Zombie) w.spawnEntity(loc, EntityType.ZOMBIE);
        convertToWarrior(zombie);
    }

}
