package Infinity.Snow.Game;

//Data class used to represent a game arena,
//it contains all neccessary information on
//areas and other mechanics for the game.

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.World;

import Infinity.Snow.Main.ConfigManager.arenaState;
import Infinity.Snow.Game.Mechanics.Fort;
import Infinity.Snow.Game.Mechanics.gameObject;
import Infinity.Snow.Game.Mechanics.snoCuboid;
import Infinity.Snow.Main.ConfigManager;
import Infinity.Snow.Main.ConfigManager.GameMode;



public class Arena extends snoCuboid{
	//For info access
	public GameManager GM;//reference to global game controller
	public ConfigManager Config;
	public String name;
	
	//Info
	public arenaState currentState = arenaState.CLOSED;//Defines what is the status of use for the arena. Starts closed, opens when it is completed.
	public ArrayList<GameMode>allowedModes = new ArrayList<GameMode>();//What game modes are allowed to be played in this arena
	public ArrayList<Fort>Forts = new ArrayList<Fort>();//List of all the forts
	public ArrayList<gameObject>gameObjects = new ArrayList<gameObject>();// in game objects durr
	public Location Lobby,Bench,rSpawn,bSpawn,rFlag,bFlag;//Listing all locations in order Lobby,Bench,RedSpawn,BlueSpawn
	
	
	
	
	public Arena(GameManager gm, ConfigManager config, World W){
		GM = gm;
		Config = config;
		world = W;
	}
	
	public void remove(){
		//does nothing yet. Later version probably
		//will most likely restore the land to it's original look
	}

	public String locToStr(Location L){
		if(L!=null){
			return L.getBlockX()+","+L.getBlockY()+","+L.getBlockZ();
		}else{
			return null;
		}
	}
	
	public Location strToLoc(String s, World w){
		String[] L = s.split(",");
		return new Location(w, Double.valueOf(L[0]), Double.valueOf(L[1]), Double.valueOf(L[2]));
	}

	public boolean toggleMode(GameMode gm) {
		if(allowedModes.contains(gm)){
			allowedModes.remove(gm);
			return false;
		}else{
			allowedModes.add(gm);
			return true;
		}
		
	}

	
}
