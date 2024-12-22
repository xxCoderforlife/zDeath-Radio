package com.nullpointercoding.zdeathradio.ZombieTypes;

import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.World;

import com.nullpointercoding.zdeathradio.Main;
import com.nullpointercoding.zdeathradio.Utils.Messages;
import com.nullpointercoding.zdeathradio.Utils.ZombieConfigManager;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

public class athleteHandler {

    private Main pl = Main.getInstance();
    private Zombie zombie;
    Messages m = new Messages();

    ZombieConfigManager zCM = pl.getZombieConfigManager();

    public LivingEntity convertToAthlete(final Zombie z) {
        String name = zCM.getAthleteConfig().getString("Athlete.Name");
        String color = zCM.getAthleteConfig().getString("Athlete.NameColor");
        z.getAttribute(Attribute.MAX_HEALTH)
                .setBaseValue((double) zCM.getAthleteConfig().getDouble("Athlete.MaxHealth"));
        z.setHealth((double) zCM.getAthleteConfig().getDouble("Athlete.MaxHealth"));
        z.customName(Component.text(name, TextColor.color(Integer.parseInt(color.split(",")[0]),
                Integer.parseInt(color.split(",")[1]), Integer.parseInt(color.split(",")[2]))));
        z.setCustomNameVisible((boolean) zCM.getAthleteConfig().getBoolean("Athlete.isCustomNameVisible"));
        z.getAttribute(Attribute.SPAWN_REINFORCEMENTS).setBaseValue((double) 0.03);
        z.getAttribute(Attribute.FOLLOW_RANGE)
                .setBaseValue((double) zCM.getAthleteConfig().getDouble("Athlete.FollowRange"));
        z.getAttribute(Attribute.ATTACK_DAMAGE)
                .setBaseValue((double) zCM.getAthleteConfig().getDouble("Athlete.AttackDamage"));
        z.getAttribute(Attribute.MOVEMENT_SPEED)
                .setBaseValue((double) zCM.getAthleteConfig().getDouble("Athlete.MovementSpeed"));
        z.setRemoveWhenFarAway((boolean) zCM.getAthleteConfig().getBoolean("Athlete.RemoveWhenFarAway"));
        z.getEquipment().clear();
        z.setCanBreakDoors(true);
        z.setShouldBurnInDay(false);
        z.setCanPickupItems((boolean) zCM.getAthleteConfig().getBoolean("Athlete.CanPickupItems"));
        ItemStack hel = new ItemStack(Material.getMaterial(zCM.getAthleteConfig().getString("Athlete.Helmet")));
        ItemStack chest = new ItemStack(Material.getMaterial(zCM.getAthleteConfig().getString("Athlete.Chestplate")));
        ItemStack legs = new ItemStack(Material.getMaterial(zCM.getAthleteConfig().getString("Athlete.Leggings")));
        ItemStack boots = new ItemStack(Material.getMaterial(zCM.getAthleteConfig().getString("Athlete.Boots")));
        ItemStack mainHand = new ItemStack(Material.getMaterial(zCM.getAthleteConfig().getString("Athlete.MainHand")));
        // ItemStack offHand = new
        // ItemStack(Material.getMaterial(zCM.getAthleteConfig().getString("Athlete.OffHand")));
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

        for (String s : zCM.getAthleteConfig().getStringList("Athlete.PotionEffects")) {
            String[] split = s.split(":");
            z.addPotionEffect(new PotionEffect(Registry.POTION_EFFECT_TYPE.get(new NamespacedKey(pl, split[0])),
                    Integer.parseInt(split[1]), Integer.parseInt(split[2])));
        }
        this.zombie = z;
        return z;

    }

    public Zombie getAthlete() {
        return zombie;
    }

    public void spawnAthlete(World w, Location loc) {
        zombie = (Zombie) w.spawnEntity(loc, org.bukkit.entity.EntityType.ZOMBIE);
        convertToAthlete(zombie);
    }

}
