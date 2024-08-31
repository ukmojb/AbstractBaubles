package com.wdcftgg.abstractbaubles.item;

import baubles.api.BaubleType;
import baubles.api.BaublesApi;
import com.wdcftgg.abstractbaubles.util.Tools;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class ItemPenNib extends BaseBaubleItem {

    public ItemPenNib() {
        super("pen_nib", BaubleType.CHARM);
    }

    public EnumRarity getRarity(ItemStack par1ItemStack) {
        return EnumRarity.RARE;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack itemStack, World worldIn, List<String> list, ITooltipFlag flagIn) {
        list.add(I18n.format("abstractbaubles.pen_nib.tooltip"));
        list.add("");

        if (itemStack.hasTagCompound()) {
            if (itemStack.getTagCompound().hasKey("pen_nib")) {
                int hitTime = itemStack.getTagCompound().getInteger("pen_nib");
                if (hitTime >= 10) {
                    list.add(I18n.format("abstractbaubles.pen_nib.tooltip.0"));
                } else {
                    list.add(I18n.format("abstractbaubles.pen_nib.tooltip.1",10 -  hitTime));
                }
            }
        } else {
            list.add(I18n.format("abstractbaubles.pen_nib.tooltip.0"));
        }
        list.add("");

        if (GuiScreen.isShiftKeyDown()) {
            list.add(I18n.format("abstractbaubles.pen_nib.tooltip.2"));
        } else {
            list.add(I18n.format("abstractbaubles.abstractbaubles.shifttooltip"));
        }
    }

    @Override
    public void onWornTick(ItemStack itemstack, EntityLivingBase entityLivingBase) {
        World world = entityLivingBase.world;
        if (!world.isRemote) {
            if (entityLivingBase instanceof EntityPlayer) {
                EntityPlayer player = (EntityPlayer) entityLivingBase;
                if (itemstack.hasTagCompound()) {
                    if (itemstack.getTagCompound().hasKey("pen_nib")) {
                        int cool = itemstack.getTagCompound().getInteger("pen_nib");
                        if (cool < 200) {
                            itemstack.getTagCompound().setInteger("pen_nib", cool + 1);
                        }
                    }
                } else {
                    NBTTagCompound nbtTagCompound = new NBTTagCompound();
                    nbtTagCompound.setInteger("pen_nib", 200);
                    if (itemstack.getTagCompound() != null) {
                        nbtTagCompound.merge(itemstack.getTagCompound());
                    }
                    itemstack.setTagCompound(nbtTagCompound);
                }
            }
        }

    }

    @SubscribeEvent
    public void onLivingDamage(LivingDamageEvent event) {
        //被攻击者
        EntityLivingBase entityLiving = (EntityLivingBase) event.getEntityLiving();
        //攻击者
        Entity entity = event.getSource().getTrueSource();
        if (!entityLiving.world.isRemote) {
            if (entity instanceof EntityPlayer) {
                EntityPlayer player = (EntityPlayer) entity;
                if (Tools.playerEquippedBauble(player, ABItems.PenNib)) {
                    ItemStack pennib = BaublesApi.getBaublesHandler(player).getStackInSlot(BaublesApi.isBaubleEquipped(player, ABItems.PenNib));
                    if (pennib.hasTagCompound()) {
                        if (pennib.getTagCompound().hasKey("pen_nib")) {
                            int cool = pennib.getTagCompound().getInteger("pen_nib");
                            if (cool >= 10) {
                                pennib.getTagCompound().setInteger("pen_nib", 0);
                            } else {
                                pennib.getTagCompound().setInteger("pen_nib", cool + 1);
                            }
                            BaublesApi.getBaublesHandler(player).setStackInSlot(BaublesApi.isBaubleEquipped(player, ABItems.PenNib), pennib);
                            event.setAmount(event.getAmount() * 2.0F);
                        }
                    }
                }
            }
        }
    }
}