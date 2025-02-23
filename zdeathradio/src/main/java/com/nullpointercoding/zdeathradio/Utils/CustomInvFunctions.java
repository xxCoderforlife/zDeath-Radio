package com.nullpointercoding.zdeathradio.Utils;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.profile.PlayerTextures;

import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;

import com.destroystokyo.paper.profile.PlayerProfile;

import net.kyori.adventure.text.Component;

public class CustomInvFunctions {

    private static final UUID RANDOM_UUID = UUID.fromString("92864445-51c5-4c3b-9039-517c9927d1b4"); // We reuse the
                                                                                                     // same "random"
                                                                                                     // UUID all the
                                                                                                     // time

    public static PlayerProfile getProfile(String url) {
        PlayerProfile profile = (PlayerProfile) Bukkit
                .createProfile(RANDOM_UUID);
        PlayerTextures textures = profile.getTextures();
        URL urlObject;
        try {
            URI uri = URI.create(url);
            urlObject = uri.toURL(); // Convert URI to URL - safer approach
        } catch (IllegalArgumentException | MalformedURLException exception) {
            throw new RuntimeException("Invalid URL", exception);
        }
        textures.setSkin(urlObject);
        profile.setTextures(textures);
        return profile;
    }

    public static ItemStack getBackButton() {
        ItemStack back = new ItemStack(Material.BARRIER);
        ItemMeta meta = back.getItemMeta();
        meta.displayName(Component.text("§c§lBACK"));
        back.setItemMeta(meta);
        return back;
    }

    /**
     * Extracts the display name from an ItemStack and attempts to find
     * an online player with that name. If a player is found, returns their UUID.
     * Otherwise, it returns null.
     *
     * @param item The ItemStack to extract the player display name from.
     * @return The UUID of the player if one is online matching the display name; otherwise null.
     */
    public static UUID getPlayerUUIDFromItemStackDisplayName(ItemStack item) {
        if (item == null || !item.hasItemMeta()) {
            return null;
        }
        ItemMeta meta = item.getItemMeta();
        if (meta == null || !meta.hasDisplayName()) {
            return null;
        }
        // Convert the Component display name to plain text.
        String playerName = PlainTextComponentSerializer.plainText().serialize(meta.displayName());
        Player player = Bukkit.getPlayerExact(playerName);
        return (player != null) ? player.getUniqueId() : null;
    }

    /**
     * Compares two ItemStacks to determine if they are the same.
     * This method checks type, amount, and item meta.
     *
     * @param item1 The first ItemStack.
     * @param item2 The second ItemStack.
     * @return true if the items are considered identical; false otherwise.
     */
    public static boolean areItemStacksEqual(ItemStack item1, ItemStack item2) {
        if (item1 == item2) {
            return true;
        }
        if (item1 == null || item2 == null) {
            return false;
        }
        if (item1.getType() != item2.getType()) {
            return false;
        }
        if (item1.getAmount() != item2.getAmount()) {
            return false;
        }
        // If one has meta but not the other, then they're not equal.
        if (item1.hasItemMeta() != item2.hasItemMeta()) {
            return false;
        }
        // If both have meta, compare the meta.
        if (item1.hasItemMeta()) {
            ItemMeta meta1 = item1.getItemMeta();
            ItemMeta meta2 = item2.getItemMeta();
            if (!meta1.equals(meta2)) {
                return false;
            }
        }
        return true;
    }
}
