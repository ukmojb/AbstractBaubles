package com.wdcftgg.abstractbaubles.core;

import com.wdcftgg.abstractbaubles.AbstractBaubles;
import net.minecraftforge.common.ForgeVersion;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import zone.rong.mixinbooter.ILateMixinLoader;

import java.util.Collections;
import java.util.List;

@IFMLLoadingPlugin.Name(AbstractBaubles.MODID)
@IFMLLoadingPlugin.MCVersion(ForgeVersion.mcVersion)
public class MixinInit implements ILateMixinLoader {
    @Override
    public List<String> getMixinConfigs() {
        return Collections.singletonList("mixins.abstractbaubles.json");
    }
}
