package com.up.betteries;

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
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;

public class BlockBattery extends Block implements ITileEntityProvider {

    public BlockBattery() {
        super(Material.IRON);
	isBlockContainer = true;
	setHardness(1.5f);
	setResistance(10.f);
	setLightOpacity(0);
        setUnlocalizedName("battery");
	setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
        setRegistryName("betteries", "battery");
    }

    @Override
    public TileEntity createNewTileEntity(World world, int i) {
        TileEntityBattery te = new TileEntityBattery();
        te.setWorld(world);
        return te;
    }

    @Override
    public void onBlockAdded(World w, BlockPos pos, IBlockState state) {
        LogManager.getLogger(Betteries.MODID).log(Level.INFO, "[Betteries] Block added");
        TileEntityBattery te = (TileEntityBattery)w.getTileEntity(pos);
        TileEntityBatteryMultiblock parent = te.getParentOfNeighbors();
        if (parent != null) {
            te.setParent(parent);
            parent.children++;
        }
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos bp, IBlockState bs, EntityPlayer ep, EnumHand hand, EnumFacing face, float f1, float f2, float f3) {
        if (!world.isRemote) {
            EnergyStorage es = ((TileEntityBattery)world.getTileEntity(bp)).store;
            TileEntityBattery te = (TileEntityBattery)world.getTileEntity(bp);
            if (te.isMultiblock()) {
                ep.sendMessage(new TextComponentString(es.getEnergyStored() + "/" + es.getMaxEnergyStored()));
            } else {
                if (te.getParent() == null) {
                    ep.sendMessage(new TextComponentString(te.children + 1 + "/8"));
                } else {
                    ep.sendMessage(new TextComponentString("Child of " + te.getParent().getPos()));
                }
            }
        }
        return true;
    }

}
