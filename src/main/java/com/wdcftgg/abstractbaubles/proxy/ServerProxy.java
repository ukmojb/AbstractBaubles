package com.wdcftgg.abstractbaubles.proxy;


import com.wdcftgg.abstractbaubles.init.ModSounds;
import com.wdcftgg.abstractbaubles.item.*;
import net.minecraftforge.common.MinecraftForge;

public class ServerProxy extends CommonProxy {


    public ServerProxy() {
    }

    public void onPreInit() {
        super.onPreInit();
    }

    public void onPostInit() {
        super.onPostInit();
    }

    public void onInit(){
        super.onInit();
        MinecraftForge.EVENT_BUS.register(new ModSounds());

        MinecraftForge.EVENT_BUS.register(new ItemEmbraceOfBlood());
        MinecraftForge.EVENT_BUS.register(new ItemOriginalSourceBlood());
        MinecraftForge.EVENT_BUS.register(new ItemLaurus());
        MinecraftForge.EVENT_BUS.register(new ItemMagicQuiver());
        MinecraftForge.EVENT_BUS.register(new ItemPummarola());
        MinecraftForge.EVENT_BUS.register(new ItemMindPowerPearl());
        MinecraftForge.EVENT_BUS.register(new ItemTungstenAlloyAod());
        MinecraftForge.EVENT_BUS.register(new ItemRottedSachet());
        MinecraftForge.EVENT_BUS.register(new ItemStalkerQuiver());
        MinecraftForge.EVENT_BUS.register(new ItemStalkerQuiver());
    }
}
