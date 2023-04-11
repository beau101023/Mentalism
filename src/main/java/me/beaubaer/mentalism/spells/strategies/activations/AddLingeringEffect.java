package me.beaubaer.mentalism.spells.strategies.activations;

import me.beaubaer.mentalism.capabilities.lingeringeffects.LingeringEffectManagerProvider;
import me.beaubaer.mentalism.spells.strategies.lingeringeffects.ILingeringEffect;
import net.minecraft.server.level.ServerPlayer;

public class AddLingeringEffect
{
    public static void addAndInit(ServerPlayer player, ILingeringEffect effect)
    {
        // get the player's lingering effect manager
        // add the effect to the manager
        player.getCapability(LingeringEffectManagerProvider.LINGERING_EFFECT_MANAGER).ifPresent(manager -> {
            if(manager.hasEffect(effect.getClass()))
                return;
            effect.intialize(player, manager);
            manager.add(effect);
        });
    }
}
