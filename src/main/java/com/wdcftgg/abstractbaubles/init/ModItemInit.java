package com.wdcftgg.abstractbaubles.init;

import com.wdcftgg.abstractbaubles.item.ABItems;
import com.wdcftgg.abstractbaubles.util.IHasModel;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@EventBusSubscriber
public class ModItemInit {
    @SubscribeEvent
    public static void onItemRegister(RegistryEvent.Register<Item> event)
    {
        event.getRegistry().registerAll(ABItems.ITEMS.toArray(new Item[0]));
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void onModelRegister(ModelRegistryEvent event)
    {
        for(Item item : ABItems.ITEMS)
        {
            if (item instanceof IHasModel)
            {
                ((IHasModel)item).registerModels();
            }
        }

    }
}
