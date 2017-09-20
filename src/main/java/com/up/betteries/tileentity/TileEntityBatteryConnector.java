package com.up.betteries.tileentity;

import com.up.betteries.block.BlockBatteryConnector;
import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergyAcceptor;
import ic2.api.energy.tile.IEnergyEmitter;
import ic2.api.energy.tile.IEnergySink;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import ic2.api.energy.tile.IEnergySource;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.energy.EnergyStorage;

/**
 *
 * @author Ricky
 */
public class TileEntityBatteryConnector extends TileEntityBatteryMultiblock implements ITickable, IEnergySource, IEnergySink {

    @Override
    public boolean hasCapability(Capability<?> cpblt, EnumFacing ef) {
        if (cpblt == CapabilityEnergy.ENERGY) {
            if (hasParent() && !getBlockType().getStateFromMeta(getBlockMetadata()).getValue(BlockBatteryConnector.out)) {
                return true;
            }
        }
        return super.hasCapability(cpblt, ef);
    }
    
    private EnergyStorage getParentStorage() {
        if (hasParent()) {
            return getParent().getStore();
        } else {
            return null;
        }
    }
    
    @Override
    public <T> T getCapability(Capability<T> cpblt, EnumFacing ef) {
        if (cpblt == CapabilityEnergy.ENERGY) {
            if (hasParent() && !getBlockType().getStateFromMeta(getBlockMetadata()).getValue(BlockBatteryConnector.out)) {
                return (T)getParent().getStore();
            }
        }
        return super.getCapability(cpblt, ef);
    }

    @Override
    public int getStorageCapacity() {
        return 200000;
    }
    
    private boolean loaded = false;
    @Override
    public void update() {
        if (!loaded) {
            MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this));
            loaded = true;
        }
        super.update();
        if (hasParent() && getBlockType().getStateFromMeta(getBlockMetadata()).getValue(BlockBatteryConnector.out)) {
            for (EnumFacing dir : EnumFacing.values()) {
                TileEntity nx = getWorld().getTileEntity(getPos().add(dir.getDirectionVec()));
                if (nx != null && nx.hasCapability(CapabilityEnergy.ENERGY, dir.getOpposite())) {
                    IEnergyStorage store = (IEnergyStorage)nx.getCapability(CapabilityEnergy.ENERGY, dir.getOpposite());
                    if (store.canReceive()) {
                        getParent().getStore().extractEnergy(store.receiveEnergy(getParent().getStore().getEnergyStored(), false), false);
                    }
                }
            }
        }
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate) {
        return false;
    }

    @Override
    public double getOfferedEnergy() {
        return hasParent() ? Math.min(getSourceTier() * 32, getParentStorage().getEnergyStored() / 4.0) : 0;
    }

    @Override
    public double getDemandedEnergy() {
        return hasParent() ? getSinkTier() * 32 : 0;
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
        return 1;
    }

    @Override
    public int getSinkTier() {
        return 1;
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
        MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));
        super.invalidate();
    }

    @Override
    public void onChunkUnload() {
        MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));
        super.onChunkUnload();
    }
    
}
