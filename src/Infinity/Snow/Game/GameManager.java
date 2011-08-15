package Infinity.Snow.Game;

//The massive control class for adding arenas, as well as 
//the arena, active games, and player caches are held here.
//There are also the game related enums defined here

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;

import org.bukkit.entity.Player;

import Infinity.Snow.Game.Timers.ActiveGame;
import Infinity.Snow.Main.ConfigManager;
import Infinity.Snow.Main.ConfigManager.GameMode;
import Infinity.Snow.Main.ConfigManager.arenaState;
import Infinity.Snow.Main.ConfigManager.objectType;

public class GameManager{
	
	public ConfigManager configMgr;
	
	private HashMap<String,Arena>arenaCache = new HashMap<String,Arena>();
	private ArrayList<ActiveGame>activeGameCache = new ArrayList<ActiveGame>();
	private ArrayList<Player>playerCache = new ArrayList<Player>();
	
	public GameManager(ConfigManager config) {//gives access to settings
		
		configMgr = config;
		
	}
	
	public boolean registerArena( String name, Arena ar){//Registers an already setup Arena to the cache
		if(arenaCache.size() < configMgr.getIntOption("MAX_ARENAS")){
			arenaCache.put(name, ar);
			return true;
		}else{
			return false;
		}
	}
	
	public void removeArena(String name){//Overloaded function, removes an arena from the map and from the 
		arenaCache.get(name).remove();
		arenaCache.remove(name);
	}
	
	public void removeArena(Arena ar){//The overload, based on known area
		arenaCache.remove(ar.name);
		ar.remove();
		
	}
	
	public objectType getObjType(String s){//Just for loading
		for(objectType ot: EnumSet.allOf(objectType.class)){
			if(ot.name().equalsIgnoreCase(s)){
				return ot;
			}
		}
		return null;
	}

	public ActiveGame getPlayerIsPlaying(Player plr){//just gets the game a player is in null for not playing a game
		for(ActiveGame ag: activeGameCache){
			if (ag.players.contains(plr)){
				return ag;
			}
		}
		return null;
	}
	
	public HashMap<String, Arena> getArenaCache(){//gets the arena cache
		return arenaCache;
	}
	
	public void addPlayer(Player plr){//Adds a player to the cache
		playerCache.add(plr);
	}
	
	public void removePlayer(Player plr){//Removes a player from the cache
		playerCache.remove(plr);
	}
	
	public boolean beginGame(Arena ar, GameMode gMode, Player host){//to be called from the commmand handler when a player starts a game
		if(activeGameCache.contains(ar) && !ar.allowedModes.contains(gMode) && ar.currentState==arenaState.OPEN){
			return false;
		}else {
			activeGameCache.add(new ActiveGame( ar, gMode, host, this));
			return true;
		}
		
	}
	
	public void endGame(ActiveGame ag){
		ag.broadcast("Thank you for playing");
		for(Player p: ag.players){
			p.teleport(ag.arena.Lobby);
			playerCache.remove(p);
		}
		activeGameCache.remove(ag);
	}
}
