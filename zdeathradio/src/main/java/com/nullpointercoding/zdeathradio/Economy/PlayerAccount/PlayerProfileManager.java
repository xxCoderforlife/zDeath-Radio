package com.nullpointercoding.zdeathradio.Economy.PlayerAccount;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.attribute.Attribute;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent.Reason;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerTextures;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.nullpointercoding.zdeathradio.Main;
import com.nullpointercoding.zdeathradio.FileManager.PlayerConfigManager;
import com.nullpointercoding.zdeathradio.FileManager.Bank.BankAccountGUI;
import com.nullpointercoding.zdeathradio.FileManager.Bank.BankAccountGUI.AccountType;
import com.nullpointercoding.zdeathradio.Utils.BlankSpaceFiller;
import com.nullpointercoding.zdeathradio.Utils.CustomInvFunctions;

import io.papermc.paper.datacomponent.item.CustomModelData;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;

public class PlayerProfileManager implements Listener {

    private final Inventory inv;
    private final Component title;
    private Player target;

    public PlayerProfileManager(Player p) {
        boolean isEventRegistered = HandlerList.getRegisteredListeners(Main.getInstance()).stream()
                .anyMatch(handler -> handler.getListener() instanceof PlayerProfileManager);
        if (!isEventRegistered) {
            Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
        }
        this.target = p;
        title = Component.text(p.getName() + "'s Profile", TextColor.color(32, 181, 77), TextDecoration.UNDERLINED);
        inv = Bukkit.createInventory(null, 36, title);

    }

    public void addItems() {
        inv.setItem(0, playerHead());
        inv.setItem(10, payPlayer());
        inv.setItem(12, setBounty());
        inv.setItem(11, requestPayMent());
        inv.setItem(25, backToPlayers());
    }

    @EventHandler
    public void onClickEvent(InventoryClickEvent e) {
        Player whoClicked = (Player) e.getWhoClicked();
        if (!(e.getView().title().equals(title))) {
            return;
        }
        e.setCancelled(true);
        ItemStack clicked = e.getCurrentItem();

        // List of item stacks and their corresponding actions
        List<ItemStack> items = List.of(
            requestPayMent(),
            payPlayer(),
            setBounty(),
            backToPlayers(),
            playerHead(),
            playerKills(whoClicked),
            cashOnPerson(whoClicked),
            bankedCash(whoClicked),
            playerHealth(),
            token(whoClicked),
            playerZombieKills(whoClicked)
        );

        // List of corresponding actions
        List<Runnable> actions = List.of(
            () -> {
                if (whoClicked.getUniqueId().equals(target.getUniqueId())) {
                    sendPlayerBackOtherPlayersPage(whoClicked);
                    whoClicked.sendMessage(Component.text("§cYou can't request payment from yourself!"));
                    whoClicked.playSound(whoClicked, Sound.ENTITY_PLAYER_HURT, 1.0f, 1.0f);
                    return;
                }
                BankAccountGUI bankGUI = new BankAccountGUI(target);
                bankGUI.setIsDespoisting(true, AccountType.PLAYER);
                bankGUI.openGUI(whoClicked);
                whoClicked.playSound(whoClicked, Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
            },
            () -> {
                if (whoClicked.getUniqueId().equals(target.getUniqueId())) {
                    sendPlayerBackOtherPlayersPage(whoClicked);
                    whoClicked.sendMessage(Component.text("§cYou can't pay yourself!"));
                    whoClicked.playSound(whoClicked, Sound.ENTITY_PLAYER_HURT, 1.0f, 1.0f);
                    return;
                }
                BankAccountGUI bankGUI = new BankAccountGUI(target);
                bankGUI.setIsDespoisting(false, AccountType.PLAYER);
                bankGUI.openGUI(whoClicked);
                whoClicked.playSound(whoClicked, Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
            },
            () -> {
                if (whoClicked.getUniqueId().equals(target.getUniqueId())) {
                    sendPlayerBackOtherPlayersPage(whoClicked);
                    whoClicked.sendMessage(Component.text("§cYou can't set a bounty on yourself!"));
                    whoClicked.playSound(whoClicked, Sound.ENTITY_PLAYER_HURT, 1.0f, 1.0f);
                    return;
                }
                BankAccountGUI bankGUI = new BankAccountGUI(target);
                bankGUI.setIsDespoisting(true, AccountType.BOUNTY);
                bankGUI.openGUI(whoClicked);
                whoClicked.playSound(whoClicked, Sound.ENTITY_PLAYER_DEATH, 1.0f, 1.0f);
            },
            () -> {
                sendPlayerBackOtherPlayersPage(whoClicked);
                whoClicked.playSound(whoClicked, Sound.ITEM_ARMOR_EQUIP_LEATHER, 1.0f, 1.0f);
            },
            () -> {
                whoClicked.playSound(whoClicked, Sound.ENTITY_PLAYER_HURT_ON_FIRE, 1.0f, 1.0f);
            },
            () -> {
                whoClicked.playSound(whoClicked, Sound.ENTITY_PLAYER_HURT_ON_FIRE, 1.0f, 1.0f);
            },
            () -> {
                whoClicked.playSound(whoClicked, Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
            },
            () -> {
                whoClicked.playSound(whoClicked, Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
            },
            () -> {
                whoClicked.playSound(whoClicked, Sound.ENTITY_PLAYER_BURP, 1.0f, 1.0f);
            },
            () -> {
                whoClicked.playSound(whoClicked, Sound.ENTITY_VILLAGER_YES, 1.0f, 1.0f);
            },
            () -> {
                whoClicked.playSound(whoClicked, Sound.ENTITY_ZOMBIE_DEATH, SoundCategory.AMBIENT, 1.0f, 1.0f);
            }
        );

        // Iterate over the items and actions
        for (int i = 0; i < items.size(); i++) {
            if (CustomInvFunctions.areItemStacksEqual(clicked, items.get(i))) {
                actions.get(i).run();
                break;
            }
        }
    }

    private ItemStack requestPayMent() {
        ItemStack item = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        meta.displayName(Component.text("§e§lRequest Payment"));
        List<Component> lore = new ArrayList<Component>();
        lore.add(Component.text("§7§oClick to request payment from a player"));
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        PlayerProfile profile = CustomInvFunctions.getProfile(
                "https://textures.minecraft.net/texture/d7d7f8fd87fe7e34f9113dd385aab7b24ef221c19d455175b2578af7ff46eecf");
        meta.setPlayerProfile(profile);
        meta.lore(lore);
        item.setItemMeta(meta);
        return item;
    }

    private ItemStack playerHead() {
        ItemStack item = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        meta.displayName(Component.text("§c§l" + target.getName()));
        meta.setOwningPlayer(target);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        List<Component> lore = new ArrayList<Component>();
        lore.add(Component.space());
        lore.add(Component.text(target.getName() + " Stats", NamedTextColor.GRAY, TextDecoration.ITALIC));
        meta.lore(lore);
        item.setItemMeta(meta);
        return item;
    }

    private ItemStack payPlayer() {
        ItemStack item = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.displayName(Component.text("§a§lPay Player"));
        List<Component> lore = new ArrayList<Component>();
        lore.add(Component.text("§7§oClick to pay a player"));
        PlayerProfile profile = CustomInvFunctions.getProfile(
                "https://textures.minecraft.net/texture/d7d7f8fd87fe7e34f9113dd385aab7b24ef221c19d455175b2578af7ff46eecf");
        meta.setPlayerProfile(profile);
        meta.lore(lore);
        item.setItemMeta(meta);
        return item;
    }

    private ItemStack setBounty() {
        ItemStack fire = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) fire.getItemMeta();
        meta.displayName(Component.text("§c§lSet Bounty"));
        List<Component> lore = new ArrayList<Component>();
        lore.add(Component.text("§7§oClick to set a bounty on a player"));
        meta.lore(lore);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        PlayerProfile profile = CustomInvFunctions.getProfile(
                "https://textures.minecraft.net/texture/2c57e391e36801da12714cf7bcaed71e2c57fde4815afb692445f2b1393cd520");
        meta.setPlayerProfile(profile);
        fire.setItemMeta(meta);
        return fire;
    }

    private ItemStack backToPlayers() {
        ItemStack bar = new ItemStack(Material.BARRIER);
        ItemMeta meta = bar.getItemMeta();
        meta.displayName(Component.text("§c§lBack"));
        List<Component> lore = new ArrayList<Component>();
        lore.add(Component.text("§7§oClick to go back to the players menu"));
        meta.lore(lore);
        bar.setItemMeta(meta);
        return bar;
    }

    private ItemStack playerZombieKills(Player whoClicked) {
        ItemStack head = new ItemStack(Material.ZOMBIE_HEAD);
        SkullMeta meta = (SkullMeta) head.getItemMeta();
        meta.displayName(Component.text("§c§lZombie Kills"));
        List<Component> lore = new ArrayList<Component>();
        lore.add(Component.space());
        lore.add(Component.text(target.getName() + " has killed "
                + target.getStatistic(org.bukkit.Statistic.KILL_ENTITY, EntityType.ZOMBIE) + " zombies"));
        lore.add(Component.text(
                "Your Zombie kills: " + whoClicked.getStatistic(org.bukkit.Statistic.KILL_ENTITY, EntityType.ZOMBIE)));
        meta.lore(lore);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        head.setItemMeta(meta);
        return head;
    }

    private ItemStack playerHealth() {
        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) head.getItemMeta();
        meta.displayName(Component.text("§c§lHealth"));
        List<Component> lore = new ArrayList<Component>();
        lore.add(Component.space());
        lore.add(Component.text(target.getName() + " has " + target.getHealth() + "/"
                + target.getAttribute(Attribute.MAX_HEALTH).getBaseValue() + " health"));
        meta.lore(lore);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        PlayerProfile profile = CustomInvFunctions.getProfile(
                "https://textures.minecraft.net/texture/fe7a810d2112275cc1821dcc6e29da3d2b8fc659af7290a3cb70be536ae2040a");
        meta.setPlayerProfile(profile);
        head.setItemMeta(meta);
        return head;
    }

    private ItemStack cashOnPerson(Player whoClicked) {
        ItemStack item = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        meta.displayName(Component.text("§c§lCash"));
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        List<Component> lore = new ArrayList<Component>();
        lore.add(Component.space());
        lore.add(Component.text("Cash: " + Main.getEconomy().getBalance(target)));
        lore.add(Component.text("Your Cash: " + Main.getEconomy().getBalance((Player) whoClicked)));
        meta.lore(lore);
        PlayerProfile profile = CustomInvFunctions.getProfile(
                "https://textures.minecraft.net/texture/99e77fae5313bac19bf14577d50093e4738ebd70fd54a4de1a27475d0ec9538f");
        meta.setPlayerProfile(profile);
        item.setItemMeta(meta);
        return item;

    }

    private ItemStack bankedCash(Player whoClicked) {
        ItemStack item = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        meta.displayName(Component.text("§c§lBanked Cash"));
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        List<Component> lore = new ArrayList<Component>();
        lore.add(Component.space());
        lore.add(Component.text("Banked Cash: " + Main.getEconomy().bankBalance(target.getUniqueId().toString()).balance));
        lore.add(Component.text("Your Banked Cash: " + Main.getEconomy().bankBalance(whoClicked.getUniqueId().toString()).balance));
        meta.lore(lore);
        PlayerProfile profile = CustomInvFunctions.getProfile(
                "https://textures.minecraft.net/texture/b25b27ce62ca88743840a95d1c39868f43ca60696a84f564fbd7dda259be00fe");
        meta.setPlayerProfile(profile);
        item.setItemMeta(meta);
        return item;
    }

    private ItemStack playerKills(Player whoClicked) {
        ItemStack item = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        meta.displayName(Component.text("§c§lPlayer Kills"));
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        List<Component> lore = new ArrayList<Component>();
        lore.add(Component.space());
        lore.add(Component.text("Player Kills: " + target.getStatistic(org.bukkit.Statistic.PLAYER_KILLS)));
        lore.add(Component.text("Your Player Kills: " + whoClicked.getStatistic(org.bukkit.Statistic.PLAYER_KILLS)));
        meta.lore(lore);
        PlayerProfile profile = CustomInvFunctions.getProfile(
                "https://textures.minecraft.net/texture/9dd36b762e0ebcae47c78308aaa2717aa77d14001ee40dcea863f5f195d23bd9");
        meta.setPlayerProfile(profile);
        item.setItemMeta(meta);
        return item;
    }

    private ItemStack token(Player whoClicked) {
        PlayerConfigManager tarConfig = new PlayerConfigManager(target.getUniqueId().toString());
        PlayerConfigManager pConfig = new PlayerConfigManager(whoClicked.getUniqueId().toString());
        ItemStack item = new ItemStack(Material.SUNFLOWER);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(Component.text("§c§lTokens"));
        meta.addEnchant(Enchantment.PUNCH, 1, false);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
        List<Component> lore = new ArrayList<Component>();
        lore.add(Component.space());
        lore.add(Component.text("Tokens: " + tarConfig.getTokens()));
        lore.add(Component.text("Your Tokens: " + pConfig.getTokens()));
        meta.lore(lore);
        item.setItemMeta(meta);
        return item;
    }

    private void sendPlayerBackOtherPlayersPage(Player playertoSend) {
        playertoSend.closeInventory(Reason.PLUGIN);
        //TODO: Fix this - Shows player is null when this version of the InventoryClickEvent is used.
       /*OtherPlayerAccounts otherPlayerAccounts = new OtherPlayerAccounts();
        otherPlayerAccounts.openGUI(playertoSend);*/
    }

    private void addPlayerItemStats(Player whoOpened) {
        inv.setItem(23, playerZombieKills(whoOpened));
        inv.setItem(22, playerHealth());
        inv.setItem(21, token(whoOpened));
        inv.setItem(19, cashOnPerson(whoOpened));
        inv.setItem(20, bankedCash(whoOpened));
        inv.setItem(24, playerKills(whoOpened));

    }

    public void openGUI(Player playerWhoOpened) {
        addPlayerItemStats(playerWhoOpened);
        addItems();
        BlankSpaceFiller.fillinBlankInv(inv, List.of(13, 14, 15, 16, 24));
        playerWhoOpened.openInventory(inv);
    }

}
