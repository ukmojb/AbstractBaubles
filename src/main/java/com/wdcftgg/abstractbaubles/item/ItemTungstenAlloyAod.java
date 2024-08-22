package com.wdcftgg.abstractbaubles.item;

import baubles.api.BaubleType;
import com.wdcftgg.abstractbaubles.Tools;
import com.wdcftgg.abstractbaubles.init.ModSounds;
import com.wdcftgg.abstractbaubles.network.MessageTungstenAlloyAodGet;
import com.wdcftgg.abstractbaubles.network.PacketHandler;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class ItemTungstenAlloyAod extends BaseBaubleItem {

    public ItemTungstenAlloyAod() {
        super("tungsten_alloy_rod", BaubleType.TRINKET);
    }

    public EnumRarity getRarity(ItemStack par1ItemStack) {
        return EnumRarity.RARE;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack itemStack, World worldIn, List<String> list, ITooltipFlag flagIn)
    {
        list.add(I18n.format("abstractbaubles.tungsten_alloy_rod.tooltip"));
    }

    @SubscribeEvent
    public void onLivingAttack(LivingDamageEvent event) {
        if (!event.getEntityLiving().world.isRemote) {
            if (event.getEntityLiving() instanceof EntityPlayer) {
                EntityPlayer player = (EntityPlayer) event.getEntityLiving();
                if (Tools.playerEquippedBauble(player, ABItems.TungstenAlloyAod)) {
                    event.setAmount(event.getAmount() * 0.9F);
                    PacketHandler.INSTANCE.sendTo(new MessageTungstenAlloyAodGet(), (EntityPlayerMP) player);
                }
            }
        }
    }
}
