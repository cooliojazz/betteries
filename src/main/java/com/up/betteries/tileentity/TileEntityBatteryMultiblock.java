package com.up.betteries.tileentity;

import java.util.ArrayList;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;

/**
 *
 * @author Ricky
 */
public abstract class TileEntityBatteryMultiblock extends TileEntityBatteryBase implements ITickable {
    
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
        } else {
            setParentPos(null);
        }
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
        return parentpos != null && getWorld().getTileEntity(parentpos) != null;
    }
    
    public boolean hasAdjacentBatteryEntity() {
        for (EnumFacing dir : EnumFacing.values()) {
            TileEntity nx = getWorld().getTileEntity(getPos().add(dir.getDirectionVec()));
            if (nx instanceof TileEntityBatteryMultiblock || nx instanceof TileEntityBatteryController) {
                return true;
            }
        }
        return false;
    }
    
    public boolean findParent() {
        TileEntityBatteryController parent = null;
        for (EnumFacing dir : EnumFacing.values()) {
            TileEntity nt = getWorld().getTileEntity(getPos().add(dir.getDirectionVec()));
            if (nt instanceof TileEntityBatteryMultiblock) {
                TileEntityBatteryMultiblock te = (TileEntityBatteryMultiblock)nt;
                if (te.getParent() != null) {
                    parent = te.getParent();
                    break;
                }
            } else if (nt instanceof TileEntityBatteryController) {
                parent = (TileEntityBatteryController)nt;
                break;
            }
        }
        if (parent != null) {
            parent.addThisToChild(this);
            notifyNeighbors(parent);
            return true;
        } else {
            return false;
        }
    }
    
    private boolean isParentRemoved() {
        ArrayList<TileEntityBatteryMultiblock> toCheck = new ArrayList<>();
        ArrayList<TileEntityBatteryMultiblock> checked = new ArrayList<>();
        toCheck.add(this);
        boolean noParent = true;
        while (toCheck.size() > 0 && noParent) {
            TileEntityBatteryMultiblock cur = toCheck.get(toCheck.size() - 1);
            for (EnumFacing dir : EnumFacing.values()) {
                TileEntity nt = getWorld().getTileEntity(getPos().add(dir.getDirectionVec()));
                if (nt instanceof TileEntityBatteryMultiblock) {
                    TileEntityBatteryMultiblock te = (TileEntityBatteryMultiblock)nt;
                    if (!toCheck.contains(te) && !checked.contains(te)) {
                        toCheck.add(te);
                    }
                } else if (nt instanceof TileEntityBatteryController) {
                    noParent = false;
                    break;
                }
            }
            toCheck.remove(cur);
            checked.add(cur);
        }
        return noParent;
    }
    
    private void notifyNeighbors(TileEntityBatteryController parent) {
        notifyNeighbors(new ArrayList<>(), parent);
    }
    
    private void notifyNeighbors(ArrayList<TileEntityBatteryMultiblock> callers, TileEntityBatteryController parent) {
        callers.add(this);
        for (EnumFacing dir : EnumFacing.values()) {
            TileEntity nt = getWorld().getTileEntity(getPos().add(dir.getDirectionVec()));
            if (nt instanceof TileEntityBatteryMultiblock) {
                TileEntityBatteryMultiblock te = (TileEntityBatteryMultiblock)nt;
                if (!te.hasParent() && !callers.contains(te)) {
                    parent.addThisToChild(te);
                    te.notifyNeighbors(callers, parent);
                }
            }
        }
        callers.remove(this);
    }
    
    private void notifyNeighborsRemoved() {
        notifyNeighborsRemoved(new ArrayList<>());
    }
    
    private void notifyNeighborsRemoved(ArrayList<TileEntityBatteryMultiblock> callers) {
        callers.add(this);
        for (EnumFacing dir : EnumFacing.values()) {
            TileEntity nt = getWorld().getTileEntity(getPos().add(dir.getDirectionVec()));
            if (nt instanceof TileEntityBatteryMultiblock) {
                TileEntityBatteryMultiblock te = (TileEntityBatteryMultiblock)nt;
                if (te.hasParent() && !callers.contains(te)) {
                    te.getParent().removeChild(te);
                    te.setParentPos(null);
                    te.notifyNeighborsRemoved(callers);
                }
            }
        }
        callers.remove(this);
    }
    
    public TileEntityBatteryController getParent() {
        return parentpos == null ? null : (TileEntityBatteryController)getWorld().getTileEntity(parentpos);
    }
    
    public void setParent(TileEntityBatteryController parent) {
        setParentPos(parent.getPos());
    }
    
    public void setParentPos(BlockPos pos) {
        if (pos == null ? parentpos != null : !pos.equals(parentpos)) {
            parentpos = pos;
            if (getWorld() != null) {
                markDirty();
            }
        }
    }
    
    public void neigborUpdateCheck() {
        if (hasParent()) {
            if (!hasAdjacentBatteryEntity()) {
                getParent().removeChild(this);
                setParentPos(null);
            } else {
                if (isParentRemoved()) {
                    getParent().removeChild(this);
                    setParentPos(null);
                    notifyNeighborsRemoved();
                }
            }
        }
    }
    
    @Override
    public void update() {
        if (!getWorld().isRemote && parentpos != null && getWorld().getTileEntity(parentpos) == null) {
            setParentPos(null);
        }
    }
    
    public abstract int getStorageCapacity();
    
}
