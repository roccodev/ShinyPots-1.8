package tk.roccodev.shinypots;

import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import org.lwjgl.opengl.GL11;
import tk.roccodev.shinypots.CustomItem;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * Created by roccodev on 18/07/18.
 */
public class CustomRenderItem extends RenderItem implements IResourceManagerReloadListener {

    private TextureManager tm;
    private HashMap<Integer, Integer> renderCache = new HashMap<>();

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


    public void callRenderQuads(WorldRenderer renderer, List<BakedQuad> quads, int color, ItemStack stack) {
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
        int hashValue;
        int hash;
        {
            String name = e1 != null ? e1.getUnlocalizedName() : "";
            int dmg = e1 != null ? e1.getItemDamage() : 0;
            int meta = e1 != null ? e1.getMetadata() : 0;
            NBTTagCompound tags = e1 != null ? e1.getTagCompound() : null;

            hash = Objects.hash(model2, color, name, dmg, meta, tags);
        }
        Integer cached = renderCache.get(hash);
        if(cached != null) {
            GlStateManager.callList(cached);
            GlStateManager.resetColor();
            return;
        }

        hashValue = GLAllocation.generateDisplayLists(1);
        GL11.glNewList(hashValue, GL11.GL_COMPILE_AND_EXECUTE);

        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.ITEM);

        for (EnumFacing enumfacing : EnumFacing.values())
        {
            this.callRenderQuads(worldrenderer, model2.getFaceQuads(enumfacing), color, e1);
        }

        this.callRenderQuads(worldrenderer, model2.getGeneralQuads(), color, e1);
        tessellator.draw();

        GL11.glEndList();
        renderCache.put(hash, hashValue);
    }

}
