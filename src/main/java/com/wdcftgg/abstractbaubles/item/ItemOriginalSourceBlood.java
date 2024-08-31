package com.wdcftgg.abstractbaubles.item;

import baubles.api.BaubleType;
import baubles.api.BaublesApi;
import com.wdcftgg.abstractbaubles.config.Config;
import com.wdcftgg.abstractbaubles.network.MessageOriginalSourceBlood;
import com.wdcftgg.abstractbaubles.network.PacketHandler;
import com.wdcftgg.abstractbaubles.util.ABDamageSource;
import com.wdcftgg.abstractbaubles.util.Tools;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.living.LivingHealEvent;
import net.minecraftforge.event.entity.player.PlayerFlyableFallEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.*;


@Mod.EventBusSubscriber
public class ItemOriginalSourceBlood extends BaseBaubleItem {

    public Map<ItemStack, Integer> itemStackIntegerMap = null;

    public final UUID uuid = UUID.fromString("A3CA2EF6-AFFB-4523-B00F-E10F01C682E5");
    public final UUID uuid0 = UUID.fromString("4859B70D-50C0-5931-B2C0-6764DA16B3E7");

    public ItemOriginalSourceBlood() {
        super("original_source_blood", BaubleType.AMULET);
    }

    public EnumRarity getRarity(ItemStack par1ItemStack) {
        return EnumRarity.RARE;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack is, World worldIn, List<String> list, ITooltipFlag flagIn)
    {
        list.add(I18n.format("abstractbaubles.original_source_blood.tooltip"));
        list.add(I18n.format("abstractbaubles.original_source_blood.tooltip.0"));
        list.add("");

        if (GuiScreen.isShiftKeyDown()) {
            list.add(I18n.format("abstractbaubles.original_source_blood.tooltip.1"));
            list.add(I18n.format("abstractbaubles.original_source_blood.tooltip.2"));
            list.add(I18n.format("abstractbaubles.original_source_blood.tooltip.3"));
            list.add(I18n.format("abstractbaubles.original_source_blood.tooltip.4"));
            list.add(I18n.format("abstractbaubles.original_source_blood.tooltip.5"));
            list.add(I18n.format("abstractbaubles.original_source_blood.tooltip.6"));
            list.add(I18n.format("abstractbaubles.original_source_blood.tooltip.7"));
            list.add(I18n.format("abstractbaubles.original_source_blood.tooltip.8"));
        } else {
            list.add(I18n.format("abstractbaubles.abstractbaubles.shifttooltip"));
        }

        list.add("");
        if (GuiScreen.isAltKeyDown()) {
            list.add(I18n.format("abstractbaubles.original_source_blood.tooltip.9"));

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
                if (player.getAttributeMap().getAttributeInstanceByName("generic.maxHealth").getModifier(uuid) == null) {
                    AttributeModifier maxHealth = new AttributeModifier(uuid, this.registryKey, 30, 0);
                    AttributeModifier armor = new AttributeModifier(uuid0, this.registryKey, 8, 0);
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

                if (itemstack.hasTagCompound()) {
                    if (itemstack.getTagCompound().hasKey("blood_totem_of_undying")) {
                        int cool = itemstack.getTagCompound().getInteger("blood_totem_of_undying");
                        itemstack.getTagCompound().setInteger("blood_totem_of_undying", cool - 1);
                    }
                } else {
                    NBTTagCompound nbtTagCompound = new NBTTagCompound();
                    nbtTagCompound.setInteger("blood_totem_of_undying", 0);
                    if (itemstack.getTagCompound() != null) {
                        nbtTagCompound.merge(itemstack.getTagCompound());
                    }
                    itemstack.setTagCompound(nbtTagCompound);
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
            AttributeModifier maxHealth = new AttributeModifier(uuid, this.registryKey, 30, 0);
            AttributeModifier armor = new AttributeModifier(uuid0, this.registryKey, 8, 0);
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
            AttributeModifier maxHealth = new AttributeModifier(uuid, this.registryKey, 30, 0);
            AttributeModifier armor = new AttributeModifier(uuid0, this.registryKey, 8, 0);
            player.getAttributeMap().getAttributeInstanceByName("generic.maxHealth").removeModifier(maxHealth);
            player.getAttributeMap().getAttributeInstanceByName("generic.armor").removeModifier(armor);
            player.attackEntityFrom(DamageSource.GENERIC, 0.01F);
        } else {
            player.getEntityData().setBoolean("eat_blood", false);
        }
    }

    @Override
    public boolean canUnequip(ItemStack itemstack, EntityLivingBase entityLivingBase) {
        if (entityLivingBase instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) entityLivingBase;
            if (player.isCreative()) return true;
        }
        return false;
    }

    @Override
    public boolean willAutoSync(ItemStack itemstack, EntityLivingBase player) {
        return true;
    }

    @SubscribeEvent
    public void onPlayerFlyableFall(PlayerFlyableFallEvent event) {
        if (event.getEntityPlayer() != null){
            if (!event.getEntityPlayer().world.isRemote) {
                EntityPlayer player = event.getEntityPlayer();
                if (Tools.playerEquippedBauble(player, ABItems.OriginalSourceBlood)) {
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
                if (Tools.playerEquippedBauble(player, ABItems.OriginalSourceBlood)){
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
                if (Tools.playerEquippedBauble(player, ABItems.OriginalSourceBlood)){
                    if (!(event.getItemStack().getItem() instanceof ItemFood)) {

                        Map<ItemStack, Integer> foods = getBloodFoodMap();
                        ItemStack eatItem = new ItemStack(event.getItemStack().getItem(), 1, event.getItemStack().getMetadata());

                        if (Config.foodInMap(eatItem, foods)) {
                            System.out.println("adwdwac");
                            player.getFoodStats().addStats(Config.foodLevelInMap(eatItem, foods), 0);
                            event.getItemStack().shrink(1);
                            PacketHandler.INSTANCE.sendTo(new MessageOriginalSourceBlood(eatItem, 1), (EntityPlayerMP) player);
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
    public void onLivingHeal(LivingHealEvent event) {
        if (!event.getEntityLiving().world.isRemote) {
            if (event.getEntityLiving() instanceof EntityPlayer) {
                EntityPlayer player = (EntityPlayer) event.getEntityLiving();
                if (Tools.playerEquippedBauble(player, ABItems.OriginalSourceBlood)) {
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
            if (entityLiving instanceof EntityPlayer) {
                EntityPlayer player = (EntityPlayer) entityLiving;
                if (Tools.playerEquippedBauble(player, ABItems.OriginalSourceBlood)) {
                    if (player.world.canSeeSky(player.getPosition())) {
                        event.setAmount((float) (event.getAmount() * 1.8));
                    }
                    if (event.getSource().damageType.equals("fall")) {
                        event.setAmount((float) (event.getAmount() * 0.6));
                    }
                }
            }
            if (target instanceof EntityPlayer) {
                EntityPlayer player = (EntityPlayer) target;
                if (Tools.playerEquippedBauble(player, ABItems.OriginalSourceBlood)) {
                    player.setHealth((float) (player.getHealth() + (event.getAmount() * 0.5)));
                }
            }
        }
    }
    @SubscribeEvent
    public void onLivingDeath(LivingDeathEvent event) {
        EntityLivingBase entityLiving = (EntityLivingBase) event.getEntityLiving();
        if (!entityLiving.world.isRemote) {
            if (entityLiving instanceof EntityPlayer) {
                EntityPlayer player = (EntityPlayer) entityLiving;
                if (Tools.playerEquippedBauble(player, ABItems.OriginalSourceBlood)) {
                    ItemStack originalSourceBlood = BaublesApi.getBaublesHandler(player).getStackInSlot(BaublesApi.isBaubleEquipped(player, ABItems.OriginalSourceBlood));
                    if (originalSourceBlood.hasTagCompound()) {
                        if (originalSourceBlood.getTagCompound().hasKey("blood_totem_of_undying")) {
                            boolean canUse = originalSourceBlood.getTagCompound().getInteger("blood_totem_of_undying") <= 0;
                            boolean totemOrBoold;
                            if (canUse) {
                                InventoryPlayer inventoryPlayer = player.inventory;
                                if (Tools.inventoryHasItem(inventoryPlayer, Items.TOTEM_OF_UNDYING)) {
                                    inventoryPlayer.clearMatchingItems(Items.TOTEM_OF_UNDYING, 0, 1, null);
                                    totemOrBoold = true;
                                } else {
                                    originalSourceBlood.getTagCompound().setInteger("blood_totem_of_undying", 5 * 60 * 20);
                                    totemOrBoold = false;
                                }
                                BaublesApi.getBaublesHandler(player).setStackInSlot(BaublesApi.isBaubleEquipped(player, ABItems.OriginalSourceBlood), originalSourceBlood);
                                player.setHealth(1.0F);
                                player.clearActivePotions();
                                player.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 900, 1));
                                player.addPotionEffect(new PotionEffect(MobEffects.ABSORPTION, 100, 1));

                                PacketHandler.INSTANCE.sendTo(new MessageOriginalSourceBlood(totemOrBoold, 0), (EntityPlayerMP) player);
                                event.setCanceled(true);
                            }
                        }
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
