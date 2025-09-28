package blackfox.fast_industrial_platform.common.util;

import blackfox.fast_industrial_platform.common.config.FIPConfig;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;


public class ValidBlocks {
    private static final Logger logger = LogManager.getLogger("FIP ValidBlocks");

    public static final List<IBlockState> VALID_BLOCKS = new ArrayList<>();

    public static boolean hasBlock(IBlockState state){
        return VALID_BLOCKS.contains(state);
    }

    public static void init(){
        for(String s: FIPConfig.VALID_BLOCKS_STR){
            VALID_BLOCKS.addAll(getBlockStateFromStr(s));
        }
    }

    public static List<IBlockState> getBlockStateFromStr(String str){
        String[] parts = str.split(":");
        List<IBlockState> states = new ArrayList<>();
        if(parts.length == 3){
            Block block = Block.getBlockFromName(parts[0] + ":" + parts[1]);
            if(block != null){
                states.add(block.getStateFromMeta(Integer.parseInt(parts[2])));
            }
        }else if(parts.length == 2){
            Block block = Block.getBlockFromName(parts[0] + ":" + parts[1]);
            if(block != null){
                states.addAll(block.getBlockState().getValidStates());
            }else{
                throw new IllegalArgumentException("Block not found: " + parts[0] + ":" + parts[1]);
            }
        }else{
            throw new IllegalArgumentException("Invalid block string: " + str);
        }
        return states;
    }

    public static String getStrFromBlockState(IBlockState state){
        if(state == null) return null;
        return state.getBlock().getRegistryName().toString() + ":" + state.getBlock().getMetaFromState(state);
    }
}
