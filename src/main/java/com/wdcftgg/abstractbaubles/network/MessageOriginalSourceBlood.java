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

public class MessageOriginalSourceBlood implements IMessageHandler<MessageOriginalSourceBlood, IMessage>, IMessage  {
    public boolean totemOrBoold;
    public int mode;
    public ItemStack itemStack;

    public MessageOriginalSourceBlood() {
    }

    public MessageOriginalSourceBlood(boolean totemOrBoold, int mode) {
        this.totemOrBoold = totemOrBoold;
        this.mode = mode;
    }

    public MessageOriginalSourceBlood(ItemStack itemStack, int mode) {
        this.itemStack = itemStack;
        this.mode = mode;
    }


    public void fromBytes(ByteBuf buf) {
        this.totemOrBoold = buf.readBoolean();
        this.itemStack = ByteBufUtils.readItemStack(buf);
        this.mode = buf.readInt();
    }

    public void toBytes(ByteBuf buf) {
        buf.writeBoolean(totemOrBoold);
        ByteBufUtils.writeItemStack(buf, itemStack);
        buf.writeInt(mode);
    }


    public IMessage onMessage(MessageOriginalSourceBlood message, MessageContext ctx) {
        EntityPlayer player = Minecraft.getMinecraft().player;
        World world = player.world;
        if (message.mode == 0) {
            if (message.totemOrBoold) {
                Minecraft.getMinecraft().effectRenderer.emitParticleAtEntity(player, EnumParticleTypes.TOTEM, 30);
                world.playSound(player.posX, player.posY, player.posZ, SoundEvents.ITEM_TOTEM_USE, player.getSoundCategory(), 1.0F, 1.0F, false);
                Minecraft.getMinecraft().entityRenderer.displayItemActivation(Items.TOTEM_OF_UNDYING.getDefaultInstance());
            } else {
                Minecraft.getMinecraft().effectRenderer.emitParticleAtEntity(player, EnumParticleTypes.TOTEM, 30);
                world.playSound(player.posX, player.posY, player.posZ, SoundEvents.ITEM_TOTEM_USE, player.getSoundCategory(), 1.0F, 1.0F, false);
                Minecraft.getMinecraft().entityRenderer.displayItemActivation(ABItems.OriginalSourceBlood.getDefaultInstance());
            }
        } else if (message.mode == 1) {
            player.getCooldownTracker().setCooldown(message.itemStack.getItem(), 5);
            world.playSound(player.posX, player.posY, player.posZ, SoundEvents.ENTITY_GENERIC_EAT, player.getSoundCategory(), 1.0F, 1.0F, false);
        }

        return null;
    }
}

