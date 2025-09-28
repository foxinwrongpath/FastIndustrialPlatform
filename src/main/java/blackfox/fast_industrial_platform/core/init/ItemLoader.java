package blackfox.fast_industrial_platform.core.init;

import blackfox.fast_industrial_platform.common.item.ItemToolPlatformBuildingStick;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class ItemLoader {
    public static final ItemToolPlatformBuildingStick PLATFORM_BUILDING_STICK = new ItemToolPlatformBuildingStick();
    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event){
        event.getRegistry().registerAll(
            PLATFORM_BUILDING_STICK
        );
    }
}
