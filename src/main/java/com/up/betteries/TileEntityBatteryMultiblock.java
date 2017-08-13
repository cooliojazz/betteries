package com.up.betteries;

import net.minecraft.nbt.NBTTagCompound;
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
    private TileEntityBatteryMultiblock parent = null;
    public int children = 0;

    @Override
    public void readFromNBT(NBTTagCompound nbttc) {
        super.readFromNBT(nbttc);
        if (nbttc.getBoolean("child")) {
            parent = (TileEntityBatteryMultiblock)getWorld().getTileEntity(new BlockPos(nbttc.getInteger("px"), nbttc.getInteger("py"), nbttc.getInteger("pz")));
        } else {
            parent = null;
        }
    }
    
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbttc) {
        LogManager.getLogger(Betteries.MODID).log(Level.INFO, "[Betteries] Wrote TEBM NBT");
        if (hasParent()) {
            nbttc.setBoolean("child", true);
            nbttc.setInteger("px", parent.getPos().getX());
            nbttc.setInteger("py", parent.getPos().getY());
            nbttc.setInteger("pz", parent.getPos().getZ());
        } else {
            nbttc.setBoolean("child", false);
        }
        return super.writeToNBT(nbttc);
    }
    
    public boolean isMultiblock() {
        return hasParent() ? parent.isMultiblock() : children > 6;
    }
    
    public boolean hasParent() {
        return parent != null;
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
        return parent;
    }
    public void setParent(TileEntityBatteryMultiblock parent) {
        this.parent = parent;
    }
    
}
