package com.wdcftgg.abstractbaubles.util;

import net.minecraft.util.DamageSource;

public class ABDamageSource {
    public static final DamageSource BloodSource = new DamageSource("blood_source").setDamageBypassesArmor().setDamageAllowedInCreativeMode();
    public static final DamageSource EatNoBloodSource = new DamageSource("eat_no_blood_source").setDamageBypassesArmor().setDamageAllowedInCreativeMode();
}
