package com.example.perfplus;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.perfplus.config.ModConfig;
import net.fabricmc.api.ModInitializer;

public class PerfPlusMod implements ModInitializer {
    public static final String MOD_ID = "perfplus";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        ModConfig.load();
        LOGGER.info("Performance Plus initialized!");
    }
}
