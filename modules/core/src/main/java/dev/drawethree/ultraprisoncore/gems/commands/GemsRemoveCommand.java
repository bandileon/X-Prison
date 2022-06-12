package dev.drawethree.ultraprisoncore.gems.commands;

import com.google.common.collect.ImmutableList;
import dev.drawethree.ultraprisoncore.api.enums.LostCause;
import dev.drawethree.ultraprisoncore.gems.UltraPrisonGems;
import dev.drawethree.ultraprisoncore.gems.managers.CommandManager;
import dev.drawethree.ultraprisoncore.utils.player.PlayerUtils;
import me.lucko.helper.utils.Players;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

public class GemsRemoveCommand extends GemsCommand {

	private static final String COMMAND_NAME = "remove";
	private static final String[] COMMAND_ALIASES = {"subtract", "delete"};

	public GemsRemoveCommand(CommandManager manager) {
		super(manager, COMMAND_NAME,COMMAND_ALIASES);
	}

	@Override
	public boolean execute(CommandSender sender, ImmutableList<String> args) {

		if (args.size() == 2) {
			try {
				long amount = Long.parseLong(args.get(1));
				OfflinePlayer target = Players.getOfflineNullable(args.get(0));
				this.commandManager.getPlugin().getGemsManager().removeGems(target, amount, sender, LostCause.ADMIN);
				return true;
			} catch (NumberFormatException e) {
				PlayerUtils.sendMessage(sender, this.commandManager.getPlugin().getMessage("not_a_number").replace("%input%", String.valueOf(args.get(0))));
			}
		}
		return false;
	}


	@Override
	public boolean canExecute(CommandSender sender) {
		return sender.hasPermission(UltraPrisonGems.GEMS_ADMIN_PERM) || sender.hasPermission(getRequiredPermission());
	}

	@Override
	public String getUsage() {
		return "/gems remove [player] [amount] - Remove gems from player.";
	}
}