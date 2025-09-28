package blackfox.fast_industrial_platform.common.block;

import blackfox.fast_industrial_platform.common.item.ItemToolPlatformBuildingStick;
import blackfox.fast_industrial_platform.common.structure.AutoBuilder;
import blackfox.fast_industrial_platform.common.util.ValidBlocks;
import blackfox.fast_industrial_platform.core.FIPMod;
import blackfox.fast_industrial_platform.core.init.ItemLoader;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@Setter
@Getter
public class BlockPlatformWrapper extends Block {

    IBlockState borderBlock = null;
    IBlockState fillBlock = null;
    public BlockPlatformWrapper() {
        super(Material.IRON);
        setRegistryName("platform_wrapper");
        setTranslationKey(FIPMod.MODID +"."+"platform_wrapper");
        setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
        setHardness(2.0F);
        setHarvestLevel("pickaxe", 0);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if(!worldIn.isRemote){
            ItemStack heldStack = playerIn.getHeldItem(hand);
            if(!playerIn.isSneaking()){
                if(heldStack.getItem().equals(ItemLoader.PLATFORM_BUILDING_STICK)){
                    int mode = ((ItemToolPlatformBuildingStick)heldStack.getItem()).getMode();
                    if(mode == 1){
                        IBlockState border = ((ItemToolPlatformBuildingStick)heldStack.getItem()).getBorderBlock();
                        IBlockState fill = ((ItemToolPlatformBuildingStick)heldStack.getItem()).getFillBlock();
                        if(border!=null && fill != null){
                            borderBlock = border;
                            fillBlock = fill;
                            playerIn.sendStatusMessage(new TextComponentString(I18n.format("message.fip.pasted_single")), true);
                        }
                    }
                }
                if(heldStack.isEmpty() && borderBlock != null && fillBlock != null){
                    AutoBuilder.buildSingleLayer(worldIn,pos, borderBlock, fillBlock);
                    return true;
                }
                if(heldStack.isEmpty()){
                    playerIn.sendStatusMessage(new TextComponentString(
                            I18n.format("message.fip.current_border", borderBlock == null ? I18n.format("message.fip.not_set") : new ItemStack(borderBlock.getBlock(),1,borderBlock.getBlock().getMetaFromState(borderBlock)).getDisplayName()) + ", " +
                                    I18n.format("message.fip.current_fill", fillBlock == null ? I18n.format("message.fip.not_set") : new ItemStack(fillBlock.getBlock(),1,fillBlock.getBlock().getMetaFromState(borderBlock)).getDisplayName())), true
                    );
                    return true;
                }
                if ((borderBlock == null || fillBlock == null) && heldStack.getItem() instanceof ItemBlock) {
                    IBlockState heldState = ((ItemBlock) heldStack.getItem()).getBlock().getStateFromMeta(heldStack.getMetadata());
                    if (ValidBlocks.hasBlock(heldState)) {
                        if (borderBlock == null) {
                            borderBlock = heldState;
                            playerIn.sendStatusMessage(new TextComponentString(
                                    I18n.format("message.fip.border_set", heldStack.getDisplayName())), true
                            );
                        } else {
                            fillBlock = heldState;
                            playerIn.sendStatusMessage(new TextComponentString(
                                    I18n.format("message.fip.fill_set", heldStack.getDisplayName())), true
                            );
                        }
                    }
                    return true;
                }
            }else{
                if(heldStack.isEmpty()){
                    borderBlock = null;
                    fillBlock = null;
                    playerIn.sendStatusMessage(new TextComponentString(
                            I18n.format("message.fip.cleared")), true
                    );
                    return true;
                }
            }

        }
        return true;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add(I18n.format("tooltip.fip.platform_wrapper.tooltip1"));
        tooltip.add(I18n.format("tooltip.fip.platform_wrapper.tooltip2"));
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        borderBlock = null;
        fillBlock = null;
        super.breakBlock(worldIn, pos, state);
    }
}
