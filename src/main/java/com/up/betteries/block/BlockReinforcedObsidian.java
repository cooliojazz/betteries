package com.up.betteries.block;

import com.up.betteries.Betteries;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class BlockReinforcedObsidian extends Block {
    
    public BlockReinforcedObsidian() {
        super(Material.ROCK);
	setHardness(100.f);
	setResistance(10000.f);
	setLightOpacity(0);
        setUnlocalizedName("reinforced_obsidian");
        setRegistryName("betteries", "reinforced_obsidian");
	setCreativeTab(Betteries.betteriesTab);
    }

}
