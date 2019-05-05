package tk.roccodev.shinypots;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by roccodev on 16/07/18.
 */
@Mod(modid="shinypots-1.8", name = "ShinyPots", version = "1.1")
public class Main {

    public static Mode mode = Mode.NORMAL;
    public static Property modeProperty;

    @Mod.Instance
    public static Main instance;
    private Configuration config;

    public void injectRenderItem() throws Exception {
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
        ClientCommandHandler.instance.registerCommand(new GuiCommand());
        try {
            injectRenderItem();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Mod.EventHandler
    public void onFMLPre(FMLPreInitializationEvent evt) {
        config = new Configuration(evt.getSuggestedConfigurationFile());
        loadConfig();
    }

    private void loadConfig() {
        config.load();

        modeProperty = config.get(Configuration.CATEGORY_GENERAL, "mode", 1, "The mode");
        Main.mode = Mode.values()[modeProperty.getInt(1)];
    }

    void saveConfig() {
        config.save();
    }

}
