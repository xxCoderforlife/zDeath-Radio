package com.nullpointercoding.zdeathradio.Economy;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.title.Title.Times;
import net.kyori.adventure.title.TitlePart;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.nullpointercoding.zdeathradio.Main;
import com.nullpointercoding.zdeathradio.FileManager.PlayerConfigManager;
import com.nullpointercoding.zdeathradio.FileManager.Bank.BankConfigManager;
import com.nullpointercoding.zdeathradio.Messages.Messages;

import io.papermc.paper.annotation.DoNotUse;


public class VaultHook implements Economy {

    private PlayerConfigManager PCM;
    private Messages messages = new Messages();

    @Override
    public EconomyResponse bankBalance(String arg0) {
        BankConfigManager bankConfigManager = new BankConfigManager(arg0 + ".yml");
        YamlConfiguration bankConfig = (YamlConfiguration) bankConfigManager.getConfig();
        return new EconomyResponse(0, bankConfig.getDouble(arg0 + ".Bank_Account.Balance"),
                EconomyResponse.ResponseType.SUCCESS, "Successfully retrieved " + arg0 + "'s bank account balance!");
    }

    @Override
    public EconomyResponse bankDeposit(String arg0, double arg1) {
        BankConfigManager bankConfigManager = new BankConfigManager(arg0 + ".yml");
        YamlConfiguration bankConfig = (YamlConfiguration) bankConfigManager.getConfig();
        bankConfig.set(arg0 + ".Bank_Account.Balance", bankConfig.getDouble(arg0 + ".Bank_Account.Balance") + arg1);
        bankConfigManager.saveConfig();
        return new EconomyResponse(arg1, bankConfig.getDouble(arg0 + ".Bank_Account.Balance"),
                EconomyResponse.ResponseType.SUCCESS,
                "Successfully deposited " + arg1 + " into " + arg0 + "'s bank account!");
    }

    @Override
    public EconomyResponse bankHas(String arg0, double arg1) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'bankHas'");
    }

    @Override
    public EconomyResponse bankWithdraw(String arg0, double arg1) {
        BankConfigManager bankConfigManager = new BankConfigManager(arg0 + ".yml");
        YamlConfiguration bankConfig = (YamlConfiguration) bankConfigManager.getConfig();
        bankConfig.set(arg0 + ".Bank_Account.Balance", bankConfig.getDouble(arg0 + ".Bank_Account.Balance") - arg1);
        bankConfigManager.saveConfig();
        return new EconomyResponse(arg1, bankConfig.getDouble(arg0 + ".Bank_Account.Balance"),
                EconomyResponse.ResponseType.SUCCESS,
                "Successfully withdrew " + arg1 + " from " + arg0 + "'s bank account!");
    }

    @Deprecated
    @Override
    public EconomyResponse createBank(String arg0, String arg1) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createBank'");
    }

    @Override
    public EconomyResponse createBank(String arg0, OfflinePlayer op) {
        if (!BankConfigManager.getBankDataFolder().exists()) {
            BankConfigManager.getBankDataFolder().mkdirs();
        }
        BankConfigManager bankConfigManager = new BankConfigManager(op.getUniqueId().toString());
        YamlConfiguration bankConfig = (YamlConfiguration) bankConfigManager.getConfig();
        bankConfig.set(op.getUniqueId().toString() + ".Bank_Account.Balance", (double) 0.0);
        bankConfig.set(op.getUniqueId().toString() + ".Bank_Account.Interest", (float) 0.82);
        bankConfigManager.saveConfig();
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.SUCCESS, "Bank created successfully!");

    }

    @Override
    @DoNotUse
    @Deprecated
    public boolean createPlayerAccount(String arg0) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createPlayerAccount'");
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer offlinePlayer) {
        Player onlinePlayer = Bukkit.getPlayer(offlinePlayer.getUniqueId());
        Component deathBankPrefix = messages.getDeathBankPrefix();
        if (!(hasAccount(offlinePlayer))) {
            PCM = new PlayerConfigManager(offlinePlayer.getUniqueId().toString());
            PCM.updatePlayerDataFile((Player)offlinePlayer);
            messages.sendConsoleMessage(deathBankPrefix.append(Component.text("Player account created for " + offlinePlayer.getName()).color(NamedTextColor.GREEN)));
            onlinePlayer.sendTitlePart(TitlePart.TITLE, Component.text("Welcome!~ " + onlinePlayer.getUniqueId()).color(NamedTextColor.AQUA));
            onlinePlayer.sendTitlePart(TitlePart.SUBTITLE, Component.text("Creating you an account... one sec").color(NamedTextColor.GREEN));
            onlinePlayer.sendTitlePart(TitlePart.TIMES, Times.times(Duration.ofMillis(20), Duration.ofMillis(40), Duration.ofMillis(20)));
            new BukkitRunnable() {
                @Override
                public void run() {
                    onlinePlayer.sendTitlePart(TitlePart.TITLE, Component.text("Account Created!").color(NamedTextColor.AQUA));
                    onlinePlayer.sendTitlePart(TitlePart.SUBTITLE, Component.text("You can now access your account - /account").color(NamedTextColor.GREEN));
                    onlinePlayer.sendTitlePart(TitlePart.TIMES, Times.times(Duration.ofMillis(20), Duration.ofMillis(40), Duration.ofMillis(20)));
                }
            }.runTaskLater(Main.getInstance(), 40L);
            return true;
        } else {
            onlinePlayer.sendTitlePart(TitlePart.TITLE, Component.text("Welcome Back User: " + onlinePlayer.getUniqueId()).color(NamedTextColor.AQUA));
            onlinePlayer.sendTitlePart(TitlePart.SUBTITLE, Component.text("You have been logged in.").color(NamedTextColor.GREEN));
            onlinePlayer.sendTitlePart(TitlePart.TIMES, Times.times(Duration.ofMillis(20), Duration.ofMillis(40), Duration.ofMillis(20)));
            messages.sendConsoleMessage(deathBankPrefix.append(Component.text("User " + offlinePlayer.getUniqueId() + " is logging in.").color(NamedTextColor.RED)));
            return false;
        }
    }

    @Override
    @DoNotUse
    @Deprecated
    public boolean createPlayerAccount(String arg0, String arg1) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createPlayerAccount'");
    }

    @Override
    @DoNotUse
    @Deprecated
    public boolean createPlayerAccount(OfflinePlayer arg0, String arg1) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createPlayerAccount'");
    }

    @Override
    public String currencyNamePlural() {
        return new String("dollars");
    }

    @Override
    public String currencyNameSingular() {
        return new String("dollar");
    }

    @Override
    public EconomyResponse deleteBank(String arg0) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteBank'");
    }

    @Override
    public EconomyResponse depositPlayer(String arg0, double arg1) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'depositPlayer'");
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer arg0, double arg1) {
        PlayerConfigManager pcm = new PlayerConfigManager(arg0.getUniqueId().toString());
        pcm.addBalance(pcm.getBalance() + arg1);
        return new EconomyResponse(arg1, pcm.getBalance(), EconomyResponse.ResponseType.SUCCESS,
                "Successfully deposited " + arg1 + " into " + arg0.getName() + "'s account!");
    }

    @Override
    public EconomyResponse depositPlayer(String arg0, String arg1, double arg2) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'depositPlayer'");
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer arg0, String arg1, double arg2) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'depositPlayer'");
    }

    @Override
    public String format(double arg0) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'format'");
    }

    @Override
    public int fractionalDigits() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'fractionalDigits'");
    }

    @Override
    public double getBalance(String arg0) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getBalance'");
    }

    @Override
    public double getBalance(OfflinePlayer arg0) {
        PlayerConfigManager pcm = new PlayerConfigManager(arg0.getUniqueId().toString());
        return pcm.getBalance();
    }

    @Override
    public double getBalance(String arg0, String arg1) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getBalance'");
    }

    @Override
    public double getBalance(OfflinePlayer arg0, String arg1) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getBalance'");
    }

    @Override
    public List<String> getBanks() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getBanks'");
    }

    @Override
    public String getName() {
        return new String("§4§lz§a§oDeath§b§oArcade§r");
    }

    @Override
    @DoNotUse
    @Deprecated
    public boolean has(String arg0, double arg1) {
        throw new UnsupportedOperationException("Unimplemented method 'has'");
    }

    @Override
    @DoNotUse
    @Deprecated
    public boolean has(OfflinePlayer arg0, double arg1) {
        throw new UnsupportedOperationException("Unimplemented method 'has'");
    }

    @Override
    @DoNotUse
    @Deprecated
    public boolean has(String arg0, String arg1, double arg2) {
        throw new UnsupportedOperationException("Unimplemented method 'has'");
    }

    @Override
    @DoNotUse
    @Deprecated
    public boolean has(OfflinePlayer arg0, String arg1, double arg2) {
        throw new UnsupportedOperationException("Unimplemented method 'has'");
    }

    @Override
    @DoNotUse
    @Deprecated
    public boolean hasAccount(String arg0) {
        throw new UnsupportedOperationException("Unimplemented method 'hasAccount'");
    }

    @Override
    public boolean hasAccount(OfflinePlayer offlinePlayer) {
        if(PlayerConfigManager.playerConfigExists(offlinePlayer.getUniqueId().toString())){
            return true;
        } else {
            messages.sendConsoleMessage(Component.text("Player account does not exist for " + offlinePlayer.getName()).color(NamedTextColor.RED));
            messages.sendConsoleMessage(Component.text("Creating player account for " + offlinePlayer.getName()).color(NamedTextColor.GREEN));
            return false;

        }
    }

    @Override
    @DoNotUse
    @Deprecated
    public boolean hasAccount(String arg0, String arg1) {
        throw new UnsupportedOperationException("Unimplemented method 'hasAccount'");
    }

    @Override
    @DoNotUse
    @Deprecated
    public boolean hasAccount(OfflinePlayer arg0, String arg1) {
        throw new UnsupportedOperationException("Unimplemented method 'hasAccount'");
    }

    @Override
    public boolean hasBankSupport() {
        return true;
    }

    @Override
    @DoNotUse
    @Deprecated
    public EconomyResponse isBankMember(String arg0, String arg1) {
        throw new UnsupportedOperationException("Unimplemented method 'isBankMember'");
    }

    @Override
    @DoNotUse
    @Deprecated
    public EconomyResponse isBankMember(String arg0, OfflinePlayer arg1) {
        throw new UnsupportedOperationException("Unimplemented method 'isBankMember'");
    }

    @Override
    @DoNotUse
    @Deprecated
    public EconomyResponse isBankOwner(String arg0, String arg1) {
        throw new UnsupportedOperationException("Unimplemented method 'isBankOwner'");
    }

    @Override
    @DoNotUse
    @Deprecated
    public EconomyResponse isBankOwner(String arg0, OfflinePlayer arg1) {
        throw new UnsupportedOperationException("Unimplemented method 'isBankOwner'");
    }

    @Override
    public boolean isEnabled() {
        if (Bukkit.getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    @DoNotUse
    @Deprecated
    public EconomyResponse withdrawPlayer(String arg0, double arg1) {
        throw new UnsupportedOperationException("Unimplemented method 'withdrawPlayer'");
    }

    @Override 
    public EconomyResponse withdrawPlayer(OfflinePlayer arg0, double arg1) {
        PlayerConfigManager pcm = new PlayerConfigManager(arg0.getUniqueId().toString());
        pcm.setBalance(pcm.getBalance() - arg1);
        return new EconomyResponse(arg1, pcm.getBalance(), EconomyResponse.ResponseType.SUCCESS,
                "Withdrawn successfully!");
    }

    public static double round(double value, int places) {
        if (places < 0)
            throw new IllegalArgumentException();

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    @Override
    @DoNotUse
    @Deprecated
    public EconomyResponse withdrawPlayer(String arg0, String arg1, double arg2) {
        throw new UnsupportedOperationException("Unimplemented method 'withdrawPlayer'");
    }

    @Override
    @DoNotUse
    @Deprecated
    public EconomyResponse withdrawPlayer(OfflinePlayer arg0, String arg1, double arg2) {
        throw new UnsupportedOperationException("Unimplemented method 'withdrawPlayer'");
    }

}