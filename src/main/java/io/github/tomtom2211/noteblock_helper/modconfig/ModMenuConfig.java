package io.github.tomtom2211.noteblock_helper.modconfig;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.text.Text;

public class ModMenuConfig implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> {
            // Create builder instance
            ConfigBuilder builder = ConfigBuilder.create()
                    .setParentScreen(parent)
                    .setTitle(Text.of("Noteblock Helper"));

            // Create general category
            var general = builder.getOrCreateCategory(Text.of("General"));
            ConfigEntryBuilder entryBuilder = builder.entryBuilder();

            // Boolean toggle: highlight blocks
            general.addEntry(
                    entryBuilder
                            .startBooleanToggle(Text.of("Enable block highlighter"), Config.config.highlightSelectedBlocks)
                            .setDefaultValue(true)
                            .setSaveConsumer(newValue -> Config.config.highlightSelectedBlocks = newValue)
                            .build()
            );

            // Color picker: highlight color
            general.addEntry(
                    entryBuilder
                            .startColorField(Text.of("First block highlight Color"), Config.config.highlightFirstColor)
                            .setAlphaMode(false)
                            .setDefaultValue(0x00ffff)
                            .setSaveConsumer(newValue -> Config.config.highlightFirstColor = newValue)
                            .build()
            );

            general.addEntry(
                    entryBuilder
                            .startColorField(Text.of("Second block highlight Color"), Config.config.highlightFirstColor)
                            .setAlphaMode(false)
                            .setDefaultValue(0x0080ff)
                            .setSaveConsumer(newValue -> Config.config.highlightSecondColor = newValue)
                            .build()
            );

            // Save button runnable
            builder.setSavingRunnable(Config::save);

            // Return config screen
            return builder.build();
        };
    }
}
