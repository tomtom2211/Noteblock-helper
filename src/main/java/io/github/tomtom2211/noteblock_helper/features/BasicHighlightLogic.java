package io.github.tomtom2211.noteblock_helper.features;

import io.github.tomtom2211.noteblock_helper.modconfig.Config;
import io.github.tomtom2211.noteblock_helper.utils.BlockHighlighter;
import io.github.tomtom2211.noteblock_helper.utils.BlockLookHelper;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.text.Text;
import net.minecraft.util.math.Box;

import java.util.Objects;

public class BasicHighlightLogic {

    public static long buttonPressTime = System.currentTimeMillis();
    public static int currentIndex = 0;

    public static void keybindInit(KeyBinding selectBlockKey, KeyBinding removeBlockKey, KeyBinding clearBlocksKey, KeyBinding resetBlocksKey, MinecraftClient client){

        // Add a block key
        if (selectBlockKey.wasPressed() && System.currentTimeMillis() - buttonPressTime >= 100 && client.player != null) {
            buttonPressTime = System.currentTimeMillis();
            Box lookingAtBlock = BlockLookHelper.getLookedAtBlockBox(client, 5);
            if(currentIndex>0) {
                Config.config.selectedBlocks.add(currentIndex++, lookingAtBlock);
            }
            else if (currentIndex==0){
                Config.config.selectedBlocks.add(lookingAtBlock);
            }
            client.player.sendMessage(Text.of("Block added!"), false);
            Config.save();
        }
        // Remove a block key
        if (removeBlockKey.wasPressed() && System.currentTimeMillis() - buttonPressTime >= 100) {
            Config.config.selectedBlocks.remove(BlockLookHelper.getLookedAtBlockBox(client, 5));
            Config.save();
        }

        // Clear all blocks key
        if (clearBlocksKey.wasPressed() && System.currentTimeMillis() - buttonPressTime >= 100) {
            Config.config.selectedBlocks.clear();
            Config.save();
        }

        // Reset index to 0
        if (resetBlocksKey.wasPressed() && System.currentTimeMillis() - buttonPressTime >= 100) {
            buttonPressTime = System.currentTimeMillis();
            currentIndex = 0;
            Objects.requireNonNull(client.player).sendMessage(Text.of("Starting from beginning..."), true);
        }
    }

    public static void renderInit(WorldRenderContext context, MinecraftClient client){
        // If the feature is enabled, then get the selected ordered block and highlight it
        if(Config.config.highlightSelectedBlocks) {
            try {
                Objects.requireNonNull(client.player).sendMessage(Text.of("Note:" + currentIndex), true);
                Box firstBlock = Config.config.selectedBlocks.get(currentIndex);
                int firstColor = Config.config.highlightFirstColor & 0xFFFFFF;
                BlockHighlighter.highlightBlock(firstBlock, context, firstColor);
            }
            catch(IndexOutOfBoundsException ignored){}
            try{
                Box secondBlock = Config.config.selectedBlocks.get(currentIndex+1);
                int secondColor = Config.config.highlightSecondColor & 0xFFFFFF;
                BlockHighlighter.highlightBlock(secondBlock,context,secondColor);
            }
            catch(IndexOutOfBoundsException ignored){}
        }
    }

    public static void attackBlockInit(){
        // Add the number 1 to the currentIndex whenever a block is hit
        if(System.currentTimeMillis()-buttonPressTime>=100) {
            if (currentIndex < Config.config.selectedBlocks.size()-1) {
                currentIndex++;
            } else {
                currentIndex = 0;
            }
            buttonPressTime=System.currentTimeMillis();
        }
    }
}
