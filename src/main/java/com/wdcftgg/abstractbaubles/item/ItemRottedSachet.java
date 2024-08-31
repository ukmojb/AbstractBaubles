package com.wdcftgg.abstractbaubles.item;

import baubles.api.BaubleType;
import com.wdcftgg.abstractbaubles.util.Tools;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.Random;

public class ItemRottedSachet extends BaseBaubleItem {

    public ItemRottedSachet() {
        super("rotted_sachet", BaubleType.BODY);
    }

    public EnumRarity getRarity(ItemStack par1ItemStack) {
        return EnumRarity.RARE;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack itemStack, World worldIn, List<String> list, ITooltipFlag flagIn)
    {
        list.add(I18n.format("abstractbaubles.rotted_sachet.tooltip"));
        list.add("");

        if (GuiScreen.isShiftKeyDown()) {
            list.add(I18n.format("abstractbaubles.rotted_sachet.tooltip.0"));
            list.add(I18n.format("abstractbaubles.rotted_sachet.tooltip.1"));
        } else {
            list.add(I18n.format("abstractbaubles.abstractbaubles.shifttooltip"));
        }
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
                if (Tools.playerEquippedBauble(player, ABItems.RottedSachet)) {
                    event.setAmount(event.getAmount() * 1.05F);
                    Random random = new Random();
                    if ((random.nextInt(100) + 1) <= 5) {
                        event.setAmount(event.getAmount() * 1.5F);
                        if (target != null)
                            player.onCriticalHit(target);
                    }
                }
            }
        }
    }
    @SubscribeEvent
    public void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        EntityLivingBase entityLivingBase = event.getEntityLiving();
        if (entityLivingBase instanceof EntityMob) {
            EntityMob mob = (EntityMob) entityLivingBase;
            if (mob.getAttackTarget() instanceof EntityPlayer) {
                EntityPlayer player = (EntityPlayer) mob.getAttackTarget();
                if (Tools.playerEquippedBauble(player, ABItems.RottedSachet)) {
                    if (mob.getDistance(player) > 20) {
                        mob.setAttackTarget(null);
                    }
                }
            }
        }
    }
}
