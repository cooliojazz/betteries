package com.up.betteries.item;

import com.up.betteries.Betteries;

/**
 *
 * @author Ricky
 */
public class ItemHyperEnergyBundle extends ItemEnergyStorage {

    public ItemHyperEnergyBundle() {
        super(50000000);
        setRegistryName(Betteries.MODID, "hyper_energy_bundle");
        setUnlocalizedName("hyper_energy_bundle");
    }
    
}
