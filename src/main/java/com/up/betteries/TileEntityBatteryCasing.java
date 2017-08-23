package com.up.betteries;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;

/**
 *
 * @author Ricky
 */
public class TileEntityBatteryCasing extends TileEntityBatteryMultiblock implements ITickable {
    
    EnergyStorage store = new EnergyStorage(10000);
    
    @Override
    public void update() {
//        System.out.println(store.getEnergyStored() + "/" + store.getMaxEnergyStored());
    }

    @Override
    public void readFromNBT(NBTTagCompound nbttc) {
        super.readFromNBT(nbttc);
        store.extractEnergy(store.getEnergyStored(), false);
        store.receiveEnergy(nbttc.getInteger("energy"), false);
    }
    
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbttc) {
        nbttc.setInteger("energy", store.getEnergyStored());
        return super.writeToNBT(nbttc);
    }

    @Override
    public boolean hasCapability(Capability<?> cpblt, EnumFacing ef) {
        if (cpblt == CapabilityEnergy.ENERGY) {
            return true;
        }
        return super.hasCapability(cpblt, ef);
    }
    
    @Override
    public <T> T getCapability(Capability<T> cpblt, EnumFacing ef) {
        if (cpblt == CapabilityEnergy.ENERGY) {
            return (T)store;
        }
        return super.getCapability(cpblt, ef);
    }
    
}