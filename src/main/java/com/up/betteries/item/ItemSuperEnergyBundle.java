package com.up.betteries.item;

import com.up.betteries.Betteries;

/**
 *
 * @author Ricky
 */
public class ItemSuperEnergyBundle extends ItemEnergyStorage {

    public ItemSuperEnergyBundle() {
        super(1000000);
        setRegistryName(Betteries.MODID, "super_energy_bundle");
        setUnlocalizedName("super_energy_bundle");
    }
    
}
