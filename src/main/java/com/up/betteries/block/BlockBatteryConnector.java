package com.up.betteries.block;

import com.up.betteries.Betteries;
import static com.up.betteries.block.BlockBatteryMultiblock.con;
import com.up.betteries.tileentity.TileEntityBatteryConnector;
import com.up.betteries.tileentity.TileEntityBatteryMultiblock;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

/**
 *
 * @author Ricky
 */
public class BlockBatteryConnector extends BlockBatteryMultiblock {

    public static PropertyBool out = PropertyBool.create("output");
    
    public BlockBatteryConnector() {
        super(Material.IRON);
	setHardness(1.5f);
	setResistance(10.f);
	setLightOpacity(0);
        setUnlocalizedName("battery_connector");
        setRegistryName("betteries", "battery_connector");
        setDefaultState(getDefaultState().withProperty(out, false));
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos bp, IBlockState bs, EntityPlayer ep, EnumHand hand, EnumFacing face, float f1, float f2, float f3) {
        if (!world.isRemote) {
            world.setBlockState(bp, bs.withProperty(out, !bs.getValue(out)).getActualState(world, bp));
            world.markBlockRangeForRenderUpdate(bp, bp);
            ep.sendStatusMessage(new TextComponentString(world.getBlockState(bp).getValue(out) ? "Output" : "Input"), true);
        }
        return true;
    }
    
    @Override
    public TileEntityBatteryMultiblock createNewTileEntity(World world, int i) {
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

    @Override
    public boolean hasComparatorInputOverride(IBlockState state) {
        return state.getValue(out);
    }

    @Override
    public int getComparatorInputOverride(IBlockState state, World world, BlockPos pos) {
        if (state.getValue(out)) {
            TileEntityBatteryConnector te = (TileEntityBatteryConnector)world.getTileEntity(pos);
            return te != null ? (te.hasParent() ? te.getParent().getRedstoneLevel() : 0) : 0;
        }
        return -1;
    }

}
