package com.up.betteries.block;

import com.up.betteries.tileentity.TileEntityBatteryCasing;
import com.up.betteries.tileentity.TileEntityBatteryMultiblock;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;

public class BlockBatteryCasing extends BlockBatteryMultiblock {
    
    public BlockBatteryCasing() {
        super(Material.IRON);
	setHardness(1.5f);
	setResistance(10.f);
	setLightOpacity(0);
        setUnlocalizedName("battery_casing");
        setRegistryName("betteries", "battery_casing");
    }

    @Override
    public TileEntityBatteryMultiblock createNewTileEntity(World world, int i) {
        TileEntityBatteryCasing te = new TileEntityBatteryCasing();
        te.setWorld(world);
        return te;
    }

}
