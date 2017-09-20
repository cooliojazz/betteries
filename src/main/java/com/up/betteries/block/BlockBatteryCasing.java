package com.up.betteries.block;

import com.up.betteries.tileentity.TileEntityBatteryCasing;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockBatteryCasing extends BlockBatteryMultiblock implements ITileEntityProvider {

    
    public BlockBatteryCasing() {
        super(Material.IRON);
	isBlockContainer = true;
	setHardness(1.5f);
	setResistance(10.f);
	setLightOpacity(0);
        setUnlocalizedName("battery_casing");
	setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
        setRegistryName("betteries", "battery_casing");
    }

    @Override
    public TileEntity createNewTileEntity(World world, int i) {
        TileEntityBatteryCasing te = new TileEntityBatteryCasing();
        te.setWorld(world);
        return te;
    }
    
    
    //Check multiblock:
    // When casing is placed
    //  Find nearest corner
    //  Use to check three touching walls
    //   In dimension d...
    //  Based on passing wall dimensions, attempt to get opposing corner
    //  Use to check remaining three walls
    //  Take count of internal storage blocks

}
