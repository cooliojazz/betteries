package com.up.betteries.gui;

import com.up.betteries.tileentity.TileEntityBatteryController;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

/**
 *
 * @author Ricky
 */
public class GuiHandler implements IGuiHandler {

    @Override
    public Container getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        TileEntity te = world.getTileEntity(new BlockPos(x, y, z));
        if (te instanceof TileEntityBatteryController) {
            return new ContainerBatteryController(player.inventory, (TileEntityBatteryController)te);
        }
        return null;
    }

    @Override
    public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        TileEntity te = world.getTileEntity(new BlockPos(x, y, z));
        if (te instanceof TileEntityBatteryController) {
            TileEntityBatteryController containerTileEntity = (TileEntityBatteryController)te;
            return new ContainerGuiBatteryController(containerTileEntity, new ContainerBatteryController(player.inventory, containerTileEntity));
        }
        return null;
    }
}
