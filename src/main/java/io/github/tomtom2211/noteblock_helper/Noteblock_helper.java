package io.github.tomtom2211.noteblock_helper;

import io.github.tomtom2211.noteblock_helper.utils.BlockHighlighter;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.util.math.Box;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;



public class Noteblock_helper implements ModInitializer {
    @Environment(EnvType.CLIENT)
    public static final Logger LOGGER = LoggerFactory.getLogger("TomsAddons");
    private static ArrayList<Box> noteBlock = new ArrayList<>();
    @Override
    public void onInitialize() {
        WorldRenderEvents.BEFORE_ENTITIES.register(context -> {
            BlockHighlighter.highlightBlock(noteBlock,context);
        });
        LOGGER.info("Noteblock_helper successfully launched!");
    }
}
