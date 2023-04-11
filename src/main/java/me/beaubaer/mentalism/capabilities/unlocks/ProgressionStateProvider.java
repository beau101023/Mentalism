package me.beaubaer.mentalism.capabilities.unlocks;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ProgressionStateProvider implements ICapabilityProvider, INBTSerializable<CompoundTag>
{
    public static final Capability<ProgressionState> PROGRESSION_STATE = CapabilityManager.get(new CapabilityToken<>() {});
    private ProgressionState progressionState = null;
    private final LazyOptional<ProgressionState> optional = LazyOptional.of(this::createProgressionState);

    private ProgressionState createProgressionState()
    {
        if(this.progressionState == null)
        {
            this.progressionState = new ProgressionState();
        }

        return this.progressionState;
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side)
    {
        if(cap == PROGRESSION_STATE)
        {
            return optional.cast();
        }

        return LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT()
    {
        CompoundTag nbt = new CompoundTag();
        createProgressionState().saveNBTData(nbt);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt)
    {
        createProgressionState().loadNBTData(nbt);
    }
}
