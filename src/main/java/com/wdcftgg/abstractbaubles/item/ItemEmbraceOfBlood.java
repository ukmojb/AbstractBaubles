package com.wdcftgg.abstractbaubles.item;

import baubles.api.BaubleType;
import com.wdcftgg.abstractbaubles.mixins.MixinHungerRenderer;
import com.wdcftgg.abstractbaubles.network.MessageEmbraceOfBlood;
import com.wdcftgg.abstractbaubles.network.MessageOriginalSourceBlood;
import com.wdcftgg.abstractbaubles.network.PacketHandler;
import com.wdcftgg.abstractbaubles.util.ABDamageSource;
import com.wdcftgg.abstractbaubles.util.Tools;
import com.wdcftgg.abstractbaubles.config.Config;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.player.PlayerFlyableFallEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import tfar.classicbar.config.ModConfig;

import javax.annotation.Nonnull;
import java.util.*;


@Mod.EventBusSubscriber
public class ItemEmbraceOfBlood extends BaseBaubleItem {

    public Map<ItemStack, Integer> itemStackIntegerMap = null;
    public final UUID uuid = UUID.fromString("A3CA2EF6-AFFB-4523-B00F-E10F01C682E5");
    public final UUID uuid0 = UUID.fromString("4859B70D-50C0-5931-B2C0-6764DA16B3E7");

    public ItemEmbraceOfBlood() {
        super("embrace_of_blood", BaubleType.AMULET);
    }

    public EnumRarity getRarity(ItemStack par1ItemStack) {
        return EnumRarity.RARE;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack is, World worldIn, List<String> list, ITooltipFlag flagIn)
    {
        list.add(I18n.format("abstractbaubles.embrace_of_blood.tooltip"));
        list.add("");

        if (GuiScreen.isShiftKeyDown()) {
            list.add(I18n.format("abstractbaubles.embrace_of_blood.tooltip.0"));
            list.add(I18n.format("abstractbaubles.embrace_of_blood.tooltip.1"));
            list.add(I18n.format("abstractbaubles.embrace_of_blood.tooltip.2"));
            list.add(I18n.format("abstractbaubles.embrace_of_blood.tooltip.3"));
            list.add(I18n.format("abstractbaubles.embrace_of_blood.tooltip.4"));
            list.add(I18n.format("abstractbaubles.embrace_of_blood.tooltip.5"));
            list.add(I18n.format("abstractbaubles.embrace_of_blood.tooltip.6"));
        } else {
            list.add(I18n.format("abstractbaubles.abstractbaubles.shifttooltip"));
        }

        list.add("");
        if (GuiScreen.isAltKeyDown()) {
            list.add(I18n.format("abstractbaubles.embrace_of_blood.tooltip.7"));

            if (itemStackIntegerMap == null) itemStackIntegerMap = getBloodFoodMap();
            for (int i = 0; i < itemStackIntegerMap.size(); i++) {
                List<ItemStack> itemStacks = new ArrayList<>(itemStackIntegerMap.keySet());
                List<Integer> integers = new ArrayList<>(itemStackIntegerMap.values());
                ItemStack itemStack = itemStacks.get(i);
                int foodlevel = integers.get(i);
                list.add(itemStack.getDisplayName() + "--" + foodlevel);
            }
        } else {
            list.add(I18n.format("abstractbaubles.abstractbaubles.alttooltip"));
        }
    }

    @Override
    public void onWornTick(ItemStack itemstack, EntityLivingBase entityLivingBase) {
        World world = entityLivingBase.world;
        if (!world.isRemote) {
            if (entityLivingBase instanceof EntityPlayer) {
                EntityPlayer player = (EntityPlayer)  entityLivingBase;
                AttributeModifier attributeModifier = new AttributeModifier(uuid, this.registryKey, 20, 0);
                if (player.getAttributeMap().getAttributeInstanceByName("generic.maxHealth").getModifier(uuid) == null) {
                    AttributeModifier maxHealth = new AttributeModifier(uuid, this.registryKey, 20, 0);
                    AttributeModifier armor = new AttributeModifier(uuid0, this.registryKey, 4, 0);
                    player.getAttributeMap().getAttributeInstanceByName("generic.maxHealth").applyModifier(maxHealth);
                    player.getAttributeMap().getAttributeInstanceByName("generic.armor").applyModifier(armor);
                }

                player.setAir(300);

                if (player.world.canSeeSky(player.getPosition()) && player.world.isDaytime()) {
                    if (player.getItemStackFromSlot(EntityEquipmentSlot.HEAD) == ItemStack.EMPTY) {
                        player.setFire(8);
                    }
                }

                if (player.getFoodStats().getSaturationLevel() <= 0 && player.getFoodStats().getFoodLevel() <= 0) {
                    player.attackEntityFrom(ABDamageSource.BloodSource, 114514F);
                } else {
                    float sat = player.getFoodStats().getSaturationLevel();
                    int food = player.getFoodStats().getFoodLevel();
                    if ((sat + food) < 20 * 0.3) {
                        int hungerlevel = ((sat + food) < 20 * 0.15 ? 1 : 0);
                        player.addPotionEffect(new PotionEffect(Potion.getPotionById(18), 5, hungerlevel));
                        player.addPotionEffect(new PotionEffect(Potion.getPotionById(4), 5, hungerlevel));
                        player.addPotionEffect(new PotionEffect(Potion.getPotionById(2), 5, hungerlevel));
                        if (hungerlevel == 1) {
                            player.addPotionEffect(new PotionEffect(Potion.getPotionById(15), 5, hungerlevel));
                        }
                    }
                }


            }
        } else {
            if (entityLivingBase instanceof EntityPlayer) {
                EntityPlayer player = (EntityPlayer) entityLivingBase;
                player.getEntityData().setBoolean("eat_blood", true);
            }
        }
    }



    @Override
    public void onEquipped(ItemStack itemstack, EntityLivingBase player) {
        World world = player.world;
        if (!world.isRemote) {
            AttributeModifier maxHealth = new AttributeModifier(uuid, this.registryKey, 20, 0);
            AttributeModifier armor = new AttributeModifier(uuid0, this.registryKey, 4, 0);
            player.getAttributeMap().getAttributeInstanceByName("generic.maxHealth").applyModifier(maxHealth);
            player.getAttributeMap().getAttributeInstanceByName("generic.armor").applyModifier(armor);
        } else {
            player.getEntityData().setBoolean("eat_blood", true);
        }
    }

    @Override
    public void onUnequipped(ItemStack itemstack, EntityLivingBase player) {
        World world = player.world;
        if (!world.isRemote) {
            AttributeModifier maxHealth = new AttributeModifier(uuid, this.registryKey, 20, 0);
            AttributeModifier armor = new AttributeModifier(uuid0, this.registryKey, 4, 0);
            player.getAttributeMap().getAttributeInstanceByName("generic.maxHealth").removeModifier(maxHealth);
            player.getAttributeMap().getAttributeInstanceByName("generic.armor").removeModifier(armor);
            player.attackEntityFrom(DamageSource.GENERIC, 0.01F);
        } else {
            player.getEntityData().setBoolean("eat_blood", false);
        }
    }

    @Override
    public boolean canUnequip(ItemStack itemstack, EntityLivingBase player) {
        return player.getHealth() == player.getMaxHealth();
    }

    @SubscribeEvent
    public void onPlayerFlyableFall(PlayerFlyableFallEvent event) {
        if (event.getEntityPlayer() != null){
            if (!event.getEntityPlayer().world.isRemote) {
                EntityPlayer player = event.getEntityPlayer();
                if (Tools.playerEquippedBauble(player, ABItems.EmbraceOfBlood)) {
                    if (event.getDistance() < 4.0F) {
                        event.setMultiplier(0);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onLivingEntityUseItemFinish(LivingEntityUseItemEvent.Finish event) {
        if (!event.getEntityLiving().world.isRemote) {
            if (event.getEntityLiving() instanceof EntityPlayer) {
                EntityPlayer player = (EntityPlayer) event.getEntityLiving();
                World world = player.world;
                if (Tools.playerEquippedBauble(player, ABItems.EmbraceOfBlood)){
                    if (event.getItem().getItem() instanceof ItemFood) {

                        Map<ItemStack, Integer> foods = getBloodFoodMap();
                        ItemStack eatItem = new ItemStack(event.getItem().getItem(), 1, event.getItem().getMetadata());
                        if (Config.foodInMap(eatItem, foods)) {
                            player.getFoodStats().addStats(Config.foodLevelInMap(eatItem, foods), 0);
                        } else {
                            Random random = new Random();
                            player.attackEntityFrom(ABDamageSource.EatNoBloodSource, random.nextInt(5));
                        }

                        Random random = new Random();
                        if (random.nextFloat() <= 0.4F) {
                            player.addPotionEffect(new PotionEffect(Potion.getPotionById(17), random.nextInt(15 * 20) + 5 * 20, random.nextInt(3)));
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onRightClickItem(PlayerInteractEvent.RightClickItem event) {
        if (!event.getEntityLiving().world.isRemote) {
            if (event.getEntityLiving() instanceof EntityPlayer) {
                EntityPlayer player = (EntityPlayer) event.getEntityLiving();
                World world = player.world;
                if (Tools.playerEquippedBauble(player, ABItems.EmbraceOfBlood)){
                    if (!(event.getItemStack().getItem() instanceof ItemFood)) {

                        Map<ItemStack, Integer> foods = getBloodFoodMap();
                        ItemStack eatItem = new ItemStack(event.getItemStack().getItem(), 1, event.getItemStack().getMetadata());

                        if (Config.foodInMap(eatItem, foods)) {
                            player.getFoodStats().addStats(Config.foodLevelInMap(eatItem, foods), 0);
                            event.getItemStack().shrink(1);
                            player.getCooldownTracker().setCooldown(eatItem.getItem(), 5);
                            PacketHandler.INSTANCE.sendTo(new MessageEmbraceOfBlood(eatItem), (EntityPlayerMP) player);
                        }

                        Random random = new Random();
                        if (random.nextFloat() <= 0.4F) {
                            player.addPotionEffect(new PotionEffect(Potion.getPotionById(17), random.nextInt(15 * 20) + 5 * 20, random.nextInt(3)));
                        }
                    }
                }
            }
        } else {
            if (event.getEntityLiving() instanceof EntityPlayer) {
                EntityPlayer player = (EntityPlayer) event.getEntityLiving();
                World world = player.world;
                if (Tools.playerEquippedBauble(player, ABItems.EmbraceOfBlood)){
                    if (!(event.getItemStack().getItem() instanceof ItemFood)) {

                        Map<ItemStack, Integer> foods = getBloodFoodMap();
                        ItemStack eatItem = new ItemStack(event.getItemStack().getItem(), 1, event.getItemStack().getMetadata());

                        if (Config.foodInMap(eatItem, foods)) {
                            world.playSound(player.posX, player.posY, player.posZ, SoundEvents.ENTITY_GENERIC_EAT, player.getSoundCategory(), 1.0F, 1.0F, false);
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onLivingHeal(LivingHealEvent event) {
        if (!event.getEntityLiving().world.isRemote) {
            if (event.getEntityLiving() instanceof EntityPlayer) {
                EntityPlayer player = (EntityPlayer) event.getEntityLiving();
                if (Tools.playerEquippedBauble(player, ABItems.EmbraceOfBlood)) {
                    event.setAmount(0);
                }
            }
        }
    }

    @SubscribeEvent
    public void onLivingDamage(LivingDamageEvent event) {
        //被攻击者
        EntityLivingBase entityLiving = (EntityLivingBase) event.getEntityLiving();
        //攻击者
        Entity target = event.getSource().getTrueSource();
        if (!entityLiving.world.isRemote) {
            if (target instanceof EntityPlayer) {
                EntityPlayer player = (EntityPlayer) target;
                if (Tools.playerEquippedBauble(player, ABItems.EmbraceOfBlood)) {
                    player.setHealth((float) (player.getHealth() + (event.getAmount() * 0.3)));
                }
            }
            if (entityLiving instanceof EntityPlayer) {
                EntityPlayer player = (EntityPlayer) entityLiving;
                if (Tools.playerEquippedBauble(player, ABItems.EmbraceOfBlood)) {
                    if (player.world.canSeeSky(player.getPosition())) {
                        event.setAmount((float) (event.getAmount() * 1.5));
                    }
                    if (event.getSource().damageType.equals("fall")) {
                        event.setAmount((float) (event.getAmount() * 0.5));
                    }
                }
            }
        }
    }

    public static Map<ItemStack, Integer> getBloodFoodMap() {
        Map<ItemStack, Integer> map = new HashMap<>();

        for (String str : Config.BloodFoodList) {
            String[] strs = str.split(" -> ");
            String[] strs0 = strs[0].split(";");
            ItemStack itemStack = new ItemStack(getItemByText(strs0[0]), 1, Integer.parseInt(strs0[1]));
            map.put(itemStack, Integer.valueOf(strs[1]));
        }

        return map;
    }

    private static Item getItemByText(String id)
    {
        ResourceLocation resourcelocation = new ResourceLocation(id);
        Item item = Item.REGISTRY.getObject(resourcelocation);

        if (item == null)
        {
            FMLLog.log.error(id + "was no found!!! we need a true item.");

//            Minecraft.getMinecraft().player.sendMessage(new TextComponentString(TextFormatting.RED + id + "was no found!!! we need a true item."));

        }
        else
        {
            return item;
        }
        return Items.AIR;
    }


}
