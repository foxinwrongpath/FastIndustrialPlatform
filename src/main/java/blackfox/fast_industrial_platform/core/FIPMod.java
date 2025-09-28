package blackfox.fast_industrial_platform.core;

import blackfox.fast_industrial_platform.core.proxy.CommonProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;

@Mod(modid = FIPMod.MODID,
     name = FIPMod.NAME,
     version = FIPMod.VERSION,
     acceptedMinecraftVersions = "[1.12.2]",
     dependencies = "required:forge@[14.23.5.2847,);")
public class FIPMod {
    public static final String NAME = "Fast Industrial Platform";
    public static final String MODID = "fast_industrial_platform";
    public static final String VERSION = "1.0.0";

    @Mod.Instance
    public static FIPMod INSTANCE;

    @SidedProxy(
            clientSide = "blackfox.fast_industrial_platform.core.proxy.ClientProxy",
            serverSide = "blackfox.fast_industrial_platform.core.proxy.CommonProxy"
    )
    public static CommonProxy proxy;

    public FIPMod(){

    }

    @Mod.EventHandler
    public void preInit(net.minecraftforge.fml.common.event.FMLPreInitializationEvent event){
        proxy.preInit(event);
    }

    @Mod.EventHandler
    public void Init(net.minecraftforge.fml.common.event.FMLInitializationEvent event){
        proxy.init(event);
    }

    @Mod.EventHandler
    public void postInit(net.minecraftforge.fml.common.event.FMLPostInitializationEvent event){
        proxy.postInit(event);
    }
}