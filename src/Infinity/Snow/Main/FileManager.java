package Infinity.Snow.Main;

//Where eventually all the saving and loading will be done the 
//arenaData class will be used to convert between files and objects


import java.io.File;
import java.util.EnumSet;
import java.util.HashMap;

import Infinity.Snow.Game.Arena;
import Infinity.Snow.Main.ConfigManager.arenaState;
import Infinity.Snow.Game.Mechanics.Fort;
import Infinity.Snow.Game.Mechanics.gameObject;
import Infinity.Snow.Main.ConfigManager.GameMode;

public class FileManager{
	
	public ConfigManager configMgr;

	public FileManager(ConfigManager cMgr) {//Standard constructor. Gives configMangager object

		configMgr = cMgr;

	}
	
	public void initConfig(){
		configMgr.plugin.log("Initializing config");
		configMgr.config = configMgr.plugin.getConfiguration();
		if(new File(configMgr.DIR+"config.yml").exists()){
			configMgr.plugin.log("Loading found data");
			loadConfig();
			loadArenas();
		}else{
			configMgr.plugin.log("Installing Snow config files");
			setupConfig();
		}
		configMgr.setup = true;
		
	}

	public void loadConfig() {//Loads all game config settings
		
		configMgr.config.load();
		
		//Load global settings
		loadConfigItem("global.SNOWBALL_STARTING_AMOUNT","SNOWBALL_STARTING_AMOUNT",256);
		loadConfigItem("global.MAX_PLAYERS","MAX_PLAYERS",8);
		loadConfigItem("global.MAX_ARENAS","MAX_ARENAS",9);
		loadConfigItem("global.MAX_ARENA_DIMS","MAX_ARENA_DIMS",400);
		loadConfigItem("global.SNOWBALL_REFILL_DELAY","SNOWBALL_REFILL_DELAY", 75.0);
		//Default game-specific config
		loadConfigItem("classic.BUILD_TIME","CLASSIC_BUILD_TIME", 300.0);
		loadConfigItem("classic.GAME_TIME","CLASSIC_GAME_TIME", 300.0);
		loadConfigItem("ctf.GAME_TIME","CTF_GAME_TIME", 300.0);
		loadConfigItem("elim.GAME_TIME","ELIM_GAME_TIME", 300.0);
		loadConfigItem("deathmtch.GAME_TIME","DEATHMTCH_GAME_TIME", 300.0);
		loadConfigItem("control.GAME_TIME","CONTROL_GAME_TIME", 300.0);
		loadConfigItem("control.DELAY_TIME","CONTROL_DELAY_TIME", 300.0);
		loadConfigItem("ctf.EG_SCORE","CTF_EG_SCORE", 3);
		loadConfigItem("elim.EG_SCORE","ELIM_EG_SCORE", 40);
		loadConfigItem("classic.HITS_RESPAWN","CLASSIC_HITS_RESPAWN", 3);
		loadConfigItem("ctf.HITS_RESPAWN","CTF_HITS_RESPAWN", 3);
		loadConfigItem("control.HITS_RESPAWN","CONTROL_HITS_RESPAWN", 3);
		loadConfigItem("ctf.FLAG_ID","CTF_FLAG_ID", 50);
		
	}
	
	public void loadConfigItem(String path, String Key, int def){
		configMgr.intOptionCache.put(Key, configMgr.config.getInt(path, def));
	}
	public void loadConfigItem(String path, String Key, double def){
		configMgr.doubleOptionCache.put(Key, configMgr.config.getDouble(path, def));
	}
	public void loadConfigItem(String path, String Key, String def){
		configMgr.strOptionCache.put(Key, configMgr.config.getString(path, def));
	}
	public void loadConfigItem(String path, String Key, boolean def){
		configMgr.boolOptionCache.put(Key, configMgr.config.getBoolean(path, def));
	}
	public void loadConfigItem(String path, String Key){
		configMgr.objOptionCache.put(Key, configMgr.config.getProperty(path));
	}
	
	public void reloadConfig(){
		saveConfig();
		initConfig();
		
	}
	
	private void setupConfig() {
		//Default global config
		configMgr.intOptionCache.put("SNOWBALL_STARTING_AMOUNT", 256);
		configMgr.intOptionCache.put("MAX_PLAYERS", 8);
		configMgr.intOptionCache.put("MAX_ARENAS", 9);
		configMgr.doubleOptionCache.put("MAX_ARENA_DIMS", 400.0);
		configMgr.doubleOptionCache.put("SNOWBALL_REFILL_DELAY", 75.0);//75 seconds between global snowball spawn 
		
		configMgr.config.setProperty("global.SNOWBALL_STARTING_AMOUNT", configMgr.intOptionCache.get("SNOWBALL_STARTING_AMOUNT"));
		configMgr.config.setProperty("global.MAX_PLAYERS", configMgr.intOptionCache.get("MAX_PLAYERS"));
		configMgr.config.setProperty("global.MAX_ARENAS", configMgr.intOptionCache.get("MAX_ARENAS"));
		configMgr.config.setProperty("global.MAX_ARENA_DIMS", configMgr.intOptionCache.get("MAX_ARENA_DIMS"));
		configMgr.config.setProperty("global.SNOWBALL_REFILL_DELAY", configMgr.intOptionCache.get("SNOWBALL_REFILL_DELAY"));
		
		//Default game-specific config
		
		configMgr.doubleOptionCache.put("CLASSIC_BUILD_TIME", 300.0);
		configMgr.doubleOptionCache.put("CLASSIC_GAME_TIME", 300.0);
		configMgr.doubleOptionCache.put("CTF_GAME_TIME", 300.0);
		configMgr.doubleOptionCache.put("ELIM_GAME_TIME", 300.0);
		configMgr.doubleOptionCache.put("DEATHMTCH_GAME_TIME", 300.0);
		configMgr.doubleOptionCache.put("CONTROL_GAME_TIME", 300.0);
		configMgr.doubleOptionCache.put("CONTROL_DELAY_TIME", 300.0);
		configMgr.intOptionCache.put("CTF_EG_SCORE", 3);
		configMgr.intOptionCache.put("ELIM_EG_SCORE", 40);
		configMgr.intOptionCache.put("CLASSIC_HITS_RESPAWN", 3);
		configMgr.intOptionCache.put("CTF_HITS_RESPAWN", 3);
		configMgr.intOptionCache.put("CONTROL_HITS_RESPAWN", 3);
		configMgr.intOptionCache.put("CTF_FLAG_ID", 50);
		
		configMgr.config.setProperty("classic.BUILD_TIME", configMgr.doubleOptionCache.get("CLASSIC_BUILD_TIME"));
		configMgr.config.setProperty("classic.GAME_TIME", configMgr.doubleOptionCache.get("CLASSIC_GAME_TIME"));
		configMgr.config.setProperty("ctf.GAME_TIME", configMgr.doubleOptionCache.get("CTF_GAME_TIME"));
		configMgr.config.setProperty("elim.GAME_TIME", configMgr.doubleOptionCache.get("ELIM_GAME_TIME"));
		configMgr.config.setProperty("deathmatch.GAME_TIME", configMgr.doubleOptionCache.get("DEATHMTCH_GAME_TIME"));
		configMgr.config.setProperty("control.GAME_TIME", configMgr.doubleOptionCache.get("CLASSIC_GAME_TIME"));
		configMgr.config.setProperty("control.DELAY_TIME", configMgr.doubleOptionCache.get("CONTROL_DELAY_TIME"));
		configMgr.config.setProperty("ctf.EG_SCORE", configMgr.intOptionCache.get("CTF_EG_SCORE"));
		configMgr.config.setProperty("elim.EG_SCORE", configMgr.intOptionCache.get("ELIM_EG_SCORE"));
		configMgr.config.setProperty("classic.HITS_RESPAWN", configMgr.intOptionCache.get("CLASSIC_HITS_RESPAWN"));
		configMgr.config.setProperty("ctf.HITS_RESPAWN", configMgr.intOptionCache.get("CTF_HITS_RESPAWN"));
		configMgr.config.setProperty("control.HITS_RESPAWN", configMgr.intOptionCache.get("CONTROL_HITS_RESPAWN"));
		configMgr.config.setProperty("ctf.FLAG_ID", configMgr.intOptionCache.get("CTF_FLAG_ID"));
		
		
		configMgr.config.save(); 
		
	}

	public void saveConfig() {
		//Setting global properties
		configMgr.config.setProperty("global.SNOWBALL_STARTING_AMOUNT", configMgr.intOptionCache.get("SNOWBALL_STARTING_AMOUNT"));
		configMgr.config.setProperty("global.MAX_PLAYERS", configMgr.intOptionCache.get("MAX_PLAYERS"));
		configMgr.config.setProperty("global.MAX_ARENAS", configMgr.intOptionCache.get("MAX_ARENAS"));
		configMgr.config.setProperty("global.MAX_ARENA_DIMS", configMgr.intOptionCache.get("MAX_ARENA_DIMS"));
		configMgr.config.setProperty("global.SNOWBALL_REFILL_DELAY", configMgr.intOptionCache.get("SNOWBALL_REFILL_DELAY"));
		//Game Mode Specific properties
		configMgr.config.setProperty("classic.BUILD_TIME", configMgr.doubleOptionCache.get("CLASSIC_BUILD_TIME"));
		configMgr.config.setProperty("classic.GAME_TIME", configMgr.doubleOptionCache.get("CLASSIC_GAME_TIME"));
		configMgr.config.setProperty("ctf.GAME_TIME", configMgr.doubleOptionCache.get("CTF_GAME_TIME"));
		configMgr.config.setProperty("elim.GAME_TIME", configMgr.doubleOptionCache.get("ELIM_GAME_TIME"));
		configMgr.config.setProperty("deathmatch.GAME_TIME", configMgr.doubleOptionCache.get("DEATHMTCH_GAME_TIME"));
		configMgr.config.setProperty("control.GAME_TIME", configMgr.doubleOptionCache.get("CLASSIC_GAME_TIME"));
		configMgr.config.setProperty("control.DELAY_TIME", configMgr.doubleOptionCache.get("CONTROL_DELAY_TIME"));
		configMgr.config.setProperty("ctf.EG_SCORE", configMgr.intOptionCache.get("CTF_EG_SCORE"));
		configMgr.config.setProperty("elim.EG_SCORE", configMgr.intOptionCache.get("ELIM_EG_SCORE"));
		configMgr.config.setProperty("classic.HITS_RESPAWN", configMgr.intOptionCache.get("CLASSIC_HITS_RESPAWN"));
		configMgr.config.setProperty("ctf.HITS_RESPAWN", configMgr.intOptionCache.get("CTF_HITS_RESPAWN"));
		configMgr.config.setProperty("control.HITS_RESPAWN", configMgr.intOptionCache.get("CONTROL_HITS_RESPAWN"));
		configMgr.config.setProperty("ctf.FLAG_ID", configMgr.intOptionCache.get("CTF_FLAG_ID"));
		
		configMgr.config.save();
		
	}

	public void saveArena(Arena ar){
		String root = ar.name; 
		configMgr.arenas.setProperty(ar.name, null);
		for( GameMode gm: EnumSet.range(GameMode.CLASSIC, GameMode.CONTROL)){
			if(ar.allowedModes.contains(gm)){
				configMgr.arenas.setProperty(root+".ALLOWED_MODES."+gm.name(),gm.name());
			}
		}
		configMgr.arenas.setProperty(root+".CURRENT_STATE",ar.currentState.name());
		configMgr.arenas.setProperty(root+".WORLD",ar.Lobby.getWorld().getName());
		int i = 0;
		if(ar.Forts.size()>0){
			for( Fort f: ar.Forts){
				configMgr.arenas.setProperty(root+".FORTS."+i+".MIN",ar.locToStr(f.getBoundMin()));
				configMgr.arenas.setProperty(root+".FORTS."+i+".MAX",ar.locToStr(f.getBoundMax()));
				i++;
			}i=0;
		}
		if(ar.gameObjects.size()>0){
			for( gameObject go: ar.gameObjects){
				configMgr.arenas.setProperty(root+".GAME_OBJ."+i+".LOCATION",ar.locToStr(go.L));
				configMgr.arenas.setProperty(root+".GAME_OBJ."+i+".TYPE",go.type.name());
				i++;
			}
		}
		
		configMgr.arenas.setProperty(root+".LOBBY", ar.locToStr(ar.Lobby));
		configMgr.arenas.setProperty(root+".BENCH", ar.locToStr(ar.Bench));
		configMgr.arenas.setProperty(root+".RSPAWN", ar.locToStr(ar.rSpawn));
		configMgr.arenas.setProperty(root+".BSPAWN", ar.locToStr(ar.bSpawn));
		configMgr.arenas.setProperty(root+".RFLAG", ar.locToStr(ar.rFlag));
		configMgr.arenas.setProperty(root+".BFLAG", ar.locToStr(ar.bFlag));
		
	}
	
	public void saveArenas(){
		HashMap<String, Arena> arList = configMgr.gameMgr.getArenaCache();
		for(Arena ar: arList.values()){
			saveArena(ar);
		}
		
		configMgr.arenas.save();
		configMgr.plugin.log("Arenas saved");
	}
	
	@SuppressWarnings("unused")
	public Arena loadArena(String name){
		String root = name;
		Arena ar = new Arena(configMgr.gameMgr,configMgr,configMgr.plugin.getServer().getWorld(configMgr.arenas.getString(root+".WORLD")));
		for( GameMode gm: EnumSet.range(GameMode.CLASSIC, GameMode.CONTROL)){
			if(configMgr.arenas.getKeys().contains(gm.name())){
				ar.allowedModes.add(gm);
			}
		}
		for(arenaState as: arenaState.values()){
			if(configMgr.arenas.getString(root+".CURRENT_STATE", "null").equalsIgnoreCase(as.name())){
				ar.currentState = as;
			}
					
		}
		int i = 0;
		if(configMgr.arenas.getKeys(root+".FORTS").size()>0){
		
			for( String n: configMgr.arenas.getKeys(root+".FORTS")){
				Fort f = new Fort(ar);
				f.setBounds(ar.strToLoc(configMgr.arenas.getString(root+".FORTS."+i+".MIN"), ar.getWorld()), ar.strToLoc(configMgr.arenas.getString(root+".FORTS."+i+".MAX"), ar.getWorld()));
				f.setWorld(ar.getWorld());
				ar.Forts.add(f);
				i++;
			}i=0;
		}
		if(configMgr.arenas.getKeys(root+".GAME_OBJ").size()>0){
			
			for(  String n: configMgr.arenas.getKeys(root+".GAME_OBJ")){
				gameObject go;
				go = new gameObject(configMgr.gameMgr.getObjType(configMgr.arenas.getString(root+".GAME_OBJ."+i+".TYPE")),
						ar.strToLoc(configMgr.arenas.getString(root+".GAME_OBJ."+i+".LOCATION"), ar.getWorld()));
				ar.gameObjects.add(go);
				i++;
			}i=0;
		}
		ar.Lobby = ar.strToLoc(configMgr.arenas.getString(root+".LOBBY"),ar.getWorld());
		ar.Bench = ar.strToLoc(configMgr.arenas.getString(root+".BENCH"),ar.getWorld());
		ar.rSpawn = ar.strToLoc(configMgr.arenas.getString(root+".RSPAWN"),ar.getWorld());
		ar.bSpawn = ar.strToLoc(configMgr.arenas.getString(root+".BSPAWN"),ar.getWorld());
		ar.rFlag = ar.strToLoc(configMgr.arenas.getString(root+".RFLAG"),ar.getWorld());
		ar.bFlag = ar.strToLoc(configMgr.arenas.getString(root+".BFLAG"),ar.getWorld());
		
		
		return ar;
		
	}
	
	public void loadArenas(){
		configMgr.arenas.load();
		for(String name:configMgr.arenas.getKeys()){
			configMgr.gameMgr.registerArena(name,loadArena( name));
		}
		configMgr.plugin.log("Arenas loaded");
		
	}
 
}
