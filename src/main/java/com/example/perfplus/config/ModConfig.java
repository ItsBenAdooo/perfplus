package com.example.perfplus.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ModConfig {

    private static final File CONFIG_FILE = FabricLoader.getInstance()
            .getConfigDir()
            .resolve("perfplus.json")
            .toFile();

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    // Ayar Değişkenleri
    public static boolean enabled = true;
    public static boolean checkWallOcclusion = true; // Duvar arkası culling ayarı
    public static int maxRenderDistance = 64;

    // Ayarları dosyadan oku
    public static void load() {
        if (!CONFIG_FILE.exists()) {
            save();
            return;
        }

        try (FileReader reader = new FileReader(CONFIG_FILE)) {
            ConfigData data = GSON.fromJson(reader, ConfigData.class);
            if (data != null) {
                enabled = data.enabled;
                checkWallOcclusion = data.checkWallOcclusion;
                maxRenderDistance = data.maxRenderDistance;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Ayarları dosyaya kaydet
    public static void save() {
        try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
            ConfigData data = new ConfigData();
            data.enabled = enabled;
            data.checkWallOcclusion = checkWallOcclusion;
            data.maxRenderDistance = maxRenderDistance;
            GSON.toJson(data, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class ConfigData {
        boolean enabled = true;
        boolean checkWallOcclusion = true;
        int maxRenderDistance = 64;
    }
}
