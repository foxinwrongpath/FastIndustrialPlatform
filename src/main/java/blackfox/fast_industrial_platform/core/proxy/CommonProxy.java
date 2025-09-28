package blackfox.fast_industrial_platform.core.proxy;

import blackfox.fast_industrial_platform.common.util.ValidBlocks;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class CommonProxy {
    public void preInit(FMLPreInitializationEvent event){
    }

    public void init(FMLInitializationEvent event){
        ValidBlocks.init();
    }

    public void postInit(FMLPostInitializationEvent event){
    }
}
