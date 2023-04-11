package me.beaubaer.mentalism.clientdata;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LingeringEffectData
{
    public static Set<String> activeEffectIDs = new HashSet<>();

    public static void addEffect(String ID)
    {
        activeEffectIDs.add(ID);
    }

    public static void removeEffect(String ID)
    {
        activeEffectIDs.remove(ID);
    }

    public static boolean effectIsActive(String ID)
    {
        return activeEffectIDs.contains(ID);
    }
}
