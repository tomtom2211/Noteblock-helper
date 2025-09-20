package io.github.tomtom2211.noteblock_helper;

import io.github.tomtom2211.noteblock_helper.features.BasicHighlightLogic;
import io.github.tomtom2211.noteblock_helper.modconfig.Config;
import io.github.tomtom2211.noteblock_helper.utils.BlockHighlighter;
import io.github.tomtom2211.noteblock_helper.utils.BlockLookHelper;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.Box;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;


public class Noteblock_helper implements ModInitializer {
    @Environment(EnvType.CLIENT)
    public static final Logger LOGGER = LoggerFactory.getLogger("TomsAddons");
    public static long buttonPressTime = System.currentTimeMillis();
    public static int currentIndex=0;

    @Override
    public void onInitialize() {
        Config.load();

        BasicHighlightLogic basicHighlightLogic = new BasicHighlightLogic();

        // Create KeyBindings
        KeyBinding clearBlocksKey = KeyBindingHelper.registerKeyBinding(new KeyBinding("Remove all blocks", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_X, "Noteblock helper"));
        KeyBinding removeBlockKey = KeyBindingHelper.registerKeyBinding(new KeyBinding("Remove a block", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_Z, "Noteblock helper"));
        KeyBinding resetBlockKey = KeyBindingHelper.registerKeyBinding(new KeyBinding("Start from 0", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_R, "Noteblock helper"));
        KeyBinding selectBlockKey = KeyBindingHelper.registerKeyBinding(new KeyBinding("Add a block", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_V, "Noteblock helper"));

        // End_client_tick event
        ClientTickEvents.END_CLIENT_TICK.register(client-> {
            basicHighlightLogic.keybindInit(selectBlockKey, removeBlockKey, clearBlocksKey, resetBlockKey, client);
        });

        // After_Entities event
        WorldRenderEvents.AFTER_ENTITIES.register(context -> {
            MinecraftClient client = MinecraftClient.getInstance();
            basicHighlightLogic.renderInit(context, client);
        });

        // AttackBlock callback
        AttackBlockCallback.EVENT.register((playerEntity, world, hand, blockPos,direction) -> {
            basicHighlightLogic.attackBlockInit();
            return ActionResult.PASS;
        });

        LOGGER.info("Noteblock_helper successfully launched!");
    }
}
