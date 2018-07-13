package com.up.betteries.gui;

import com.up.betteries.tileentity.TileEntityBatteryController;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

/**
 *
 * @author Ricky
 */
public class ContainerBatteryController extends Container {

    TileEntityBatteryController te;

    public ContainerBatteryController(IInventory pi, TileEntityBatteryController te) {
        this.te = te;
        
        IItemHandler ih = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
        addSlotToContainer(new SlotItemHandler(ih, 0, 8, 8) {
            @Override
            public void onSlotChanged() {
                super.onSlotChanged();
                te.markDirty();
            }
        });
        addSlotToContainer(new SlotItemHandler(ih, 1, 26, 8) {
            @Override
            public void onSlotChanged() {
                super.onSlotChanged();
                te.markDirty();
            }
        });
        
        // Slots for the main inventory
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; ++col) {
                this.addSlotToContainer(new Slot(pi, col + row * 9 + 9, 8 + col * 18, row * 18 + 84));
            }
        }
        // Slots for the hotbar
        for (int row = 0; row < 9; row++) {
            this.addSlotToContainer(new Slot(pi, row, 8 + row * 18, 142));
        }
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
        ItemStack newis = ItemStack.EMPTY;
        Slot slot = inventorySlots.get(index);

        if (slot != null && slot.getHasStack()) {
            ItemStack is = slot.getStack();
            newis = is.copy();

            if (index < 2) {
                if (!mergeItemStack(is, 2, inventorySlots.size(), true)) return ItemStack.EMPTY;
            } else if (!mergeItemStack(is, 0, 2, false)) {
                return ItemStack.EMPTY;
            }

            if (is.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }
        }

        return newis;
    }
    
    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return !te.isInvalid() && player.getDistanceSq(te.getPos().add(0.5, 0.5, 0.5)) <= 32;
    }

}
