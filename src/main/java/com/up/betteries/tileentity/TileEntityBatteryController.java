package com.up.betteries.tileentity;

import com.up.betteries.item.ItemEnergyStorage;
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

    private static final int BASE_CAPACITY = 1000000;
    
    public class BatteryEnergyStorage extends EnergyStorage {

        private static final int BASE_TRANSFER = 10000;
        private static final int TRANSFER_LIMIT = 1000000;
        
        public BatteryEnergyStorage(long capacity) {
            this(capacity, 0);
        }
        
        public BatteryEnergyStorage(long capacity, int energy) {
            this(capacity > Integer.MAX_VALUE ? Integer.MAX_VALUE : (int)capacity, energy);
        }
        
        private BatteryEnergyStorage(int capacity, int energy) {
            super(capacity, capacity, capacity, energy);
        }
        
        public int getMaxTransfer() {
            return (int)(TRANSFER_LIMIT / 2 / (1 + Math.exp(-(TileEntityBatteryController.this.capacity - (double)BASE_CAPACITY) / Integer.MAX_VALUE)) - TRANSFER_LIMIT) + BASE_TRANSFER;
        }
        
        @Override
        public int extractEnergy(int maxExtract, boolean simulate) {
            int oldRS = getRedstoneLevel();
            int ret = super.extractEnergy(Math.min(maxExtract, getMaxTransfer()), simulate);
            if (!simulate) {
                if (oldRS == getRedstoneLevel()) {
                    markDirty(false, true);
                } else {
                    markDirty();
                }
            }
//            lastout += ret;
            return ret;
        }

        @Override
        public int receiveEnergy(int maxReceive, boolean simulate) {
            int oldRS = getRedstoneLevel();
            int ret = super.receiveEnergy(Math.min(maxReceive, getMaxTransfer()), simulate);
            if (!simulate) {
                if (oldRS == getRedstoneLevel()) {
                    markDirty(false, true);
                } else {
                    markDirty();
                }
            }
//            lastin += ret;
            return ret;
        }
        
    }
    
    public long capacity = BASE_CAPACITY;
    private EnergyStorage store = new BatteryEnergyStorage(capacity);
    private ItemStackHandler inv = new ItemStackHandler(2) {
        
        @Override
        protected void onContentsChanged(int slot) {
            markDirty(false, true);
        }
        
        @Override
        public ItemStack insertItem(int slot, ItemStack is, boolean simulate) {
            if (is.getItem() instanceof ItemEnergyStorage && getStackInSlot(slot).isEmpty()) {
                return super.insertItem(slot, is, simulate);
            }
            return is;
        }

        @Override
        public int getSlotLimit(int slot) {
            return 1;
        }
        
    };
    
    //This would all have to be synced to the client as well to show in the gui, im not sure how to do that efficiently...
    
//    private class FixedArrayList<E> extends ArrayList<E> {
//        int length;
//
//        public FixedArrayList(int length) {
//            super(length);
//            this.length = length;
//        }
//        
//        @Override
//        public boolean add(E e) {
//            if (size() > length) remove(0);
//            return super.add(e);
//        }
//        
//    }
//
//    private int lastin = 0;
//    private int lastout = 0;
//    private ArrayList<Integer> ins = new FixedArrayList<>(20);
//    private ArrayList<Integer> outs = new FixedArrayList<>(20);
//
//    public int getAverageIn() {
//        return (int)(double)ins.stream().collect(Collectors.averagingInt(i -> i));
//    }
//    
//    public int getAverageOut() {
//        return (int)(double)outs.stream().collect(Collectors.averagingInt(i -> i));
//    }
    
    @Override
    public void readFromNBT(NBTTagCompound nbttc) {
        super.readFromNBT(nbttc);
        capacity = nbttc.getLong("capacity");
        store = new BatteryEnergyStorage(capacity, nbttc.getInteger("energy"));
        if (nbttc.hasKey("items")) {
            inv.deserializeNBT(nbttc.getCompoundTag("items"));
        }
    }
    
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbttc) {
        nbttc.setInteger("energy", store.getEnergyStored());
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
        int e = store.getEnergyStored();
        store = new BatteryEnergyStorage(capacity, e);
        markDirty(false, true);
    }

    public EnergyStorage getStore() {
        return store;
    }
    
    public int getRedstoneLevel() {
        return getStore().getEnergyStored() * 16 / getStore().getMaxEnergyStored();
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
//        ins.add(lastin);
//        outs.add(lastout);
//        lastin = 0;
//        lastout = 0;
        
        ItemStack in = inv.getStackInSlot(0);
        ItemStack out = inv.getStackInSlot(1);
        if (!in.isEmpty()) {
            IEnergyStorage store = in.getCapability(CapabilityEnergy.ENERGY, null);
            if (store.getEnergyStored() > 0) {
                this.store.receiveEnergy(store.extractEnergy(Math.min(1024, this.store.getMaxEnergyStored() - this.store.getEnergyStored()), false), false);
            }
        }
        if (!out.isEmpty()) {
            IEnergyStorage store = out.getCapability(CapabilityEnergy.ENERGY, null);
            if (store.getEnergyStored() < store.getMaxEnergyStored() && this.store.getEnergyStored() > 0) {
                store.receiveEnergy(this.store.extractEnergy(Math.min(1024, store.getMaxEnergyStored() - store.getEnergyStored()), false), false);
            }
        }
    }
    
}
