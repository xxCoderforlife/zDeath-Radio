package com.nullpointercoding.zdeathradio;

import java.io.File;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import com.nullpointercoding.zdeathradio.Commands.Commands;
import com.nullpointercoding.zdeathradio.Commands.TabCommands;
import com.nullpointercoding.zdeathradio.Events.EatingEvent;
import com.nullpointercoding.zdeathradio.Events.ZombieBlockBreak;
import com.nullpointercoding.zdeathradio.Events.ZombieDeathEvent;
import com.nullpointercoding.zdeathradio.Events.ZombieExplodeEvent;
import com.nullpointercoding.zdeathradio.Events.ZombieHitEvent;
import com.nullpointercoding.zdeathradio.Events.ZombieSpawnEvent;
import com.nullpointercoding.zdeathradio.Utils.CustomRecipes;
import com.nullpointercoding.zdeathradio.Utils.Messages;
import com.nullpointercoding.zdeathradio.Utils.ZombieConfigManager;

public class Main extends JavaPlugin{

    private static Main pl;

    private ZombieConfigManager zCM;

    private File cFile;
    private FileConfiguration cFileConfig;
    private Messages m;

    private CustomRecipes cr;

    @Override
    public void onEnable(){
        pl = this;
        createConfigFile();
        zCM = new ZombieConfigManager();
        getZombieConfigManager().createConfigs();
        cr = new CustomRecipes();
        getCustomRecipes().addRecipes();
        registerEvents();
    }
    @Override
    public void onLoad(){
        m = new Messages();
        m.splashMessage();

    }
    @Override
    public void onDisable(){
    }

    public static Main getInstance(){
        return pl;
    }

    public ZombieConfigManager getZombieConfigManager(){
        return zCM;
    }

    public CustomRecipes getCustomRecipes(){
        return cr;
    }

    private void registerEvents(){
        getServer().getPluginManager().registerEvents(new ZombieSpawnEvent(), this);
        getServer().getPluginCommand("zdeathradio").setExecutor(new Commands());
        getServer().getPluginCommand("zdeathradio").setTabCompleter(new TabCommands());
        getServer().getPluginManager().registerEvents(new ZombieDeathEvent(), this);
        getServer().getPluginManager().registerEvents(new ZombieHitEvent(), this);
        getServer().getPluginManager().registerEvents(new EatingEvent(), this);
        getServer().getPluginManager().registerEvents(new ZombieBlockBreak(), this);
        getServer().getPluginManager().registerEvents(new ZombieExplodeEvent(), this);
    }

    private void createConfigFile(){
        cFile = new File(getDataFolder(), "config.yml");
        if(!(cFile.exists())){
            cFile.getParentFile().mkdirs();
            saveResource("config.yml", false);
        }
        loadConfigFile();

    }

    private void loadConfigFile(){
        cFileConfig = new YamlConfiguration();
        try{
            cFileConfig.load(cFile);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}