package com.nullpointercoding.zdeathradio.Utils;

import java.io.File;
import java.io.InputStreamReader;
import java.io.Reader;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.nullpointercoding.zdeathradio.Main;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;

public class ZombieConfigManager {

    private Main pl = Main.getInstance();
    private File zFolder;
    private Messages m = new Messages();


    public ZombieConfigManager(){
        zFolder = new File(pl.getDataFolder() + File.separator + "Zombies");
        if(!zFolder.exists()){
            zFolder.mkdir();
        }

    }
    
    private File zFile;
    private FileConfiguration zConfig;

    private File wFile;
    private FileConfiguration wConfig;

    private File aFile;
    private FileConfiguration aConfig;

    private File hFile;
    private FileConfiguration hConfig;

    private File bFile;
    private FileConfiguration bConfig;

    private File asFile;
    private FileConfiguration asConfig;

    private File sBFile;
    private FileConfiguration sBConfig;


    public YamlConfiguration getZombieConfig(){
        return (YamlConfiguration) zConfig;
    }

    public YamlConfiguration getWarriorConfig(){
        return (YamlConfiguration) wConfig;
    }

    public YamlConfiguration getAthleteConfig(){
        return (YamlConfiguration) aConfig;
    }

    public YamlConfiguration getHenchmenConfig(){
        return (YamlConfiguration) hConfig;
    }
    
    public YamlConfiguration getBruteConfig(){
        return (YamlConfiguration) bConfig;
    }

    public YamlConfiguration getAssassinConfig(){
        return (YamlConfiguration) asConfig;
    }

    public YamlConfiguration getSuicideBomberConfig(){
        return (YamlConfiguration) sBConfig;
    }


    public void createConfigs() {
        zFile = new File(zFolder, "zombie.yml");
        wFile = new File(zFolder, "warrior.yml");
        aFile = new File(zFolder, "athlete.yml");
        hFile = new File(zFolder, "henchmen.yml");
        bFile = new File(zFolder, "brute.yml");
        asFile = new File(zFolder, "assassin.yml");
        sBFile = new File(zFolder, "suicidebomber.yml");
        if (!zFolder.exists()) {
            zFolder.mkdir();
        }
        if (!zFile.exists()) {
            try {
                m.sendConsoleMessage(Component.text("Creating Zombie Config File..."), 0, 255, 43);
                zFile.createNewFile();
                Reader r = new InputStreamReader(this.getClass().getResourceAsStream("/zombie.yml"));
                zConfig = YamlConfiguration.loadConfiguration(r);
                zConfig.save(zFile);
                r.close();
                m.sendConsoleMessage(Component.text("Zombie Config File Created"), 0, 255, 43);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (!wFile.exists()) {
            try {
                m.sendConsoleMessage(Component.text("Creating Warrior Config File..."), 0, 255, 43);
                wFile.createNewFile();
                Reader r = new InputStreamReader(this.getClass().getResourceAsStream("/warrior.yml"));
                wConfig = YamlConfiguration.loadConfiguration(r);
                wConfig.save(wFile);
                r.close();
                m.sendConsoleMessage(Component.text("Warrior Config File Created"), 0, 255, 43);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (!aFile.exists()) {
            try {
                m.sendConsoleMessage(Component.text("Creating Athlete Config File..."), 0, 255, 43);
                aFile.createNewFile();
                Reader r = new InputStreamReader(this.getClass().getResourceAsStream("/athlete.yml"));
                aConfig = YamlConfiguration.loadConfiguration(r);
                aConfig.save(aFile);
                r.close();
                m.sendConsoleMessage(Component.text("Created Athlete Config File"), 0, 255, 43);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (!hFile.exists()) {
            try {
                m.sendConsoleMessage(Component.text("Creating Henchmen Config File..."), 0, 255, 43);
                hFile.createNewFile();
                Reader r = new InputStreamReader(this.getClass().getResourceAsStream("/henchmen.yml"));
                hConfig = YamlConfiguration.loadConfiguration(r);
                hConfig.save(hFile);
                r.close();
                m.sendConsoleMessage(Component.text("Created Henchmen Config File"), 0, 255, 43);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (!bFile.exists()) {
            try {
                m.sendConsoleMessage(Component.text("Creating Brute Config File"), 0, 255, 43);
                bFile.createNewFile();
                Reader r = new InputStreamReader(this.getClass().getResourceAsStream("/brute.yml"));
                bConfig = YamlConfiguration.loadConfiguration(r);
                bConfig.save(bFile);
                r.close();
                m.sendConsoleMessage(Component.text("Created Brute Config File"), 0, 255, 43);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (!asFile.exists()) {
            try {
                m.sendConsoleMessage(Component.text("Creating Assassin Config File"), 0, 255, 43);
                asFile.createNewFile();
                Reader r = new InputStreamReader(this.getClass().getResourceAsStream("/assassin.yml"));
                asConfig = YamlConfiguration.loadConfiguration(r);
                asConfig.save(asFile);
                r.close();
                m.sendConsoleMessage(Component.text("Created Assassin Config File"), 0, 255, 43);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if(!sBFile.exists()){
            try {
                m.sendConsoleMessage(Component.text("Creating SuicideBomber Config File"), 0, 255, 43);
                sBFile.createNewFile();
                Reader r = new InputStreamReader(this.getClass().getResourceAsStream("/suicidebomber.yml"));
                sBConfig = YamlConfiguration.loadConfiguration(r);
                sBConfig.save(sBFile);
                r.close();
                m.sendConsoleMessage(Component.text("Created SuicideBomber Config File"), 0, 255, 43);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        loadConfigs();
    }

    private void loadConfigs(){
        for(File zF : zFolder.listFiles()){
            if(zF.getName().equals("zombie.yml")){
                zConfig = new YamlConfiguration();
                try{
                    zConfig.load(zF);
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
            if(zF.getName().equals("warrior.yml")){
                wConfig = new YamlConfiguration();
                try{
                    wConfig.load(zF);
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
            if(zF.getName().equals("athlete.yml")){
                aConfig = new YamlConfiguration();
                try{
                    aConfig.load(zF);
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
            if(zF.getName().equals("henchmen.yml")){
                hConfig = new YamlConfiguration();
                try{
                    hConfig.load(zF);
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
            if(zF.getName().equals("brute.yml")){
                bConfig = new YamlConfiguration();
                try{
                    bConfig.load(zF);
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
            if(zF.getName().equals("assassin.yml")){
                asConfig = new YamlConfiguration();
                try{
                    asConfig.load(zF);
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
            if(zF.getName().equals("suicidebomber.yml")){
                sBConfig = new YamlConfiguration();
                try{
                    sBConfig.load(zF);
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }
        m.sendConsoleMessage(Component.text("Loaded All Configuration Files!"), 0, 255, 90, TextDecoration.ITALIC);
    }

    public void saveConfigs(){
        for(File zF : zFolder.listFiles()){
            if(zF.getName().equals("zombie.yml")){
                try{
                    zConfig.save(zF);
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
            if(zF.getName().equals("warrior.yml")){
                try{
                    wConfig.save(zF);
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
            if(zF.getName().equals("athlete.yml")){
                try{
                    aConfig.save(zF);
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
            if(zF.getName().equals("henchmen.yml")){
                try{
                    hConfig.save(zF);
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
            if(zF.getName().equals("brute.yml")){
                try{
                    bConfig.save(zF);
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
            if(zF.getName().equals("assassin.yml")){
                try{
                    asConfig.save(zF);
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
            if(zF.getName().equals("suicidebomber.yml")){
                try{
                    sBConfig.save(zF);
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }
        m.sendConsoleMessage(Component.text("All Configuration File have been loaded."), 0, 255, 102);
    }

    public void reloadConfigs(){
        loadConfigs();
        m.sendConsoleMessage(Component.text("All Configuration Files have been reloaded."), 0, 255, 102);
    }
}
