package com.up.betteries.tileentity;

import buildcraft.api.mj.IMjConnector;
import buildcraft.api.mj.IMjPassiveProvider;
import buildcraft.api.mj.IMjReceiver;
import buildcraft.api.mj.MjAPI;
import com.up.betteries.block.BlockBatteryConnector;
import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergyAcceptor;
import ic2.api.energy.tile.IEnergyEmitter;
import ic2.api.energy.tile.IEnergySink;
import ic2.api.energy.tile.IEnergySource;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Optional;

/**
 *
 * @author Ricky
 */
@Optional.Interface(iface = "buildcraft.api.mj.IMjReceiver", modid = "buildcraftenergy")
@Optional.Interface(iface = "buildcraft.api.mj.IMjPassiveProvider", modid = "buildcraftenergy")
@Optional.Interface(iface = "ic2.api.energy.tile.IEnergySource", modid = "ic2")
@Optional.Interface(iface = "ic2.api.energy.tile.IEnergySink", modid = "ic2")
public class TileEntityBatteryConnector extends TileEntityBatteryMultiblock implements IEnergySource, IEnergySink, IMjReceiver, IMjPassiveProvider {

    
    @Override
    public int getStorageCapacity() {
        return 100000;
    }
    
    private EnergyStorage getParentStorage() {
        if (hasParent()) {
            return getParent().getStore();
        } else {
            return null;
        }
    }
    
    @Override
    public void update() {
        if (!ic2netreg && Loader.isModLoaded("ic2") && !world.isRemote) {
            MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this));
            ic2netreg = true;
        }
        super.update();
        if (hasParent() && getBlockType().getStateFromMeta(getBlockMetadata()).getValue(BlockBatteryConnector.out)) {
            for (EnumFacing dir : EnumFacing.values()) {
                TileEntity nt =  getWorld().getTileEntity(getPos().add(dir.getDirectionVec()));
                if (nt != null) {
                    //Forge Energy
                    if (nt.hasCapability(CapabilityEnergy.ENERGY, dir.getOpposite())) {
                        IEnergyStorage store = (IEnergyStorage)nt.getCapability(CapabilityEnergy.ENERGY, dir.getOpposite());
                        if (store.canReceive()) {
                            getParent().getStore().extractEnergy(store.receiveEnergy(getParent().getStore().getEnergyStored(), false), false);
                        }
                    }
                    //Buildcraft
                    if (Loader.isModLoaded("buildcraftenergy") && nt.hasCapability(MjAPI.CAP_RECEIVER, dir.getOpposite())) {
                        IMjReceiver recv = (IMjReceiver)nt.getCapability(MjAPI.CAP_RECEIVER, dir.getOpposite());
                        if (recv.canReceive()) {
                            long req = Math.min(recv.getPowerRequested(), getParent().getStore().getEnergyStored() * MjAPI.MJ);
                            getParent().getStore().extractEnergy((int)((req - recv.receivePower(req, false)) / MjAPI.MJ), false);
                        }
                    }
                }
            }
        }
    }

    //Forge Energy
    
    @Override
    public boolean hasCapability(Capability<?> cpblt, EnumFacing ef) {
        if (hasParent()) {
            if (cpblt == CapabilityEnergy.ENERGY && !getBlockType().getStateFromMeta(getBlockMetadata()).getValue(BlockBatteryConnector.out)) {
                return true;
            }
        }
        return super.hasCapability(cpblt, ef);
    }
    
    @Override
    public <T> T getCapability(Capability<T> cpblt, EnumFacing ef) {
        if (cpblt == CapabilityEnergy.ENERGY) {
            if (hasParent() && !getBlockType().getStateFromMeta(getBlockMetadata()).getValue(BlockBatteryConnector.out)) {
                return (T)getParent().getStore();
            }
        }
        if ((cpblt == MjAPI.CAP_CONNECTOR || cpblt == MjAPI.CAP_RECEIVER) && !getBlockType().getStateFromMeta(getBlockMetadata()).getValue(BlockBatteryConnector.out)) {
            return (T)this;
        }
        if (cpblt == MjAPI.CAP_PASSIVE_PROVIDER && getBlockType().getStateFromMeta(getBlockMetadata()).getValue(BlockBatteryConnector.out)) {
            return (T)this;
        }
        return super.getCapability(cpblt, ef);
    }

    
    //IC2
    
    private boolean ic2netreg = false;
    
    @Override
    public double getOfferedEnergy() {
        return hasParent() && getBlockType().getStateFromMeta(getBlockMetadata()).getValue(BlockBatteryConnector.out) ? Math.min(512, getParentStorage().getEnergyStored() / 4.0) : 0;
    }

    @Override
    public double getDemandedEnergy() {
        return hasParent() && !getBlockType().getStateFromMeta(getBlockMetadata()).getValue(BlockBatteryConnector.out) ? 512 : 0;
    }

    @Override
    public void drawEnergy(double amount) {
        getParentStorage().extractEnergy((int)amount * 4, false);
    }

    @Override
    public double injectEnergy(EnumFacing directionFrom, double amount, double voltage) {
        return amount - getParentStorage().receiveEnergy((int)amount * 4, false) / 4.0;
    }

    @Override
    public int getSourceTier() {
        return 3;
    }

    @Override
    public int getSinkTier() {
        return 3;
    }

    @Override
    public boolean emitsEnergyTo(IEnergyAcceptor receiver, EnumFacing side) {
        return hasParent() && getBlockType().getStateFromMeta(getBlockMetadata()).getValue(BlockBatteryConnector.out);
    }

    @Override
    public boolean acceptsEnergyFrom(IEnergyEmitter emitter, EnumFacing side) {
        return hasParent() && !getBlockType().getStateFromMeta(getBlockMetadata()).getValue(BlockBatteryConnector.out);
    }

    @Override
    public void invalidate() {
        if (ic2netreg && !world.isRemote) {
            MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));
            ic2netreg = false;
        }
        super.invalidate();
    }

    @Override
    public void onChunkUnload() {
        if (ic2netreg && !world.isRemote) {
            MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));
            ic2netreg = false;
        }
        super.onChunkUnload();
    }

    
    //Buildcraft
    
    @Override
    public long getPowerRequested() {
        return Math.min(1024, getParentStorage().getMaxEnergyStored() - getParentStorage().getEnergyStored()) * MjAPI.MJ / 10;
    }

    @Override
    public long receivePower(long microJoules, boolean simulate) {
        return getParentStorage().receiveEnergy((int)(microJoules / MjAPI.MJ) * 10, simulate);
    }

    @Override
    public boolean canReceive() {
        return hasParent();
    }

    @Override
    public boolean canConnect(IMjConnector other) {
        return true;
    }

    //Unused? Even with being a "passive provider", buildcraft still seems to need you to push energy to other blocks yourself
    @Override
    public long extractPower(long min, long max, boolean simulate) {
        long amount = getParentStorage().extractEnergy((int)(max / MjAPI.MJ) * 10, simulate) * MjAPI.MJ / 10;
        if (amount < min) {
            amount = 0;
            getParentStorage().receiveEnergy((int)(amount / MjAPI.MJ) * 10, simulate);
        }
        return amount;
    }
    
}
