package com.wdcftgg.abstractbaubles.item;

import baubles.api.BaubleType;
import baubles.api.BaublesApi;
import com.wdcftgg.abstractbaubles.Tools;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.Random;

public class ItemStalkerQuiver extends BaseBaubleItem {

    public ItemStalkerQuiver() {
        super("stalker_quiver", BaubleType.BODY);
    }

    public EnumRarity getRarity(ItemStack par1ItemStack) {
        return EnumRarity.RARE;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack itemStack, World worldIn, List<String> list, ITooltipFlag flagIn)
    {
        list.add(I18n.format("abstractbaubles.stalker_quiver.tooltip"));
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
                if (Tools.playerEquippedBauble(player, ABItems.StalkerQuiver)) {
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
    public void onEntityJoinWorld(EntityJoinWorldEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof EntityArrow) {
            EntityArrow arrow = (EntityArrow) entity;
            if (arrow.shootingEntity instanceof EntityPlayer)  {
                EntityPlayer player = (EntityPlayer) arrow.shootingEntity;
                if (Tools.playerEquippedBauble(player, ABItems.StalkerQuiver)) {
                    arrow.setDamage(arrow.getDamage() * 1.15F);
                    arrow.motionX *= 1.8;
                    arrow.motionY *= 1.8;
                    arrow.motionZ *= 1.8;
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
                if (Tools.playerEquippedBauble(player, ABItems.StalkerQuiver)) {
                    if (mob.getDistance(player) > 20) {
                        mob.setAttackTarget(null);
                    }
                }
            }
        }
    }
}
