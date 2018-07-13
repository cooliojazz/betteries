package com.up.betteries.item;

import java.util.List;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

/**
 *
 * @author Ricky
 */
public class ItemCapacityText extends ItemBetteriesBase {
    
    String capacity;
    
    public ItemCapacityText(String capacity) {
        super();
        this.capacity = capacity;
    }

    @Override
    public void addInformation(ItemStack is, World world, List<String> list, ITooltipFlag itf) {
        list.add(TextFormatting.ITALIC + "" + TextFormatting.DARK_GRAY + "Stores " + capacity + " FE");
    }
}
