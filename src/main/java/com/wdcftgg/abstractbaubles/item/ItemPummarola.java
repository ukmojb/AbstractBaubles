package com.wdcftgg.abstractbaubles.item;

import baubles.api.BaubleType;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class ItemPummarola extends BaseBaubleItem {

    public ItemPummarola() {
        super("pummarola", BaubleType.AMULET);
    }

    public EnumRarity getRarity(ItemStack par1ItemStack) {
        return EnumRarity.RARE;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack itemStack, World worldIn, List<String> list, ITooltipFlag flagIn)
    {
        list.add(I18n.format("abstractbaubles.pummarola.tooltip.0"));
        list.add(I18n.format("abstractbaubles.pummarola.tooltip.1"));
        list.add("");

        if (GuiScreen.isShiftKeyDown()) {
            list.add(I18n.format("abstractbaubles.pummarola.tooltip.2"));
            list.add(I18n.format("abstractbaubles.pummarola.tooltip.3"));
            list.add(I18n.format("abstractbaubles.pummarola.tooltip.4"));
        } else {
            list.add(I18n.format("abstractbaubles.abstractbaubles.shifttooltip"));
        }
    }

    @Override
    public void onWornTick(ItemStack itemstack, EntityLivingBase entityLivingBase) {
        World world = entityLivingBase.world;
        if (!world.isRemote) {
            if (entityLivingBase instanceof EntityPlayer) {
                EntityPlayer player = (EntityPlayer) entityLivingBase;
                float health = player.getHealth();
                float maxhealth = player.getMaxHealth();
                if (health < maxhealth * 0.3) {
                    if (world.getWorldTime() % 30 == 0) {
                        player.heal(0.5F);
                    }
                } else if (health < maxhealth * 0.5) {
                    if (world.getWorldTime() % 20 == 0) {
                        player.heal(0.5F);
                    }
                } else {
                    if (world.getWorldTime() % 16 == 0) {
                        player.heal(0.75F);
                    }
                }
            }
        }

    }
}

