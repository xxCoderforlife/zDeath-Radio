package com.nullpointercoding.zdeathradio.ZombieTypes;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;

import com.nullpointercoding.zdeathradio.Main;
import com.nullpointercoding.zdeathradio.Utils.Messages;
import com.nullpointercoding.zdeathradio.Utils.ZombieConfigManager;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

public class henchmenHandler {

    private Main pl = Main.getInstance();
    private Zombie zombie;
    Messages m = new Messages();

    ZombieConfigManager zCM = pl.getZombieConfigManager();

    public LivingEntity convertToHenchmen(final Zombie z){
        String name = zCM.getHenchmenConfig().getString("Henchmen.Name");
        String color = zCM.getHenchmenConfig().getString("Henchmen.NameColor");
        z.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue((double) zCM.getHenchmenConfig().getDouble("Henchmen.MaxHealth"));
        z.customName(Component.text(name,TextColor.color(Integer.parseInt(color.split(",")[0]), Integer.parseInt(color.split(",")[1]), Integer.parseInt(color.split(",")[2]))));
        z.setHealth((double) zCM.getHenchmenConfig().getDouble("Henchmen.MaxHealth"));
        z.setCustomNameVisible((boolean) zCM.getHenchmenConfig().getBoolean("Henchmen.isCustomNameVisible"));
		z.getAttribute(Attribute.ZOMBIE_SPAWN_REINFORCEMENTS).setBaseValue((double) 0.03);
		z.getAttribute(Attribute.GENERIC_FOLLOW_RANGE).setBaseValue((double) zCM.getHenchmenConfig().getDouble("Henchmen.FollowRange"));
		z.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue((double) zCM.getHenchmenConfig().getDouble("Henchmen.AttackDamage"));
		z.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue((double) zCM.getHenchmenConfig().getDouble("Henchmen.MovementSpeed"));
		z.setRemoveWhenFarAway((boolean) zCM.getHenchmenConfig().getBoolean("Henchmen.RemoveWhenFarAway"));
		z.getEquipment().clear();
        z.setCanBreakDoors(true);
        z.setShouldBurnInDay(false);
		z.setCanPickupItems((boolean) zCM.getHenchmenConfig().getBoolean("Henchmen.CanPickupItems"));
        ItemStack hel = new ItemStack(Material.getMaterial(zCM.getHenchmenConfig().getString("Henchmen.Helmet")));
        ItemStack chest = new ItemStack(Material.getMaterial(zCM.getHenchmenConfig().getString("Henchmen.Chestplate")));
        ItemStack legs = new ItemStack(Material.getMaterial(zCM.getHenchmenConfig().getString("Henchmen.Leggings")));
        ItemStack boots = new ItemStack(Material.getMaterial(zCM.getHenchmenConfig().getString("Henchmen.Boots")));
        ItemStack mainHand = new ItemStack(Material.getMaterial(zCM.getHenchmenConfig().getString("Henchmen.MainHand")));
       // ItemStack offHand = new ItemStack(Material.getMaterial(zCM.getHenchmenConfig().getString("Henchmen.OffHand")));
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

        for(String s : zCM.getHenchmenConfig().getStringList("Henchmen.PotionEffects")){
            String[] split = s.split(":");
            z.addPotionEffect(new PotionEffect(PotionEffectType.getByName(split[0]), Integer.parseInt(split[1]), Integer.parseInt(split[2])));
        }

        this.zombie = z;
        return z;
        
    }

    public Zombie getHenchmen(){
        return zombie;
    }

    public void spawnHenchmen(World w, Location loc){
        zombie = (Zombie) w.spawnEntity(loc, org.bukkit.entity.EntityType.ZOMBIE);
        convertToHenchmen(zombie);
    }
    
}
