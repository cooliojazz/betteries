package com.up.betteries.block;

import com.up.betteries.tileentity.TileEntityBatteryController;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.energy.EnergyStorage;

/**
 *
 * @author Ricky
 */
public class BlockBatteryController extends Block implements ITileEntityProvider {

    public BlockBatteryController() {
        super(Material.IRON);
	isBlockContainer = true;
	setHardness(1.5f);
	setResistance(10.f);
	setLightOpacity(0);
        setUnlocalizedName("battery_controller");
	setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
        setRegistryName("betteries", "battery_controller");
    }

    @Override
    public TileEntity createNewTileEntity(World world, int i) {
        return new TileEntityBatteryController();
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos bp, IBlockState bs, EntityPlayer ep, EnumHand hand, EnumFacing face, float f1, float f2, float f3) {
        if (!world.isRemote) {
            TileEntityBatteryController te = (TileEntityBatteryController)world.getTileEntity(bp);
            EnergyStorage es = te.getStore();
            ep.sendMessage(new TextComponentString(es.getEnergyStored() / 1000 + "K / " + (es.getMaxEnergyStored() / 1000) + "K"));
        }
        return true;
    }
}
