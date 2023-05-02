package com.nullpointercoding.zdeathradio.ZombieTypes;

import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;

import com.nullpointercoding.zdeathradio.Main;
import com.nullpointercoding.zdeathradio.Utils.Messages;
import com.nullpointercoding.zdeathradio.Utils.ZombieConfigManager;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

public class bruteHandler {

    private Main pl = Main.getInstance();
    private Zombie zombie;
    Messages m = new Messages();

    ZombieConfigManager zCM = pl.getZombieConfigManager();

    public LivingEntity convertToBrute(final Zombie z){
        String name = zCM.getBruteConfig().getString("Brute.Name");
        String color = zCM.getBruteConfig().getString("Brute.NameColor");
        z.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue((double) zCM.getBruteConfig().getDouble("Brute.MaxHealth"));
        z.customName(Component.text(name,TextColor.color(Integer.parseInt(color.split(",")[0]), Integer.parseInt(color.split(",")[1]), Integer.parseInt(color.split(",")[2]))));
        z.setHealth((double) zCM.getBruteConfig().getDouble("Brute.MaxHealth"));
        z.setCustomNameVisible((boolean) zCM.getBruteConfig().getBoolean("Brute.isCustomNameVisible"));
		z.getAttribute(Attribute.ZOMBIE_SPAWN_REINFORCEMENTS).setBaseValue((double) 0.03);
		z.getAttribute(Attribute.GENERIC_FOLLOW_RANGE).setBaseValue((double) zCM.getBruteConfig().getDouble("Brute.FollowRange"));
		z.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue((double) zCM.getBruteConfig().getDouble("Brute.AttackDamage"));
		z.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue((double) zCM.getBruteConfig().getDouble("Brute.MovementSpeed"));
		z.setRemoveWhenFarAway((boolean) zCM.getBruteConfig().getBoolean("Brute.RemoveWhenFarAway"));
		z.getEquipment().clear();
        z.setCanBreakDoors(true);
        z.setShouldBurnInDay(false);
		z.setCanPickupItems((boolean) zCM.getBruteConfig().getBoolean("Brute.CanPickupItems"));
        ItemStack hel = new ItemStack(Material.getMaterial(zCM.getBruteConfig().getString("Brute.Helmet")));
        ItemStack chest = new ItemStack(Material.getMaterial(zCM.getBruteConfig().getString("Brute.Chestplate")));
        ItemStack legs = new ItemStack(Material.getMaterial(zCM.getBruteConfig().getString("Brute.Leggings")));
        ItemStack boots = new ItemStack(Material.getMaterial(zCM.getBruteConfig().getString("Brute.Boots")));
        ItemStack mainHand = new ItemStack(Material.getMaterial(zCM.getBruteConfig().getString("Brute.MainHand")));
       // ItemStack offHand = new ItemStack(Material.getMaterial(zCM.getBruteConfig().getString("Brute.OffHand")));
        z.getEquipment().setHelmet(hel);
        z.getEquipment().setChestplate(chest);
        z.getEquipment().setLeggings(legs);
        z.getEquipment().setBoots(boots);
        z.getEquipment().setItemInMainHand(mainHand);
        //z.getEquipment().setItemInOffHand(offHand);
        z.setAI(true);
        for(PotionEffect pe : z.getActivePotionEffects()){
            z.removePotionEffect(pe.getType());
        }
        for(String s : zCM.getBruteConfig().getStringList("Brute.PotionEffects")){
            if(s == null){
                zCM.getBruteConfig().set("Brute.PotionEffects", "INCREASE_DAMAGE:1:1");
                z.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 1, 1));

            }else{
            String[] split = s.split(":");
            z.addPotionEffect(new PotionEffect(PotionEffectType.getByName(split[0]), Integer.parseInt(split[1]), Integer.parseInt(split[2])));
            }
        }
        this.zombie = z;
        return z;
    }

    public Zombie getBrute(){
        return zombie;
    }

    public void spawnBrute(World w, Location loc){
        zombie = (Zombie) w.spawnEntity(loc, org.bukkit.entity.EntityType.ZOMBIE);
        convertToBrute(zombie);

    }
    
}
