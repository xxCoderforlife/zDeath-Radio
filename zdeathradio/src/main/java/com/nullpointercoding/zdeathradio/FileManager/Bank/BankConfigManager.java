package com.nullpointercoding.zdeathradio.FileManager.Bank;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.nullpointercoding.zdeathradio.Main;

public class BankConfigManager {

    private File configFile;
    private FileConfiguration config;
    private String configName;
    private Boolean isConfigCreated;
    private static File bankDataFolder = new File(Main.getInstance().getDataFolder() + File.separator + "Bank Data");

    public BankConfigManager(String configName) {
        if (!bankDataFolder.exists()) {
            bankDataFolder.mkdirs();
        }
        this.configName = configName;
        configHandler();
    }

    public String getConfigName() {
        return configName;
    }

    public FileConfiguration getConfig() {
        return (YamlConfiguration) config;
    }

    public File getFile() {
        return configFile;
    }

    public void saveConfig() {
        try {
            config.save(configFile);
        } catch (IOException e) {
            Main.getInstance().saveResource(configName, false);
        }
    }

    private void configHandler() {
        configFile = new File(bankDataFolder + File.separator + configName + ".yml");
        if (!configFile.exists()) {
            try {
                configFile.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        config = new YamlConfiguration();
        try {
            config.load(configFile);
            isConfigCreated = true;
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public Boolean isConfigCreated() {
        return isConfigCreated;
    }

    public static File getBankDataFolder() {
        return bankDataFolder;
    }
}
