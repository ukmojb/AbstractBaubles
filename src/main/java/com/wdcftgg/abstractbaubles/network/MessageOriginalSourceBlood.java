package com.wdcftgg.abstractbaubles.network;

import baubles.api.BaublesApi;
import com.wdcftgg.abstractbaubles.AbstractBaubles;
import com.wdcftgg.abstractbaubles.Tools;
import com.wdcftgg.abstractbaubles.item.ABItems;
import com.wdcftgg.abstractbaubles.item.ItemOriginalSourceBlood;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.Objects;

public class MessageOriginalSourceBlood implements IMessageHandler<MessageOriginalSourceBlood, IMessage>, IMessage  {
    public boolean totemOrBoold;

    public MessageOriginalSourceBlood() {
    }

    public MessageOriginalSourceBlood( boolean totemOrBoold) {
        this.totemOrBoold = totemOrBoold;
    }


    public void fromBytes(ByteBuf buf) {
        this.totemOrBoold = buf.readBoolean();
    }

    public void toBytes(ByteBuf buf) {
        buf.writeBoolean(totemOrBoold);
    }


    public IMessage onMessage(MessageOriginalSourceBlood message, MessageContext ctx) {
        EntityPlayer player = Minecraft.getMinecraft().player;
        World world = player.world;
        if (message.totemOrBoold) {
            Minecraft.getMinecraft().effectRenderer.emitParticleAtEntity(player, EnumParticleTypes.TOTEM, 30);
            world.playSound(player.posX, player.posY, player.posZ, SoundEvents.ITEM_TOTEM_USE, player.getSoundCategory(), 1.0F, 1.0F, false);
            Minecraft.getMinecraft().entityRenderer.displayItemActivation(Items.TOTEM_OF_UNDYING.getDefaultInstance());
        } else {
            Minecraft.getMinecraft().effectRenderer.emitParticleAtEntity(player, EnumParticleTypes.TOTEM, 30);
            world.playSound(player.posX, player.posY, player.posZ, SoundEvents.ITEM_TOTEM_USE, player.getSoundCategory(), 1.0F, 1.0F, false);
            Minecraft.getMinecraft().entityRenderer.displayItemActivation(ABItems.OriginalSourceBlood.getDefaultInstance());
        }
        return null;
    }
}

