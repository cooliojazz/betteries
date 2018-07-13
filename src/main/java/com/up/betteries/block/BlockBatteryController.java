package com.up.betteries.block;

import com.up.betteries.Betteries;
import com.up.betteries.tileentity.TileEntityBatteryBase;
import com.up.betteries.tileentity.TileEntityBatteryController;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 *
 * @author Ricky
 */
public class BlockBatteryController extends BlockBatteryBase {
    
    public static final int GUIID = 1;

    public BlockBatteryController() {
        super(Material.IRON);
	setLightOpacity(0);
	setHardness(1.5f);
	setResistance(10.f);
        setUnlocalizedName("battery_controller");
        setRegistryName("betteries", "battery_controller");
    }

    @Override
    public TileEntityBatteryBase createNewTileEntity(World world, int i) {
        return new TileEntityBatteryController();
    }
    
    @Override
    public boolean onBlockActivated(World world, BlockPos bp, IBlockState bs, EntityPlayer ep, EnumHand hand, EnumFacing face, float f1, float f2, float f3) {
        if (!world.isRemote) {
            ep.openGui(Betteries.instance, GUIID, world, bp.getX(), bp.getY(), bp.getZ());
        }
        return true;
    }

    @Override
    public boolean hasComparatorInputOverride(IBlockState state) {
        return true;
    }

    @Override
    public int getComparatorInputOverride(IBlockState blockState, World worldIn, BlockPos pos) {
        TileEntityBatteryController te = (TileEntityBatteryController)worldIn.getTileEntity(pos);
        return te == null ? 0 : te.getRedstoneLevel();
    }
    
    
}
