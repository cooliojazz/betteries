package com.up.betteries;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.energy.EnergyStorage;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;

public class BlockBatteryCasing extends Block implements ITileEntityProvider {

    static PropertyBool con = PropertyBool.create("connected");
    
    public BlockBatteryCasing() {
        super(Material.IRON);
	isBlockContainer = true;
	setHardness(1.5f);
	setResistance(10.f);
	setLightOpacity(0);
        setUnlocalizedName("battery");
	setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
        setRegistryName("betteries", "battery");
//        setDefaultState(blockState.getBaseState().withProperty(con, false));
    }

    @Override
    public TileEntity createNewTileEntity(World world, int i) {
        TileEntityBatteryCasing te = new TileEntityBatteryCasing();
        te.setWorld(world);
        return te;
    }

    @Override
    public void onBlockAdded(World w, BlockPos pos, IBlockState state) {
        TileEntityBatteryCasing te = (TileEntityBatteryCasing)w.getTileEntity(pos);
        TileEntityBatteryMultiblock parent = te.getParentOfNeighbors();
        if (parent != null) {
            te.setParent(parent);
            parent.children++;
            w.notifyBlockUpdate(parent.getPos(), state, getActualState(state, w, parent.getPos()), 0);
            w.notifyBlockUpdate(pos, state, getActualState(state, w, pos), 0);
        }
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        if (!world.isRemote) {
            TileEntityBatteryCasing te = (TileEntityBatteryCasing)world.getTileEntity(pos);
            if (te.hasParent()) {
                te.getParent().children--;
                world.notifyBlockUpdate(te.getParent().getPos(), state, getActualState(state, world, te.getParent().getPos()), 0);
            } else {
                //??????
            }
        }
        super.breakBlock(world, pos, state);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos bp, IBlockState bs, EntityPlayer ep, EnumHand hand, EnumFacing face, float f1, float f2, float f3) {
        if (!world.isRemote) {
            TileEntityBatteryCasing te = (TileEntityBatteryCasing)world.getTileEntity(bp);
            if (te.isMultiblock()) {
                EnergyStorage es;
                if (te.hasParent()) {
                    es = ((TileEntityBatteryCasing)te.getParent()).store;
                } else {
                    es = te.store;
                }
                ep.sendMessage(new TextComponentString(es.getEnergyStored() + "/" + es.getMaxEnergyStored()));
            } else {
                if (!te.hasParent()) {
                    ep.sendMessage(new TextComponentString(te.children + 1 + "/8"));
                } else {
                    ep.sendMessage(new TextComponentString("Child of " + te.getParent().getPos()));
                }
            }
        }
        return true;
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, con);
    }
    
    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState();
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return 0;
    }
    
    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
        LogManager.getLogger(Betteries.MODID).log(Level.INFO, "[Betteries] Updated visual state of " + pos);
        return state.withProperty(con, ((TileEntityBatteryCasing)world.getTileEntity(pos)).isMultiblock());
//        return state.withProperty(con, true);
    }
    
    //Check multiblock:
    // When casing is placed
    //  Find nearest corner
    //  Use to check three touching walls
    //   In dimension d...
    //  Based on passing wall dimensions, attempt to get opposing corner
    //  Use to check remaining three walls
    //  Take count of internal storage blocks

}
