package me.beaubaer.mentalism.capabilities.lingeringeffects;

import me.beaubaer.mentalism.Mentalism;
import me.beaubaer.mentalism.spells.strategies.lingeringeffects.ILingeringEffect;
import net.minecraft.server.level.ServerPlayer;

import java.util.ArrayList;
import java.util.List;

public class LingeringEffectManager
{
    // holds lingering effects that affect the player every tick
    // this is a capability
    List<ILingeringEffect> effects = new ArrayList<>();
    List<ILingeringEffect> effectsToRemove = new ArrayList<>();
    ServerPlayer p;

    public LingeringEffectManager(ServerPlayer player)
    {
        this.p = player;
    }

    public void update()
    {
        // remove lingering effects that have been marked for removal
        effectsToRemove.forEach(ILingeringEffect::onRemoved);
        effects.removeAll(effectsToRemove);
        effectsToRemove.clear();

        Mentalism.LOGGER.info("Lingering effects: " + effects.size());

        for(ILingeringEffect effect : effects)
        {
            effect.update();
        }
    }

    public void add(ILingeringEffect effect)
    {
        effects.add(effect);
    }

    public void remove(ILingeringEffect effect)
    {
        effectsToRemove.add(effect);
    }

    public boolean hasEffect(Class<? extends ILingeringEffect> aClass)
    {
        for(ILingeringEffect effect : effects)
        {
            if(effect.getClass() == aClass)
                return true;
        }
        return false;
    }
}
