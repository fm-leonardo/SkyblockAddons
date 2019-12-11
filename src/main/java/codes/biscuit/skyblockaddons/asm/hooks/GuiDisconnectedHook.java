package codes.biscuit.skyblockaddons.asm.hooks;

import codes.biscuit.skyblockaddons.SkyblockAddons;
import codes.biscuit.skyblockaddons.utils.discord.DiscordRPCManager;

public class GuiDisconnectedHook {

    public static void onDisconnect() {
        DiscordRPCManager discordRPCManager = SkyblockAddons.getInstance().getDiscordRPCManager();
        if(discordRPCManager.isActive()) {
            discordRPCManager.stop();
        }
    }
}
