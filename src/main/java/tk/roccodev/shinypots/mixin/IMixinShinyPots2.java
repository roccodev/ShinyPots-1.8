package tk.roccodev.shinypots.mixin;

import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

/**
 * Created by roccodev on 16/07/18.
 * */
 @Mixin(RenderItem.class)
 public interface IMixinShinyPots2 {

 @Invoker
 void callRenderModel(IBakedModel model2, ItemStack e1);
 }