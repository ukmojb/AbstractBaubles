package com.wdcftgg.abstractbaubles.item;

import baubles.api.BaubleType;
import com.wdcftgg.abstractbaubles.Tools;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class ItemVampireTooth extends BaseBaubleItem {

    public ItemVampireTooth() {
        super("vampire_tooth", BaubleType.BODY);
    }

    public EnumRarity getRarity(ItemStack par1ItemStack) {
        return EnumRarity.RARE;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack itemStack, World worldIn, List<String> list, ITooltipFlag flagIn) {
        list.add(I18n.format("abstractbaubles.vampire_tooth.tooltip"));
    }

    @SubscribeEvent
    public void onLivingDamage(LivingDamageEvent event) {
        //被攻击者
        EntityLivingBase entityLiving = (EntityLivingBase) event.getEntityLiving();
        //攻击者
        Entity target = event.getSource().getTrueSource();
        if (!entityLiving.world.isRemote) {
            if (entityLiving instanceof EntityPlayer) {
                EntityPlayer player = (EntityPlayer) entityLiving;
                if (Tools.playerEquippedBauble(player, ABItems.VampireTooth)) {
                    player.heal(event.getAmount() * 0.3F);
                }
            }
        }
    }
}