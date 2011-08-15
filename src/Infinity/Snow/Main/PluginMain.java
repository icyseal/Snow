package Infinity.Snow.Main;

//the main class. u should know what it does

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import Infinity.Snow.Game.GameManager;
import Infinity.Snow.Event.SnoEventHandler;



public class PluginMain extends JavaPlugin{
	
	//Management Classes and Shit
	public FileManager fileMgr;
	public ConfigManager configMgr;
	public GameManager gameMgr = new GameManager(configMgr);
	public PermissionsManager permissionsMgr = new PermissionsManager(configMgr);
	public SnoEventHandler eventHandler = new SnoEventHandler(configMgr);
	public Logger plLog = this.getServer().getLogger();
	
	
	//Basic Functions
	public void sm(Player p, String message){
		p.sendMessage("[Sno]"+message);
	}
	
	public void output(String msg){
		System.out.println("[Sno]"+msg);
	}
	
	public void log(String msg){
		plLog.info("[Sno]"+msg);
	}
	
	@Override
	public void onDisable() {
		fileMgr.saveConfig();
		fileMgr.saveArenas();
		configMgr.setup = false;
		output("Snow saved and disabled.");

	}

	@Override
	public void onEnable() {
		configMgr = new ConfigManager(this,gameMgr,permissionsMgr,eventHandler,fileMgr);
		
		if (new File(configMgr.DIR).mkdir()) {
			output("Created /Snow directory.");
		}
		
		fileMgr = new FileManager( configMgr);

		permissionsMgr.initPermissons();
		eventHandler.registerEvents();
		

	}
	

}
