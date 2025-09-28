package blackfox.fast_industrial_platform.common.structure;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AutoBuilder {
    private static Logger logger = LogManager.getLogger("FIP AutoBuilder");

    public static void buildSingleLayer(World world, BlockPos pos, IBlockState border, IBlockState fill){
        int y = pos.getY();
        int baseX = Math.floorDiv(pos.getX(), 16) * 16;
        int baseZ = Math.floorDiv(pos.getZ(), 16) * 16;
        int maxX = baseX + 16;
        int maxZ = baseZ + 16;
        for(int z = baseZ; z < maxZ ; z++){
            for(int x = baseX; x < maxX ; x++){
                if(x == baseX || x == maxX - 1 || z == baseZ || z == maxZ - 1){
                    world.setBlockState(new BlockPos(x,y,z),border);
                } else {
                    world.setBlockState(new BlockPos(x,y,z),fill);
                }
            }
        }
    }

    public static void buildDoubleLayer(World world, BlockPos pos, IBlockState border, IBlockState fill, IBlockState pillar, int height) {
        buildSingleLayer(world, pos, border, fill);
        BlockPos firstCorner = new BlockPos(Math.floorDiv(pos.getX(), 16) * 16, pos.getY(), Math.floorDiv(pos.getZ(), 16) * 16);
        BlockPos secondCorner = new BlockPos(firstCorner.getX() + 15, pos.getY(), firstCorner.getZ() + 15);
        BlockPos thirdCorner = new BlockPos(firstCorner.getX(), pos.getY(), firstCorner.getZ() + 15);
        BlockPos fourthCorner = new BlockPos(firstCorner.getX() + 15, pos.getY(), firstCorner.getZ());
        int maxY = pos.getY() + height;
        for(int y = pos.getY() + 1; y <= maxY; y++){
            world.setBlockState(new BlockPos(firstCorner.getX(), y, firstCorner.getZ()), pillar);
            world.setBlockState(new BlockPos(secondCorner.getX(), y, secondCorner.getZ()), pillar);
            world.setBlockState(new BlockPos(thirdCorner.getX(), y, thirdCorner.getZ()), pillar);
            world.setBlockState(new BlockPos(fourthCorner.getX(), y, fourthCorner.getZ()), pillar);
        }
        buildSingleLayer(world, new BlockPos(pos.getX(), maxY + 1, pos.getZ()), border, fill);
    }

    public static void renderSingleLayerPreview(BlockPos pos, IBlockState border, IBlockState fill){
        int y = pos.getY();
        int baseX = Math.floorDiv(pos.getX(), 16) * 16;
        int baseZ = Math.floorDiv(pos.getZ(), 16) * 16;
        int maxX = baseX + 16;
        int maxZ = baseZ + 16;
        Minecraft mc = Minecraft.getMinecraft();
        World world = mc.world;
        if(world == null) return;
        double viewerX = mc.getRenderManager().viewerPosX;
        double viewerY = mc.getRenderManager().viewerPosY;
        double viewerZ = mc.getRenderManager().viewerPosZ;

        BlockRendererDispatcher dispatcher = mc.getBlockRendererDispatcher();

        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        GlStateManager.enablePolygonOffset();
        GlStateManager.doPolygonOffset(-1, -1);

        for(int z = baseZ; z < maxZ ; z++){
            for(int x = baseX; x < maxX ; x++){
                GlStateManager.pushMatrix();
                GlStateManager.translate(
                        x - viewerX,
                        y - viewerY,
                        z - viewerZ + 1
                );
                GlStateManager.translate(0.5, 0.5, -0.5);
                GlStateManager.scale(0.8f, 0.8f, 0.8f);
                GlStateManager.translate(-0.5, -0.5, +0.5);
                GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                if(x == baseX || x == maxX - 1 || z == baseZ || z == maxZ - 1){
                    if(border != null){
                        dispatcher.renderBlockBrightness(border, 1.0f);
                    }
                } else {
                    if(fill != null){
                        dispatcher.renderBlockBrightness(fill, 1.0f);
                    }
                }
                GlStateManager.popMatrix();
            }
        }

        GlStateManager.disablePolygonOffset();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }

    public static void renderDoubleLayerPreview(BlockPos pos, IBlockState border, IBlockState fill, IBlockState pillar, int height){
        renderSingleLayerPreview(pos,border,fill);

        BlockPos firstCorner = new BlockPos(Math.floorDiv(pos.getX(), 16) * 16, pos.getY(), Math.floorDiv(pos.getZ(), 16) * 16);
        BlockPos secondCorner = new BlockPos(firstCorner.getX() + 15, pos.getY(), firstCorner.getZ() + 15);
        BlockPos thirdCorner = new BlockPos(firstCorner.getX(), pos.getY(), firstCorner.getZ() + 15);
        BlockPos fourthCorner = new BlockPos(firstCorner.getX() + 15, pos.getY(), firstCorner.getZ());
        int maxY = pos.getY() + height;
        Minecraft mc = Minecraft.getMinecraft();
        World world = mc.world;
        if(world == null) return;
        double viewerX = mc.getRenderManager().viewerPosX;
        double viewerY = mc.getRenderManager().viewerPosY;
        double viewerZ = mc.getRenderManager().viewerPosZ;
        BlockRendererDispatcher dispatcher = mc.getBlockRendererDispatcher();
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        GlStateManager.enablePolygonOffset();
        GlStateManager.doPolygonOffset(-1, -1);

        for(int y = pos.getY() + 1; y <= maxY; y++){
            int[][] corners = {
                    {firstCorner.getX(), firstCorner.getZ()},
                    {secondCorner.getX(), secondCorner.getZ()},
                    {thirdCorner.getX(), thirdCorner.getZ()},
                    {fourthCorner.getX(), fourthCorner.getZ()}
            };
            for(int[] corner : corners){
                GlStateManager.pushMatrix();
                GlStateManager.translate(
                        corner[0] - viewerX,
                        y - viewerY,
                        corner[1] - viewerZ + 1
                );
                GlStateManager.translate(0.5, 0.5, -0.5);
                GlStateManager.scale(0.8f, 0.8f, 0.8f);
                GlStateManager.translate(-0.5, -0.5, +0.5);
                GlStateManager.color(1.0f, 1.0f, 1.0f, 0.7f);
                if(pillar != null) {
                    dispatcher.renderBlockBrightness(pillar, 0.8f);
                }
                GlStateManager.popMatrix();
            }
        }

        renderSingleLayerPreview(pos.add(0,height+1,0),border,fill);
        GlStateManager.disablePolygonOffset();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }

}
