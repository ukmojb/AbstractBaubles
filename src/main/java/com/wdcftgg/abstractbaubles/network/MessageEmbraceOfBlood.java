package com.wdcftgg.abstractbaubles.network;

import com.wdcftgg.abstractbaubles.item.ABItems;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageEmbraceOfBlood implements IMessageHandler<MessageEmbraceOfBlood, IMessage>, IMessage  {

    public ItemStack itemStack;

    public MessageEmbraceOfBlood() {
    }

    public MessageEmbraceOfBlood(ItemStack itemStack) {
        this.itemStack = itemStack;
    }


    public void fromBytes(ByteBuf buf) {
        this.itemStack = ByteBufUtils.readItemStack(buf);
    }

    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeItemStack(buf, itemStack);
    }


    public IMessage onMessage(MessageEmbraceOfBlood message, MessageContext ctx) {
        EntityPlayer player = Minecraft.getMinecraft().player;
        World world = player.world;

        player.getCooldownTracker().setCooldown(message.itemStack.getItem(), 5);
        world.playSound(player.posX, player.posY, player.posZ, SoundEvents.ENTITY_GENERIC_EAT, player.getSoundCategory(), 1.0F, 1.0F, false);


        return null;
    }
}

