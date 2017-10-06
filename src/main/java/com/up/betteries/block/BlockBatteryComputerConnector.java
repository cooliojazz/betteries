package com.up.betteries.block;

import com.up.betteries.tileentity.TileEntityBatteryComputerConnector;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 *
 * @author Ricky
 */
public class BlockBatteryComputerConnector extends BlockBatteryMultiblock implements ITileEntityProvider {
    
    public BlockBatteryComputerConnector() {
        super(Material.IRON);
	isBlockContainer = true;
	setHardness(1.5f);
	setResistance(10.f);
	setLightOpacity(0);
        setUnlocalizedName("battery_computer_connector");
	setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
        setRegistryName("betteries", "battery_computer_connector");
    }

    @Override
    public TileEntity createNewTileEntity(World world, int i) {
        TileEntityBatteryComputerConnector te = new TileEntityBatteryComputerConnector();
        te.setWorld(world);
        return te;
    }

}
