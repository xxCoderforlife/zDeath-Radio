package com.nullpointercoding.zdeathradio.Events;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import com.nullpointercoding.zdeathradio.Main;
import com.nullpointercoding.zdeathradio.Utils.CustomRecipes;
import com.nullpointercoding.zdeathradio.Utils.Messages;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;

public class EatingEvent implements Listener {

    private Main pl = Main.getInstance();
    private CustomRecipes cr = pl.getCustomRecipes();
    private Messages m = new Messages();

    @EventHandler
    public void onRottenFleshEat(final PlayerInteractEvent e) {
        Player p = e.getPlayer();
        Action pa = e.getAction();
        ItemStack food = p.getInventory().getItemInMainHand();

        if(!pa.equals(Action.RIGHT_CLICK_AIR) && !pa.equals(Action.RIGHT_CLICK_BLOCK)) {
            return;
        }
        
        if(e.getHand() == EquipmentSlot.OFF_HAND) { 
            return;
        }


        if(!(food.getType() == Material.ROTTEN_FLESH)) {
            return;
        }
            


        if (food.hasItemMeta() && food.getItemMeta().displayName().equals(cr.getCookedRottenFlesh().getItemMeta().displayName())) {

            if(p.getGameMode().equals(GameMode.CREATIVE)){

            }else{
                if(p.getFoodLevel() == 20){
                    final TextComponent tc = Component.text("You are already full!", TextColor.color(253, 242, 0), TextDecoration.ITALIC).toBuilder().build();
                    p.sendMessage(m.getPrefix().append(tc));
                    e.setCancelled(true);
                    return;
                }else{
                    if(food.getAmount() == 1){
                        p.setFoodLevel(p.getFoodLevel() + 1);
                        p.sendActionBar(Component.text("Current Food Level: " + p.getFoodLevel(),NamedTextColor.YELLOW,TextDecoration.ITALIC));
                        p.getInventory().setItemInMainHand(null);
                        e.setCancelled(true);
                    }else{
                        food.setAmount(food.getAmount() - 1);
                        p.sendActionBar(Component.text("Current Food Level: " + p.getFoodLevel(),NamedTextColor.YELLOW,TextDecoration.ITALIC));
                        p.setFoodLevel(p.getFoodLevel() + 1);
                        p.getInventory().setItemInMainHand(food);
                        e.setCancelled(true);
                    }
                    e.setCancelled(true);
                }
                e.setCancelled(true);
            }

        }
    }
}
