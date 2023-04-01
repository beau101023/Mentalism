package me.beaubaer.mentalism.clientdata;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public class SpellCastData
{
    public static float localSpellProgress = 0f;
    public static List<Integer> availableNums = new ArrayList<>();
    public static List<Integer> canCastNums = new ArrayList<>();
}
