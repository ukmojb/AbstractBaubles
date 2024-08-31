package com.wdcftgg.abstractbaubles.item;

import baubles.api.BaubleType;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.smileycorp.raids.common.RaidsContent;

import java.util.List;
import java.util.Random;


public class ItemMemberShipCard extends BaseBaubleItem {

    public ItemMemberShipCard() {
        super("membership_card", BaubleType.TRINKET);
    }

    public EnumRarity getRarity(ItemStack par1ItemStack) {
        return EnumRarity.RARE;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack itemStack, World worldIn, List<String> list, ITooltipFlag flagIn)
    {

        if (Loader.isModLoaded("raids")) {
            list.add(I18n.format("abstractbaubles.membership_card.tooltip.0"));
        } else {
            list.add(I18n.format("abstractbaubles.membership_card.tooltip.1"));
            list.add("");

            if (GuiScreen.isShiftKeyDown()) {
                list.add(I18n.format("abstractbaubles.membership_card.tooltip.2"));
            } else {
                list.add(I18n.format("abstractbaubles.abstractbaubles.shifttooltip"));
            }
        }
    }

    @Override
    public void onWornTick(ItemStack itemstack, EntityLivingBase entityLivingBase) {
        World world = entityLivingBase.world;
        if (!world.isRemote) {
            if (entityLivingBase instanceof EntityPlayer) {
                EntityPlayer player = (EntityPlayer) entityLivingBase;
                if (Loader.isModLoaded("raids")) {
                    player.addPotionEffect(new PotionEffect(RaidsContent.HERO_OF_THE_VILLAGE,40, 4));
                }
            }
        }
    }

    @SubscribeEvent
    public void onAttackEntity(AttackEntityEvent event){
        if (!event.getEntityPlayer().world.isRemote) {
            EntityPlayer player = event.getEntityPlayer();
            Entity target = event.getTarget();
            if (target instanceof EntityVillager) {
                if (player.getHeldItemMainhand().getItem().equals(ABItems.MemberShipCard) || player.getHeldItemOffhand().getItem().equals(ABItems.MemberShipCard)) {
                    EntityVillager villager = (EntityVillager) target;
                    villager.attackEntityFrom(DamageSource.GENERIC, villager.getHealth() * 0.1F);
                    villager.knockBack(player, 5F, (double) MathHelper.sin(player.rotationYaw * 0.02F), (double)(-MathHelper.cos(player.rotationYaw * 0.02F)));

                    Random random = new Random();
                    if (random.nextInt(10) <= 2) {
                        villager.entityDropItem(new ItemStack(Items.EMERALD, random.nextInt(2) + 1), 0.1F);
                    }
                }
            }
        }
    }
}
