package com.wdcftgg.abstractbaubles.mixins;

import com.wdcftgg.abstractbaubles.AbstractBaubles;
import com.wdcftgg.abstractbaubles.config.Config;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.GuiIngameForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import tfar.classicbar.Color;
import tfar.classicbar.ColorUtils;
import tfar.classicbar.ModUtils;
import tfar.classicbar.compat.HungerHelper;
import tfar.classicbar.config.ModConfig;
import tfar.classicbar.overlays.vanillaoverlays.HungerRenderer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static tfar.classicbar.ColorUtils.hex2Color;
import static tfar.classicbar.ModUtils.*;
import static tfar.classicbar.config.ModConfig.colors;
import static tfar.classicbar.config.ModConfig.general;

@Mixin(value = HungerRenderer.class, remap = false)
public class MixinHungerRenderer {

    @Shadow
    public boolean side;

    @Shadow
    private float foodAlpha = 0.0F;

    @Shadow
    private boolean foodIncrease = true;


    /**
     * @author
     * @reason
     */
    @Overwrite
    public void renderBar(EntityPlayer player, int width, int height) {
        double hunger = (double)player.getFoodStats().getFoodLevel();
        double maxHunger = HungerHelper.getMaxHunger(player);
        double currentSat = (double)player.getFoodStats().getSaturationLevel();
        float exhaustion = ModUtils.getExhaustion(player);
        int xStart = width / 2 + 10;
        int yStart = height - this.getSidedOffset();
        ModUtils.mc.profiler.startSection("hunger");
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        float alpha2 = hunger / maxHunger <= ModConfig.general.overlays.lowHungerThreshold && ModConfig.general.overlays.lowHungerWarning ? (float)((int)(Minecraft.getSystemTime() / 250L) % 2) : 1.0F;
        Color.reset();
        ModUtils.drawTexturedModalRect((float)xStart, (float)yStart, 0, 0, 81, 9);
        float f = (float)(xStart + 79 - ModUtils.getWidth(hunger, maxHunger));
        boolean hungerActive = player.isPotionActive(MobEffects.HUNGER);

        if (player.getEntityData().hasKey("eat_blood")) {
            if (player.getEntityData().getBoolean("eat_blood")) {
                ColorUtils.hex2Color("#8B1A1A").color2Gla(alpha2);
            } else {
                ColorUtils.hex2Color(hungerActive ? ModConfig.colors.hungerBarDebuffColor : ModConfig.colors.hungerBarColor).color2Gla(alpha2);
            }
        } else {
            ColorUtils.hex2Color(hungerActive ? ModConfig.colors.hungerBarDebuffColor : ModConfig.colors.hungerBarColor).color2Gla(alpha2);
        }

        ModUtils.drawTexturedModalRect(f, (float)(yStart + 1), 1, 10, ModUtils.getWidth(hunger, maxHunger), 7);
        if (currentSat > 0 && general.overlays.hunger.showSaturationBar) {
            //draw saturation
            if (player.getEntityData().hasKey("eat_blood")) {
                if (player.getEntityData().getBoolean("eat_blood")) {
                    ColorUtils.hex2Color("#FF0000").color2Gla(alpha2);
                } else {
                    ColorUtils.hex2Color(hungerActive ? ModConfig.colors.saturationBarDebuffColor : ModConfig.colors.saturationBarColor).color2Gla(alpha2);
                }
            } else {
                ColorUtils.hex2Color(hungerActive ? ModConfig.colors.saturationBarDebuffColor : ModConfig.colors.saturationBarColor).color2Gla(alpha2);
            }
            f += getWidth(hunger, maxHunger) - getWidth(currentSat, maxHunger);
            ModUtils.drawTexturedModalRect(f, yStart + 1, 1, 10, getWidth(currentSat, maxHunger), 7);
        }

        if (general.overlays.hunger.showHeldFoodOverlay &&
                player.getHeldItemMainhand().getItem() instanceof ItemFood) {
            ItemStack stack = player.getHeldItemMainhand();
            if (foodIncrease) foodAlpha += general.overlays.hunger.transitionSpeed;
            else foodAlpha -= general.overlays.hunger.transitionSpeed;
            if (foodAlpha >= 1) foodIncrease = false;
            else if (foodAlpha <= 0) foodIncrease = true;
            ItemFood foodItem = ((ItemFood) stack.getItem());
            double hungerOverlay = foodItem.getHealAmount(stack);
            double saturationMultiplier = foodItem.getSaturationModifier(stack);
            double potentialSat = 2 * hungerOverlay * saturationMultiplier;

            //Draw Potential hunger
            double hungerWidth = Math.min(maxHunger - hunger, hungerOverlay);
            //don't render the bar at all if hunger is full
            if (hunger < maxHunger) {
                f = xStart - getWidth(hungerWidth + hunger, maxHunger) + 78;
                if (player.getEntityData().hasKey("eat_blood")) {
                    if (player.getEntityData().getBoolean("eat_blood")) {
                        ColorUtils.hex2Color("#8B1A1A").color2Gla(foodAlpha);

                        if (foodInMap(stack, getBloodFoodMap())) {
                            hungerOverlay = foodLevelInMap(stack, getBloodFoodMap());
                        }
                    } else {
                        ColorUtils.hex2Color(hungerActive ? ModConfig.colors.hungerBarDebuffColor : ModConfig.colors.hungerBarColor).color2Gla(foodAlpha);
                    }
                } else {
                    ColorUtils.hex2Color(hungerActive ? ModConfig.colors.hungerBarDebuffColor : ModConfig.colors.hungerBarColor).color2Gla(foodAlpha);
                }
                drawTexturedModalRect(f + 1, yStart + 1, 1, 10, getWidth(hunger + hungerOverlay, maxHunger), 7);
            }

            //Draw Potential saturation
            if (general.overlays.hunger.showSaturationBar) {
                //maximum potential saturation cannot combine with current saturation to go over 20
                double saturationWidth = Math.min(potentialSat, maxHunger - currentSat);

                //Potential Saturation cannot go over potential hunger + current hunger combined
                saturationWidth = Math.min(saturationWidth, hunger + hungerWidth);
                saturationWidth = Math.min(saturationWidth, hungerOverlay + hunger);
                if ((potentialSat + currentSat) > (hunger + hungerWidth)) {
                    double diff = (potentialSat + currentSat) - (hunger + hungerWidth);
                    saturationWidth = potentialSat - diff;
                }
                //offset used to decide where to place the bar
                f = xStart - getWidth(saturationWidth + currentSat, maxHunger) + 78;
                if (player.getEntityData().hasKey("eat_blood")) {
                    if (!player.getEntityData().getBoolean("eat_blood")) {
                        ColorUtils.hex2Color(hungerActive ? ModConfig.colors.saturationBarDebuffColor : ModConfig.colors.saturationBarColor).color2Gla(foodAlpha);
                    } else {
                        ColorUtils.hex2Color(hungerActive ? ModConfig.colors.saturationBarDebuffColor : ModConfig.colors.saturationBarColor).color2Gla(foodAlpha);
                    }
                } else {
                    ColorUtils.hex2Color(hungerActive ? ModConfig.colors.saturationBarDebuffColor : ModConfig.colors.saturationBarColor).color2Gla(foodAlpha);
                }
                if (true)//currentSat > 0)
                    drawTexturedModalRect(f + 1, yStart + 1, 1, 10, getWidth(saturationWidth + currentSat, maxHunger), 7);
                else ;//drawTexturedModalRect(f, yStart+1, 1, 10, getWidthfloor(saturationWidth,20), 7);

            }
        }

        if (ModConfig.general.overlays.hunger.showExhaustionOverlay) {
            exhaustion = Math.min(exhaustion, 4.0F);
            f = (float)(xStart - ModUtils.getWidth((double)exhaustion, 4.0) + 80);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 0.25F);
            ModUtils.drawTexturedModalRect(f, (float)(yStart + 1), 1, 28, ModUtils.getWidth((double)exhaustion, 4.0), 9);
        }

        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
        ModUtils.mc.profiler.endSection();
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public void renderIcon(EntityPlayer player, int width, int height) {
        int xStart = width / 2 + 10;
        int yStart = height - this.getSidedOffset();
        ResourceLocation res = new ResourceLocation(AbstractBaubles.MODID, "textures/gui/blood.png");
        if (player.getEntityData().getBoolean("eat_blood")) {
            if (player.getEntityData().getBoolean("eat_blood")) {
                ModUtils.mc.getTextureManager().bindTexture(res);
            } else {
                ModUtils.mc.getTextureManager().bindTexture(Gui.ICONS);
            }
        } else {
            ModUtils.mc.getTextureManager().bindTexture(Gui.ICONS);
        }
        boolean hungerActive = player.isPotionActive(MobEffects.HUNGER);
        int k5 = 52;
        int k6 = 16;
        if (hungerActive) {
            k5 += 36;
            k6 = k5 + 45;
        }

        ModUtils.drawTexturedModalRect((float)(xStart + 82), (float)yStart, k6, 27, 9, 9);
        if (player.getEntityData().getBoolean("eat_blood")) {
            if (player.getEntityData().getBoolean("eat_blood")) {
                ModUtils.drawTexturedModalRect((float)(xStart + 82), (float)yStart, 0, 0, 9, 9);
            } else {
                ModUtils.drawTexturedModalRect((float)(xStart + 82), (float)yStart, k5, 27, 9, 9);
            }
        } else {
            ModUtils.drawTexturedModalRect((float)(xStart + 82), (float)yStart, k5, 27, 9, 9);
        }
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public void renderText(EntityPlayer player, int width, int height) {
        int xStart = width / 2 + 10;
        int yStart = height - this.getSidedOffset();
        double hunger = (double)player.getFoodStats().getFoodLevel();
        boolean hungerActive = player.isPotionActive(MobEffects.HUNGER);
        int h1 = (int)Math.floor(hunger);
        int i3 = ModConfig.general.displayIcons ? 1 : 0;
        if (ModConfig.numbers.showPercent) {
            h1 = (int)hunger * 5;
        }

        int c;
        if (player.getEntityData().getBoolean("eat_blood")) {
            if (player.getEntityData().getBoolean("eat_blood")) {
                c = Integer.decode("#8B1A1A");
            } else {
                c = Integer.decode(hungerActive ? ModConfig.colors.hungerBarDebuffColor : ModConfig.colors.hungerBarColor);
            }
        } else {
            c = Integer.decode(hungerActive ? ModConfig.colors.hungerBarDebuffColor : ModConfig.colors.hungerBarColor);
        }
        if (ModConfig.numbers.showHungerNumbers) {
            ModUtils.drawStringOnHUD(h1 + "", xStart + 9 * i3 + 82, yStart - 1, c);
        }

    }

    private int getSidedOffset(){
        return side ? GuiIngameForge.right_height : GuiIngameForge.left_height;
    }

    private Map<ItemStack, Integer> getBloodFoodMap() {
        Map<ItemStack, Integer> map = new HashMap<>();

        for (String str : Config.BloodFoodList) {
            String[] strs = str.split(" -> ");
            String[] strs0 = strs[0].split(";");
            ItemStack itemStack = new ItemStack(Item.getByNameOrId(strs0[0]), 1, Integer.valueOf(strs0[1]));
            map.put(itemStack, Integer.valueOf(strs[1]));
        }

        return map;
    }

    private boolean foodInMap(ItemStack itemStack, Map<ItemStack, Integer> map) {
        boolean pass = false;
        List<ItemStack> itemStackList = new ArrayList<>();
        itemStackList.addAll(map.keySet());
        List<Integer> integerList = new ArrayList<>();
        integerList.addAll(map.values());

        for (int i = 0; i < map.size(); i++) {
            if (itemStackList.get(i).getItem().equals(itemStack.getItem())
                    && itemStackList.get(i).getMetadata() == itemStack.getMetadata()) {
                pass = true;
                break;
            }
        }

        return pass;
    }

    private int foodLevelInMap(ItemStack itemStack, Map<ItemStack, Integer> map) {
        int foodLevel = 0;
        List<ItemStack> itemStackList = new ArrayList<>();
        itemStackList.addAll(map.keySet());
        List<Integer> integerList = new ArrayList<>();
        integerList.addAll(map.values());

        for (int i = 0; i < map.size(); i++) {
            if (itemStackList.get(i).getItem().equals(itemStack.getItem())
                    && itemStackList.get(i).getMetadata() == itemStack.getMetadata()) {
                foodLevel = integerList.get(i);
                break;
            }
        }

        return foodLevel;
    }
}

