package com.up.betteries;

import com.up.betteries.tileentity.TileEntityBatteryController;
import com.up.betteries.tileentity.TileEntityBatteryCasing;
import com.up.betteries.tileentity.TileEntityBatteryConnector;
import com.up.betteries.block.BlockBatteryConnector;
import com.up.betteries.block.BlockBatteryCasing;
import com.up.betteries.block.BlockBatteryCasing2;
import com.up.betteries.block.BlockBatteryComputerConnector;
import com.up.betteries.block.BlockBatteryController;
import com.up.betteries.tileentity.TileEntityBatteryCasing2;
import com.up.betteries.tileentity.TileEntityBatteryComputerConnector;
import java.util.ArrayList;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

@Mod(modid = Betteries.MODID, version = Betteries.VERSION)
@Mod.EventBusSubscriber
public class Betteries {
    
    public static final String MODID = "betteries";
    public static final String VERSION = "1.0";
    
    public BlockBatteryCasing bbca = new BlockBatteryCasing();
    public Item bbcai = new ItemBlock(bbca).setRegistryName(bbca.getRegistryName());
    public BlockBatteryCasing2 bbca2 = new BlockBatteryCasing2();
    public Item bbcai2 = new ItemBlock(bbca2).setRegistryName(bbca2.getRegistryName());
    public BlockBatteryConnector bbco = new BlockBatteryConnector();
    public Item bbcoi = new ItemBlock(bbco).setRegistryName(bbco.getRegistryName());
    public BlockBatteryController bbct = new BlockBatteryController();
    public Item bbcti = new ItemBlock(bbct).setRegistryName(bbct.getRegistryName());
    public BlockBatteryComputerConnector bbcc = new BlockBatteryComputerConnector();
    public Item bbcci = new ItemBlock(bbcc).setRegistryName(bbcc.getRegistryName());
    public Item crbi = new Item().setCreativeTab(CreativeTabs.MATERIALS).setRegistryName(MODID, "compressed_redstone").setUnlocalizedName("compressed_redstone");
    public Item sri = new Item().setCreativeTab(CreativeTabs.MATERIALS).setRegistryName(MODID, "stabalizer_ring").setUnlocalizedName("stabalizer_ring");
    public Item sebi = new Item().setCreativeTab(CreativeTabs.MATERIALS).setRegistryName(MODID, "energy_bundle").setUnlocalizedName("energy_bundle");
    
    @SidedProxy(clientSide = "com.up.betteries.DumbProxyClient", serverSide = "com.up.betteries.DumbProxyServer")
    public static DumbProxy proxy;

    public Betteries() {
        MinecraftForge.EVENT_BUS.register(this);
    }
    
    @EventHandler
    public void init(FMLInitializationEvent event) {
        GameRegistry.registerTileEntity(TileEntityBatteryCasing.class, "betteries_battery_casing");
        GameRegistry.registerTileEntity(TileEntityBatteryCasing2.class, "betteries_battery_casing_2");
        GameRegistry.registerTileEntity(TileEntityBatteryConnector.class, "betteries_battery_connector");
        GameRegistry.registerTileEntity(TileEntityBatteryController.class, "betteries_battery_controller");
        GameRegistry.registerTileEntity(TileEntityBatteryComputerConnector.class, "betteries_battery_computer_connector");
         
        registerRecipies();
        
//        LogManager.getLogger(MODID).log(Level.INFO, "[Betteries] Initialized");
    }
    
    public void registerRecipies() {
        GameRegistry.addShapedRecipe(new ItemStack(crbi), "RRR", "RRR", "RRR", 'R', Item.getByNameOrId("minecraft:redstone_block"));
        GameRegistry.addShapedRecipe(new ItemStack(sri), "IOI", "ORO", "IOI", 'I', Item.getByNameOrId("minecraft:iron_ingot"), 'O', Item.getByNameOrId("minecraft:obsidian"), 'R', Item.getByNameOrId("minecraft:redstone_block"));
        GameRegistry.addShapedRecipe(new ItemStack(sebi, 4), " R ", "RBR", " R ", 'B', crbi, 'R', sri);
        GameRegistry.addShapedRecipe(new ItemStack(bbcai), "IRI", "RBR", "IRI", 'I', Item.getByNameOrId("minecraft:iron_ingot"), 'B', sebi, 'R', Item.getByNameOrId("minecraft:redstone"));
        GameRegistry.addShapedRecipe(new ItemStack(bbcai2), "CRC", "RBR", "CRC", 'C', crbi, 'B', sebi, 'R', Item.getByNameOrId("minecraft:redstone"));
        GameRegistry.addShapedRecipe(new ItemStack(bbcoi), "GRG", "RCR", "GRG", 'C', bbcai, 'R', Item.getByNameOrId("minecraft:redstone"), 'G', Item.getByNameOrId("minecraft:gold_ingot"));
        GameRegistry.addShapedRecipe(new ItemStack(bbcci), " R ", "RCR", " R ", 'C', bbcai, 'R', Item.getByNameOrId("opencomputers:cable"));
        GameRegistry.addShapedRecipe(new ItemStack(bbcti), "CRC", "RDR", "CRC", 'C', bbcai, 'D', Item.getByNameOrId("minecraft:diamond"), 'R', Item.getByNameOrId("minecraft:redstone"));
    }
    
    @SubscribeEvent
    public void registerBlocks(RegistryEvent.Register<Block> event) {
        event.getRegistry().register(bbca);
        event.getRegistry().register(bbca2);
        event.getRegistry().register(bbco);
        event.getRegistry().register(bbct);
        event.getRegistry().register(bbcc);
//        LogManager.getLogger(MODID).log(Level.INFO, "[Betteries] Registered blocks");
    }
    
    @SubscribeEvent
    public void registerItems(RegistryEvent.Register<Item> event) {
        event.getRegistry().register(bbcai);
        event.getRegistry().register(bbcai2);
        event.getRegistry().register(bbcoi);
        event.getRegistry().register(bbcti);
        event.getRegistry().register(bbcci);
        event.getRegistry().register(crbi);
        event.getRegistry().register(sri);
        event.getRegistry().register(sebi);
        
        ArrayList<Item> items = new ArrayList<Item>();
        items.add(bbcai);
        items.add(bbcai2);
        items.add(bbcoi);
        items.add(bbcti);
        items.add(bbcci);
        items.add(crbi);
        items.add(sri);
        items.add(sebi);
        proxy.registerModels(items);
    }
    
}
