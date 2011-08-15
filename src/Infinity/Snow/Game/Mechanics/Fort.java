package Infinity.Snow.Game.Mechanics;

//Used to represent any of the various area types
//used in an arena. classified by the areatype enum

import org.bukkit.entity.Player;

import Infinity.Snow.Game.Arena;
import Infinity.Snow.Game.Timers.ActiveGame;
import Infinity.Snow.Main.ConfigManager.GameTeam;

public class Fort extends snoCuboid{
	
	protected Arena arena;//arena it is a part of
	protected GameTeam controller;//what team controls the fort. null if the fort is neutral or not a fort
	
	
	
	public Fort(Arena ar){
		arena = ar;
	}
	
	public Arena getArena(){
		return arena;
	}
	public void setArena(Arena a){
		arena = a;
	}
	
	public void updateController(ActiveGame ag){
		boolean red = false; boolean blue = false;controller=null;
		for(Player plr: ag.players){
			if( isInside(plr.getLocation())){
				if(ag.getTeam(plr)==GameTeam.RED){
					red=true;
					controller = GameTeam.RED;
				}else{
					blue=true;
					controller = GameTeam.BLUE;
				}
			}
		}
		if(red && blue){
			controller = null;
		}
	}
	
	public GameTeam getController(){
		return controller;
	}
	public void setController(GameTeam gt){
		controller = gt;
	}
	
	
}