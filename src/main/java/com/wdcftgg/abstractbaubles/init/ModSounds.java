package com.wdcftgg.abstractbaubles.init;

import com.wdcftgg.abstractbaubles.AbstractBaubles;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

@Mod.EventBusSubscriber
public class ModSounds {
    public static SoundEvent TungstenAlloyAod = initSound( "tungsten_alloy_rod");

    @SubscribeEvent
    public void onRegisterSoundEvents(RegistryEvent.Register<SoundEvent> event)
    {
        IForgeRegistry<SoundEvent> registry = event.getRegistry();
        registry.register(TungstenAlloyAod);
    }

    private static SoundEvent initSound(String name) {
        ResourceLocation location = new ResourceLocation(AbstractBaubles.MODID, name);
        return new SoundEvent(location).setRegistryName(location);
    }
}

