package me.beaubaer.mentalism.capabilities.focus;

import me.beaubaer.mentalism.capabilities.focus.Focus;
import me.beaubaer.mentalism.capabilities.focus.modifiers.FocusLevel;
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

public class FocusProvider implements ICapabilityProvider, INBTSerializable<CompoundTag>
{
    public static final Capability<Focus> FOCUS = CapabilityManager.get(new CapabilityToken<>() {});
    private Focus focus = null;
    private final LazyOptional<Focus> optional = LazyOptional.of(this::createFocus);

    private Focus createFocus()
    {
        if(this.focus == null)
        {
            this.focus = new Focus();
            this.focus.putModifier(FocusLevel.UNIVERSAL_FOCUSLEVEL);
        }

        return this.focus;
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if(cap == FOCUS)
        {
            return optional.cast();
        }

        return LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT()
    {
        CompoundTag nbt = new CompoundTag();
        createFocus().saveNBTData(nbt);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt)
    {
        createFocus().loadNBTData(nbt);
    }
}
