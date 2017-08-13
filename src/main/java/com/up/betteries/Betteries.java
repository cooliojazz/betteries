package com.up.betteries;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
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
    
    public BlockBattery bb = new BlockBattery();

    public Betteries() {
        LogManager.getLogger(MODID).log(Level.INFO, "[Betteries] Created class Betteries");
        MinecraftForge.EVENT_BUS.register(this);
    }
    
    @EventHandler
    public void init(FMLInitializationEvent event) {
        GameRegistry.registerTileEntity(TileEntityBattery.class, "betteries_batttery");
        LogManager.getLogger(MODID).log(Level.INFO, "[Betteries] Initialized");
    }
    
    @SubscribeEvent
    public void registerBlocks(RegistryEvent.Register<Block> event) {
        event.getRegistry().register(bb);
        LogManager.getLogger(MODID).log(Level.INFO, "[Betteries] Registered blocks");
    }
    
    @SubscribeEvent
    public void registerItems(RegistryEvent.Register<Item> event) {
        event.getRegistry().register(new ItemBlock(bb).setRegistryName(bb.getRegistryName()));
        LogManager.getLogger(MODID).log(Level.INFO, "[Betteries] Registered items");
    }
}
