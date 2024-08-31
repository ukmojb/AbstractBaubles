package com.wdcftgg.abstractbaubles.item;

import baubles.api.BaubleType;
import com.wdcftgg.abstractbaubles.util.Tools;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingHealEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.UUID;

@Mod.EventBusSubscriber
public class ItemEmptyHeart extends BaseBaubleItem {

    public final UUID uuid = UUID.fromString("150A67E9-0455-0AE2-87C3-4D659809FBBE");

    public ItemEmptyHeart() {
        super("empty_heart", BaubleType.AMULET);
    }

    public EnumRarity getRarity(ItemStack par1ItemStack) {
        return EnumRarity.RARE;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack is, World worldIn, List<String> list, ITooltipFlag flagIn)
    {
        list.add(I18n.format("abstractbaubles.empty_heart.tooltip"));
    }

    @Override
    public void onWornTick(ItemStack itemstack, EntityLivingBase player) {
        World world = player.world;
        if (!world.isRemote) {
            AttributeModifier attributeModifier = new AttributeModifier(uuid, this.registryKey, -0.5, 1);
            if (player.getAttributeMap().getAttributeInstanceByName("generic.maxHealth").getModifier(uuid) == null) {
                player.getAttributeMap().getAttributeInstanceByName("generic.maxHealth").applyModifier(attributeModifier);
            }
        }

    }

    @Override
    public void onEquipped(ItemStack itemstack, EntityLivingBase player) {
        World world = player.world;
        if (!world.isRemote) {
            AttributeModifier attributeModifier = new AttributeModifier(uuid, this.registryKey, -0.5, 1);
            player.getAttributeMap().getAttributeInstanceByName("generic.maxHealth").applyModifier(attributeModifier);
        }
    }

    @Override
    public void onUnequipped(ItemStack itemstack, EntityLivingBase player) {
        World world = player.world;
        if (!world.isRemote) {
            AttributeModifier attributeModifier = new AttributeModifier(uuid, this.registryKey, -0.5, 1);
            player.getAttributeMap().getAttributeInstanceByName("generic.maxHealth").removeModifier(attributeModifier);
            player.attackEntityFrom(DamageSource.GENERIC, 0.01F);
        }
    }

    @SubscribeEvent
    public void onEntityLivingHeal(LivingHealEvent event) {
        if (!event.getEntityLiving().world.isRemote) {
            if (event.getEntityLiving() instanceof EntityPlayer) {
                EntityPlayer player = (EntityPlayer) event.getEntityLiving();
                if (Tools.playerEquippedBauble(player, this)) {
                    event.setAmount((float) (event.getAmount() * 1.6));
                }
            }
        }
    }
}
