package Infinity.Snow.Main;

import java.util.HashMap;
import java.util.List;


import org.bukkit.entity.Player;
import org.bukkit.util.config.*;

import Infinity.Snow.Event.SnoEventHandler;
import Infinity.Snow.Game.GameManager;

//the Settings class, settings will get loaded into here

public class ConfigManager {
	
	public enum GameMode{//enum of all teh game modes. Global included for config
		GLOBAL,CLASSIC,CLASSICG,CTF,ELIM,DEATHMTCH,CONTROL
	}
	public enum GameTeam{//enum with player teams
		RED,BLUE
	}
	public enum objectType{//Defines types for in game objects
		CHEST,FREEZE_TRAP
	}
	public enum arenaState{//Current state of arena
		CLOSED,OPEN,LOADING,IN_GAME
	}
	public enum restriction{//Gamewide restrictions
		NO_WALKING,NO_TURNING,NO_JUMPING,NO_ENTRY,NO_BREAKING,NO_BUILDING,NO_PROJECTILES,NO_RESPAWN
	}
	
	
	public final String DIR = "plugins/Snow/";
	
	//all major manager were given a copy of this class
	//this way they can access them thru the main config file
	
	public PluginMain plugin;
	public FileManager fileMgr;
	public GameManager gameMgr;
	public PermissionsManager permissionsMgr;
	public SnoEventHandler eventHandler;
	
	public ConfigManager(PluginMain pluginMain, GameManager gMgr, PermissionsManager pMgr, SnoEventHandler eHandler, FileManager fMgr) {
		plugin = pluginMain;
		fileMgr = fMgr;
		gameMgr = gMgr;
		permissionsMgr = pMgr;
		eventHandler = eHandler;
	}
	
	protected Configuration config,arenas;
	protected boolean setup = false;
	
	//Each cache will hold options based on their config key
 	protected HashMap<String,Integer>intOptionCache = new HashMap<String,Integer>();
 	protected HashMap<String,Double>doubleOptionCache = new HashMap<String,Double>();
 	protected HashMap<String,Boolean>boolOptionCache = new HashMap<String,Boolean>();
 	protected HashMap<String,String>strOptionCache = new HashMap<String,String>();
 	protected HashMap<String,Object>objOptionCache = new HashMap<String,Object>();
 	
 	public Player getPlayer(String name){
 		return plugin.getServer().getPlayer(name);
 	}
 	
 	public int getIntOption(String key) {

		return intOptionCache.get( key);

	}

	public double getDoubleOption( String key) {

		return doubleOptionCache.get( key);

	}

	public boolean getBoolOption( String key) {

		return boolOptionCache.get( key);

	}

	public String getStrOption( String key) {

		return strOptionCache.get( key);

	}

	public Object getObjOption( String key) {

		return objOptionCache.get( key);

	}
	
	public void setIntOption( String key, int value) {

		intOptionCache.put( key, value);

	}
	
	public void setDoubleOption( String key, double value) {

		doubleOptionCache.put( key, value);

	}
	
	public void setBoolOption( String key, boolean value) {

		boolOptionCache.put( key, value);

	}
	
	public void setStrOption( String key, String value) {

		strOptionCache.put( key, value);

	}
	
	public void setObjOption( String key, Object obj) {

		objOptionCache.put( key, obj);

	}
	
}
