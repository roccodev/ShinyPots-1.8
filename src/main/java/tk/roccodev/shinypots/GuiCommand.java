package tk.roccodev.shinypots;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;

public class GuiCommand extends CommandBase {
    @Override
    public String getCommandName() {
        return "shinypots";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/shinypots";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        new SettingsGui().show();
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }
}
