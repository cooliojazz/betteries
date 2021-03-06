package com.up.betteries.block;

import com.up.betteries.Betteries;
import com.up.betteries.tileentity.TileEntityBatteryComputerConnector;
import com.up.betteries.tileentity.TileEntityBatteryMultiblock;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;

/**
 *
 * @author Ricky
 */
public class BlockBatteryComputerConnector extends BlockBatteryMultiblock {
    
    public BlockBatteryComputerConnector() {
        super(Material.IRON);
	setHardness(1.5f);
	setResistance(10.f);
	setLightOpacity(0);
        setUnlocalizedName("battery_computer_connector");
        setRegistryName("betteries", "battery_computer_connector");
    }

    @Override
    public TileEntityBatteryMultiblock createNewTileEntity(World world, int i) {
        TileEntityBatteryComputerConnector te = new TileEntityBatteryComputerConnector();
        te.setWorld(world);
        return te;
    }

}
