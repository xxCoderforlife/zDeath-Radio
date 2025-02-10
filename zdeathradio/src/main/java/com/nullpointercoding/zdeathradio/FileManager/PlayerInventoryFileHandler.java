package com.nullpointercoding.zdeathradio.FileManager;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import com.nullpointercoding.zdeathradio.Main;

public class PlayerInventoryFileHandler {

    private File configFile;
    private FileConfiguration config;
    private String configName;
    private File playersThatQuit;
    private FileConfiguration playersThatQuitConfig;

    private static File playerInventoryDataFolder = new File(Main.getInstance().getDataFolder() + File.separator + "Player Inventory Data");

    public PlayerInventoryFileHandler(String configName) {
        if(!playerInventoryDataFolder.exists()) {
            playerInventoryDataFolder.mkdirs();
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

    public void saveConfig() {
        try {
            config.save(configFile);
        } catch (IOException e) {
            Main.getInstance().saveResource(configName, false);
        }
    }

    private void configHandler() {
        configFile = new File(playerInventoryDataFolder + File.separator + configName + ".yml");
        if (!configFile.exists()) {
            configFile.getParentFile().mkdirs();
            try {
                configFile.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        config = new YamlConfiguration();
        try {
            config.load(configFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public void playersThatQuit() {
        playersThatQuit = new File(playerInventoryDataFolder + File.separator + "playersThatQuit.yml");
        if (!playersThatQuit.exists()) {
            playersThatQuit.getParentFile().mkdirs();
            try {
                playersThatQuit.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        playersThatQuitConfig = new YamlConfiguration();
        try {
            playersThatQuitConfig.load(playersThatQuit);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public YamlConfiguration getPlayersThatQuitConfig() {
        return (YamlConfiguration) playersThatQuitConfig;
    }

    public File getPlayersThatQuit() {
        return playersThatQuit;
    }

    public void deletePlayerInventoryFile() {
        try {
            configFile.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Converts the player inventory to a Base64 encoded string.
     *
     * @param playerInventory to turn into an array of strings.
     * @return string with serialized Inventory
     * @throws IllegalStateException
     */
    public static String playerInventoryToBase64(PlayerInventory playerInventory) throws IllegalStateException {
        // This contains contents, armor and offhand (contents are indexes 0 - 35, armor
        // 36 - 39, offhand - 40)
        return itemStackArrayToBase64(playerInventory.getContents());
    }

    /**
     *
     * A method to serialize an {@link ItemStack} array to Base64 String.
     *
     * @param items to turn into a Base64 String.
     * @return Base64 string of the items.
     * @throws IllegalStateException
     */
    public static String itemStackArrayToBase64(ItemStack[] items) throws IllegalStateException {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ObjectOutputStream dataOutput = new ObjectOutputStream(outputStream);

            dataOutput.writeInt(items.length);

            for (ItemStack item : items) {
                if (item != null) {
                    dataOutput.writeObject(item.serializeAsBytes());
                } else {
                    dataOutput.writeObject(null);
                }
            }

            dataOutput.close();
            return Base64Coder.encodeLines(outputStream.toByteArray());
        } catch (Exception e) {
            throw new IllegalStateException("Unable to save item stacks.", e);
        }
    }

    /**
     * Gets an array of ItemStacks from Base64 string.
     *
     * @param data Base64 string to convert to ItemStack array.
     * @return ItemStack array created from the Base64 string.
     * @throws IOException
     */
    public static ItemStack[] itemStackArrayFromBase64(String data) throws IOException {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
            ObjectInputStream dataInput = new ObjectInputStream(inputStream);
            ItemStack[] items = new ItemStack[dataInput.readInt()];

            for (int Index = 0; Index < items.length; Index++) {
                byte[] stack = (byte[]) dataInput.readObject();

                if (stack != null) {
                    items[Index] = ItemStack.deserializeBytes(stack);
                } else {
                    items[Index] = null;
                }
            }

            dataInput.close();
            return items;
        } catch (ClassNotFoundException e) {
            throw new IOException("Unable to decode class type.", e);
        }
    }

    public static File getPlayerInventoryDataFolder() {
        return playerInventoryDataFolder;
    }
}