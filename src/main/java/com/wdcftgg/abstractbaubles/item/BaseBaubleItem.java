package com.wdcftgg.abstractbaubles.item;

import baubles.api.BaubleType;
import baubles.api.IBauble;
import com.wdcftgg.abstractbaubles.ABCreativeTab;
import com.wdcftgg.abstractbaubles.AbstractBaubles;
import com.wdcftgg.abstractbaubles.util.IHasModel;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.Objects;


@MethodsReturnNonnullByDefault
public class BaseBaubleItem extends Item implements IBauble, IHasModel {
    public final BaubleType type;

    public final String registryKey;

    public BaseBaubleItem(String registryKey, BaubleType type) {
        this.type = type;
        this.registryKey = registryKey;
        this.setMaxStackSize(1);
        this.setHasSubtypes(true);
        this.setMaxDamage(0);
        this.setCreativeTab(ABCreativeTab.AbstractBaublesTab);
        this.setTranslationKey(AbstractBaubles.MODID + "." + registryKey);
        this.setRegistryName(registryKey);
        ABItems.ITEMS.add(this);
    }

    public BaubleType getBaubleType(ItemStack itemstack) {
        return type;
    }

    public void onWornTick(ItemStack itemstack, EntityLivingBase player) {

    }


    public void onEquipped(ItemStack itemstack, EntityLivingBase player) {

    }

    public void onUnequipped(ItemStack itemstack, EntityLivingBase player) {

    }

    @Override
    public void registerModels()
    {
        AbstractBaubles.proxy.registerItemRenderer(this, 0, "inventory");
    }

    public Item getThisItem() {
        return Objects.requireNonNull(Item.getByNameOrId(AbstractBaubles.MODID + ":" + this.registryKey));
    }
}

