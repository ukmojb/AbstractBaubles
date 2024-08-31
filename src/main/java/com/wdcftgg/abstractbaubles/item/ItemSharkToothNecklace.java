package com.wdcftgg.abstractbaubles.item;

import baubles.api.BaubleType;
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
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class ItemSharkToothNecklace extends BaseBaubleItem {

    public ItemSharkToothNecklace() {
        super("shark_tooth_necklace", BaubleType.CHARM);
    }

    public EnumRarity getRarity(ItemStack par1ItemStack) {
        return EnumRarity.RARE;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack itemStack, World worldIn, List<String> list, ITooltipFlag flagIn) {
        list.add(I18n.format("abstractbaubles.shark_tooth_necklace.tooltip"));
    }

    @SubscribeEvent
    public void onLivingDamage(LivingDamageEvent event) {
        //被攻击者
        EntityLivingBase entityLiving = (EntityLivingBase) event.getEntityLiving();
        //攻击者
        Entity target = event.getSource().getTrueSource();
        if (!entityLiving.world.isRemote) {
            if (entityLiving instanceof EntityPlayer && target instanceof EntityLivingBase) {
                EntityPlayer player = (EntityPlayer) entityLiving;
                EntityLivingBase entityLivingBase = (EntityLivingBase) target;
                if (Tools.playerEquippedBauble(player, ABItems.SharkToothNecklace)) {
                    int armor = entityLivingBase.arrowHitTimer;
                    float damage = event.getAmount();
                    float origin_damage = (float) (armor + Math.sqrt(0.0004F * armor * armor - 0.08F + 0.08F * damage));
                    float newreduction = (1.0F - 0.04F * (Math.max(0, (armor - 6.0F)) - origin_damage * 0.5F));
                    float newdamage = newreduction * origin_damage;
                    event.setAmount(newdamage);
                }
            }
        }
    }
}