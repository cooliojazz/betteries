package com.up.betteries.tileentity;

import java.util.ArrayList;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;

/**
 *
 * @author Ricky
 */
public abstract class TileEntityBatteryMultiblock extends TileEntity implements ITickable {
    
    private BlockPos parentpos = null;

    @Override
    public void onLoad() {
        getWorld().markBlockRangeForRenderUpdate(getPos(), getPos());
    }
    
    @Override
    public void readFromNBT(NBTTagCompound nbttc) {
        super.readFromNBT(nbttc);
        if (nbttc.getBoolean("child")) {
            setParentPos(new BlockPos(nbttc.getInteger("px"), nbttc.getInteger("py"), nbttc.getInteger("pz")));
        }
//        if (getWorld() != null) getWorld().markBlockRangeForRenderUpdate(getPos(), getPos());
    }
    
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbttc) {
        nbttc.setBoolean("child", hasParent());
        if (hasParent()) {
            nbttc.setInteger("px", parentpos.getX());
            nbttc.setInteger("py", parentpos.getY());
            nbttc.setInteger("pz", parentpos.getZ());
        }
        return super.writeToNBT(nbttc);
    }
    
    public boolean hasParent() {
        return parentpos != null;
    }
    
    public boolean findParent() {
        return findParent(new ArrayList<TileEntityBatteryMultiblock>(), new ArrayList<TileEntityBatteryMultiblock>());
    }
    
    private boolean findParent(ArrayList<TileEntityBatteryMultiblock> callers, ArrayList<TileEntityBatteryMultiblock> ncallers) {
        if (!hasParent()) {
            ArrayList<TileEntityBatteryController> parents = new ArrayList<TileEntityBatteryController>();
            callers.add(this);
            for (EnumFacing dir : EnumFacing.values()) {
                TileEntity nx = getWorld().getTileEntity(getPos().add(dir.getDirectionVec()));
                if (nx instanceof TileEntityBatteryMultiblock) {
                    TileEntityBatteryMultiblock te = (TileEntityBatteryMultiblock)nx;
                    if (!callers.contains(te)) parents.add(te.getParent(callers, ncallers));
                } else if (nx instanceof TileEntityBatteryController) {
                    parents.add((TileEntityBatteryController)nx);
                }
            }
            callers.remove(this);
            for (TileEntityBatteryController te : parents) {
                if (te != null) {
                    te.addThisToChild(this);
                    notifyNeighbors(ncallers);
                    return true;
                }
            }
        }
        return false;
    }
    
    private void notifyNeighbors(ArrayList<TileEntityBatteryMultiblock> callers) {
        callers.add(this);
        for (EnumFacing dir : EnumFacing.values()) {
            TileEntity nx = getWorld().getTileEntity(getPos().add(dir.getDirectionVec()));
            if (nx instanceof TileEntityBatteryMultiblock) {
                TileEntityBatteryMultiblock te = (TileEntityBatteryMultiblock)nx;
                if (!te.hasParent() && !callers.contains(te)) {
                    te.findParent(new ArrayList<TileEntityBatteryMultiblock>(), callers);
                }
            }
        }
        callers.remove(this);
    }
    
    public TileEntityBatteryController getParent() {
        return getParent(new ArrayList<TileEntityBatteryMultiblock>(), new ArrayList<TileEntityBatteryMultiblock>());
    }
    
    private TileEntityBatteryController getParent(ArrayList<TileEntityBatteryMultiblock> callers, ArrayList<TileEntityBatteryMultiblock> ncallers) {
        TileEntityBatteryController parent;
        if (hasParent()) {
            TileEntityBatteryController te = (TileEntityBatteryController)getWorld().getTileEntity(parentpos);
            if (te == null) {
                if (findParent(callers, ncallers)) {
                    parent = (TileEntityBatteryController)getWorld().getTileEntity(parentpos);
                } else {
                    parent = null;
                }
            } else {
                parent = te;
            }
        } else {
            if (findParent(callers, ncallers)) {
                parent = (TileEntityBatteryController)getWorld().getTileEntity(parentpos);
            } else {
                parent = null;
            }
        }
        return parent;
    }
    
    public void setParent(TileEntityBatteryController parent) {
        setParentPos(parent.getPos());
    }
    
    public void setParentPos(BlockPos pos) {
        if (pos == null ? parentpos != null : !pos.equals(parentpos)) {
            IBlockState state = null;
            if (getWorld() != null) state = getWorld().getBlockState(getPos());
            parentpos = pos;
            if (getWorld() != null) getWorld().notifyBlockUpdate(getPos(), state, getWorld().getBlockState(getPos()), 0);
        }
    }

    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(pos, 0, getUpdateTag());
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        readFromNBT(pkt.getNbtCompound());
    }
    
    @Override
    public void update() {
        if (hasParent() && getWorld().getTileEntity(parentpos) == null) {
            setParentPos(null);
        }
    }

    @Override
    public void handleUpdateTag(NBTTagCompound tag) {
        readFromNBT(tag);
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        return writeToNBT(new NBTTagCompound());
    }
    
    public abstract int getStorageCapacity();
    
}
