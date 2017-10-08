package com.up.betteries.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.energy.EnergyStorage;

/**
 *
 * @author Ricky
 */
public class TileEntityBatteryController extends TileEntityBatteryBase {
    
    public int capacity = 2500000;
    private EnergyStorage store = new EnergyStorage(capacity);

    @Override
    public void readFromNBT(NBTTagCompound nbttc) {
        super.readFromNBT(nbttc);
        capacity = nbttc.getInteger("capacity");
        updateStorageSize();
        store.extractEnergy(store.getEnergyStored(), false);
        store.receiveEnergy(nbttc.getInteger("energy"), false);
    }
    
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbttc) {
        nbttc.setInteger("energy", store.getEnergyStored());
        nbttc.setInteger("capacity", capacity);
        return super.writeToNBT(nbttc);
    }
    
    public void addThisToChild(TileEntityBatteryMultiblock te) {
        te.setParent(this);
        capacity += te.getStorageCapacity();
        updateStorageSize();
    }
    
    public void removeChild(TileEntityBatteryMultiblock te) {
        capacity -= te.getStorageCapacity();
        updateStorageSize();
    }
    
    private void updateStorageSize() {
        int e = store.getEnergyStored();
        store = new EnergyStorage(capacity);
        store.receiveEnergy(e, false);
    }

    public EnergyStorage getStore() {
        return store;
    }
}
