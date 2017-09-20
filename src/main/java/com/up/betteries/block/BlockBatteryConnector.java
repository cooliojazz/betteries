package com.up.betteries.block;

import static com.up.betteries.block.BlockBatteryMultiblock.con;
import com.up.betteries.tileentity.TileEntityBatteryConnector;
import com.up.betteries.tileentity.TileEntityBatteryMultiblock;
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
import net.minecraft.world.ChunkCache;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

/**
 *
 * @author Ricky
 */
public class BlockBatteryConnector extends BlockBatteryMultiblock implements ITileEntityProvider {

    public static PropertyBool out = PropertyBool.create("output");
    
    public BlockBatteryConnector() {
        super(Material.IRON);
	isBlockContainer = true;
	setHardness(1.5f);
	setResistance(10.f);
	setLightOpacity(0);
        setUnlocalizedName("battery_connector");
	setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
        setRegistryName("betteries", "battery_connector");
        setDefaultState(getDefaultState().withProperty(out, false));
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos bp, IBlockState bs, EntityPlayer ep, EnumHand hand, EnumFacing face, float f1, float f2, float f3) {
        if (!world.isRemote) {
            world.setBlockState(bp, bs.withProperty(out, !bs.getValue(out)));
        }
        return true;
    }
    
    @Override
    public TileEntity createNewTileEntity(World world, int i) {
        TileEntityBatteryConnector te = new TileEntityBatteryConnector();
        te.setWorld(world);
        return te;
    }
    
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, out, con);
    }
    
    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(out, meta == 1);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(out) ? 1 : 0;
    }

}
