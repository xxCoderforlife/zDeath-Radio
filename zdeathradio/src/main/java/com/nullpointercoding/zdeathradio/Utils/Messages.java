package com.nullpointercoding.zdeathradio.Utils;



import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;


import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;

public class Messages {
    
    private final TextComponent prefix = Component.text("[",TextColor.color(127, 122, 122),TextDecoration.BOLD).append(
        Component.text("z",TextColor.color(17, 255, 0),TextDecoration.BOLD).append(
            Component.text("DZ",TextColor.color(223, 24, 24),TextDecoration.BOLD).append(
                Component.text("] ",TextColor.color(127, 122, 122),TextDecoration.BOLD)))).toBuilder().build();

    private final TextComponent dash = Component.text("- ",TextColor.color(92, 92, 98),TextDecoration.BOLD);

    private void consoleMessageWithColor(TextComponent tc,int r,int g,int b) {
        final TextComponent message = tc.style(Style.style(TextColor.color(r, g, b))).toBuilder().build();
        CommandSender console = Bukkit.getServer().getConsoleSender();
        console.sendMessage(Component.empty().append(prefix).append(Component.empty().append(message)));
            }
    private void consoleMessageWithStyle(TextComponent tc,int r,int g,int b,TextDecoration textDecoration) {
        final TextComponent message = tc.style(Style.style(TextColor.color(r, g, b), textDecoration)).toBuilder().build();
        CommandSender console = Bukkit.getServer().getConsoleSender();
        console.sendMessage(Component.empty().append(prefix).append(Component.empty().append(message)));
            }
    private void consoleMessage(String message) {
        Component tc = Component.text(message);
        CommandSender console = Bukkit.getServer().getConsoleSender();
        console.sendMessage(Component.empty().append(prefix).append(Component.empty().append(tc)));
            }    
    
    public void sendConsoleMessage(TextComponent tc,int r,int g,int b) {
        consoleMessageWithColor(tc,r,g,b);
    }

    public void sendConsoleMessage(TextComponent tc,int r,int g,int b,TextDecoration textDecoration) {
        consoleMessageWithStyle(tc,r,g,b,textDecoration);
    }

    public void sendConsoleMessage(String message) {
        consoleMessage(message);
    }

    public final TextComponent getPrefix() {
        return prefix.append(Component.empty());
    }

    public final Component getDash() {
        return dash.append(Component.empty());
    }

    public void splashMessage(){
        Logger logger = Bukkit.getLogger();
        logger.info("§a____ ___   ___");
        logger.info("§a|_  /|   \\ | _ \\");
        logger.info(" §a/ / | |) ||   /");
        logger.info("§a/___||___/ |_|_\\");
        logger.info(" ");
        logger.info("§c§cA Plugin Made By xxCoderforlife - https://NullPointer-Coding.dev");
        
        
         
        
        
        
    }


}
