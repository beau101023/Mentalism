package me.beaubaer.mentalism.capabilities.spellmanager;

import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SpellManagerProvider implements ICapabilityProvider
{
    private ServerPlayer p;

    public static final Capability<SpellManager> SPELL_MANAGER = CapabilityManager.get(new CapabilityToken<>() {});
    private SpellManager spellManager = null;
    private final LazyOptional<SpellManager> optional = LazyOptional.of(this::createSpellManager);

    public SpellManagerProvider(ServerPlayer p)
    {
        this.p = p;
    }

    private SpellManager createSpellManager()
    {
        if(this.spellManager == null)
        {
            this.spellManager = new SpellManager(p);
        }

        return this.spellManager;
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side)
    {
        if(cap == SPELL_MANAGER)
        {
            return optional.cast();
        }

        return LazyOptional.empty();
    }
}
