package io.github.tomtom2211.noteblock_helper;

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




public class Noteblock_helper implements ModInitializer {
    @Environment(EnvType.CLIENT)
    public static final Logger LOGGER = LoggerFactory.getLogger("TomsAddons");
    public static long buttonPressTime = System.currentTimeMillis();

    @Override
    public void onInitialize() {
        Config.load();

        KeyBinding clearBlocksKey = KeyBindingHelper.registerKeyBinding(new KeyBinding("Clear all blocks", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_X, "Noteblock helper"));
        KeyBinding removeBlockKey = KeyBindingHelper.registerKeyBinding(new KeyBinding("Remove Block", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_Z, "Noteblock helper"));
        KeyBinding selectBlockKey = KeyBindingHelper.registerKeyBinding(new KeyBinding("Select Block", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_V, "Noteblock helper"));

        ClientTickEvents.END_CLIENT_TICK.register(client-> {
            //Select block key
            if(selectBlockKey.wasPressed()&&System.currentTimeMillis()-buttonPressTime>=100&&client.player!=null){
                Config.config.selectedBlocks.add(BlockLookHelper.getLookedAtBlockBox(client,5));
                buttonPressTime = System.currentTimeMillis();
                client.player.sendMessage(Text.of("Block added!"),false);
                Config.save();
            }
            //Remove block key
            if(removeBlockKey.wasPressed()&&System.currentTimeMillis()-buttonPressTime>=100){
                Config.config.selectedBlocks.remove(BlockLookHelper.getLookedAtBlockBox(client, 5));
                Config.save();
            }
            //Clear all blocks key
            if(clearBlocksKey.wasPressed()&&System.currentTimeMillis()-buttonPressTime>=100){
                Config.config.selectedBlocks.clear();
                Config.save();
            }
        });

        WorldRenderEvents.AFTER_ENTITIES.register(context -> {
            // Highlight logic (first block from the array is highlighted first)
            if(Config.config.selectedBlocks.isEmpty()){
                Config.load();
                assert MinecraftClient.getInstance().player != null;
                MinecraftClient.getInstance().player.sendMessage(Text.of("Reset"), false);
            }
            else if(Config.config.highlightSelectedBlocks) {
                Box selectedBlock = Config.config.selectedBlocks.getFirst();
                BlockHighlighter.highlightBlock(selectedBlock, context);
            }
        });

        AttackBlockCallback.EVENT.register((playerEntity, world, hand, blockPos,direction) -> {
            Box attackedBlock = new Box(blockPos);
            Config.config.selectedBlocks.remove(attackedBlock);
            return ActionResult.PASS;
        });
        LOGGER.info("Noteblock_helper successfully launched!");
    }
}
