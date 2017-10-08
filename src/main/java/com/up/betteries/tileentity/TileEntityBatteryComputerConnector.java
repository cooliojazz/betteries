package com.up.betteries.tileentity;

import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Callback;
import li.cil.oc.api.machine.Context;
import li.cil.oc.api.network.SimpleComponent;
import net.minecraftforge.fml.common.Optional;

/**
 *
 * @author Ricky
 */
@Optional.Interface(iface = "li.cil.oc.api.network.SimpleComponent", modid = "opencomputers")
public class TileEntityBatteryComputerConnector extends TileEntityBatteryMultiblock implements SimpleComponent {

    @Override
    public int getStorageCapacity() {
        return 250000;
    }

    @Override
    public String getComponentName() {
        return "bettery";
    }

    @Callback
    @Optional.Method(modid = "opencomputers")
    public Object[] getCapacity(Context ctx, Arguments args) throws Exception {
        return hasParent() ? new Object[] {getParent().getStore().getMaxEnergyStored()} : new Object[] {null, "not connected to bettery"};
    }

    @Callback
    @Optional.Method(modid = "opencomputers")
    public Object[] getStored(Context ctx, Arguments args) throws Exception {
        return hasParent() ? new Object[] {getParent().getStore().getEnergyStored()} : new Object[] {null, "not connected to bettery"};
    }
    
}
