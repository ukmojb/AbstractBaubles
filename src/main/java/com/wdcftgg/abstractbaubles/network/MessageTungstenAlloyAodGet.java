package com.wdcftgg.abstractbaubles.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageTungstenAlloyAodGet implements IMessageHandler<MessageTungstenAlloyAodGet, IMessage>, IMessage  {

    public MessageTungstenAlloyAodGet() {
    }


    public void fromBytes(ByteBuf buf) {
    }

    public void toBytes(ByteBuf buf) {
    }


    public IMessage onMessage(MessageTungstenAlloyAodGet message, MessageContext ctx) {
        EntityPlayer player = Minecraft.getMinecraft().player;
        SoundCategory maxVolume = SoundCategory.MUSIC;
        for (SoundCategory soundCategory : SoundCategory.values()) {
            maxVolume = (Minecraft.getMinecraft().gameSettings.getSoundLevel(soundCategory) > Minecraft.getMinecraft().gameSettings.getSoundLevel(maxVolume) ? soundCategory : maxVolume);
        }
        System.out.println(maxVolume.getName());
        PacketHandler.INSTANCE.sendToServer(new MessageTungstenAlloyAodTo(maxVolume));
        return null;
    }
}
