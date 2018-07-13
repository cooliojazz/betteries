package com.up.betteries.item;

import com.up.betteries.Betteries;

/**
 *
 * @author Ricky
 */
public class ItemEnergyBundle extends ItemEnergyStorage {

    public ItemEnergyBundle() {
        super(100000);
        setRegistryName(Betteries.MODID, "energy_bundle");
        setUnlocalizedName("energy_bundle");
    }
    
}
