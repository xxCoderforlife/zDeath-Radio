package com.nullpointercoding.zdeathradio;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

import com.nullpointercoding.zdeathradio.Commands.PlayerCommands;
import com.nullpointercoding.zdeathradio.Commands.SysCommands;
import com.nullpointercoding.zdeathradio.Economy.EcoCommands;
import com.nullpointercoding.zdeathradio.Economy.VaultHook;
import com.nullpointercoding.zdeathradio.Economy.PlayerAccount.PlayerAccountCommands;
import com.nullpointercoding.zdeathradio.Messages.Messages;
import com.nullpointercoding.zdeathradio.PlayerEvents.PlayerEvents;
import com.nullpointercoding.zdeathradio.Zombies.ZombieGC;
import com.nullpointercoding.zdeathradio.Zombies.ZombieHandler;

import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;

public class Main extends JavaPlugin {

    private static Main pl;
    private static ZombieGC zGC;
    private static Messages messages;
    private static Economy econ = null;
    private static Chat chat = null;
    private File BankDataFolder = new File(getDataFolder() + File.separator + "Bank Data");
    private File PlayerDataFolder;
    private File PlayerConfigFolder;
    private File PlayerAccountFolder;

    private File cFile;
    private FileConfiguration cFileConfig;

    @Override
    public void onEnable() {
        pl = this;
        registerEcon();
        createConfigFile();
        registerEvents();

        if (!setupEconomy() ) {
            getLogger().severe(String.format("[%s] - Disabled due to no Vault dependency found!", getPluginMeta().getDescription()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        setupChat();
    }

    @Override
    public void onDisable() {
    }

    public static Main getInstance() {
        return pl;
    }

    private void registerEvents() {
        registerMessages();
        getServer().getPluginManager().registerEvents(new ZombieHandler(), this);
        getServer().getPluginManager().registerEvents(new PlayerEvents(), this);
        getCommand("deathadmin").setExecutor(new SysCommands());
        getCommand("deathplayer").setExecutor(new PlayerCommands());
        getCommand("economy").setExecutor(new EcoCommands());
        getCommand("account").setExecutor(new PlayerAccountCommands());

        createDataFolders();

        //
        // HEY! Always keep the ZombieGC last in the registerEvents() method. It will
        // not work if it is not last.
        registerZombieGC();
        //

    }

    private void registerMessages() {
        messages = new Messages();
    }

    private void registerZombieGC() {
        zGC = new ZombieGC();
    }

    private void createConfigFile() {
        cFile = new File(getDataFolder(), "config.yml");
        if (!(cFile.exists())) {
            cFile.getParentFile().mkdirs();
            saveResource("config.yml", false);
        }
        loadConfigFile();

    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    private boolean setupChat() {
        RegisteredServiceProvider<Chat> rsp = getServer().getServicesManager().getRegistration(Chat.class);
        chat = rsp.getProvider();
        return chat != null;
    }

    private void loadConfigFile() {
        cFileConfig = new YamlConfiguration();
        try {
            cFileConfig.load(cFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ZombieGC getZombieGC() {
        return zGC;
    }

    public Messages getMessages() {
        return messages;
    }
    public static Economy getEconomy() {
        return econ;
    }

    public static Chat getChat() {
        return chat;
    }
    private void registerEcon() {
        Bukkit.getServer().getServicesManager().register(Economy.class, new VaultHook(), this,
                ServicePriority.Highest);
    }

    private void createDataFolders(){
        PlayerDataFolder = new File(getDataFolder() + File.separator + "Player Data");
        PlayerConfigFolder = new File(getDataFolder() + File.separator + "Player Configs");
        PlayerAccountFolder = new File(getDataFolder() + File.separator + "Player Accounts");

        if (!PlayerDataFolder.exists()) {
            PlayerDataFolder.mkdirs();
            Bukkit.getConsoleSender().sendMessage("§aPlayer Data folder has been created!");
        }
        if (!PlayerConfigFolder.exists()) {
            PlayerConfigFolder.mkdirs();
            Bukkit.getConsoleSender().sendMessage("§aPlayer Configs folder has been created!");
        }
        if (!PlayerAccountFolder.exists()) {
            PlayerAccountFolder.mkdirs();
            Bukkit.getConsoleSender().sendMessage("§aPlayer Accounts folder has been created!");
        }
    }

    public File getPlayerDataFolder() {
        return PlayerDataFolder;
    }   

    public File getPlayerConfigFolder() {
        return PlayerConfigFolder;
    }

    public File getPlayerAccountFolder() {
        return PlayerAccountFolder;
    }   

}