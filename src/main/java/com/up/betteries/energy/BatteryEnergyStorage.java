package com.up.betteries.energy;

import static com.up.betteries.energy.LongEnergyStorage.safeDowncast;
import com.up.betteries.tileentity.TileEntityBatteryController;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.stream.Collectors;
import net.minecraftforge.energy.EnergyStorage;

/**
 *
 * @author Ricky Talbot
 */
public class BatteryEnergyStorage extends LongEnergyStorage {
        
    
    private class InputBatteryEnergyStorage extends EnergyStorage {
        
        private BatteryEnergyStorage store;

        public InputBatteryEnergyStorage(BatteryEnergyStorage store) {
            super(store.getMaxEnergyStored());
            this.store = store;
        }

        @Override
        public boolean canExtract() {
            return false;
        }
        
        @Override
        public int extractEnergy(int maxExtract, boolean simulate) {
            return 0;
        }
        
        @Override
        public int receiveEnergy(int maxReceive, boolean simulate) {
            return store.receiveEnergy(maxReceive, simulate);
        }
        
    }
    
    private class OutputBatteryEnergyStorage extends EnergyStorage {
        
        private BatteryEnergyStorage store;

        public OutputBatteryEnergyStorage(BatteryEnergyStorage store) {
            super(store.getMaxEnergyStored());
            this.store = store;
        }

        @Override
        public boolean canReceive() {
            return false;
        }
        
        @Override
        public int receiveEnergy(int maxReceive, boolean simulate) {
            return 0;
        }
        
        @Override
        public int extractEnergy(int maxExtract, boolean simulate) {
            return store.receiveEnergy(maxReceive, simulate);
        }
        
    }
    
    public static class SlidingAverage {

        private ArrayList<Integer> current = new ArrayList<>();
        private LinkedList<Integer> history = new LinkedList<>();

        public SlidingAverage(int max) {
            for (int i = 0; i < max; i++) history.push(0);
        }

        public SlidingAverage(LinkedList<Integer> history) {
            this.history = history;
        }

        public void push(Integer e) {
            current.add(e);
        }

        public void next() {
            history.push((int)current.stream().collect(Collectors.summingInt(i -> i)));
            current = new ArrayList<>();
            history.removeLast();
        }

        public double getAverage() {
            return history.stream().collect(Collectors.averagingInt(i -> i));
        }

        public LinkedList<Integer> getHistory() {
            return history;
        }

        public void setHistory(LinkedList<Integer> old) {
            this.history = old;
        }
        
    }

    private SlidingAverage out = new SlidingAverage(TileEntityBatteryController.AVERAGE_LENGTH);
    private SlidingAverage in = new SlidingAverage(TileEntityBatteryController.AVERAGE_LENGTH);


    private static final int BASE_TRANSFER = 1000;
    private static final int TRANSFER_LIMIT = 10000000;
    
    TileEntityBatteryController te;

    public BatteryEnergyStorage(TileEntityBatteryController te, long capacity) {
        super(capacity);
        this.te = te;
    }

    public BatteryEnergyStorage(TileEntityBatteryController te, long capacity, long energy) {
        super(capacity, safeDowncast(capacity), safeDowncast(capacity), energy);
        this.te = te;
    }

    public int getMaxTransfer() {
        return (int)(TRANSFER_LIMIT * 2 / (1 + Math.exp(-(te.capacity - (double)TileEntityBatteryController.BASE_CAPACITY) / Integer.MAX_VALUE)) - TRANSFER_LIMIT) + BASE_TRANSFER;
    }

    public SlidingAverage getHistoryOut() {
        return out;
    }

    public SlidingAverage getHistoryIn() {
        return in;
    }

    public double getAverageOut() {
        return out.getAverage();
    }

    public double getAverageIn() {
        return in.getAverage();
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        if (maxExtract < 0) maxExtract = Integer.MAX_VALUE;
        int oldRS = te.getRedstoneLevel();
        int ret = super.extractEnergy(Math.min(maxExtract, getMaxTransfer()), simulate);
        if (!simulate) {
            if (oldRS == te.getRedstoneLevel()) {
                te.markDirty(false, true);
            } else {
                te.markDirty();
            }
            out.push(ret);
        }
        return ret;
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        if (maxReceive < 0) maxReceive = Integer.MAX_VALUE;
        int oldRS = te.getRedstoneLevel();
        int ret = super.receiveEnergy(Math.min(maxReceive, getMaxTransfer()), simulate);
        if (!simulate) {
            if (oldRS == te.getRedstoneLevel()) {
                te.markDirty(false, true);
            } else {
                te.markDirty();
            }
            in.push(ret);
        }
        return ret;
    }


    public EnergyStorage getInputView() {
        return new InputBatteryEnergyStorage(this);
    }

    public EnergyStorage getOutputView() {
        return new OutputBatteryEnergyStorage(this);
    }
}