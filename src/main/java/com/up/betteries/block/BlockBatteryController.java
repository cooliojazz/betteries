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
	setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
	setLightOpacity(0);
	setHardness(1.5f);
	setResistance(10.f);
        setUnlocalizedName("battery_controller");
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
            ep.sendStatusMessage(new TextComponentString(abbreviateInteger(es.getEnergyStored()) + "RF / " + abbreviateInteger(es.getMaxEnergyStored()) + "RF"), true);
        }
        return true;
    }

    private final String[] abvs = {"", "k", "M", "G", "T", "P", "E"};
    
    private String abbreviateInteger(int i) {
        int exp = Math.max(Math.min((int)(Math.log(i) / Math.log(1000)), abvs.length - 1), 0);
        return String.format("%.2f", i / Math.pow(1000, exp)) + abvs[exp];
    }
}
