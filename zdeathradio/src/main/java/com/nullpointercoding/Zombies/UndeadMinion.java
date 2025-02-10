package com.nullpointercoding.Zombies;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;

public class UndeadMinion {

    private Zombie z;
    private final TextComponent undeadMinionName = Component.text("Undead Minion").decorate(TextDecoration.BOLD).color(NamedTextColor.DARK_RED);

    public UndeadMinion() {
    }

    public UndeadMinion(Entity entityToBeUsed) {
        if (entityToBeUsed.getType() != EntityType.ZOMBIE) {
            return;
        }
        spawnUndeadMinion(entityToBeUsed.getWorld(), entityToBeUsed.getLocation());
        // entityToBeUsed.remove();
    }

    private void spawnUndeadMinion(World world, Location location) {
        if (world != null && location != null) {
            z = (Zombie) world.spawnEntity(location, EntityType.ZOMBIE);
            z.customName(undeadMinionName);
            z.setCustomNameVisible(true);
            z.setAdult();
            z.setImmuneToFire(true);
            EntityEquipment zE = z.getEquipment();
            zE.setHelmet(new ItemStack(Material.IRON_HELMET));
            z.getAttribute(Attribute.MAX_HEALTH).setBaseValue(8);
            z.getAttribute(Attribute.MOVEMENT_SPEED).setBaseValue(0.3);

            // Ensure the zombie does not burn in sunlight
            z.setShouldBurnInDay(false);
        }
    }
}
