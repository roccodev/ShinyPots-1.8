package tk.roccodev.shinypots;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemSkull;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import tk.roccodev.shinypots.mixin.IMixinShinyPots;
import tk.roccodev.shinypots.mixin.IMixinShinyPots2;

import java.util.HashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by roccodev on 16/07/18.
 */
public class CustomItem {

    private static final ResourceLocation RES_ITEM_GLINT = new ResourceLocation("textures/misc/enchanted_item_glint.png");

    private RenderItem parent;


    public CustomItem(RenderItem parent) {
        this.parent = parent;
    }



    public void renderItemIntoGUI(ItemStack stack, int x, int y) {
        IBakedModel ibakedmodel = parent.getItemModelMesher().getItemModel(stack);
        GlStateManager.pushMatrix();
        ((IMixinShinyPots) parent).getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
        ((IMixinShinyPots) parent).getTextureManager().getTexture(TextureMap.locationBlocksTexture)
                .setBlurMipmap(false, false);
        GlStateManager.enableRescaleNormal();
        GlStateManager.enableAlpha();
        GlStateManager.alphaFunc(516, 0.1F);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(770, 771);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        ((IMixinShinyPots) parent).callSetupGuiTransform(x, y, ibakedmodel.isGui3d());
        ibakedmodel.getItemCameraTransforms()
                .applyTransform(ItemCameraTransforms.TransformType.GUI);

        this.renderItem(stack, ibakedmodel, true);

        GlStateManager.disableAlpha();
        GlStateManager.disableRescaleNormal();
        GlStateManager.disableLighting();
        GlStateManager.popMatrix();
        ((IMixinShinyPots) parent).getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
        ((IMixinShinyPots) parent).getTextureManager().getTexture(TextureMap.locationBlocksTexture).restoreLastBlurMipmap();
    }


    public void renderItem(ItemStack stack, IBakedModel model, boolean isInv) {
        if (stack != null) {
            GlStateManager.pushMatrix();
            GlStateManager.scale(0.5F, 0.5F, 0.5F);


            if (model.isBuiltInRenderer()) {
                GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
                GlStateManager.translate(-0.5F, -0.5F, -0.5F);
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                GlStateManager.enableRescaleNormal();


                TileEntityItemStackRenderer.instance.renderByItem(stack);

            } else {
                // Used to detect if the item has a already had an effect rendered
                boolean renderedAsPotion = false;

                GlStateManager.translate(-0.5F, -0.5F, -0.5F);


                if (isInv && stack.getItem() != null && stack.getItem() instanceof ItemPotion) {
                    renderPot(model);

                    renderedAsPotion = true;
                }



                // Normal item renderer
                ((IMixinShinyPots2) parent).callRenderModel(model, stack);


                if (!renderedAsPotion && stack.hasEffect()) {
                    this.renderEffect(model);
                }

            }

            GlStateManager.popMatrix();
        }
    }

    /**
     * Basically the same as the above method, but does not include the depth code
     *
     * @param model the model
     */
    public void renderPot(IBakedModel model) {
        GlStateManager.depthMask(false);
        GlStateManager.disableLighting();
        GlStateManager.blendFunc(768, 1);
        ((IMixinShinyPots) parent).getTextureManager().bindTexture(RES_ITEM_GLINT);
        GlStateManager.matrixMode(5890);
        GlStateManager.pushMatrix();
        GlStateManager.scale(8.0F, 8.0F, 8.0F);
        float f = (float) (Minecraft.getSystemTime() % 3000L) / 3000.0F / 8.0F;
        GlStateManager.translate(f, 0.0F, 0.0F);
        GlStateManager.rotate(-50.0F, 0.0F, 0.0F, 1.0F);
        ((IMixinShinyPots) parent).callRenderModel(model, -8372020);
        GlStateManager.popMatrix();
        GlStateManager.pushMatrix();
        GlStateManager.scale(8.0F, 8.0F, 8.0F);
        float f1 = (float) (Minecraft.getSystemTime() % 4873L) / 4873.0F / 8.0F;
        GlStateManager.translate(-f1, 0.0F, 0.0F);
        GlStateManager.rotate(10.0F, 0.0F, 0.0F, 1.0F);
        ((IMixinShinyPots) parent).callRenderModel(model, -8372020);
        GlStateManager.popMatrix();
        GlStateManager.matrixMode(5888);
        GlStateManager.blendFunc(770, 771);
        GlStateManager.enableLighting();
        GlStateManager.depthMask(true);

        ((IMixinShinyPots) parent).getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
    }

    /**
     * Normal code for rendering an effect/enchantment on an item
     *
     * @param model the model of the item
     */
    private void renderEffect(IBakedModel model) {
        GlStateManager.depthMask(false);

        GlStateManager.depthFunc(514); // This is for render depth

        GlStateManager.disableLighting();
        GlStateManager.blendFunc(768, 1);
        ((IMixinShinyPots) parent).getTextureManager().bindTexture(RES_ITEM_GLINT);
        GlStateManager.matrixMode(5890);
        GlStateManager.pushMatrix();
        GlStateManager.scale(8.0F, 8.0F, 8.0F);
        float f = (float) (Minecraft.getSystemTime() % 3000L) / 3000.0F / 8.0F; // Animates the effect
        GlStateManager.translate(f, 0.0F, 0.0F);
        GlStateManager.rotate(-50.0F, 0.0F, 0.0F, 1.0F);
        ((IMixinShinyPots) parent).callRenderModel(model, 0);
        GlStateManager.popMatrix();
        GlStateManager.pushMatrix();
        GlStateManager.scale(8.0F, 8.0F, 8.0F);
        float f1 = (float) (Minecraft.getSystemTime() % 4873L) / 4873.0F / 8.0F;
        GlStateManager.translate(-f1, 0.0F, 0.0F);
        GlStateManager.rotate(10.0F, 0.0F, 0.0F, 1.0F);
        ((IMixinShinyPots) parent).callRenderModel(model, 0);
        GlStateManager.popMatrix();
        GlStateManager.matrixMode(5888);
        GlStateManager.blendFunc(770, 771);
        GlStateManager.enableLighting();

        GlStateManager.depthFunc(515); // Changes back to the normal depth

        GlStateManager.depthMask(true);
        ((IMixinShinyPots) parent).getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
    }


}
