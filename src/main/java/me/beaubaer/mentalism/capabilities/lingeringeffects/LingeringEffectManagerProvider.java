package me.beaubaer.mentalism.capabilities.lingeringeffects;

import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class LingeringEffectManagerProvider implements ICapabilityProvider
{
    private ServerPlayer player;

    public LingeringEffectManagerProvider(ServerPlayer player)
    {
        this.player = player;
    }

    public static final Capability<LingeringEffectManager> LINGERING_EFFECT_MANAGER = CapabilityManager.get(new CapabilityToken<>() {});
    private LingeringEffectManager manager = null;
    private final LazyOptional<LingeringEffectManager> optional = LazyOptional.of(this::createLinteringEffectManager);

    private LingeringEffectManager createLinteringEffectManager()
    {
        if(this.manager == null)
        {
            this.manager = new LingeringEffectManager(player);
        }

        return this.manager;
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side)
    {
        if(cap == LINGERING_EFFECT_MANAGER)
        {
            return optional.cast();
        }

        return LazyOptional.empty();
    }
}
