package me.beaubaer.mentalism.spells.strategies.lingeringeffects;

import me.beaubaer.mentalism.capabilities.lingeringeffects.LingeringEffectManager;
import net.minecraft.server.level.ServerPlayer;

public interface ILingeringEffect
{
    void update();

    void intialize(ServerPlayer p, LingeringEffectManager manager);

    void onRemoved();

    String getName();
}
