package com.wdcftgg.abstractbaubles.item;

import baubles.api.BaubleType;
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

public class ItemMindPowerPearl extends BaseBaubleItem {

    public ItemMindPowerPearl() {
        super("mind_power_pearl", BaubleType.TRINKET);
    }

    public EnumRarity getRarity(ItemStack par1ItemStack) {
        return EnumRarity.RARE;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack itemStack, World worldIn, List<String> list, ITooltipFlag flagIn)
    {
        list.add(I18n.format("abstractbaubles.mind_power_pearl.tooltip"));
    }

    @Override
    public void onWornTick(ItemStack itemstack, EntityLivingBase entityLivingBase) {
        World world = entityLivingBase.world;
        if (!world.isRemote) {
            if (entityLivingBase instanceof EntityPlayer) {
                EntityPlayer player = (EntityPlayer) entityLivingBase;
                List<EntityItem> entityList = world.getEntitiesWithinAABB(EntityItem.class,
                        new AxisAlignedBB(player.getPosition().south(5).up(3).west(5), player.getPosition().north(5).down(3).east(5)));
                for (EntityItem entityItem : entityList) {
                    BlockPos itemPos = entityItem.getPosition();
                    Vec3d vec3d = new Vec3d(player.posX - itemPos.getX(), player.posY - itemPos.getY(), player.posZ - itemPos.getZ()).normalize();
                    if (!entityItem.cannotPickup()) {
                        float speed = 0.2F;
                        entityItem.motionX += vec3d.x * speed;
                        entityItem.motionY += vec3d.y * speed;
                        entityItem.motionZ += vec3d.z * speed;
                    }
                }
            }
        }

    }
}

