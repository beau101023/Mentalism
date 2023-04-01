package me.beaubaer.mentalism.capabilities.spellmanager;

import me.beaubaer.mentalism.Mentalism;
import me.beaubaer.mentalism.networking.S2C.AvailableSpellsSyncS2CPacket;
import me.beaubaer.mentalism.networking.S2C.CanCastSpellsSyncS2CPacket;
import me.beaubaer.mentalism.networking.MentalismMessages;
import me.beaubaer.mentalism.networking.S2C.SpellProgressSyncS2CPacket;
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

    public SpellManager()
    {
        availableSpells = new ArrayList<>();
    }

    // called every player tick serverside
    public void update(ServerPlayer p)
    {
        sync(p);

        if(selectedSpell == null)
            return;


        if(selectedSpell.canCast(p) && tryingToCast)
        {
            castProgress = Math.min(castProgress + selectedSpell.getCastIncrement(), 1.0f);

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

        for (Spell sp : Mentalism.SPELLS.get().getValues())
        {
            if (sp.getSpellNum() == spellNum)
            {
                selectedSpell = sp;
                return;
            }
        }
    }

    public Integer[] getAvailableSpellNums(ServerPlayer p)
    {
        updateAvailableSpells(p);

        return availableSpells.toArray(new Integer[0]);
    }

    public Integer[] getCanCastSpellNums(ServerPlayer p)
    {
        List<Integer> spellNums = new ArrayList<>();

        for (Spell sp : Mentalism.SPELLS.get().getValues())
        {
            if (sp.canCast(p))
            {
                spellNums.add(sp.getSpellNum());
            }
        }

        return spellNums.toArray(new Integer[0]);
    }

    private void updateAvailableSpells(ServerPlayer p)
    {
        availableSpells.clear();

        for (Spell sp : Mentalism.SPELLS.get().getValues())
        {
            if (sp.available(p))
            {
                availableSpells.add(sp.getSpellNum());
            }
        }
    }

    public void sync(ServerPlayer p)
    {
        MentalismMessages.sendToPlayer(new SpellProgressSyncS2CPacket(castProgress), p);
        MentalismMessages.sendToPlayer(new AvailableSpellsSyncS2CPacket(getAvailableSpellNums(p)), p);
        MentalismMessages.sendToPlayer(new CanCastSpellsSyncS2CPacket(getCanCastSpellNums(p)), p);
    }
}
