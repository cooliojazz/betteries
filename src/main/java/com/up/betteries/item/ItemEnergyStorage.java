package com.up.betteries.item;

import com.up.betteries.Betteries;
import com.up.betteries.gui.ContainerGuiBatteryController;
import java.util.List;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 *
 * @author Ricky
 */
public abstract class ItemEnergyStorage extends ItemCapacityText {

    private int capacity;
    
    public ItemEnergyStorage(int capacity) {
        super(String.format("%,d", capacity));
        this.capacity = capacity;
        setMaxDamage(100);
        setHasSubtypes(false);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack is, World world, List<String> list, ITooltipFlag itf) {
        super.addInformation(is, world, list, itf);
        if (isDamaged(is)) list.add(ContainerGuiBatteryController.abbreviateInteger(is.getCapability(CapabilityEnergy.ENERGY, null).getEnergyStored()) + " FE Stored");
    }
    
    @Override
    public boolean isDamaged(ItemStack stack) {
        return getDamage(stack) < 100;
    }

    @Override
    public int getDamage(ItemStack stack) {
        IEnergyStorage store = stack.getCapability(CapabilityEnergy.ENERGY, null);
        return (store.getMaxEnergyStored() - store.getEnergyStored()) * 100 / store.getMaxEnergyStored();
    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack is, NBTTagCompound nbttc) {
        return new EnergyCapabilityProvider(new EnergyStorage(capacity, capacity / 100));
    }

    @Override
    public NBTTagCompound getNBTShareTag(ItemStack is) {
        NBTTagCompound nbt = super.getNBTShareTag(is);
        if (nbt == null) nbt = new NBTTagCompound();
        EnergyStorage store = (EnergyStorage)is.getCapability(CapabilityEnergy.ENERGY, null);
        if (store != null) nbt.setTag("store", new EnergyCapabilityProvider(store).serializeNBT());
        return nbt;
    }

    @Override
    public void readNBTShareTag(ItemStack is, NBTTagCompound nbttc) {
        super.readNBTShareTag(is, nbttc);
        EnergyStorage store = (EnergyStorage)is.getCapability(CapabilityEnergy.ENERGY, null);
        if (nbttc == null && store != null) new EnergyCapabilityProvider(store).deserializeNBT(nbttc.getTag("store"));
    }
    
    public static class EnergyCapabilityProvider implements ICapabilitySerializable<NBTBase> {
            
        EnergyStorage store;

        public EnergyCapabilityProvider(EnergyStorage store) {
            this.store = store;
        }

        @Override
        public boolean hasCapability(Capability<?> cpblt, EnumFacing ef) {
            return cpblt == CapabilityEnergy.ENERGY;
        }

        @Override
        public <T> T getCapability(Capability<T> cpblt, EnumFacing ef) {
            if (cpblt == CapabilityEnergy.ENERGY) return CapabilityEnergy.ENERGY.cast(store);
            return null;
        }
        
        @Override
	public NBTBase serializeNBT() {
            return CapabilityEnergy.ENERGY.writeNBT(store, null);
	}

	@Override
	public void deserializeNBT(final NBTBase nbt) {
            CapabilityEnergy.ENERGY.readNBT(store, null, nbt);
        }
    }
}
