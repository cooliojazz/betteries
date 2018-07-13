package com.up.betteries.block;

import com.up.betteries.tileentity.TileEntityBatteryController;
import com.up.betteries.tileentity.TileEntityBatteryMultiblock;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ChunkCache;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

/**
 *
 * @author Ricky
 */
public abstract class BlockBatteryMultiblock extends BlockBatteryBase {

    public static PropertyBool con = PropertyBool.create("connected");
    
    public BlockBatteryMultiblock(Material mat) {
        super(mat);
        setDefaultState(getDefaultState().withProperty(con, false));
    }
    
    @Override
    public void onBlockAdded(World w, BlockPos pos, IBlockState state) {
        if (!w.isRemote) {
            ((TileEntityBatteryMultiblock)w.getTileEntity(pos)).findParent();
        }
    }
    
    @Override
    public void neighborChanged(IBlockState state, World world, BlockPos pos, Block blockIn, BlockPos neighbor) {
        TileEntity entity = world.getTileEntity(neighbor);
        if (!world.isRemote && entity instanceof TileEntityBatteryController) {
            TileEntityBatteryMultiblock te = ((TileEntityBatteryMultiblock)world.getTileEntity(pos));
            if (!te.hasParent()) te.findParent();
        }
        if (!world.isRemote && entity == null) {
            TileEntityBatteryMultiblock te = ((TileEntityBatteryMultiblock)world.getTileEntity(pos));
            te.neigborUpdateCheck();
        }
    }
    
    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        if (!world.isRemote) {
            TileEntityBatteryMultiblock te = (TileEntityBatteryMultiblock)world.getTileEntity(pos);
            if (te.hasParent()) {
                te.getParent().removeChild(te);
            }
        }
        super.breakBlock(world, pos, state);
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
        TileEntityBatteryMultiblock te = (TileEntityBatteryMultiblock)(world instanceof ChunkCache ? ((ChunkCache)world).getTileEntity(pos, Chunk.EnumCreateEntityType.CHECK) : world.getTileEntity(pos));
        return state.withProperty(con, te != null ? te.hasParent() : false);
    }

    @Override
    public abstract TileEntityBatteryMultiblock createNewTileEntity(World world, int i);
    
}
