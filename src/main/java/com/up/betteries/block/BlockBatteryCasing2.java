package com.up.betteries.block;

import com.up.betteries.tileentity.TileEntityBatteryCasing2;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 *
 * @author Ricky
 */
public class BlockBatteryCasing2 extends BlockBatteryMultiblock implements ITileEntityProvider {

    
    public BlockBatteryCasing2() {
        super(Material.IRON);
	isBlockContainer = true;
	setHardness(1.5f);
	setResistance(10.f);
	setLightOpacity(0);
        setUnlocalizedName("battery_casing_2");
	setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
        setRegistryName("betteries", "battery_casing_2");
    }

    @Override
    public TileEntity createNewTileEntity(World world, int i) {
        TileEntityBatteryCasing2 te = new TileEntityBatteryCasing2();
        te.setWorld(world);
        return te;
    }
    
}