package com.wdcftgg.abstractbaubles.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class BattleTime {

    @SubscribeEvent
    public void onLivingDamage(LivingDamageEvent event) {
        //被攻击者
        EntityLivingBase entityLiving = (EntityLivingBase) event.getEntityLiving();
        //攻击者
        Entity target = event.getSource().getTrueSource();
        if (!entityLiving.world.isRemote) {
            if (entityLiving instanceof EntityPlayer) {
                EntityPlayer player = (EntityPlayer) entityLiving;
                player.getEntityData().setInteger("battleTime", 200);
            }
            if (target instanceof EntityPlayer) {
                EntityPlayer player = (EntityPlayer) target;
                player.getEntityData().setInteger("battleTime", 200);
            }
        }
    }

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        EntityPlayer player = event.player;
        if (!player.world.isRemote) {
            if (player.getEntityData().hasKey("battleTime")) {
                int data = player.getEntityData().getInteger("battleTime");
                if (data > 0) data--;
                player.getEntityData().setInteger("battleTime", data);
            }
        }
    }

    public static boolean isBattle(EntityPlayer player) {
        if (player.getEntityData().hasKey("battleTime")) {
            int data = player.getEntityData().getInteger("battleTime");
            return data > 0;
        }
        return false;
    }

}
