package com.up.betteries;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;

/**
 *
 * @author Ricky
 */
public class TileEntityBatteryMultiblock extends TileEntity {
    
    public int children = 0;
    private BlockPos parentpos = null;
    
    @Override
    public void readFromNBT(NBTTagCompound nbttc) {
        super.readFromNBT(nbttc);
        if (nbttc.getBoolean("child")) {
            parentpos = new BlockPos(nbttc.getInteger("px"), nbttc.getInteger("py"), nbttc.getInteger("pz"));
        } else {
            children = nbttc.getInteger("children");
        }
        getWorld().markBlockRangeForRenderUpdate(getPos(), getPos());
    }
    
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbttc) {
        if (hasParent()) {
            nbttc.setBoolean("child", true);
            nbttc.setInteger("px", parentpos.getX());
            nbttc.setInteger("py", parentpos.getY());
            nbttc.setInteger("pz", parentpos.getZ());
        } else {
            nbttc.setBoolean("child", false);
            nbttc.setInteger("children", children);
        }
        return super.writeToNBT(nbttc);
    }
    
    public boolean isMultiblock() {
        return hasParent() ? getParent().isMultiblock() : children > 6;
    }
    
    public boolean hasParent() {
        return parentpos != null;
    }
    
    public TileEntityBatteryMultiblock getParentOfNeighbors() {
        TileEntity nx = getWorld().getTileEntity(getPos().add(-1, 0, 0));
        if (nx instanceof TileEntityBatteryMultiblock) {
            if (((TileEntityBatteryMultiblock)nx).hasParent()) {
                return ((TileEntityBatteryMultiblock)nx).getParent();
            } else {
                return ((TileEntityBatteryMultiblock)nx);
            }
        }
        TileEntity px = getWorld().getTileEntity(getPos().add(1, 0, 0));
        if (px instanceof TileEntityBatteryMultiblock) {
            if (((TileEntityBatteryMultiblock)px).hasParent()) {
                return ((TileEntityBatteryMultiblock)px).getParent();
            } else {
                return ((TileEntityBatteryMultiblock)px);
            }
        }
        TileEntity ny = getWorld().getTileEntity(getPos().add(0, -1, 0));
        if (ny instanceof TileEntityBatteryMultiblock) {
            if (((TileEntityBatteryMultiblock)ny).hasParent()) {
                return ((TileEntityBatteryMultiblock)ny).getParent();
            } else {
                return ((TileEntityBatteryMultiblock)ny);
            }
        }
        TileEntity py = getWorld().getTileEntity(getPos().add(0, 1, 0));
        if (py instanceof TileEntityBatteryMultiblock) {
            if (((TileEntityBatteryMultiblock)py).hasParent()) {
                return ((TileEntityBatteryMultiblock)py).getParent();
            } else {
                return ((TileEntityBatteryMultiblock)py);
            }
        }
        TileEntity nz = getWorld().getTileEntity(getPos().add(0, 0, -1));
        if (nz instanceof TileEntityBatteryMultiblock) {
            if (((TileEntityBatteryMultiblock)nz).hasParent()) {
                return ((TileEntityBatteryMultiblock)nz).getParent();
            } else {
                return ((TileEntityBatteryMultiblock)nz);
            }
        }
        TileEntity pz = getWorld().getTileEntity(getPos().add(0, 0, 1));
        if (pz instanceof TileEntityBatteryMultiblock) {
            if (((TileEntityBatteryMultiblock)pz).hasParent()) {
                return ((TileEntityBatteryMultiblock)pz).getParent();
            } else {
                return ((TileEntityBatteryMultiblock)pz);
            }
        }
        return null;
    }
    
    public TileEntityBatteryMultiblock getParent() {
        if (parentpos == null) return null;
        World w = getWorld();
        TileEntity te = w.getTileEntity(parentpos);
        return (TileEntityBatteryMultiblock)te;
    }
    
    public void setParent(TileEntityBatteryMultiblock parent) {
        parentpos = parent.getPos();
    }

    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(pos, 0, writeToNBT(new NBTTagCompound()));
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        readFromNBT(pkt.getNbtCompound());
        LogManager.getLogger(Betteries.MODID).log(Level.INFO, "[Betteries] Received update packet. Now " + getPos() + " is " + isMultiblock());
    }
    
}
