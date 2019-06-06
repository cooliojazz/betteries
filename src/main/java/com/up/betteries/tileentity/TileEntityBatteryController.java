package com.up.betteries.tileentity;

import com.up.betteries.energy.BatteryEnergyStorage;
import com.up.betteries.energy.Conversions;
import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;
import java.util.LinkedList;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

/**
 *
 * @author Ricky
 */
public class TileEntityBatteryController extends TileEntityBatteryBase implements ITickable {

    public static final int BASE_CAPACITY = 2000000;
    public static final int AVERAGE_LENGTH = 20;
    
    public long capacity = BASE_CAPACITY;
    private BatteryEnergyStorage store = new BatteryEnergyStorage(this, capacity);
    private final ItemStackHandler inv = new ItemStackHandler(2) {
        
        @Override
        protected void onContentsChanged(int slot) {
            markDirty(false, true);
        }
        
        @Override
        public ItemStack insertItem(int slot, ItemStack is, boolean simulate) {
            if (is.hasCapability(CapabilityEnergy.ENERGY, null) && getStackInSlot(slot).isEmpty()) {
                return super.insertItem(slot, is, simulate);
            }
            if (Loader.isModLoaded("ic2") && is.getItem() instanceof IElectricItem) {
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
        store = new BatteryEnergyStorage(this, capacity, nbttc.getLong("energy"));
        store.getHistoryIn().setHistory(new LinkedList<>(IntStream.of(nbttc.getIntArray("ins").length >= AVERAGE_LENGTH ? nbttc.getIntArray("ins") : new int[AVERAGE_LENGTH]).mapToObj(i -> i).limit(AVERAGE_LENGTH).collect(Collectors.toList())));
        store.getHistoryOut().setHistory(new LinkedList<>(IntStream.of(nbttc.getIntArray("outs").length >= AVERAGE_LENGTH ? nbttc.getIntArray("outs") : new int[AVERAGE_LENGTH]).mapToObj(i -> i).limit(AVERAGE_LENGTH).collect(Collectors.toList())));
        if (nbttc.hasKey("items")) {
            inv.deserializeNBT(nbttc.getCompoundTag("items"));
        }
    }
    
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbttc) {
        nbttc.setLong("energy", store.getRealEnergyStored());
        nbttc.setIntArray("ins", store.getHistoryIn().getHistory().stream().mapToInt(i -> i).toArray());
        nbttc.setIntArray("outs", store.getHistoryOut().getHistory().stream().mapToInt(i -> i).toArray());
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
        store = new BatteryEnergyStorage(this, capacity, e);
        markDirty(false, true);
    }

    public BatteryEnergyStorage getStore() {
        return store;
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
            store.getHistoryIn().next();
            store.getHistoryOut().next();
        }
        
        ItemStack in = inv.getStackInSlot(0);
        ItemStack out = inv.getStackInSlot(1);
        if (!in.isEmpty()) {
            IEnergyStorage istore = in.getCapability(CapabilityEnergy.ENERGY, null);
            if (istore != null && istore.getEnergyStored() > 0) {
                this.store.receiveEnergy(istore.extractEnergy(Math.min(this.store.getMaxTransfer(), this.store.getMaxEnergyStored() - this.store.getEnergyStored()), false), false);
            }
            if (Loader.isModLoaded("ic2") && in.getItem() instanceof IElectricItem) {
                double discharge = ElectricItem.manager.discharge(in, Math.min(this.store.getMaxTransfer(), this.store.getMaxEnergyStored() - this.store.getEnergyStored()) / Conversions.IC2_RATIO, 4, false, true, false) * Conversions.IC2_RATIO;
                this.store.receiveEnergy((int)discharge, false);
            }
        }
        if (!out.isEmpty()) {
            IEnergyStorage istore = out.getCapability(CapabilityEnergy.ENERGY, null);
            if (istore != null && istore.getEnergyStored() < istore.getMaxEnergyStored() && this.store.getEnergyStored() > 0) {
                istore.receiveEnergy(this.store.extractEnergy(Math.min(this.store.getMaxTransfer(), istore.getMaxEnergyStored() - istore.getEnergyStored()), false), false);
            }
            if (Loader.isModLoaded("ic2") && out.getItem() instanceof IElectricItem) {
                double charge = ElectricItem.manager.charge(out, Math.min(this.store.getMaxTransfer(), this.store.getEnergyStored()) / Conversions.IC2_RATIO, 4, false, false) * Conversions.IC2_RATIO;
                this.store.extractEnergy((int)charge, false);
            }
        }
    }
    
    public ItemStackHandler getInventory() {
        return inv;
    }
}
