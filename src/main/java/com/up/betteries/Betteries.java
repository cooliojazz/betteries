package com.up.betteries;

import com.up.betteries.item.ItemStabilizerRing;
import com.up.betteries.tileentity.TileEntityBatteryController;
import com.up.betteries.tileentity.TileEntityBatteryCasing;
import com.up.betteries.tileentity.TileEntityBatteryConnector;
import com.up.betteries.block.BlockBatteryConnector;
import com.up.betteries.block.BlockBatteryCasing;
import com.up.betteries.block.BlockBatteryCasing2;
import com.up.betteries.block.BlockBatteryCasing3;
import com.up.betteries.block.BlockBatteryComputerConnector;
import com.up.betteries.block.BlockBatteryController;
import com.up.betteries.block.BlockBatteryMultiblock;
import com.up.betteries.block.BlockReinforcedObsidian;
import com.up.betteries.gui.GuiHandler;
import com.up.betteries.item.ItemCompressedRedstone;
import com.up.betteries.item.ItemEnergyBundle;
import com.up.betteries.item.ItemSuperEnergyBundle;
import com.up.betteries.item.ItemSuperStabilizerRing;
import com.up.betteries.tileentity.TileEntityBatteryCasing2;
import com.up.betteries.tileentity.TileEntityBatteryCasing3;
import com.up.betteries.tileentity.TileEntityBatteryComputerConnector;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;

@Mod(modid = Betteries.MODID, version = Betteries.VERSION, dependencies = "after:buildcraftenergy;after:ic2")
@Mod.EventBusSubscriber
public class Betteries {
    
    private static class ItemBlockCapacityText extends ItemBlock {
        String capacity;
        public ItemBlockCapacityText(String capacity, Block block) {
            super(block);
            this.capacity = capacity;
        }
        
        @Override
        public void addInformation(ItemStack is, World world, List<String> list, ITooltipFlag itf) {
            list.add(TextFormatting.ITALIC + "" + TextFormatting.DARK_GRAY + "Stores " + capacity + " FE");
        }
    }
    
    private static class ItemBlockMultiblockCapacityText extends ItemBlockCapacityText {
        public ItemBlockMultiblockCapacityText(BlockBatteryMultiblock block) {
            super(String.format("%,d", block.createNewTileEntity(null, 0).getStorageCapacity()), block);
        }
    }
    
    public static final String MODID = "betteries";
    public static final String VERSION = "1.1";
    public static Betteries instance = null;
    
    public static CreativeTabs betteriesTab = null;
    
    public BlockBatteryCasing bbca = new BlockBatteryCasing();
    public Item bbcai = new ItemBlockMultiblockCapacityText(bbca).setRegistryName(bbca.getRegistryName());
    {
        betteriesTab = new CreativeTabs("betteries") {
            @Override
            public ItemStack getTabIconItem() {
                return bbcoi.getDefaultInstance();
            }
        };
    }
    public BlockReinforcedObsidian bro = new BlockReinforcedObsidian();
    public Item broi = new ItemBlock(bro).setRegistryName(bro.getRegistryName());
    public BlockBatteryCasing2 bbca2 = new BlockBatteryCasing2();
    public Item bbca2i = new ItemBlockMultiblockCapacityText(bbca2).setRegistryName(bbca2.getRegistryName());
    public BlockBatteryCasing3 bbca3 = new BlockBatteryCasing3();
    public Item bbca3i = new ItemBlockMultiblockCapacityText(bbca3).setRegistryName(bbca3.getRegistryName());
    public BlockBatteryConnector bbco = new BlockBatteryConnector();
    public Item bbcoi = new ItemBlockMultiblockCapacityText(bbco).setRegistryName(bbco.getRegistryName());
    public BlockBatteryController bbct = new BlockBatteryController();
    public Item bbcti = new ItemBlock(bbct) {
        @Override
        public void addInformation(ItemStack is, World world, List<String> list, ITooltipFlag itf) {
            list.add(TextFormatting.ITALIC + "Heart of the battery");
            list.add(TextFormatting.ITALIC + "" + TextFormatting.DARK_GRAY + "Stores 1,000,000 FE");
        }
    }.setRegistryName(bbct.getRegistryName());
    public BlockBatteryComputerConnector bbcc = new BlockBatteryComputerConnector();
    public Item bbcci = new ItemBlockMultiblockCapacityText(bbcc).setRegistryName(bbcc.getRegistryName());
    public Item crbi = new ItemCompressedRedstone();
    public Item sri = new ItemStabilizerRing();
    public Item sebi = new ItemEnergyBundle();
    public Item ssri = new ItemSuperStabilizerRing();
    public Item ssebi = new ItemSuperEnergyBundle();
        
    @SidedProxy(clientSide = "com.up.betteries.DumbProxyClient", serverSide = "com.up.betteries.DumbProxyServer")
    public static DumbProxy proxy;

    public Betteries() {
        instance = this;
        MinecraftForge.EVENT_BUS.register(this);
        bbca.setCreativeTab(betteriesTab);
    }
    
    @EventHandler
    public void init(FMLInitializationEvent event) {
        GameRegistry.registerTileEntity(TileEntityBatteryCasing.class, "betteries:battery_casing");
        GameRegistry.registerTileEntity(TileEntityBatteryCasing2.class, "betteries:battery_casing_2");
        GameRegistry.registerTileEntity(TileEntityBatteryCasing3.class, "betteries:battery_casing_3");
        GameRegistry.registerTileEntity(TileEntityBatteryConnector.class, "betteries:battery_connector");
        GameRegistry.registerTileEntity(TileEntityBatteryController.class, "betteries:battery_controller");
        GameRegistry.registerTileEntity(TileEntityBatteryComputerConnector.class, "betteries:battery_computer_connector");
         
        registerRecipies();
        
        NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler());
    }
    
    public void registerRecipies() {
        ItemStack lapis = new ItemStack(Item.getByNameOrId("minecraft:dye"), 1, 4);
        
        GameRegistry.addShapedRecipe(new ResourceLocation(MODID, crbi.getUnlocalizedName()), new ResourceLocation(MODID), new ItemStack(crbi), "RRR", "RRR", "RRR", 'R', Item.getByNameOrId("minecraft:redstone_block"));
        GameRegistry.addShapedRecipe(new ResourceLocation(MODID, sri.getUnlocalizedName()), new ResourceLocation(MODID), new ItemStack(sri), "IOI", "ORO", "IOI", 'I', Item.getByNameOrId("minecraft:iron_ingot"), 'O', Item.getByNameOrId("minecraft:obsidian"), 'R', Item.getByNameOrId("minecraft:redstone_block"));
        GameRegistry.addShapedRecipe(new ResourceLocation(MODID, ssri.getUnlocalizedName()), new ResourceLocation(MODID), new ItemStack(ssri), "DOD", "ORO", "DOD", 'D', Item.getByNameOrId("minecraft:diamond"), 'O', broi, 'R', sri);
        GameRegistry.addShapedRecipe(new ResourceLocation(MODID, sebi.getUnlocalizedName()), new ResourceLocation(MODID), new ItemStack(sebi, 4), "SLS", "RBR", "SLS", 'B', crbi, 'S', sri, 'R', Item.getByNameOrId("minecraft:redstone"), 'L', lapis);
        GameRegistry.addShapedRecipe(new ResourceLocation(MODID, ssebi.getUnlocalizedName()), new ResourceLocation(MODID), new ItemStack(ssebi, 4), "SLS", "BRB", "SLS", 'B', crbi, 'R', sebi, 'S', ssri, 'L', Item.getByNameOrId("minecraft:lapis_block"));
        
        GameRegistry.addShapedRecipe(new ResourceLocation(MODID, broi.getUnlocalizedName()), new ResourceLocation(MODID), new ItemStack(broi), "OIO", "IOI", "OIO", 'I', Item.getByNameOrId("minecraft:iron_bars"), 'O', Item.getByNameOrId("minecraft:obsidian"));
        GameRegistry.addShapedRecipe(new ResourceLocation(MODID, bbcai.getUnlocalizedName()), new ResourceLocation(MODID), new ItemStack(bbcai, 4), "ILI", "RBR", "ILI", 'I', Item.getByNameOrId("minecraft:iron_ingot"), 'B', sebi, 'R', Item.getByNameOrId("minecraft:redstone"), 'L', lapis);
        GameRegistry.addShapedRecipe(new ResourceLocation(MODID, bbca2i.getUnlocalizedName()), new ResourceLocation(MODID), new ItemStack(bbca2i, 4), "OCO", "CBC", "OCO", 'C', bbcai, 'B', sebi, 'O', Item.getByNameOrId("minecraft:obsidian"));
        GameRegistry.addShapedRecipe(new ResourceLocation(MODID, bbca3i.getUnlocalizedName()), new ResourceLocation(MODID), new ItemStack(bbca3i, 4), "OCO", "CSC", "OCO", 'C', bbca2i, 'S', ssebi, 'O', broi);
        GameRegistry.addShapedRecipe(new ResourceLocation(MODID, bbcoi.getUnlocalizedName()), new ResourceLocation(MODID), new ItemStack(bbcoi), "GRG", "RCR", "GRG", 'C', bbcai, 'R', Item.getByNameOrId("minecraft:redstone"), 'G', Item.getByNameOrId("minecraft:gold_ingot"));
        GameRegistry.addShapedRecipe(new ResourceLocation(MODID, bbcti.getUnlocalizedName()), new ResourceLocation(MODID), new ItemStack(bbcti), "CRC", "EOL", "CDC", 'C', bbcai, 'O', broi, 'D', Item.getByNameOrId("minecraft:diamond"), 'R', Item.getByNameOrId("minecraft:redstone_block"), 'L', Item.getByNameOrId("minecraft:lapis_block"), 'E', Item.getByNameOrId("minecraft:emerald"));
        if (Loader.isModLoaded("opencomputers")) GameRegistry.addShapedRecipe(new ResourceLocation(MODID, bbcci.getUnlocalizedName()), new ResourceLocation(MODID), new ItemStack(bbcci), " R ", "RCR", " R ", 'C', bbcai, 'R', Item.getByNameOrId("opencomputers:cable"));
    }
    
    @SubscribeEvent
    public void registerBlocks(RegistryEvent.Register<Block> event) {
        event.getRegistry().register(bro);
        event.getRegistry().register(bbca);
        event.getRegistry().register(bbca2);
        event.getRegistry().register(bbca3);
        event.getRegistry().register(bbco);
        event.getRegistry().register(bbct);
        event.getRegistry().register(bbcc);
    }
    
    @SubscribeEvent
    public void registerItems(RegistryEvent.Register<Item> event) {
        event.getRegistry().register(broi);
        event.getRegistry().register(bbcai);
        event.getRegistry().register(bbca2i);
        event.getRegistry().register(bbca3i);
        event.getRegistry().register(bbcoi);
        event.getRegistry().register(bbcti);
        event.getRegistry().register(bbcci);
        event.getRegistry().register(crbi);
        event.getRegistry().register(sri);
        event.getRegistry().register(sebi);
        event.getRegistry().register(ssri);
        event.getRegistry().register(ssebi);
        
        ArrayList<Item> items = new ArrayList<Item>();
        items.add(broi);
        items.add(bbcai);
        items.add(bbca2i);
        items.add(bbca3i);
        items.add(bbcoi);
        items.add(bbcti);
        items.add(bbcci);
        items.add(crbi);
        items.add(sri);
        items.add(sebi);
        items.add(ssri);
        items.add(ssebi);
        proxy.registerModels(items);
    }
    
}
