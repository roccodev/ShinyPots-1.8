package tk.roccodev.shinypots.mixin;

import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import tk.roccodev.shinypots.CustomItem;


@Mixin(RenderItem.class)
public abstract class MixinShinyPots implements IResourceManagerReloadListener {


    private CustomItem item = new CustomItem((RenderItem) (Object) this);


    @Overwrite
    public void renderItemIntoGUI(ItemStack stack, int x, int y) {
        item.renderItemIntoGUI(stack, x, y);
    }


}
