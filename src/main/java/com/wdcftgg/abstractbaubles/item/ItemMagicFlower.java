package com.wdcftgg.abstractbaubles.item;

import baubles.api.BaubleType;
import com.wdcftgg.abstractbaubles.util.Tools;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingHealEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class ItemMagicFlower extends BaseBaubleItem {

    public ItemMagicFlower() {
        super("magic_flower", BaubleType.HEAD);
    }

    public EnumRarity getRarity(ItemStack par1ItemStack) {
        return EnumRarity.RARE;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack itemStack, World worldIn, List<String> list, ITooltipFlag flagIn)
    {
        list.add(I18n.format("abstractbaubles.magic_flower.tooltip"));
    }

    @SubscribeEvent
    public void onLivingHeal(LivingHealEvent event) {
        if (!event.getEntityLiving().world.isRemote) {
            if (event.getEntityLiving() instanceof EntityPlayer) {
                EntityPlayer player = (EntityPlayer) event.getEntityLiving();
                if (Tools.playerEquippedBauble(player, ABItems.MagicQuiver)) {
                    event.setAmount(event.getAmount() * 1.5F);
                }
            }
        }
    }
}