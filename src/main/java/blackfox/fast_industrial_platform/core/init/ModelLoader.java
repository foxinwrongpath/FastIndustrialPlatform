package blackfox.fast_industrial_platform.core.init;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

import java.util.Objects;

@Mod.EventBusSubscriber(value = Side.CLIENT)
public class ModelLoader {

    @SubscribeEvent
    public static void onModReg(ModelRegistryEvent event){
        registerItemModel(ItemLoader.PLATFORM_BUILDING_STICK);

        registerItemBlockModel(BlockLoader.PLATFORM_WRAPPER);
        registerItemBlockModel(BlockLoader.DOUBLE_PLATFORM_WRAPPER);
    }

    private static void registerItemModel(Item item){
        ModelResourceLocation modelResourceLocation = new ModelResourceLocation(Objects.requireNonNull(item.getRegistryName()),"inventory");
        net.minecraftforge.client.model.ModelLoader.setCustomModelResourceLocation(item,0,modelResourceLocation);
    }

    private static void registerItemBlockModel(Block block){
        ModelResourceLocation modelResourceLocation = new ModelResourceLocation(Objects.requireNonNull(block.getRegistryName()),"inventory");
        net.minecraftforge.client.model.ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block),0,modelResourceLocation);
    }

    private static void registerItemBlockModel(Block block, int meta, String variant){
        ModelResourceLocation modelResourceLocation = new ModelResourceLocation(Objects.requireNonNull(block.getRegistryName()),variant);
        net.minecraftforge.client.model.ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block),meta,modelResourceLocation);
    }
}
