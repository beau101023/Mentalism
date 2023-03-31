package me.beaubaer.mentalism.keymappings;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.ClientRegistry;
import net.minecraftforge.client.settings.KeyConflictContext;
import org.lwjgl.glfw.GLFW;

public class KeyMappings
{
    public static final String KEY_CATEGORY_MENTALISM = "key.category.mentalism.main";
    public static final String KEY_FOCUS = "key.mentalism.focus";

    public static final KeyMapping FOCUS_KEY = new KeyMapping(KEY_FOCUS, KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_F, KEY_CATEGORY_MENTALISM);

    public static void initializeMappings()
    {
        registerKey(FOCUS_KEY);
    }

    private static void registerKey(KeyMapping key)
    {
        ClientRegistry.registerKeyBinding(key);
    }
}
