package com.up.betteries;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;

@Mod(modid = Betteries.MODID, version = Betteries.VERSION)
@Mod.EventBusSubscriber
public class Betteries {
    
    public static final String MODID = "betteries";
    public static final String VERSION = "1.0";
    
    public BlockBatteryCasing bb = new BlockBatteryCasing();
    public Item bbi = new ItemBlock(bb).setRegistryName(bb.getRegistryName());

    public Betteries() {
        LogManager.getLogger(MODID).log(Level.INFO, "[Betteries] Created class Betteries");
        MinecraftForge.EVENT_BUS.register(this);
    }
    
    @EventHandler
    public void init(FMLInitializationEvent event) {
        GameRegistry.registerTileEntity(TileEntityBatteryCasing.class, "betteries_batttery");
                
        LogManager.getLogger(MODID).log(Level.INFO, "[Betteries] Initialized");
    }
    
    @SubscribeEvent
    public void registerBlocks(RegistryEvent.Register<Block> event) {
        event.getRegistry().register(bb);
        LogManager.getLogger(MODID).log(Level.INFO, "[Betteries] Registered blocks");
    }
    
    @SubscribeEvent
    public void registerItems(RegistryEvent.Register<Item> event) {
        event.getRegistry().register(bbi);
        ModelLoader.setCustomModelResourceLocation(bbi, 0, new ModelResourceLocation(bb.getRegistryName(), "normal"));
        ModelLoader.setCustomModelResourceLocation(bbi, 0, new ModelResourceLocation(bb.getRegistryName(), "inventory"));
        LogManager.getLogger(MODID).log(Level.INFO, "[Betteries] Registered items");
    }
    
}
