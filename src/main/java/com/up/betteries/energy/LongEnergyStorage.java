package com.up.betteries.energy;

import net.minecraftforge.energy.IEnergyStorage;

/**
 *
 * @author Ricky
 */
public class LongEnergyStorage implements IEnergyStorage {

    protected int energy;
    protected long realEnergy;
    protected int capacity;
    protected long realCapacity;
    protected int maxReceive;
    protected int maxExtract;

    public LongEnergyStorage(long capacity) {
        this(capacity, safeDowncast(capacity));
    }

    public LongEnergyStorage(long capacity, int maxTransfer) {
        this(capacity, maxTransfer, maxTransfer);
    }

    public LongEnergyStorage(long capacity, int maxReceive, int maxExtract) {
        this(capacity, maxReceive, maxExtract, 0);
    }

    public LongEnergyStorage(long capacity, int maxReceive, int maxExtract, long energy) {
        this.realCapacity = capacity;
        this.capacity = safeDowncast(capacity);
        this.maxReceive = maxReceive;
        this.maxExtract = maxExtract;
        this.realEnergy = Math.max(0, Math.min(capacity, energy));
        this.energy = safeDowncast(this.realEnergy);
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        if (!canReceive()) {
            return 0;
        }
        
        int energyReceived = safeDowncast(Math.min(realCapacity - realEnergy, Math.min(maxReceive, maxReceive)));
        if (!simulate) {
            realEnergy += energyReceived;
            energy = safeDowncast(realEnergy);
        }
        return energyReceived;
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        if (!canExtract()) {
            return 0;
        }

        int energyExtracted = safeDowncast(Math.min(realEnergy, Math.min(maxExtract, maxExtract)));
        if (!simulate) {
            realEnergy -= energyExtracted;
            energy = safeDowncast(realEnergy);
        }
        return energyExtracted;
    }

    @Override
    public int getEnergyStored() {
        return energy;
    }

    public long getRealEnergyStored() {
        return realEnergy;
    }

    @Override
    public int getMaxEnergyStored() {
        return capacity;
    }

    public long getRealMaxEnergyStored() {
        return realCapacity;
    }

    @Override
    public boolean canExtract() {
        return maxExtract > 0;
    }

    @Override
    public boolean canReceive() {
        return maxReceive > 0;
    }
    
    protected static int safeDowncast(long l) {
        return (int)Math.max(Math.min(l, Integer.MAX_VALUE), Integer.MIN_VALUE);
    }
    
}
