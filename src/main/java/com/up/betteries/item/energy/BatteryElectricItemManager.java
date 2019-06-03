package com.up.betteries.item.energy;

import com.up.betteries.energy.Conversions;
import com.up.betteries.item.ItemEnergyStorage;
import com.up.betteries.util.GuiUtils;
import ic2.api.item.IBackupElectricItemManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

/**
 *
 * @author Ricky Talbot
 */
public class BatteryElectricItemManager implements IBackupElectricItemManager {

    @Override
    public double charge(ItemStack stack, double amount, int tier, boolean ignoreTransferLimit, boolean simulate) {
        IEnergyStorage store = stack.getCapability(CapabilityEnergy.ENERGY, null);
        if (store != null) {
            return Conversions.feToEu(store.receiveEnergy(Conversions.euToFe(amount), simulate));
        }
        return 0;
    }

    @Override
    public double discharge(ItemStack stack, double amount, int tier, boolean ignoreTransferLimit, boolean externally, boolean simulate) {
        IEnergyStorage store = stack.getCapability(CapabilityEnergy.ENERGY, null);
        if (store != null) {
            return Conversions.feToEu(store.extractEnergy(Conversions.euToFe(amount), simulate));
        }
        return 0;
    }

    @Override
    public double getCharge(ItemStack stack) {
        IEnergyStorage store = stack.getCapability(CapabilityEnergy.ENERGY, null);
        if (store != null) {
            return Conversions.feToEu(store.getEnergyStored());
        }
        return 0;
    }

    @Override
    public double getMaxCharge(ItemStack stack) {
        IEnergyStorage store = stack.getCapability(CapabilityEnergy.ENERGY, null);
        if (store != null) {
            return Conversions.feToEu(store.getMaxEnergyStored());
        }
        return 0;
    }

    @Override
    public boolean canUse(ItemStack stack, double amount) {
//        return getCharge(stack) >= amount;
        return false;
    }

    @Override
    public boolean use(ItemStack stack, double amount, EntityLivingBase entity) {
        return false;
    }

    @Override
    public void chargeFromArmor(ItemStack stack, EntityLivingBase entity) {
//        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getToolTip(ItemStack stack) {
        return GuiUtils.abbreviate((int)getCharge(stack)) + " EU / " + GuiUtils.abbreviate((int)getMaxCharge(stack)) + " EU";
    }

    @Override
    public int getTier(ItemStack stack) {
        return 1;
    }

    @Override
    public boolean handles(ItemStack stack) {
        return stack.getItem() instanceof ItemEnergyStorage;
    }
    
}
