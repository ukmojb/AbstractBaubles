package com.wdcftgg.abstractbaubles.network;

import com.wdcftgg.abstractbaubles.init.ModSounds;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageTungstenAlloyAodTo implements IMessageHandler<MessageTungstenAlloyAodTo, IMessage>, IMessage  {

    private String soundCategory;

    public MessageTungstenAlloyAodTo() {
    }

    public MessageTungstenAlloyAodTo(SoundCategory soundCategory) {
        this.soundCategory = soundCategory.getName();
    }


    public void fromBytes(ByteBuf buf) {
        this.soundCategory = ByteBufUtils.readUTF8String(buf);
    }

    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, this.soundCategory);
    }


    public IMessage onMessage(MessageTungstenAlloyAodTo message, MessageContext ctx) {
//        ItemTungstenAlloyAod.maxVolume = SoundCategory.getByName(message.soundCategory);
        EntityPlayer player = ctx.getServerHandler().player;
        player.world.playSound(null, player.posX, player.posY, player.posZ, ModSounds.TungstenAlloyAod, SoundCategory.getByName(message.soundCategory), 4f, 1f);
        return null;
    }
}
