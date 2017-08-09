package com.up.betteries;

import net.minecraft.tileentity.TileEntity;

/**
 *
 * @author Ricky
 */
public class TileEntityBatteryMultiblock extends TileEntity {
    private TileEntityBatteryMultiblock parent = null;
    public int children = 0;

    public TileEntityBatteryMultiblock() {
        TileEntityBatteryMultiblock parent = getParentOfNeighbors();
        if (parent != null) {
            this.parent = parent;
            parent.children++;
        }
    }
    
    public boolean isMultiblock() {
        return parent != null ? parent.isMultiblock() : children > 8;
    }
    
    public boolean hasParent() {
        return parent != null;
    }
    
    public TileEntityBatteryMultiblock getParentOfNeighbors() {
        TileEntity nx = getWorld().getTileEntity(getPos().add(-1, 0, 0));
        if (nx instanceof TileEntityBatteryMultiblock) {
            if (((TileEntityBatteryMultiblock)nx).hasParent()) {
                return ((TileEntityBatteryMultiblock)nx).getParent();
            }
        }
        TileEntity px = getWorld().getTileEntity(getPos().add(1, 0, 0));
        if (px instanceof TileEntityBatteryMultiblock) {
            if (((TileEntityBatteryMultiblock)px).hasParent()) {
                return ((TileEntityBatteryMultiblock)px).getParent();
            }
        }
        TileEntity ny = getWorld().getTileEntity(getPos().add(0, -1, 0));
        if (ny instanceof TileEntityBatteryMultiblock) {
            if (((TileEntityBatteryMultiblock)ny).hasParent()) {
                return ((TileEntityBatteryMultiblock)ny).getParent();
            }
        }
        TileEntity py = getWorld().getTileEntity(getPos().add(0, 1, 0));
        if (py instanceof TileEntityBatteryMultiblock) {
            if (((TileEntityBatteryMultiblock)py).hasParent()) {
                return ((TileEntityBatteryMultiblock)py).getParent();
            }
        }
        TileEntity nz = getWorld().getTileEntity(getPos().add(0, 0, -1));
        if (nx instanceof TileEntityBatteryMultiblock) {
            if (((TileEntityBatteryMultiblock)nz).hasParent()) {
                return ((TileEntityBatteryMultiblock)nz).getParent();
            }
        }
        TileEntity pz = getWorld().getTileEntity(getPos().add(0, 0, 1));
        if (pz instanceof TileEntityBatteryMultiblock) {
            if (((TileEntityBatteryMultiblock)pz).hasParent()) {
                return ((TileEntityBatteryMultiblock)pz).getParent();
            }
        }
        return null;
    }

    public TileEntityBatteryMultiblock getParent() {
        return parent;
    }
    
}
