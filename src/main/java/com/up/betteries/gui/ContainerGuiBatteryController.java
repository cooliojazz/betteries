package com.up.betteries.gui;

import com.up.betteries.Betteries;
import com.up.betteries.tileentity.TileEntityBatteryController;
import java.awt.Color;
import java.util.Arrays;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.util.ResourceLocation;

/**
 *
 * @author Ricky
 */
public class ContainerGuiBatteryController extends GuiContainer {

    private static final ResourceLocation background = new ResourceLocation(Betteries.MODID, "textures/gui/battery_controller.png");
    
    private TileEntityBatteryController te;

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
        this.fontRenderer.drawString("Battery Controller", 60, 6, 4210752);
        this.fontRenderer.drawString("Inventory", 8, this.ySize - 96 + 2, 4210752);
        int barx = 62;
        int bary = 40;
        int barw = 105;
        int barh = 25;
        drawRect(barx - 2, bary - 2, barx + barw + 2, bary + barh + 2, Color.darkGray.darker().getRGB());
        drawRect(barx, bary, barx + barw, bary + 25, Color.black.getRGB());
        drawRect(barx, bary, barx + (int)(te.getStore().getRealEnergyStored() * barw / te.getStore().getRealMaxEnergyStored()), bary + barh, Color.red.getRGB());
        this.fontRenderer.drawString(abbreviate(te.getStore().getRealEnergyStored()) + " / " + abbreviate(te.getStore().getRealMaxEnergyStored()) + " FE", barx, bary - 15, 4210752);
        if (mouseX - guiLeft > barx && mouseY - guiTop > bary && mouseX - guiLeft < barx + barw && mouseY - guiTop < bary + barh) {
            double txtotal = te.getStore().getAverageIn() - te.getStore().getAverageOut();
            drawHoveringText(Arrays.asList(new String[] {
                te.getStore().getRealEnergyStored() * 100l / te.getStore().getRealMaxEnergyStored() + "%",
                abbreviate(te.getStore().getMaxTransfer()) + "/t max",
                te.getStore().getAverageIn() + "/t in | " + te.getStore().getAverageOut() + "/t out (average/1s)",
                txtotal < 0 ? suffixTime(Math.round(te.getStore().getRealEnergyStored() / Math.abs(txtotal))) + " until empty" : (txtotal == 0 ? "<==>" : suffixTime(Math.round((te.getStore().getRealMaxEnergyStored() - te.getStore().getRealEnergyStored()) / txtotal)) + " until full")
            }), mouseX - guiLeft, mouseY - guiTop);
        }
    }

    private static final String[] abvs = {"", "k", "M", "G", "T", "P", "E", "Z", "Y", "?"};
    
    public static String abbreviate(int i) {
        int exp = Math.max(Math.min((int)(Math.log(i) / Math.log(1000)), abvs.length - 1), 0);
        return String.format("%.2f", i / Math.pow(1000, exp)) + abvs[exp];
    }
    public static String abbreviate(long i) {
        int exp = Math.max(Math.min((int)(Math.log(i) / Math.log(1000)), abvs.length - 1), 0);
        return String.format("%.2f", i / Math.pow(1000, exp)) + abvs[exp];
    }
    
    public static String suffixTime(long t) {
        long s = t /20;
        if (s < 10) return t + "t";
        long m = s / 60;
        if (m < 2) return s + "s";
        long h = m / 60;
        if (h < 2) return m + "m";
        long d = h / 24;
        if (h < 2) return d + "d";
        return h + "h";
    }
    
    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        mc.getTextureManager().bindTexture(background);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
    }
    
}
