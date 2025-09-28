package blackfox.fast_industrial_platform.core.init;

import blackfox.fast_industrial_platform.common.block.BlockDoublePlatformWrapper;
import blackfox.fast_industrial_platform.common.block.BlockPlatformWrapper;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Objects;

@Mod.EventBusSubscriber
public class BlockLoader {

    public static final BlockPlatformWrapper PLATFORM_WRAPPER = new BlockPlatformWrapper();
    public static final BlockDoublePlatformWrapper DOUBLE_PLATFORM_WRAPPER = new BlockDoublePlatformWrapper();

    @SubscribeEvent
    public static void registerBlock(RegistryEvent.Register<Block> event){
        event.getRegistry().registerAll(
                PLATFORM_WRAPPER,
                DOUBLE_PLATFORM_WRAPPER
        );
    }

    @SubscribeEvent
    public static void registerItemBlock(RegistryEvent.Register<Item> event){
        event.getRegistry().registerAll(
            new ItemBlock(PLATFORM_WRAPPER).setRegistryName(Objects.requireNonNull(PLATFORM_WRAPPER.getRegistryName())),
            new ItemBlock(DOUBLE_PLATFORM_WRAPPER).setRegistryName(Objects.requireNonNull(DOUBLE_PLATFORM_WRAPPER.getRegistryName()))
        );
    }
}
