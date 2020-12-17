package com.up.betteries.gui;

import com.up.betteries.Betteries;
import com.up.betteries.tileentity.TileEntityBatteryController;
import com.up.betteries.util.GuiUtils;
import java.awt.Color;
import java.util.Arrays;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.util.ResourceLocation;

/**
 *
 * @author Ricky
 */
public class ContainerGuiBatteryController extends GuiContainer {

    private static final ResourceLocation background = new ResourceLocation(Betteries.MODID, "textures/gui/battery_controller.png");
    
    private TileEntityBatteryController te;
//    private Minecraft mc = Minecraft.getMinecraft();
//    private FontRenderer unicodeRenderer = new FontRenderer(mc.gameSettings, new ResourceLocation("textures/font/ascii.png"), mc.renderEngine, true);
    
    public ContainerGuiBatteryController(TileEntityBatteryController te, ContainerBatteryController container) {
        super(container);

        this.te = te;
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks){
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }
    
    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        int color = 4210752;
        this.fontRenderer.drawString("Battery Controller", 60, 6, color);
        this.fontRenderer.drawString("Inventory", 8, this.ySize - 96 + 2, color);
        int barx = 62;
        int bary = 40;
        int barw = 105;
        int barh = 25;
        drawRect(barx - 2, bary - 2, barx + barw + 2, bary + barh + 2, Color.darkGray.darker().getRGB());
        drawRect(barx, bary, barx + barw, bary + 25, Color.black.getRGB());
        drawRect(barx, bary, barx + (int)(te.getStore().getRealEnergyStored() * barw / te.getStore().getRealMaxEnergyStored()), bary + barh, Color.red.getRGB());
        this.fontRenderer.drawString(GuiUtils.abbreviate(te.getStore().getRealEnergyStored()) + " / " + GuiUtils.abbreviate(te.getStore().getRealMaxEnergyStored()) + " FE", barx, bary - 15, color);
        if (mouseX - guiLeft > barx && mouseY - guiTop > bary && mouseX - guiLeft < barx + barw && mouseY - guiTop < bary + barh) {
            double txtotal = te.getStore().getAverageIn() - te.getStore().getAverageOut();
            drawHoveringText(Arrays.asList(new String[] {
                String.format("%.2f%%", (te.getStore().getRealEnergyStored() * 10000 / te.getStore().getRealMaxEnergyStored()) / 100.0),
                GuiUtils.abbreviate(te.getStore().getMaxTransfer()) + "/t max",
                GuiUtils.abbreviate(te.getStore().getAverageIn()) + "/t " + (char)0xbb + " " + GuiUtils.abbreviate(te.getStore().getAverageOut()) + "/t (average over last second)",
                txtotal < 0 ? GuiUtils.suffixTime(Math.round(te.getStore().getRealEnergyStored() / Math.abs(txtotal))) + " until empty" : (txtotal == 0 ? "<==>" : GuiUtils.suffixTime(Math.round((te.getStore().getRealMaxEnergyStored() - te.getStore().getRealEnergyStored()) / txtotal)) + " until full")
            }), mouseX - guiLeft, mouseY - guiTop);
        }
    }
    
    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        mc.getTextureManager().bindTexture(background);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
    }
    
}
