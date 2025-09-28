package blackfox.fast_industrial_platform.common.config;

import blackfox.fast_industrial_platform.core.FIPMod;
import net.minecraftforge.common.config.Config;

@Config(modid = FIPMod.MODID)
public class FIPConfig {
    @Config.Comment({"Valid blocks for platform builder", "Add blocks in the format modid:blockname:meta"})
    @Config.Name("validBlocks")
    @Config.RequiresWorldRestart
    @Config.RequiresMcRestart
    public static String[] VALID_BLOCKS_STR = new String[]{
        "minecraft:concrete",
            "minecraft:stone",
            "minecraft:cobblestone",
            "minecraft:stonebrick",
            "minecraft:stained_hardened_clay",
            "minecraft:sandstone",
            "minecraft:brick_block"
    };
}
