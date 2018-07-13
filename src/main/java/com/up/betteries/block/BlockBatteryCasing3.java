package com.up.betteries.block;

import com.up.betteries.Betteries;
import com.up.betteries.tileentity.TileEntityBatteryCasing3;
import com.up.betteries.tileentity.TileEntityBatteryMultiblock;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;

/**
 *
 * @author Ricky
 */
public class BlockBatteryCasing3 extends BlockBatteryMultiblock {

    
    public BlockBatteryCasing3() {
        super(Material.IRON);
	setHardness(1.5f);
	setResistance(10.f);
	setLightOpacity(0);
        setUnlocalizedName("battery_casing_3");
        setRegistryName("betteries", "battery_casing_3");
    }

    @Override
    public TileEntityBatteryMultiblock createNewTileEntity(World world, int i) {
        TileEntityBatteryCasing3 te = new TileEntityBatteryCasing3();
        te.setWorld(world);
        return te;
    }
    
}
