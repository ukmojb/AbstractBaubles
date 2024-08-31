package com.wdcftgg.abstractbaubles.proxy;


import com.wdcftgg.abstractbaubles.init.ModSounds;
import com.wdcftgg.abstractbaubles.item.*;
import com.wdcftgg.abstractbaubles.item.mods.ItemThirdManaEye;
import com.wdcftgg.abstractbaubles.util.BattleTime;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Loader;

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
        MinecraftForge.EVENT_BUS.register(new BattleTime());
        MinecraftForge.EVENT_BUS.register(new ModSounds());

        MinecraftForge.EVENT_BUS.register(new ItemEmbraceOfBlood());
        MinecraftForge.EVENT_BUS.register(new ItemEmptyHeart());
        MinecraftForge.EVENT_BUS.register(new ItemGiantHand());
        MinecraftForge.EVENT_BUS.register(new ItemLaurus());
        MinecraftForge.EVENT_BUS.register(new ItemLizardTail());
        MinecraftForge.EVENT_BUS.register(new ItemMagicFlower());
        MinecraftForge.EVENT_BUS.register(new ItemMagicQuiver());
        MinecraftForge.EVENT_BUS.register(new ItemMemberShipCard());
        MinecraftForge.EVENT_BUS.register(new ItemMindPowerPearl());
        MinecraftForge.EVENT_BUS.register(new ItemOriginalSourceBlood());
        MinecraftForge.EVENT_BUS.register(new ItemPenNib());
        MinecraftForge.EVENT_BUS.register(new ItemPummarola());
        MinecraftForge.EVENT_BUS.register(new ItemRottedSachet());
        MinecraftForge.EVENT_BUS.register(new ItemSharkToothNecklace());
        MinecraftForge.EVENT_BUS.register(new ItemThirdManaEye());
        MinecraftForge.EVENT_BUS.register(new ItemStalkerQuiver());
        MinecraftForge.EVENT_BUS.register(new ItemTungstenAlloyAod());
        MinecraftForge.EVENT_BUS.register(new ItemVampireTooth());

        if (Loader.isModLoaded("botania")) {
            MinecraftForge.EVENT_BUS.register(new ItemThirdManaEye());
        }
    }
}
