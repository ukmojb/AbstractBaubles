package com.wdcftgg.abstractbaubles.item;

import baubles.api.BaubleType;
import baubles.api.BaublesApi;
import com.wdcftgg.abstractbaubles.util.Tools;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.Random;

public class ItemLizardTail extends BaseBaubleItem {

    public ItemLizardTail() {
        super("lizard_tail", BaubleType.AMULET);
    }

    public EnumRarity getRarity(ItemStack par1ItemStack) {
        return EnumRarity.RARE;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack itemStack, World world, List<String> list, ITooltipFlag flagIn)
    {
        list.add(I18n.format("abstractbaubles.lizard_tail.tooltip"));
        list.add("");

        if (itemStack.hasTagCompound()) {
            if (itemStack.getTagCompound().hasKey("lizard_tail")) {
                list.add(I18n.format("abstractbaubles.lizard_tail.tooltip.0"));
            }
        } else {
            list.add(I18n.format("abstractbaubles.lizard_tail.tooltip.1"));
        }

        list.add("");

        if (GuiScreen.isShiftKeyDown()) {
            list.add(I18n.format("abstractbaubles.lizard_tail.tooltip.2"));
        } else {
            list.add(I18n.format("abstractbaubles.abstractbaubles.shifttooltip"));
        }
    }

    @SubscribeEvent
    public void onLivingDeath(LivingDeathEvent event) {
        EntityLivingBase entityLiving = (EntityLivingBase) event.getEntityLiving();
        if (entityLiving instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) entityLiving;
            World world = player.world;
            if (Tools.playerEquippedBauble(player, ABItems.LizardTail)) {
                ItemStack lizardTail = BaublesApi.getBaublesHandler(player).getStackInSlot(BaublesApi.isBaubleEquipped(player, ABItems.LizardTail));

                world.playSound((EntityPlayer)null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_ENDERPEARL_THROW, SoundCategory.NEUTRAL, 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
                player.getCooldownTracker().setCooldown(this, 20);

                if (!world.isRemote)
                {
                    BaublesApi.getBaublesHandler(player).setStackInSlot(BaublesApi.isBaubleEquipped(player, ABItems.LizardTail), ItemStack.EMPTY);
                    player.setHealth(player.getMaxHealth() * 0.2F);
                    player.clearActivePotions();

                    EntityEnderPearl entityenderpearl = new EntityEnderPearl(world, player);

                    float rotationPitch = 0;
                    float rotationYaw = 0;

                    boolean hasNbt = false;

                    if (lizardTail.hasTagCompound()) {
                        if (lizardTail.getTagCompound().hasKey("lizard_tail")) {
                            String[] strings = lizardTail.getTagCompound().getString("lizard_tail").split("/");
                            rotationPitch = Integer.getInteger(strings[0]);
                            rotationYaw = Integer.getInteger(strings[0]);
                            hasNbt = true;
                        }
                    }

                    if (!hasNbt) {
                        Random random = new Random();
                        rotationPitch = random.nextInt(181) - 90;
                        rotationYaw = random.nextInt(361) - 180;
                    }

                    entityenderpearl.shoot(player, rotationPitch, rotationYaw, 0.0F, 1.5F, 1.0F);

                    world.spawnEntity(entityenderpearl);
                }
            }
        }
    }

    @SubscribeEvent
    public void onRightClick(PlayerInteractEvent.RightClickItem event) {
        EntityLivingBase entityLiving = (EntityLivingBase) event.getEntityLiving();
        EntityPlayer player = event.getEntityPlayer();
        if (!entityLiving.world.isRemote) {
            if (event.getItemStack().getItem() == ABItems.LizardTail) {
                if (player.isSneaking()) {
                    ItemStack lizardTail = event.getItemStack();

                    if (lizardTail.hasTagCompound()) {
                        if (lizardTail.getTagCompound().hasKey("lizard_tail")) {
                            lizardTail.getTagCompound().setString("lizard_tail", player.rotationPitch + "/" + player.rotationYaw);
                        }
                        BaublesApi.getBaublesHandler(player).setStackInSlot(BaublesApi.isBaubleEquipped(player, ABItems.Laurus), lizardTail);
                    } else {
                        NBTTagCompound nbtTagCompound = new NBTTagCompound();
                        nbtTagCompound.setString("lizard_tail", player.rotationPitch + "/" + player.rotationYaw);
                        if (lizardTail.getTagCompound() != null) {
                            nbtTagCompound.merge(lizardTail.getTagCompound());
                        }
                        lizardTail.setTagCompound(nbtTagCompound);

                        BaublesApi.getBaublesHandler(player).setStackInSlot(BaublesApi.isBaubleEquipped(player, ABItems.Laurus), lizardTail);
                    }
                }
            }
        }
    }
}