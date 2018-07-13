package com.up.betteries.tileentity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 *
 * @author Ricky
 */
public abstract class TileEntityBatteryBase extends TileEntity {

    /**
     * Whether or not a new tile entity should be created
     */
    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
        return oldState.getBlock() != newState.getBlock();
    }

    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(pos, 0, getUpdateTag());
    }

    @Override
    public void markDirty() {
        super.markDirty();
        notifyBlockUpdate(true, true);
    }
    
    public void markDirty(boolean blockUpdate, boolean sendToClient) {
        super.markDirty();
        notifyBlockUpdate(blockUpdate, sendToClient);
    }

    public void notifyBlockUpdate(boolean blockUpdate, boolean sendToClient) {
        if (getWorld() != null) getWorld().notifyBlockUpdate(getPos(), getWorld().getBlockState(pos), getWorld().getBlockState(pos), (blockUpdate ? 1 : 0) | (sendToClient ? 2 : 0));
    }
    
    
    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        readFromNBT(pkt.getNbtCompound());
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        return writeToNBT(new NBTTagCompound());
    }
}
