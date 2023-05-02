package com.nullpointercoding.zdeathradio.ZombieTypes;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.nullpointercoding.zdeathradio.Main;
import com.nullpointercoding.zdeathradio.Utils.ZombieConfigManager;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

public class zombieHandler{
    
    private Main pl = Main.getInstance();
    private Zombie zombie;

    public zombieHandler() {
    }

    ZombieConfigManager zCM = pl.getZombieConfigManager();


    public LivingEntity convertToZombie(final Zombie z){
        String name = zCM.getZombieConfig().getString("Zombie.Name");
        String color = zCM.getZombieConfig().getString("Zombie.NameColor");
        if(z.getLocation().getChunk().isEntitiesLoaded()){
            z.customName(Component.text(name,TextColor.color(Integer.parseInt(color.split(",")[0]), Integer.parseInt(color.split(",")[1]), Integer.parseInt(color.split(",")[2]))));
            z.setCustomNameVisible((boolean) zCM.getZombieConfig().getBoolean("Zombie.isCustomNameVisible"));
            z.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue((double) zCM.getZombieConfig().getDouble("Zombie.MaxHealth"));
            z.getAttribute(Attribute.ZOMBIE_SPAWN_REINFORCEMENTS).setBaseValue((double) 0.03);
            z.getAttribute(Attribute.GENERIC_FOLLOW_RANGE).setBaseValue((double) zCM.getZombieConfig().getDouble("Zombie.FollowRange"));
            z.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue((double) zCM.getZombieConfig().getDouble("Zombie.AttackDamage"));
            z.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue((double) zCM.getZombieConfig().getDouble("Zombie.MovementSpeed"));
            z.setRemoveWhenFarAway((boolean) zCM.getZombieConfig().getBoolean("Zombie.RemoveWhenFarAway"));
            z.setCanBreakDoors(true);
            z.setShouldBurnInDay(false);
            z.getEquipment().clear();
            z.setCanPickupItems((boolean) zCM.getZombieConfig().getBoolean("Zombie.CanPickupItems"));
            ItemStack hel = new ItemStack(Material.getMaterial(zCM.getZombieConfig().getString("Zombie.Helmet")));
            ItemStack chest = new ItemStack(Material.getMaterial(zCM.getZombieConfig().getString("Zombie.Chestplate")));
            ItemStack legs = new ItemStack(Material.getMaterial(zCM.getZombieConfig().getString("Zombie.Leggings")));
            ItemStack boots = new ItemStack(Material.getMaterial(zCM.getZombieConfig().getString("Zombie.Boots")));
            ItemStack mainHand = new ItemStack(Material.getMaterial(zCM.getZombieConfig().getString("Zombie.MainHand")));
           // ItemStack offHand = new ItemStack(Material.getMaterial(zCM.getZombieConfig().getString("Zombie.OffHand")));
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
            for(String s : zCM.getZombieConfig().getStringList("Zombie.PotionEffects")){
                z.addPotionEffect(new PotionEffect(PotionEffectType.getByName(s.split(":")[0]), Integer.parseInt(s.split(":")[1]), Integer.parseInt(s.split(":")[2])));
            }
        }else{
            Bukkit.getConsoleSender().sendMessage("Tried to spawn a zombie in a unloaded chunk!");
        }
        this.zombie = z;
        return z;
    }

    public Zombie getZombie(){
        return zombie;
    }

    public void spawnZombie(World w, Location loc){
        zombie = (Zombie) w.spawnEntity(loc, EntityType.ZOMBIE);
        convertToZombie(zombie);

    }


    }
    
