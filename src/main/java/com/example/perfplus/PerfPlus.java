package com.example.perfplus;

import com.example.perfplus.config.ModConfig;
import net.fabricmc.api.ModInitializer;

public class PerfPlus implements ModInitializer {

    @Override
    public void onInitialize() {
        // Mod başlatıldığında konfigürasyon dosyalarını yükle
        ModConfig.load();
    }
}
