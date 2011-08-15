package Infinity.Snow.Event;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import Infinity.Snow.Game.Arena;
import Infinity.Snow.Main.ConfigManager.arenaState;
import Infinity.Snow.Main.ConfigManager.objectType;
import Infinity.Snow.Game.Mechanics.Fort;
import Infinity.Snow.Game.Mechanics.gameObject;
import Infinity.Snow.Main.ConfigManager;
import Infinity.Snow.Main.ConfigManager.GameMode;

public class SetupHandler {
	
	
	
	protected ConfigManager configMgr;
	protected Player constructor;
	protected boolean editing;
	protected Arena arena;
	
	
	
	public SetupHandler(ConfigManager configmgr, Player p) {
		
		configMgr = configmgr;
		constructor = p;
		
	}
	
	public void startArena(boolean edit){
		editing = edit;
		arena = new Arena(configMgr.gameMgr, configMgr, constructor.getWorld());
		
	}
	
 	public boolean setArenaArea(Player plr){//Sets the arena area
		if (configMgr.eventHandler.pSelection.get(plr.getName())!=null){
			if (configMgr.eventHandler.pSelection.get(plr.getName()).size() == 2){
				
				if((Math.abs(configMgr.eventHandler.pSelection.get(plr.getName()).get(0).getBlockX() - configMgr.eventHandler.pSelection.get(plr.getName()).get(1).getBlockX()) <= configMgr.getIntOption("MAX_ARENA_DIMS")) &&
					(Math.abs(configMgr.eventHandler.pSelection.get(plr.getName()).get(0).getBlockZ() - configMgr.eventHandler.pSelection.get(plr.getName()).get(1).getBlockX()) <= configMgr.getIntOption("MAX_ARENA_DIMS"))){
					
					arena.setBounds(configMgr.eventHandler.pSelection.get(plr.getName()).get(0), configMgr.eventHandler.pSelection.get(plr.getName()).get(1));
					plr.sendMessage("[Sno] Arena bounds set!");
					return true;
				}else{
					plr.sendMessage(ChatColor.RED+"[Sno] Arena bounds too large");
				}
			}else{
				plr.sendMessage(ChatColor.RED+"[Sno] Select another bound");
			}
		}else{
			plr.sendMessage(ChatColor.RED+"[Sno] Begin by selecting bounds");
		}
		return false;
	}
	
	public void setArenaName(Player plr, String name){//Sets arena name
		arena.name = name;
	}
	
	public boolean toggleArenaMode(Player plr, String s){//toggles arena modes
		if(s.equalsIgnoreCase("classic")){
			arena.toggleMode(GameMode.CLASSIC); return true;
		}else if(s.equalsIgnoreCase("elim")){
			arena.toggleMode(GameMode.ELIM); return true;
		}else if(s.equalsIgnoreCase("ctf")){
			arena.toggleMode(GameMode.CTF); return true;
		}else if(s.equalsIgnoreCase("deathmtch")){
			arena.toggleMode(GameMode.DEATHMTCH); return true;
		}else if(s.equalsIgnoreCase("control")){
			arena.toggleMode(GameMode.CONTROL); return true;
		}else {
			plr.sendMessage(ChatColor.RED+"[Sno] This is not a recognized game mode");
			return false;
		}
		
	}
	
	public void setLobby(Player plr){
		if (configMgr.eventHandler.pSelection.get(plr.getName())!=null){
			arena.Lobby = configMgr.eventHandler.pSelection.get(plr.getName()).get(0);
			configMgr.eventHandler.pSelection.put(plr.getName(),null);
		}
	}
	public void setBench(Player plr){
		if (configMgr.eventHandler.pSelection.get(plr.getName())!=null){
			arena.Bench = configMgr.eventHandler.pSelection.get(plr.getName()).get(0);
			configMgr.eventHandler.pSelection.put(plr.getName(),null);
		}
	}
	public void setRSpawn(Player plr){
		if (configMgr.eventHandler.pSelection.get(plr.getName())!=null){
			arena.rSpawn = configMgr.eventHandler.pSelection.get(plr.getName()).get(0);
			configMgr.eventHandler.pSelection.put(plr.getName(),null);
		}
	}
	public void setBSpawn(Player plr){
		if (configMgr.eventHandler.pSelection.get(plr.getName())!=null){
			arena.bSpawn = configMgr.eventHandler.pSelection.get(plr.getName()).get(0);
			configMgr.eventHandler.pSelection.put(plr.getName(),null);
		}
	}
	public void setRFlag(Player plr){
		if (configMgr.eventHandler.pSelection.get(plr.getName())!=null){
			arena.rFlag = configMgr.eventHandler.pSelection.get(plr.getName()).get(0);
			configMgr.eventHandler.pSelection.put(plr.getName(),null);
		}
	}
	public void setBFlag(Player plr){
		if (configMgr.eventHandler.pSelection.get(plr.getName())!=null){
			arena.bFlag = configMgr.eventHandler.pSelection.get(plr.getName()).get(0);
			configMgr.eventHandler.pSelection.put(plr.getName(),null);
		}
	}

	public boolean addFort(Player plr){
		if (configMgr.eventHandler.pSelection.get(plr.getName())!=null){
			if (configMgr.eventHandler.pSelection.get(plr.getName()).size() == 2){
				
				arena.Forts.add(new Fort(arena));
				arena.Forts.get(arena.Forts.size()-1).setBounds(configMgr.eventHandler.pSelection.get(plr.getName()).get(0), configMgr.eventHandler.pSelection.get(plr.getName()).get(1));
				plr.sendMessage("[Sno] New fort added!");
				return true;
				
			}else{
				plr.sendMessage(ChatColor.RED+"[Sno] Select another bound");
			}
		}else{
			plr.sendMessage(ChatColor.RED+"[Sno] Begin by selecting bounds");
		}
		return false;

	}

	public boolean addGameObject(Player plr, String oType){
		
		if(oType.equalsIgnoreCase("chest")){
			arena.gameObjects.add(new gameObject(objectType.CHEST,configMgr.eventHandler.pSelection.get(plr.getName()).get(0).add(new Location(plr.getWorld(), 0, 1, 0)))); return true;
		}else if(oType.equalsIgnoreCase("trap")){
			arena.gameObjects.add(new gameObject(objectType.FREEZE_TRAP,configMgr.eventHandler.pSelection.get(plr.getName()).get(0).add(new Location(plr.getWorld(), 0, 1, 0)))); return true;
		}else {
			plr.sendMessage(ChatColor.RED+"[Sno] This is not a recognized object type");
			return false;
		}
	}

	public boolean finishArena(){
		if(configMgr.eventHandler.checkComplete(arena)){
			arena.currentState = arenaState.OPEN;
			configMgr.eventHandler.setupHandles.remove(constructor.getName());
			configMgr.gameMgr.registerArena(arena.name,arena);
			return true;
		}
		return false;
	}
	
	public void respond(Player plr, String[] args){
		
	}
	
}
