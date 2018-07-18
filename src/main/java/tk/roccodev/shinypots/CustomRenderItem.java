package tk.roccodev.shinypots;

import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import tk.roccodev.shinypots.CustomItem;

import java.util.List;

/**
 * Created by roccodev on 18/07/18.
 */
public class CustomRenderItem extends RenderItem implements IResourceManagerReloadListener {

    private TextureManager tm;

    public CustomRenderItem(TextureManager textureManager, ModelManager modelManager) {
        super(textureManager, modelManager);
        tm = textureManager;
    }

    private CustomItem item = new CustomItem((RenderItem) (Object) this);

    @Override
    public void renderItemIntoGUI(ItemStack stack, int x, int y) {
      item.renderItemIntoGUI(stack, x, y);
    }



    public TextureManager getTextureManager() {
        return tm;
    }


    public void callSetupGuiTransform(int xPosition, int yPosition, boolean isGui3d) {
        GlStateManager.translate((float)xPosition, (float)yPosition, 100.0F + this.zLevel);
        GlStateManager.translate(8.0F, 8.0F, 0.0F);
        GlStateManager.scale(1.0F, 1.0F, -1.0F);
        GlStateManager.scale(0.5F, 0.5F, 0.5F);

        if (isGui3d)
        {
            GlStateManager.scale(40.0F, 40.0F, 40.0F);
            GlStateManager.rotate(210.0F, 1.0F, 0.0F, 0.0F);
            GlStateManager.rotate(-135.0F, 0.0F, 1.0F, 0.0F);
            GlStateManager.enableLighting();
        }
        else
        {
            GlStateManager.scale(64.0F, 64.0F, 64.0F);
            GlStateManager.rotate(180.0F, 1.0F, 0.0F, 0.0F);
            GlStateManager.disableLighting();
        }
    }


    public void callRenderModel(IBakedModel model, int e) {
        this.renderModel(model, e, null);
    }




    public void callRenderModel(IBakedModel model2, ItemStack e1) {
        renderModel(model2, -1, e1);
    }


    private void renderQuads(WorldRenderer renderer, List<BakedQuad> quads, int color, ItemStack stack) {
        boolean flag = color == -1 && stack != null;
        int i = 0;

        for (int j = quads.size(); i < j; ++i) {
            BakedQuad bakedquad = (BakedQuad) quads.get(i);
            int k = color;

            if (flag && bakedquad.hasTintIndex()) {
                k = stack.getItem().getColorFromItemStack(stack, bakedquad.getTintIndex());

                if (EntityRenderer.anaglyphEnable) {
                    k = TextureUtil.anaglyphColor(k);
                }

                k = k | -16777216;
            }

            net.minecraftforge.client.model.pipeline.LightUtil.renderQuadColor(renderer, bakedquad, k);
        }

    }







    private void renderModel(IBakedModel model2, int color, ItemStack e1) {
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.ITEM);

        for (EnumFacing enumfacing : EnumFacing.values())
        {
            this.renderQuads(worldrenderer, model2.getFaceQuads(enumfacing), color, e1);
        }

        this.renderQuads(worldrenderer, model2.getGeneralQuads(), color, e1);
        tessellator.draw();
    }

}
