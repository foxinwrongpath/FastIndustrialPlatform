package blackfox.fast_industrial_platform.common.item;

import blackfox.fast_industrial_platform.common.block.BlockDoublePlatformWrapper;
import blackfox.fast_industrial_platform.common.block.BlockPlatformWrapper;
import blackfox.fast_industrial_platform.common.structure.AutoBuilder;
import blackfox.fast_industrial_platform.common.util.ValidBlocks;
import blackfox.fast_industrial_platform.core.FIPMod;
import lombok.Getter;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.logging.Logger;

@Mod.EventBusSubscriber(Side.CLIENT)
public class ItemToolPlatformBuildingStick extends Item {
    private static final Logger logger = Logger.getLogger("FIP:PlatformBuildingStick");

    private static boolean renderSingle = false;
    private static boolean renderDouble = false;
    private static BlockPos renderPos = null;
    private static IBlockState renderBorder = null;
    private static IBlockState renderFill = null;
    private static IBlockState renderPillar = null;
    private static int renderHeight = 3;

    @Getter
    public static enum Mode{
        HEIGHT_ADJUSTER(I18n.format("item.fip.platform_building_stick.mode.height_adjuster")),
        COPY_PASTE(I18n.format("item.fip.platform_building_stick.mode.copy_paste")),
        PREVIEW(I18n.format("item.fip.platform_building_stick.mode.preview"));

        private final String name;

        private Mode(String name){
            this.name = name;
        }

    }

    public ItemToolPlatformBuildingStick(){
        this.setRegistryName("platform_building_stick");
        this.setTranslationKey(FIPMod.MODID +"."+"platform_building_stick");
        this.setMaxStackSize(1);
        this.setCreativeTab(CreativeTabs.TOOLS);
    }

    @SubscribeEvent
    public static void onMouseScroll(MouseEvent event){
        EntityPlayerSP player = Minecraft.getMinecraft().player;
        if(player == null || !player.isSneaking()) return;
        ItemStack stack = player.getHeldItemMainhand();
        if(stack.getItem() instanceof ItemToolPlatformBuildingStick){
            NBTTagCompound tag = stack.getOrCreateSubCompound("FIP");
            int mode = Mode.HEIGHT_ADJUSTER.ordinal();
            if(tag.hasKey("mode")){
                mode = tag.getInteger("mode");
            }
            if(mode != Mode.HEIGHT_ADJUSTER.ordinal()) return;
            int delta = event.getDwheel();
            if(delta != 0){
                int height = tag.getInteger("height");
                height = Math.max(2, Math.min(255,height + delta));
                tag.setInteger("height", height);

                player.sendStatusMessage(new TextComponentString(
                        I18n.format("message.fip.pillar_height_set",height)
                ), true);
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void onRenderWorldLastEvent(RenderWorldLastEvent event) {
        EntityPlayerSP player = Minecraft.getMinecraft().player;
        if(player == null) return;
        ItemStack stack = player.getHeldItemMainhand();
        if(stack.getItem() instanceof ItemToolPlatformBuildingStick){
            NBTTagCompound tag = stack.getOrCreateSubCompound("FIP");
            int mode = Mode.HEIGHT_ADJUSTER.ordinal();
            if(tag.hasKey("mode")){
                mode = tag.getInteger("mode");
            }
            if(mode != Mode.PREVIEW.ordinal()) return;
            World world = player.getEntityWorld();
            if(renderPos == null) return;
            if(world.getBlockState(renderPos).getBlock().equals(Blocks.AIR)){
                renderSingle = false;
                renderDouble = false;
                return;
            }
            if(renderSingle){
                AutoBuilder.renderSingleLayerPreview(renderPos, renderBorder, renderFill);
            }else  if(renderDouble){
                AutoBuilder.renderDoubleLayerPreview(renderPos, renderBorder, renderFill, renderPillar, renderHeight);
            }
        }
    }

    public int getHeight(){
        EntityPlayerSP player = Minecraft.getMinecraft().player;
        if(player == null) return 3;
        ItemStack stack = player.getHeldItemMainhand();
        if(stack.getItem() instanceof ItemToolPlatformBuildingStick){
            NBTTagCompound tag = stack.getOrCreateSubCompound("FIP");
            return Math.max(2, tag.getInteger("height"));
        }
        return 3;
    }

    public int getMode(){
        EntityPlayerSP player = Minecraft.getMinecraft().player;
        if(player == null) return 0;
        ItemStack stack = player.getHeldItemMainhand();
        if(stack.getItem() instanceof ItemToolPlatformBuildingStick){
            NBTTagCompound tag = stack.getOrCreateSubCompound("FIP");
            return Math.max(0, Math.min(2, tag.getInteger("mode")));
        }
        return 0;
    }

    public IBlockState getBorderBlock(){
        EntityPlayerSP player = Minecraft.getMinecraft().player;
        if(player == null) return null;
        ItemStack stack = player.getHeldItemMainhand();
        if(stack.getItem() instanceof ItemToolPlatformBuildingStick){
            NBTTagCompound tag = stack.getOrCreateSubCompound("FIP");
            if(tag.hasKey("copy_border")){
                String str = tag.getString("copy_border");
                if(str.equals("null")) return null;
                List<IBlockState> states = ValidBlocks.getBlockStateFromStr(str);
                if(!states.isEmpty()) return states.get(0);
            }
        }
        return null;
    }

    public IBlockState getFillBlock(){
        EntityPlayerSP player = Minecraft.getMinecraft().player;
        if(player == null) return null;
        ItemStack stack = player.getHeldItemMainhand();
        if(stack.getItem() instanceof ItemToolPlatformBuildingStick){
            NBTTagCompound tag = stack.getOrCreateSubCompound("FIP");
            if(tag.hasKey("copy_fill")){
                String str = tag.getString("copy_fill");
                if(str.equals("null")) return null;
                List<IBlockState> states = ValidBlocks.getBlockStateFromStr(str);
                if(!states.isEmpty()) return states.get(0);
            }
        }
        return null;
    }

    public IBlockState getPillarBlock(){
        EntityPlayerSP player = Minecraft.getMinecraft().player;
        if(player == null) return null;
        ItemStack stack = player.getHeldItemMainhand();
        if(stack.getItem() instanceof ItemToolPlatformBuildingStick){
            NBTTagCompound tag = stack.getOrCreateSubCompound("FIP");
            if(tag.hasKey("copy_pillar")){
                String str = tag.getString("copy_pillar");
                if(str.equals("null")) return null;
                List<IBlockState> states = ValidBlocks.getBlockStateFromStr(str);
                if(!states.isEmpty()) return states.get(0);
            }
        }
        return null;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        EntityPlayerSP player = Minecraft.getMinecraft().player;
        if(player == null) return;
        if(!(player.getHeldItemMainhand().getItem() instanceof ItemToolPlatformBuildingStick)){
            tooltip.add(I18n.format("tooltip.fip.platform_building_stick.information"));
        }else{
            int mode = getMode();
            tooltip.add(I18n.format("tooltip.fip.platform_building_stick.current_mode", Mode.values()[mode].getName()));
            switch (mode){
                case 0:
                    tooltip.add(I18n.format("tooltip.fip.platform_building_stick.mode0.current_height", getHeight()));
                    tooltip.add(I18n.format("tooltip.fip.platform_building_stick.mode0.tooltip1"));
                    tooltip.add(I18n.format("tooltip.fip.platform_building_stick.mode0.tooltip2"));
                    break;
                case 1:
                    tooltip.add(I18n.format("tooltip.fip.platform_building_stick.mode1.current_border", getBorderBlock() == null ? I18n.format("message.fip.not_set") : new ItemStack(getBorderBlock().getBlock(),1,getBorderBlock().getBlock().getMetaFromState(getBorderBlock())).getDisplayName()));
                    tooltip.add(I18n.format("tooltip.fip.platform_building_stick.mode1.current_fill", getFillBlock() == null ? I18n.format("message.fip.not_set")
                            : new ItemStack(getFillBlock().getBlock(),1,getFillBlock().getBlock().getMetaFromState(getFillBlock())).getDisplayName()));
                    tooltip.add(I18n.format("tooltip.fip.platform_building_stick.mode1.current_pillar", getPillarBlock() == null ? I18n.format("message.fip.not_set")
                            : new ItemStack(getPillarBlock().getBlock(),1,getPillarBlock().getBlock().getMetaFromState(getPillarBlock())).getDisplayName()));
                    tooltip.add(I18n.format("tooltip.fip.platform_building_stick.mode1.current_height", getHeight()));
                    tooltip.add(I18n.format("tooltip.fip.platform_building_stick.mode1.tooltip1"));
                    tooltip.add(I18n.format("tooltip.fip.platform_building_stick.mode1.tooltip2"));
                    break;
                case 2:
                    tooltip.add(I18n.format("tooltip.fip.platform_building_stick.mode2.tooltip1"));
                    break;
            }
        }
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        ItemStack heldStack = playerIn.getHeldItem(handIn);
        if(worldIn.isRemote) {
            return ActionResult.newResult(EnumActionResult.PASS, heldStack);
        }
        if(heldStack.getItem() instanceof ItemToolPlatformBuildingStick){
            NBTTagCompound tag = heldStack.getOrCreateSubCompound("FIP");
            if(playerIn.isSneaking()){
                RayTraceResult ray = playerIn.rayTrace(5.0D, 1.0F);
                if (ray == null || ray.typeOfHit == RayTraceResult.Type.MISS){
                    int mode = tag.getInteger("mode");
                    mode = (mode + 1) % Mode.values().length;
                    tag.setInteger("mode", mode);
                    playerIn.sendStatusMessage(new TextComponentString(
                            I18n.format("message.fip.mode_switch", Mode.values()[mode].getName())
                    ), false);
                    return ActionResult.newResult(EnumActionResult.SUCCESS, heldStack);
                }

            }
            return ActionResult.newResult(EnumActionResult.PASS, heldStack);
        }
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if(!worldIn.isRemote){
            if(player.getHeldItem(hand).getItem() instanceof ItemToolPlatformBuildingStick){
                NBTTagCompound tag = player.getHeldItem(hand).getOrCreateSubCompound("FIP");
                int mode = tag.getInteger("mode");
                switch (mode){
                    case 0:break;
                    case 1:
                        if(worldIn.getBlockState(pos).getBlock() instanceof BlockPlatformWrapper wrapper){
                            tag.setString("copy_border", wrapper.getBorderBlock() == null ? "null" : ValidBlocks.getStrFromBlockState(wrapper.getBorderBlock()));
                            tag.setString("copy_fill", wrapper.getFillBlock() == null ? "null" : ValidBlocks.getStrFromBlockState(wrapper.getFillBlock()));
                            player.sendStatusMessage(new TextComponentString(
                                    I18n.format("message.fip.copied_single", wrapper.getBorderBlock() == null ? I18n.format("message.fip.not_set") : new ItemStack(wrapper.getBorderBlock().getBlock(),1,wrapper.getBorderBlock().getBlock().getMetaFromState(wrapper.getBorderBlock())).getDisplayName(),
                                            wrapper.getFillBlock() == null ? I18n.format("message.fip.not_set") : new ItemStack(wrapper.getFillBlock().getBlock(),1,wrapper.getFillBlock().getBlock().getMetaFromState(wrapper.getFillBlock())).getDisplayName())
                            ), false);
                            return EnumActionResult.SUCCESS;
                        }else if(worldIn.getBlockState(pos).getBlock() instanceof BlockDoublePlatformWrapper wrapper){
                            tag.setString("copy_border", wrapper.getBorderBlock() == null ? "null" : ValidBlocks.getStrFromBlockState(wrapper.getBorderBlock()));
                            tag.setString("copy_fill", wrapper.getFillBlock() == null ? "null" : ValidBlocks.getStrFromBlockState(wrapper.getFillBlock()));
                            tag.setString("copy_pillar", wrapper.getPillarBlock() == null ? "null" : ValidBlocks.getStrFromBlockState(wrapper.getPillarBlock()));
                            tag.setInteger("copy_height", wrapper.getHeight());
                            player.sendStatusMessage(new TextComponentString(
                                    I18n.format("message.fip.copied_double")
                            ), false);
                            return EnumActionResult.SUCCESS;
                        }
                        break;
                    case 2:
                        if(worldIn.getBlockState(pos).getBlock() instanceof BlockPlatformWrapper){
                            player.sendStatusMessage(new TextComponentString(I18n.format("message.fip.switch_preview_state")), true);
                            renderSingle = !renderSingle;
                            renderPos = pos;
                            renderBorder = ((BlockPlatformWrapper)worldIn.getBlockState(pos).getBlock()).getBorderBlock();
                            renderFill = ((BlockPlatformWrapper)worldIn.getBlockState(pos).getBlock()).getFillBlock();
                        }else if(worldIn.getBlockState(pos).getBlock() instanceof BlockDoublePlatformWrapper){
                            player.sendStatusMessage(new TextComponentString(I18n.format("message.fip.switch_preview_state")), true);
                            renderDouble = !renderDouble;
                            renderPos = pos;
                            renderBorder = ((BlockDoublePlatformWrapper)worldIn.getBlockState(pos).getBlock()).getBorderBlock();
                            renderFill = ((BlockDoublePlatformWrapper)worldIn.getBlockState(pos).getBlock()).getFillBlock();
                            renderPillar = ((BlockDoublePlatformWrapper)worldIn.getBlockState(pos).getBlock()).getPillarBlock();
                            renderHeight = ((BlockDoublePlatformWrapper)worldIn.getBlockState(pos).getBlock()).getHeight();
                        }
                }
            }
        }
        return super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
    }
}
