package com.wdcftgg.abstractbaubles.item.mods;

import baubles.api.BaubleType;
import com.wdcftgg.abstractbaubles.item.ABItems;
import com.wdcftgg.abstractbaubles.item.mods.BaseModBaubleItem;
import com.wdcftgg.abstractbaubles.util.Tools;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.botania.api.mana.ManaDiscountEvent;

import java.util.List;

public class ItemThirdManaEye extends BaseModBaubleItem {

    public ItemThirdManaEye() {
        super("third_mana_eye", "botania", BaubleType.CHARM);
    }

    public EnumRarity getRarity(ItemStack par1ItemStack) {
        return EnumRarity.RARE;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack itemStack, World worldIn, List<String> list, ITooltipFlag flagIn) {
        list.add(I18n.format("abstractbaubles.third_mana_eye.tooltip"));
    }

    @Optional.Method(modid = "botania")
    @SubscribeEvent
    public void onManaDiscount(ManaDiscountEvent event) {
        EntityPlayer player = event.getEntityPlayer();
        if (!player.world.isRemote) {
            if (Tools.playerEquippedBauble(player, ABItems.ThirdManaEye)) {
                event.setDiscount(event.getDiscount() + 0.2F);
            }
        }
    }

    @SubscribeEvent
    public void onLivingDamage(LivingDamageEvent event) {
        //被攻击者
        EntityLivingBase entityLiving = (EntityLivingBase) event.getEntityLiving();
        //攻击者
        Entity target = event.getSource().getTrueSource();
        if (!entityLiving.world.isRemote) {
            if (target instanceof EntityPlayer) {
                EntityPlayer player = (EntityPlayer) target;
                if (Tools.playerEquippedBauble(player, ABItems.ThirdManaEye)) {
                    if (event.getSource().isMagicDamage()) {
                        event.setAmount(event.getAmount() + 3);
                    }
                }
            }
        }
    }
}