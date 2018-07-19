package com.up.betteries.block;

import com.up.betteries.Betteries;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class BlockHyperReinforcedObsidian extends Block {
    
    public BlockHyperReinforcedObsidian() {
        super(Material.ROCK);
	setHardness(500.f);
	setResistance(50000.f);
	setLightOpacity(0);
        setUnlocalizedName("hyper_reinforced_obsidian");
        setRegistryName("betteries", "hyper_reinforced_obsidian");
	setCreativeTab(Betteries.betteriesTab);
    }

}
