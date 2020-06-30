package codes.biscuit.skyblockaddons.utils;

import codes.biscuit.skyblockaddons.SkyblockAddons;
import lombok.Setter;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;

import java.awt.*;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

public class BrewingStandTimerManager {

    private static Map<BlockPos, Float> brewingStands = new HashMap<>();
    private static BlockPos lastBrewingStand;
    private static final Color GREEN = new Color(0, 195, 0);
    private static final Color RED = new Color(255, 0, 0);
    @Setter static GuiChest lastOpenChest;

    public static void onRightClickBlock(BlockPos pos) {
        Block block = Minecraft.getMinecraft().theWorld.getBlockState(pos).getBlock();
        if (block.equals(Blocks.brewing_stand)) {
            lastBrewingStand = pos;
        }
    }

    public static void onClose() {
        IInventory inv = lastOpenChest.lowerChestInventory;
        if(EnumUtils.InventoryType.getCurrentInventoryType(inv.getName()) == EnumUtils.InventoryType.BREWING_STAND) {
            ItemStack pane = inv.getStackInSlot(20);
            if(pane.getMetadata() == 1 || pane.getMetadata() == 4) {
                float timeRemaining = Float.parseFloat(EnumChatFormatting.getTextWithoutFormattingCodes(pane.getDisplayName()).replace("s", ""));
                if(lastBrewingStand != null) {
                    brewingStands.put(lastBrewingStand, timeRemaining);
                }
            } else if(pane.getMetadata() == 3) {
                brewingStands.remove(lastBrewingStand);
            }
        }
    }

    public static void onRender(float partialTicks) {
        brewingStands.forEach((pos, time) -> {
            int x = pos.getX();
            int y = pos.getY();
            int z = pos.getZ();

            String str = new DecimalFormat("#0.0's'").format(time);

            SkyblockAddons.getInstance().getUtils().renderNameTag(str, x + 0.5F, y + 1.0F, z + 0.5F, getColor(time), partialTicks);
        });
    }

    public static void onTick() {
        brewingStands.forEach((pos, time) -> {
            time -= 0.05F;
            if (time > 0) {
                brewingStands.put(pos, time);
            } else{
                brewingStands.put(pos, 0.0F);
            }
        });
    }

    public static Color getColor(float time) {
        if(time == 0) return GREEN;
        float p = 1 - time / 20;
        float[] hsb1 = Color.RGBtoHSB(RED.getRed(), RED.getGreen(), RED.getBlue(), null);
        float[] hsb2 = Color.RGBtoHSB(GREEN.getRed(), GREEN.getGreen(), GREEN.getBlue(), null);
        float[] hsb3 = new float[3];

        hsb3[0] = (hsb2[0] - hsb1[0]) * p + hsb1[0];
        hsb3[1] = (hsb2[1] - hsb1[1]) * p + hsb1[1];
        hsb3[2] = (hsb2[2] - hsb1[2]) * p + hsb1[2];

        return new Color(Color.HSBtoRGB(hsb3[0], hsb3[1], hsb3[2]));
    }

}
