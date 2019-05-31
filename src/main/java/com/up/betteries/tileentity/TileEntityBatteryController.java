package com.up.betteries.tileentity;

import com.up.betteries.energy.LongEnergyStorage;
import com.up.betteries.item.ItemEnergyStorage;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

/**
 *
 * @author Ricky
 */
public class TileEntityBatteryController extends TileEntityBatteryBase implements ITickable {

    private static final int BASE_CAPACITY = 2000000;
    public static final int AVERAGE_LENGTH = 20;
    
    public class InputBatteryEnergyStorage extends EnergyStorage {
        
        private BatteryEnergyStorage store;

        public InputBatteryEnergyStorage(BatteryEnergyStorage store) {
            super(store.getMaxEnergyStored());
            this.store = store;
        }

        @Override
        public boolean canExtract() {
            return false;
        }
        
        @Override
        public int extractEnergy(int maxExtract, boolean simulate) {
            return 0;
        }
        
        @Override
        public int receiveEnergy(int maxReceive, boolean simulate) {
            return store.receiveEnergy(maxReceive, simulate);
        }
        
    }
    
    public class OutputBatteryEnergyStorage extends EnergyStorage {
        
        private BatteryEnergyStorage store;

        public OutputBatteryEnergyStorage(BatteryEnergyStorage store) {
            super(store.getMaxEnergyStored());
            this.store = store;
        }

        @Override
        public boolean canReceive() {
            return false;
        }
        
        @Override
        public int receiveEnergy(int maxReceive, boolean simulate) {
            return 0;
        }
        
        @Override
        public int extractEnergy(int maxExtract, boolean simulate) {
            return store.receiveEnergy(maxReceive, simulate);
        }
        
    }
    
    public class BatteryEnergyStorage extends LongEnergyStorage {
        
        private class SlidingAverage {
            
            private ArrayList<Integer> bucket = new ArrayList<>();
            private LinkedList<Integer> old = new LinkedList<>();

            public SlidingAverage(int max) {
                for (int i = 0; i < max; i++) old.push(0);
            }
            
            public SlidingAverage(LinkedList<Integer> old) {
                this.old = old;
            }

            public void push(Integer e) {
                bucket.add(e);
            }
            
            public void next() {
                old.push((int)bucket.stream().collect(Collectors.summingInt(i -> i)));
                bucket = new ArrayList<>();
                old.removeLast();
            }
            
            public double getAverage() {
                return old.stream().collect(Collectors.averagingInt(i -> i));
            }
        }

        private SlidingAverage out = new SlidingAverage(AVERAGE_LENGTH);
        private SlidingAverage in = new SlidingAverage(AVERAGE_LENGTH);
        
        
        private static final int BASE_TRANSFER = 1000;
        private static final int TRANSFER_LIMIT = 10000000;
        
        public BatteryEnergyStorage(long capacity) {
            super(capacity);
        }
        
        public BatteryEnergyStorage(long capacity, long energy) {
            super(capacity, safeDowncast(capacity), safeDowncast(capacity), energy);
        }
        
        public int getMaxTransfer() {
            return (int)(TRANSFER_LIMIT * 2 / (1 + Math.exp(-(TileEntityBatteryController.this.capacity - (double)BASE_CAPACITY) / Integer.MAX_VALUE)) - TRANSFER_LIMIT) + BASE_TRANSFER;
        }
        
        public double getAverageOut() {
            return out.getAverage();
        }
        
        public double getAverageIn() {
            return in.getAverage();
        }
        
        @Override
        public int extractEnergy(int maxExtract, boolean simulate) {
            if (maxExtract < 0) maxExtract = Integer.MAX_VALUE;
            int oldRS = getRedstoneLevel();
            int ret = super.extractEnergy(Math.min(maxExtract, getMaxTransfer()), simulate);
            if (!simulate) {
                if (oldRS == getRedstoneLevel()) {
                    markDirty(false, true);
                } else {
                    markDirty();
                }
                out.push(ret);
            }
            return ret;
        }

        @Override
        public int receiveEnergy(int maxReceive, boolean simulate) {
            if (maxReceive < 0) maxReceive = Integer.MAX_VALUE;
            int oldRS = getRedstoneLevel();
            int ret = super.receiveEnergy(Math.min(maxReceive, getMaxTransfer()), simulate);
            if (!simulate) {
                if (oldRS == getRedstoneLevel()) {
                    markDirty(false, true);
                } else {
                    markDirty();
                }
                in.push(ret);
            }
            return ret;
        }
        
    }
    
    public long capacity = BASE_CAPACITY;
    private BatteryEnergyStorage store = new BatteryEnergyStorage(capacity);
    private ItemStackHandler inv = new ItemStackHandler(2) {
        
        @Override
        protected void onContentsChanged(int slot) {
            markDirty(false, true);
        }
        
        @Override
        public ItemStack insertItem(int slot, ItemStack is, boolean simulate) {
            if (is.hasCapability(CapabilityEnergy.ENERGY, null) && getStackInSlot(slot).isEmpty()) {
                return super.insertItem(slot, is, simulate);
            }
            return is;
        }

        @Override
        public int getSlotLimit(int slot) {
            return 1;
        }
        
    };
    
    @Override
    public void readFromNBT(NBTTagCompound nbttc) {
        super.readFromNBT(nbttc);
        capacity = nbttc.getLong("capacity");
        store = new BatteryEnergyStorage(capacity, nbttc.getLong("energy"));
        store.in.old = new LinkedList<>(IntStream.of(nbttc.getIntArray("ins").length >= AVERAGE_LENGTH ? nbttc.getIntArray("ins") : new int[AVERAGE_LENGTH]).mapToObj(i -> i).limit(AVERAGE_LENGTH).collect(Collectors.toList()));
        store.out.old = new LinkedList<>(IntStream.of(nbttc.getIntArray("outs").length >= AVERAGE_LENGTH ? nbttc.getIntArray("outs") : new int[AVERAGE_LENGTH]).mapToObj(i -> i).limit(AVERAGE_LENGTH).collect(Collectors.toList()));
        if (nbttc.hasKey("items")) {
            inv.deserializeNBT(nbttc.getCompoundTag("items"));
        }
    }
    
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbttc) {
        nbttc.setLong("energy", store.getRealEnergyStored());
        nbttc.setIntArray("ins", store.in.old.stream().mapToInt(i -> i).toArray());
        nbttc.setIntArray("outs", store.out.old.stream().mapToInt(i -> i).toArray());
        nbttc.setLong("capacity", capacity);
        nbttc.setTag("items", inv.serializeNBT());
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
        long e = store.getRealEnergyStored();
        store = new BatteryEnergyStorage(capacity, e);
        markDirty(false, true);
    }

    public BatteryEnergyStorage getStore() {
        return store;
    }

    public InputBatteryEnergyStorage getInputStore() {
        return new InputBatteryEnergyStorage(store);
    }

    public OutputBatteryEnergyStorage getOutputStore() {
        return new OutputBatteryEnergyStorage(store);
    }
    
    public int getRedstoneLevel() {
        return (int)(getStore().getRealEnergyStored() * 16 / getStore().getRealMaxEnergyStored());
    }

    @Override
    public boolean hasCapability(Capability<?> cpblt, EnumFacing ef) {
        if (cpblt == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) return true;
        return super.hasCapability(cpblt, ef);
    }

    @Override
    public <T> T getCapability(Capability<T> cpblt, EnumFacing ef) {
        if (cpblt == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(inv);
        return super.getCapability(cpblt, ef);
    }

    @Override
    public void update() {
        if (!getWorld().isRemote) {
            store.in.next();
            store.out.next();
        }
        
        ItemStack in = inv.getStackInSlot(0);
        ItemStack out = inv.getStackInSlot(1);
        if (!in.isEmpty()) {
            IEnergyStorage store = in.getCapability(CapabilityEnergy.ENERGY, null);
            if (store.getEnergyStored() > 0) {
                this.store.receiveEnergy(store.extractEnergy(Math.min(this.store.getMaxTransfer(), this.store.getMaxEnergyStored() - this.store.getEnergyStored()), false), false);
            }
        }
        if (!out.isEmpty()) {
            IEnergyStorage store = out.getCapability(CapabilityEnergy.ENERGY, null);
            if (store.getEnergyStored() < store.getMaxEnergyStored() && this.store.getEnergyStored() > 0) {
                store.receiveEnergy(this.store.extractEnergy(Math.min(this.store.getMaxTransfer(), store.getMaxEnergyStored() - store.getEnergyStored()), false), false);
            }
        }
    }
    
}
