package com.wdcftgg.abstractbaubles.item;

import baubles.api.BaubleType;
import baubles.api.BaublesApi;
import com.wdcftgg.abstractbaubles.Tools;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingHealEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.UUID;

public class ItemLaurus extends BaseBaubleItem {

    public ItemLaurus() {
        super("laurus", BaubleType.AMULET);
    }

    public EnumRarity getRarity(ItemStack par1ItemStack) {
        return EnumRarity.RARE;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack itemStack, World worldIn, List<String> list, ITooltipFlag flagIn)
    {
        if (itemStack.hasTagCompound()) {
            if (itemStack.getTagCompound().hasKey("laurus")) {
                int cool = itemStack.getTagCompound().getInteger("laurus");
                list.add(I18n.format("abstractbaubles.laurus.tooltip.0", (int) Math.floor((double) cool / 200)));
                if ((int) Math.floor((double) cool / 200) >= 2) {
                    list.add(I18n.format("abstractbaubles.laurus.tooltip.1", (int) ((200 - Math.floor(cool % 200)) / 20)));
                }
            }
        } else {
            list.add(I18n.format("abstractbaubles.laurus.tooltip.2"));
        }
    }

    @Override
    public void onWornTick(ItemStack itemstack, EntityLivingBase entityLivingBase) {
        World world = entityLivingBase.world;
        if (!world.isRemote) {
            if (entityLivingBase instanceof EntityPlayer) {
                EntityPlayer player = (EntityPlayer) entityLivingBase;
                if (itemstack.hasTagCompound()) {
                    if (itemstack.getTagCompound().hasKey("laurus")) {
                        int cool = itemstack.getTagCompound().getInteger("laurus");
                        if (cool < 400) {
                            itemstack.getTagCompound().setInteger("laurus", cool + 1);
                        }
                    }
                    BaublesApi.getBaublesHandler(player).setStackInSlot(BaublesApi.isBaubleEquipped(player, ABItems.Laurus), itemstack);
                } else {
                    NBTTagCompound nbtTagCompound = new NBTTagCompound();
                    nbtTagCompound.setInteger("laurus", 0);
                    if (itemstack.getTagCompound() != null) {
                        nbtTagCompound.merge(itemstack.getTagCompound());
                    }
                    itemstack.setTagCompound(nbtTagCompound);
                }
            }
        }

    }

    @SubscribeEvent
    public void onLivingAttack(LivingAttackEvent event) {
        if (!event.getEntityLiving().world.isRemote) {
            if (event.getEntityLiving() instanceof EntityPlayer) {
                EntityPlayer player = (EntityPlayer) event.getEntityLiving();
                if (Tools.playerEquippedBauble(player, ABItems.Laurus)) {
                    ItemStack laurus = BaublesApi.getBaublesHandler(player).getStackInSlot(BaublesApi.isBaubleEquipped(player, ABItems.Laurus));
                    if (laurus.hasTagCompound()) {
                        if (laurus.getTagCompound().hasKey("laurus")) {
                            int cool = laurus.getTagCompound().getInteger("laurus");
                            if ((cool - 200) >= 0) {
                                laurus.getTagCompound().setInteger("laurus", cool - 200);
                            }
                            BaublesApi.getBaublesHandler(player).setStackInSlot(BaublesApi.isBaubleEquipped(player, ABItems.Laurus), laurus);
                            player.world.playSound(player, player.getPosition(), SoundEvents.ITEM_SHIELD_BLOCK, SoundCategory.NEUTRAL, 1.0F, 0.8F + player.world.rand.nextFloat() * 0.4F);
                        }
                    }
                }
            }
        }
    }
}
