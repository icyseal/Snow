package Infinity.Snow.Game.Mechanics;

//Will be used to represent ingame objects such 
//as chests, flags, and traps classified with
//objectType

import org.bukkit.Location;

import Infinity.Snow.Main.ConfigManager.objectType;

public class gameObject {
	
	public objectType type;
	public Location L;
	
	public gameObject(objectType ot, Location l){
		type = ot; L = l;
	}
}
