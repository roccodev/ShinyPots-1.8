package tk.roccodev.shinypots;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by roccodev on 16/07/18.
 */
@Mod(modid="shinypots-1.8", name = "ShinyPots", version = "1.1")
public class Main {


    public void injectRenderItem() throws NoSuchFieldException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        Class clazz = Minecraft.class;



        boolean devEnv = false; // Replace with 'true' if in a dev environment
        String modelManagerS = devEnv ? "modelManager" : "field_175617_aL";
        String renderItemS = devEnv ? "renderItem" : "field_175621_X";
        String mcRmS = devEnv ? "mcResourceManager" : "field_110451_am";


        Field mmField = clazz.getDeclaredField(modelManagerS);
        mmField.setAccessible(true);
        ModelManager mm = (ModelManager) mmField.get(Minecraft.getMinecraft());


        Field renderItemField = clazz.getDeclaredField(renderItemS);
        renderItemField.setAccessible(true);
        CustomRenderItem cri =  new CustomRenderItem(Minecraft.getMinecraft().renderEngine, mm);
        renderItemField.set(Minecraft.getMinecraft(), cri);

        cri.getItemModelMesher().getModelManager().onResourceManagerReload(Minecraft.getMinecraft().getResourceManager());


        Field rmField = clazz.getDeclaredField(mcRmS);
        rmField.setAccessible(true);
        IReloadableResourceManager rm = (IReloadableResourceManager) rmField.get(Minecraft.getMinecraft());
        rm.registerReloadListener(cri);





    }

    @Mod.EventHandler
    public void onFMLInit(FMLInitializationEvent evt) {
        try {
            injectRenderItem();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

}
