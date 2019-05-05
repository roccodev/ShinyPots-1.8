package tk.roccodev.shinypots;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.io.IOException;

public class SettingsGui extends GuiScreen {
    @Override
    public void initGui() {
        this.buttonList.add(new GuiButton(0, width / 2 - 75, height / 2 - 10, 150, 20, "Shiny Pots: " +
                Main.mode.getDisplay()));
        super.initGui();
    }

    public void show() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onTick(TickEvent.PlayerTickEvent event) {
        MinecraftForge.EVENT_BUS.unregister(this);
        Minecraft.getMinecraft().displayGuiScreen(this);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if(button.id == 0) {
            switch(Main.mode) {
                case DISABLED:
                    Main.mode = Mode.NORMAL;
                    break;
                case NORMAL:
                    Main.mode = Mode.COLOR;
                    break;
                case COLOR:
                    Main.mode = Mode.DISABLED;
                    break;
            }
            button.displayString = "Mode: " + Main.mode.getDisplay();
        }
        super.actionPerformed(button);
    }

    @Override
    public void onGuiClosed() {
        Main.instance.saveConfig();
        super.onGuiClosed();
    }
}
