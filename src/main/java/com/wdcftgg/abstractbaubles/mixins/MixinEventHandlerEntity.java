package com.wdcftgg.abstractbaubles.mixins;

import baubles.api.BaublesApi;
import baubles.api.IBauble;
import baubles.api.cap.BaubleItem;
import baubles.api.cap.IBaublesItemHandler;
import baubles.common.event.EventHandlerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(value = EventHandlerEntity.class, remap = false)
public class MixinEventHandlerEntity {

    /**
     * @author
     * @reason
     */
    @Overwrite
    public void dropItemsAt(EntityPlayer player, List<EntityItem> drops, Entity e) {
        IBaublesItemHandler baubles = BaublesApi.getBaublesHandler(player);

        for(int i = 0; i < baubles.getSlots(); ++i) {
            if (baubles.getStackInSlot(i) != null && !baubles.getStackInSlot(i).isEmpty()) {
                ItemStack bauble = baubles.getStackInSlot(i);
                boolean shouldDrop = true;
                if (bauble.getItem() instanceof IBauble) {
                    IBauble bauble1 = (IBauble) bauble.getItem();
                    if (!bauble1.canUnequip(bauble, player)) {
                        shouldDrop = false;
                    }
                }

                if (shouldDrop) {
                    EntityItem ei = new EntityItem(e.world, e.posX, e.posY + (double)e.getEyeHeight(), e.posZ, baubles.getStackInSlot(i).copy());
                    ei.setPickupDelay(40);
                    float f1 = e.world.rand.nextFloat() * 0.5F;
                    float f2 = e.world.rand.nextFloat() * 3.1415927F * 2.0F;
                    ei.motionX = (double)(-MathHelper.sin(f2) * f1);
                    ei.motionZ = (double)(MathHelper.cos(f2) * f1);
                    ei.motionY = 0.20000000298023224;
                    drops.add(ei);
                    baubles.setStackInSlot(i, ItemStack.EMPTY);
                }
            }
        }

    }
}
