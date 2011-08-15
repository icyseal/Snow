package Infinity.Snow.Event;

//All eventual commands will be processed here
//then specific calls can be sent out 
//to other classes.

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;

import Infinity.Snow.Main.ConfigManager;
import Infinity.Snow.Main.PermissionsManager;


public class CommandHandler implements CommandExecutor{

	ConfigManager configMgr;
	PermissionsManager perMgr;
	
	public CommandHandler(ConfigManager configmgr) {
		configMgr = configmgr;
		perMgr = configMgr.permissionsMgr;
	}

	public boolean miscSender(CommandSender sender, Command cmd, String alias, String[] args){//for consoles and other things
		
		return false;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String alias, String[] args) {//Event functions durrrr
		if(!(sender instanceof Player)){
			return miscSender( sender, cmd, alias, args);
		}
		//players processing should begin here
		Player plr = (Player)sender;
		
		
		
		
		
		
		

		return true;
		
	}

}
