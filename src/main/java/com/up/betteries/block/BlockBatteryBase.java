package com.up.betteries.block;

import com.up.betteries.Betteries;
import com.up.betteries.tileentity.TileEntityBatteryBase;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;

/**
 *
 * @author Ricky
 */
public abstract class BlockBatteryBase extends Block implements ITileEntityProvider {
    
    public BlockBatteryBase(Material mat) {
        super(mat);
	isBlockContainer = true;
	setCreativeTab(Betteries.betteriesTab);
    }
    
    @Override
    public abstract TileEntityBatteryBase createNewTileEntity(World world, int i);
    
}
