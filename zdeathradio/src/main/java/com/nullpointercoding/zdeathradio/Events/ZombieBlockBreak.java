package com.nullpointercoding.zdeathradio.Events;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.nullpointercoding.zdeathradio.Main;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;

import io.papermc.paper.event.entity.EntityMoveEvent;

public class ZombieBlockBreak implements Listener {

    private Main pl = Main.getInstance();

    @EventHandler
    public void onZombieWalk(EntityMoveEvent ev) {
        
        if (!(ev.getEntity() instanceof Zombie)) {
            return;
        }

        Zombie z = (Zombie) ev.getEntity();
        if(z.getTarget() == null || !(z.getTarget() instanceof Player)){
            return;
        }
        Block zBlock = z.getTargetBlockExact(1);
        if (zBlock == null) {
            return;
        }
        Player p = (Player) z.getTarget();

        Block zBlock2 = zBlock.getLocation().getBlock().getRelative(BlockFace.DOWN);
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionQuery query = container.createQuery();
        ApplicableRegionSet set = query.getApplicableRegions(BukkitAdapter.adapt(p.getLocation()));
        for(ProtectedRegion pr : set){
            if(isInRegionZombie(z, pr.getId())){
                return;
            }

        }

        for (String s : pl.getConfig().getStringList("zDeath-Radio.breakableBlocks")) {
            if (zBlock.getType() == Material.getMaterial(s)) {
                if(p.getLocation().distance(z.getLocation()) <= 3){
                    zBlock.breakNaturally();

                }
            }

            if (!(zBlock2 == null)) {
                if (zBlock2.getType() == Material.getMaterial(s)) {
                    if(p.getLocation().distance(z.getLocation()) <= 3){
                        zBlock2.breakNaturally();
    
                    }
                }
            }

        }

    }
      private boolean isInRegionZombie(Zombie p, String region) {
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionQuery query = container.createQuery();
        ApplicableRegionSet set = query.getApplicableRegions(BukkitAdapter.adapt(p.getLocation()));
        for (ProtectedRegion pr : set) if (pr.getId().equalsIgnoreCase(region)) return true;
        return false;
      }

}
