package com.wdcftgg.abstractbaubles;


import com.wdcftgg.abstractbaubles.config.Config;
import com.wdcftgg.abstractbaubles.network.PacketHandler;
import com.wdcftgg.abstractbaubles.proxy.CommonProxy;
import com.wdcftgg.abstractbaubles.proxy.ServerProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

@Mod(modid = AbstractBaubles.MODID, name = AbstractBaubles.NAME, version = AbstractBaubles.VERSION, dependencies="required-after:baubles;after:botania,classicbar")
public class AbstractBaubles {

    public static final String MODID = "abstractbaubles";
    public static final String NAME = "AbstractBaubles";
    public static final String VERSION = "1.3";
    public static Logger logger;

    public static final String CLIENT_PROXY_CLASS = "com.wdcftgg.abstractbaubles.proxy.ClientProxy";
    public static final String SERVER_PROXY_CLASS = "com.wdcftgg.abstractbaubles.proxy.CommonProxy";

    @SidedProxy(clientSide = CLIENT_PROXY_CLASS, serverSide = SERVER_PROXY_CLASS)
    public static CommonProxy proxy;
    public static ServerProxy serverProxy = new ServerProxy();

    @Mod.Instance
    public static AbstractBaubles instance;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();

        Config.init(event.getSuggestedConfigurationFile());

        proxy.onPreInit();
        serverProxy.onPreInit();

    }


    @Mod.EventHandler
    public static void Init(FMLInitializationEvent event) {

        proxy.onInit();
        serverProxy.onInit();

        PacketHandler.init();


    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {

        proxy.onPostInit();
        serverProxy.onPostInit();
    }
}
