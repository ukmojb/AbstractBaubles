package com.wdcftgg.abstractbaubles.item;

import baubles.api.BaubleType;
import com.wdcftgg.abstractbaubles.Tools;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.Random;

public class ItemMagicQuiver extends BaseBaubleItem {

    public ItemMagicQuiver() {
        super("magic_quiver", BaubleType.BODY);
    }

    public EnumRarity getRarity(ItemStack par1ItemStack) {
        return EnumRarity.RARE;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack itemStack, World worldIn, List<String> list, ITooltipFlag flagIn)
    {
        list.add(I18n.format("abstractbaubles.magic_quiver.tooltip"));
    }


    @SubscribeEvent
    public void onEntityJoinWorld(EntityJoinWorldEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof EntityArrow) {
            EntityArrow arrow = (EntityArrow) entity;
            if (arrow.shootingEntity instanceof EntityPlayer)  {
                EntityPlayer player = (EntityPlayer) arrow.shootingEntity;
                if (Tools.playerEquippedBauble(player, ABItems.MagicQuiver)) {
                    arrow.setDamage(arrow.getDamage() * 1.05F);
                    arrow.motionX *= 1.3;
                    arrow.motionY *= 1.3;
                    arrow.motionZ *= 1.3;
                }
            }
        }
    }
}
