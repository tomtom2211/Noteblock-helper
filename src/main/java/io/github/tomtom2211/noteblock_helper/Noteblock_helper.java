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

import java.util.Objects;


public class Noteblock_helper implements ModInitializer {
    @Environment(EnvType.CLIENT)
    public static final Logger LOGGER = LoggerFactory.getLogger("TomsAddons");
    public static long buttonPressTime = System.currentTimeMillis();
    public static int currentIndex=0;

    @Override
    public void onInitialize() {
        Config.load();

        KeyBinding clearBlocksKey = KeyBindingHelper.registerKeyBinding(new KeyBinding("Remove all blocks", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_X, "Noteblock helper"));
        KeyBinding removeBlockKey = KeyBindingHelper.registerKeyBinding(new KeyBinding("Remove a block", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_Z, "Noteblock helper"));
        KeyBinding resetBlockKey = KeyBindingHelper.registerKeyBinding(new KeyBinding("Start from 0", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_R, "Noteblock helper"));
        KeyBinding selectBlockKey = KeyBindingHelper.registerKeyBinding(new KeyBinding("Select a block", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_V, "Noteblock helper"));

        ClientTickEvents.END_CLIENT_TICK.register(client-> {
            // Select block key
            if(selectBlockKey.wasPressed()&&System.currentTimeMillis()-buttonPressTime>=100&&client.player!=null){
                buttonPressTime = System.currentTimeMillis();
                currentIndex=0;
                Config.config.selectedBlocks.add(BlockLookHelper.getLookedAtBlockBox(client,5));
                client.player.sendMessage(Text.of("Block added!"),false);
                Config.save();
            }
            // Remove block key
            if(removeBlockKey.wasPressed()&&System.currentTimeMillis()-buttonPressTime>=100){
                Config.config.selectedBlocks.remove(BlockLookHelper.getLookedAtBlockBox(client, 5));
                Config.save();
            }
            // Clear all blocks key
            if(clearBlocksKey.wasPressed()&&System.currentTimeMillis()-buttonPressTime>=100){
                Config.config.selectedBlocks.clear();
                Config.save();
            }
            // Reset index to 0
            if(resetBlockKey.wasPressed()&&System.currentTimeMillis()-buttonPressTime>=100){
                buttonPressTime = System.currentTimeMillis();
                currentIndex=0;
                Objects.requireNonNull(client.player).sendMessage(Text.of("Starting from beginning..."),true);
            }
        });

        WorldRenderEvents.AFTER_ENTITIES.register(context -> {
            // Highlight logic (first block from the array is highlighted first)
            if(Config.config.selectedBlocks.isEmpty()){
                Config.load();
                assert MinecraftClient.getInstance().player != null;
                MinecraftClient.getInstance().player.sendMessage(Text.of("Reset"), true);
            }
            else if(Config.config.highlightSelectedBlocks) {
                try {
                    Objects.requireNonNull(MinecraftClient.getInstance().player).sendMessage(Text.of("Note:" + currentIndex), true);
                    Box selectedBlock = Config.config.selectedBlocks.get(currentIndex);
                    BlockHighlighter.highlightBlock(selectedBlock, context);
                }
                catch(IndexOutOfBoundsException e){
                    System.out.println("Not good");
                }
            }
        });

        AttackBlockCallback.EVENT.register((playerEntity, world, hand, blockPos,direction) -> {
            if(System.currentTimeMillis()-buttonPressTime>=100) {
                if (currentIndex < Config.config.selectedBlocks.size()-1) {
                    currentIndex++;
                } else {
                    currentIndex = 0;
                }
                buttonPressTime=System.currentTimeMillis();
            }
            return ActionResult.PASS;
        });
        LOGGER.info("Noteblock_helper successfully launched!");
    }
}
