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
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@Setter
@Getter
public class BlockDoublePlatformWrapper extends Block {
    private IBlockState borderBlock = null;
    private IBlockState fillBlock = null;
    private IBlockState pillarBlock = null;
    private int height = 3;

    public BlockDoublePlatformWrapper() {
        super(Material.IRON);
        setRegistryName("double_platform_wrapper");
        setTranslationKey(FIPMod.MODID +"."+"double_platform_wrapper");
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
                    if(mode == 0){
                        int newHeight = ((ItemToolPlatformBuildingStick)heldStack.getItem()).getHeight();
                        if(pos.getY() + newHeight + 1 > worldIn.getHeight()){
                            playerIn.sendStatusMessage(new TextComponentString(
                                    I18n.format("message.fip.too_tall", worldIn.getHeight())
                            ), true);
                            return true;
                        }
                        height = newHeight;
                        playerIn.sendStatusMessage(new TextComponentString(
                                I18n.format("message.fip.pillar_height_set", height)
                        ), true);
                        return true;
                    }else if(mode == 1){
                        IBlockState border = ((ItemToolPlatformBuildingStick)heldStack.getItem()).getBorderBlock();
                        IBlockState fill = ((ItemToolPlatformBuildingStick)heldStack.getItem()).getFillBlock();
                        IBlockState pillar = ((ItemToolPlatformBuildingStick)heldStack.getItem()).getPillarBlock();
                        int height = ((ItemToolPlatformBuildingStick)heldStack.getItem()).getHeight();
                        if(pos.getY() + height + 1 > worldIn.getHeight()){
                            playerIn.sendStatusMessage(new TextComponentString(
                                    I18n.format("message.fip.too_tall", worldIn.getHeight())
                            ), true);
                            return true;
                        }
                        if(border != null && fill != null && pillar != null){
                            this.borderBlock = border;
                            this.fillBlock = fill;
                            this.pillarBlock = pillar;
                            this.height = height;
                            playerIn.sendStatusMessage(new TextComponentString(I18n.format("message.fip.pasted_double")), true);
                            return true;
                        }
                    }
                }
                if(heldStack.isEmpty()){
                    if(borderBlock!=null && fillBlock!=null && pillarBlock!=null){
                        AutoBuilder.buildDoubleLayer(worldIn,pos, borderBlock, fillBlock, pillarBlock, height);
                    }else{
                        playerIn.sendStatusMessage(new TextComponentString(
                                I18n.format("message.fip.current_border", borderBlock == null ? I18n.format("message.fip.not_set") : new ItemStack(borderBlock.getBlock(),1,borderBlock.getBlock().getMetaFromState(borderBlock)).getDisplayName()) + ", " +
                                        I18n.format("message.fip.current_fill", fillBlock == null ? I18n.format("message.fip.not_set") : new ItemStack(fillBlock.getBlock(),1,fillBlock.getBlock().getMetaFromState(borderBlock)).getDisplayName()) + ", " +
                                        I18n.format("message.fip.current_pillar", pillarBlock == null ? I18n.format("message.fip.not_set") : new ItemStack(pillarBlock.getBlock(),1,pillarBlock.getBlock().getMetaFromState(pillarBlock)).getDisplayName()) + ", " +
                                        I18n.format("message.fip.current_height", height)
                        ), true);
                    }
                }else{
                    if(heldStack.getItem() instanceof ItemBlock){
                        IBlockState heldState = ((ItemBlock) heldStack.getItem()).getBlock().getStateFromMeta(heldStack.getMetadata());
                        if(ValidBlocks.hasBlock(heldState)){
                            if(borderBlock == null){
                                borderBlock = heldState;
                                playerIn.sendStatusMessage(new TextComponentString(
                                        I18n.format("message.fip.border_set", heldStack.getDisplayName())), true
                                );
                            }else if(fillBlock == null){
                                fillBlock = heldState;
                                playerIn.sendStatusMessage(new TextComponentString(
                                        I18n.format("message.fip.fill_set", heldStack.getDisplayName())), true
                                );
                            }else if(pillarBlock == null){
                                pillarBlock = heldState;
                                playerIn.sendStatusMessage(new TextComponentString(
                                        I18n.format("message.fip.pillar_set", heldStack.getDisplayName())), true
                                );
                            }
                        }
                    }
                }
            }else{
                if(heldStack.isEmpty()){
                    borderBlock = null;
                    fillBlock = null;
                    pillarBlock = null;
                    height = 3;
                    playerIn.sendStatusMessage(new TextComponentString(
                            I18n.format("message.fip.cleared")), true
                    );
                    return true;
                }
            }
            return true;
        }
        return true;
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        borderBlock = null;
        fillBlock = null;
        pillarBlock = null;
        height = 3;
        super.breakBlock(worldIn, pos, state);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add(I18n.format("tooltip.fip.double_platform_wrapper.tooltip1"));
        tooltip.add(I18n.format("tooltip.fip.double_platform_wrapper.tooltip2"));
        tooltip.add(I18n.format("tooltip.fip.double_platform_wrapper.tooltip3"));
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }
}
