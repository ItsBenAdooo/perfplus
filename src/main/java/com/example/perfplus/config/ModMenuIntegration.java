package com.example.perfplus.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.text.Text;

public class ModMenuIntegration implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> {
            ConfigBuilder builder = ConfigBuilder.create()
                    .setParentScreen(parent)
                    .setTitle(Text.literal("PerfPlus Ayarları"));

            ConfigCategory general = builder.getOrCreateCategory(Text.literal("Genel"));
            ConfigEntryBuilder entryBuilder = builder.entryBuilder();

            // Culling Açık / Kapalı Toggle
            general.addEntry(entryBuilder.startBooleanToggle(Text.literal("Entity Culling Aktif"), ModConfig.enabled)
                    .setDefaultValue(true)
                    .setTooltip(Text.literal("Kamera açısı dışındaki varlıkların çizimini engeller."))
                    .setSaveConsumer(newValue -> ModConfig.enabled = newValue)
                    .build());

            // Duvar Arkası Culling Toggle
            general.addEntry(entryBuilder.startBooleanToggle(Text.literal("Duvar Arkası Culling"), ModConfig.checkWallOcclusion)
                    .setDefaultValue(true)
                    .setTooltip(Text.literal("Duvar veya blok arkasında kalan (görünmeyen) varlıkları gizler."))
                    .setSaveConsumer(newValue -> ModConfig.checkWallOcclusion = newValue)
                    .build());

            // Maksimum Render Mesafesi Slider
            general.addEntry(entryBuilder.startIntSlider(Text.literal("Maksimum Culling Mesafesi (Blok)"), ModConfig.maxRenderDistance, 16, 128)
                    .setDefaultValue(64)
                    .setTooltip(Text.literal("Bu mesafeden uzak olan varlıklar ekrana çizilmez."))
                    .setSaveConsumer(newValue -> ModConfig.maxRenderDistance = newValue)
                    .build());

            builder.setSavingRunnable(ModConfig::save);

            return builder.build();
        };
    }
}
