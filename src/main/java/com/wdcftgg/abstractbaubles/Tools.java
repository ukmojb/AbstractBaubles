package com.wdcftgg.abstractbaubles;

import baubles.api.BaublesApi;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.Item;

public class Tools {

    public static boolean playerEquippedBauble(EntityPlayer player, Item bauble) {
        if (!player.world.isRemote) {
            return BaublesApi.isBaubleEquipped(player, bauble) != -1;
        }
        return false;
    }

    public static boolean inventoryHasItem(IInventory inventory, Item item) {
        for (int i = 0; i < inventory.getSizeInventory(); i++) {
            if (inventory.getStackInSlot(i).getItem().equals(Items.AIR)) continue;
            if (item.equals(inventory.getStackInSlot(i).getItem())) {
                return true;
            }
        }
        return false;
    }
}
