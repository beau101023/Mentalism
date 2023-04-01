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

public class UnlockStateProvider implements ICapabilityProvider, INBTSerializable<CompoundTag>
{
    public static Capability<UnlockState> UNLOCK_STATE = CapabilityManager.get(new CapabilityToken<>() {});
    private UnlockState unlockState = null;
    private final LazyOptional<UnlockState> optional = LazyOptional.of(this::createUnlockManager);

    private UnlockState createUnlockManager()
    {
        if(this.unlockState == null)
        {
            this.unlockState = new UnlockState();
        }

        return this.unlockState;
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side)
    {
        if(cap == UNLOCK_STATE)
        {
            return optional.cast();
        }

        return LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT()
    {
        CompoundTag nbt = new CompoundTag();
        createUnlockManager().saveNBTData(nbt);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt)
    {
        createUnlockManager().loadNBTData(nbt);
    }
}
