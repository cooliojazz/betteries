package com.up.betteries.block;

import com.up.betteries.Betteries;
import com.up.betteries.tileentity.TileEntityBatteryCasing2;
import com.up.betteries.tileentity.TileEntityBatteryMultiblock;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;

/**
 *
 * @author Ricky
 */
public class BlockBatteryCasing2 extends BlockBatteryMultiblock {

    
    public BlockBatteryCasing2() {
        super(Material.IRON);
	isBlockContainer = true;
	setHardness(1.5f);
	setResistance(10.f);
	setLightOpacity(0);
        setUnlocalizedName("battery_casing_2");
        setRegistryName("betteries", "battery_casing_2");
	setCreativeTab(Betteries.betteriesTab);
    }

    @Override
    public TileEntityBatteryMultiblock createNewTileEntity(World world, int i) {
        TileEntityBatteryCasing2 te = new TileEntityBatteryCasing2();
        te.setWorld(world);
        return te;
    }
    
}
