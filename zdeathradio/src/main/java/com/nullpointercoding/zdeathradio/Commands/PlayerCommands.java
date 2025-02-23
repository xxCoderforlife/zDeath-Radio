package com.nullpointercoding.zdeathradio.Commands;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlayerCommands implements TabExecutor {

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender arg0, @NotNull Command arg1,
            @NotNull String arg2, @NotNull String @NotNull [] arg3) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'onTabComplete'");
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String commandLabel,
            @NotNull String @NotNull [] args) {
                if (sender instanceof ConsoleCommandSender) {
                    ConsoleCommandSender console = (ConsoleCommandSender) sender;
                    console.sendMessage(" Error: You must be a player to use this command");
                    console.sendMessage("Error: Adding in Console Commands in later udpates");
                    return false;
                }
                                return false;
    }

}
