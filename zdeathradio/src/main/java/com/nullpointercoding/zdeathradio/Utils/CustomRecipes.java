package com.nullpointercoding.zdeathradio.Utils;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.BlastingRecipe;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.SmokingRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import com.nullpointercoding.zdeathradio.Main;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;

public class CustomRecipes {
    

    private Main pl = Main.getInstance();
    private Messages m = new Messages();
    private final Component rottenFleshName = Component.text("Cooked Rotten Flesh", TextColor.color(43, 144, 40),TextDecoration.ITALIC);


    public void addRecipes(){

        furanceRecipe();
        smokerRecipe();
        blastFuranceRecipe();
        Bukkit.getConsoleSender().sendMessage(m.getPrefix().append(Component.text("Loaded all Recipes!",TextColor.color(0, 255, 43),TextDecoration.ITALIC)));

    }

    private void blastFuranceRecipe(){
        NamespacedKey key = new NamespacedKey(pl, "blastFurance_rotten_flesh");
        BlastingRecipe bR = new BlastingRecipe(key, getCookedRottenFlesh(), Material.ROTTEN_FLESH, (float) 0.4, 100);
        pl.getServer().addRecipe(bR);
    }

    private void smokerRecipe(){
        NamespacedKey key = new NamespacedKey(pl, "smoker_rotten_flesh");
        SmokingRecipe sR = new SmokingRecipe(key, getCookedRottenFlesh(), Material.ROTTEN_FLESH, (float) 0.21, 200);
        pl.getServer().addRecipe(sR);
    }

    private void furanceRecipe(){
        NamespacedKey key = new NamespacedKey(pl, "furance_rotten_flesh");
        FurnaceRecipe fr = new FurnaceRecipe(key,getCookedRottenFlesh(), Material.ROTTEN_FLESH, (float).2, 400);
        pl.getServer().addRecipe(fr);
    }

    public Component getRottenFleshName(){
        return rottenFleshName;
    }

    private ItemStack cookedRottenFlesh(){
        ItemStack fur = new ItemStack(Material.ROTTEN_FLESH);
        ItemMeta fMeta = fur.getItemMeta();
        fMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES);
        fMeta.displayName(rottenFleshName);
        ArrayList<Component> lore = new ArrayList<Component>();
        lore.add(Component.space());
        lore.add(Component.text("You can eat this now"));
        fMeta.lore(lore);
        fMeta.addEnchant(Enchantment.SHARPNESS, 1, true);
        fur.setItemMeta(fMeta);
        return fur;
    }

    public ItemStack getCookedRottenFlesh(){
        return cookedRottenFlesh();
    }
}
