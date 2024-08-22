package com.wdcftgg.abstractbaubles.config;

import com.wdcftgg.abstractbaubles.item.ABItems;
import net.minecraftforge.common.config.Configuration;

import java.io.File;


public class Config {

    private static final String Baubles = "baubles";

    public static String[] BloodFoodList = new String[]{
            "minecraft:golden_apple;1 -> 3"
    };

    public Config() {
    }

    public static void init(File configurationFile) {
        Configuration config = new Configuration(configurationFile);


        try {
            config.load();
            Init(config);
        } catch (Exception var11) {
        } finally {
            config.save();
        }

    }


    private static void Init(Configuration config) {

        BloodFoodList = config.get("EmbraceOfBlood", "BloodFoodList", BloodFoodList, "When deciding to wear the Embrace of Blood, the foods that can be eaten and the amount of hunger restored. \n Example \n[item];[meta] -> [foodLevel]\n minecraft:golden_apple;1 -> 3").getStringList();

    }
}