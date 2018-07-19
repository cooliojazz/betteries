package com.up.betteries.block;

import com.up.betteries.Betteries;
import com.up.betteries.tileentity.TileEntityBatteryCasing4;
import com.up.betteries.tileentity.TileEntityBatteryMultiblock;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;

/**
 *
 * @author Ricky
 */
public class BlockBatteryCasing4 extends BlockBatteryMultiblock {

    
    public BlockBatteryCasing4() {
        super(Material.IRON);
	setHardness(1.5f);
	setResistance(10.f);
	setLightOpacity(0);
        setUnlocalizedName("battery_casing_4");
        setRegistryName("betteries", "battery_casing_4");
    }

    @Override
    public TileEntityBatteryMultiblock createNewTileEntity(World world, int i) {
        TileEntityBatteryCasing4 te = new TileEntityBatteryCasing4();
        te.setWorld(world);
        return te;
    }
    
}
