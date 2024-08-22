package com.wdcftgg.abstractbaubles;

import com.wdcftgg.abstractbaubles.item.ABItems;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ABCreativeTab {
    public static final CreativeTabs AbstractBaublesTab = new CreativeTabs(CreativeTabs.getNextID(), "abstractbaublestab") {
        @SideOnly(Side.CLIENT)
        public ItemStack createIcon() {
            return new ItemStack(ABItems.OriginalSourceBlood);
        }
    };
}
