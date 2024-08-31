package com.wdcftgg.abstractbaubles.config;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.FMLLog;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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



    public static boolean foodInMap(ItemStack itemStack, Map<ItemStack, Integer> map) {
        boolean pass = false;
        List<ItemStack> itemStackList = new ArrayList<>();
        itemStackList.addAll(map.keySet());
        List<Integer> integerList = new ArrayList<>();
        integerList.addAll(map.values());

        for (int i = 0; i < map.size(); i++) {
            if (itemStackList.get(i).getItem().equals(itemStack.getItem())
                    && itemStackList.get(i).getMetadata() == itemStack.getMetadata()) {
                pass = true;
                break;
            }
        }

        return pass;
    }

    public static int foodLevelInMap(ItemStack itemStack, Map<ItemStack, Integer> map) {
        int foodLevel = 0;
        List<ItemStack> itemStackList = new ArrayList<>();
        itemStackList.addAll(map.keySet());
        List<Integer> integerList = new ArrayList<>();
        integerList.addAll(map.values());

        for (int i = 0; i < map.size(); i++) {
            if (itemStackList.get(i).getItem().equals(itemStack.getItem())
                    && itemStackList.get(i).getMetadata() == itemStack.getMetadata()) {
                foodLevel = integerList.get(i);
                break;
            }
        }

        return foodLevel;
    }
}