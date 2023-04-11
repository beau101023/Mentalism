package me.beaubaer.mentalism.capabilities.spellmanager;

import me.beaubaer.mentalism.networking.S2C.AvailableSpellsSyncS2CPacket;
import me.beaubaer.mentalism.networking.S2C.CanCastSpellsSyncS2CPacket;
import me.beaubaer.mentalism.networking.MentalismMessages;
import me.beaubaer.mentalism.networking.S2C.SpellProgressSyncS2CPacket;
import me.beaubaer.mentalism.spells.SpellRegistry;
import me.beaubaer.mentalism.spells.Spell;
import net.minecraft.server.level.ServerPlayer;

import java.util.ArrayList;
import java.util.List;

public class SpellManager
{

    public float castProgress = 0f;
    public final List<Integer> availableSpells;

    public Spell selectedSpell = null;

    public boolean tryingToCast = false;

    private final ServerPlayer p;
    public SpellManager(ServerPlayer p)
    {
        this.p = p;
        availableSpells = new ArrayList<>();
    }

    // called every player tick serverside
    public void update()
    {
        sync();

        if(selectedSpell == null)
            return;

        if(selectedSpell.canCast(p) && tryingToCast)
        {
            castProgress = Math.min(castProgress + selectedSpell.getCastIncrement(), 1.0f);

            selectedSpell.whileCasting(p, castProgress);

            if (castProgress == 1f)
            {
                selectedSpell.activate(p);
                castProgress = 0f;
            }
        }
    }

    public void updateSelectedSpell(int spellNum)
    {
        if(spellNum == -1)
        {
            selectedSpell = null;
            return;
        }

        for (Spell sp : SpellRegistry.SPELLS.get().getValues())
        {
            if (sp.getSpellNum() == spellNum)
            {
                selectedSpell = sp;
                return;
            }
        }
    }

    public Integer[] getAvailableSpellNums()
    {
        updateAvailableSpells();

        return availableSpells.toArray(new Integer[0]);
    }

    public Integer[] getCanCastSpellNums()
    {
        List<Integer> spellNums = new ArrayList<>();

        for (Spell sp : SpellRegistry.SPELLS.get().getValues())
        {
            if (sp.canCast(p))
            {
                spellNums.add(sp.getSpellNum());
            }
        }

        return spellNums.toArray(new Integer[0]);
    }

    private void updateAvailableSpells()
    {
        availableSpells.clear();

        for (Spell sp : SpellRegistry.SPELLS.get().getValues())
        {
            if (sp.available(p))
            {
                availableSpells.add(sp.getSpellNum());
            }
        }
    }

    public void sync()
    {
        MentalismMessages.sendToPlayer(new SpellProgressSyncS2CPacket(castProgress), p);
        MentalismMessages.sendToPlayer(new AvailableSpellsSyncS2CPacket(getAvailableSpellNums()), p);
        MentalismMessages.sendToPlayer(new CanCastSpellsSyncS2CPacket(getCanCastSpellNums()), p);
    }
}
