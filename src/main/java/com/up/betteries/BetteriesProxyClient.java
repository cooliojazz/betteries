package com.up.betteries;

import java.util.ArrayList;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;

/**
 *
 * @author Ricky
 */
public class BetteriesProxyClient implements BetteriesProxy {

    @Override
    public void registerModels(ArrayList<Item> items) {
         for (Item i : items) ModelLoader.setCustomModelResourceLocation(i, 0, new ModelResourceLocation(i.getRegistryName(), "inventory"));
    }

}
