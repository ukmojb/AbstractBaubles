package com.wdcftgg.abstractbaubles.mixins.vanilla;

import com.wdcftgg.abstractbaubles.item.ABItems;
import com.wdcftgg.abstractbaubles.util.ABDamageSource;
import com.wdcftgg.abstractbaubles.util.Tools;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.stats.StatList;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import tfar.classicbar.ModUtils;

import java.util.Random;

@Mixin(ItemFood.class)
public class MixinItemFood {

    @Shadow
    private PotionEffect potionId;

    @Shadow
    private float potionEffectProbability;

//    @Shadow
//    private final int healAmount;


    /**
     * @author
     * @reason
     */
    @Overwrite
    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving)
    {
        if (entityLiving instanceof EntityPlayer)
        {
            EntityPlayer entityplayer = (EntityPlayer)entityLiving;
            worldIn.playSound((EntityPlayer)null, entityplayer.posX, entityplayer.posY, entityplayer.posZ, SoundEvents.ENTITY_PLAYER_BURP, SoundCategory.PLAYERS, 0.5F, worldIn.rand.nextFloat() * 0.1F + 0.9F);
                this.onFoodEaten(stack, worldIn, entityplayer);

            if (!worldIn.isRemote) {
                if (!(Tools.playerEquippedBauble(entityplayer, ABItems.EmbraceOfBlood)
                        || Tools.playerEquippedBauble(entityplayer, ABItems.OriginalSourceBlood)))
                {
                    entityplayer.getFoodStats().addStats((ItemFood) stack.getItem(), stack);
                    entityplayer.addStat(StatList.getObjectUseStats((ItemFood) stack.getItem()));

                    if (entityplayer instanceof EntityPlayerMP) {
                        CriteriaTriggers.CONSUME_ITEM.trigger((EntityPlayerMP) entityplayer, stack);
                    }
                }
            }
        }

        stack.shrink(1);
        return stack;
    }

    @Unique
    private void onFoodEaten(ItemStack stack, World worldIn, EntityPlayer player)
    {
        if (!worldIn.isRemote && this.potionId != null && worldIn.rand.nextFloat() < this.potionEffectProbability)
        {
            player.addPotionEffect(new PotionEffect(this.potionId));
        }
    }
}
