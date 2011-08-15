package Infinity.Snow.Event;

//Used almost only for the setup and
//registration of various events and handlers

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import Infinity.Snow.Game.Arena;
import Infinity.Snow.Main.ConfigManager;
import Infinity.Snow.Main.ConfigManager.GameMode;

public class SnoEventHandler {
	
	public ConfigManager configMgr;
	
	public CommandHandler cmdHandle;
	public EntityHandler entHandle;
	public PlayerHandler plrHandle;
	public BlockHandler blkHandle;
	public HashMap<Player,SetupHandler>setupHandles = new HashMap<Player,SetupHandler>();
	
	protected HashMap<String,ArrayList<Location>>pSelection = new HashMap<String,ArrayList<Location>>();
	
	public SnoEventHandler(ConfigManager Config) {
		
		configMgr = Config;
		
	}

	public void registerEvents() {
		
		cmdHandle = new CommandHandler(configMgr);
		entHandle = new EntityHandler(configMgr);
		plrHandle = new PlayerHandler(configMgr);
		blkHandle = new BlockHandler(configMgr);
		
		configMgr.plugin.getCommand("snow").setExecutor(cmdHandle);
		
		configMgr.plugin.getServer().getPluginManager().registerEvent(Event.Type.ENTITY_DAMAGE, entHandle, Event.Priority.Normal, configMgr.plugin);
		configMgr.plugin.getServer().getPluginManager().registerEvent(Event.Type.PLAYER_MOVE, plrHandle, Event.Priority.Normal, configMgr.plugin);
		configMgr.plugin.getServer().getPluginManager().registerEvent(Event.Type.PLAYER_KICK, plrHandle, Event.Priority.Normal, configMgr.plugin);
		configMgr.plugin.getServer().getPluginManager().registerEvent(Event.Type.PLAYER_QUIT, plrHandle, Event.Priority.Normal, configMgr.plugin);
		configMgr.plugin.getServer().getPluginManager().registerEvent(Event.Type.PLAYER_INTERACT, plrHandle, Event.Priority.Normal, configMgr.plugin);
		configMgr.plugin.getServer().getPluginManager().registerEvent(Event.Type.BLOCK_BREAK, blkHandle, Event.Priority.Normal, configMgr.plugin);
		configMgr.plugin.getServer().getPluginManager().registerEvent(Event.Type.BLOCK_PLACE, blkHandle, Event.Priority.Normal, configMgr.plugin);
		
	}
	
	protected void addPlayer(String name){
		pSelection.put(name, null);
	}
	
	public void addLocation(String name, Location loc){
		if(!pSelection.containsKey(name)){
			addPlayer(name);
		}
		ArrayList<Location>L = new ArrayList<Location>();
		if(pSelection.get(name) == null){
			configMgr.plugin.getServer().getPlayer(name).sendMessage("[Sno] First point selected");
			L.add(loc);
		}else if(pSelection.get(name).size()>=2){
			configMgr.plugin.getServer().getPlayer(name).sendMessage("[Sno] First point selected");
			L.add(loc); 
		}else{
			configMgr.plugin.getServer().getPlayer(name).sendMessage("[Sno] Second point selected");
			L = pSelection.get(name);
			L.add(loc);
		}
		pSelection.put(name, L);
		
	}
	
	public ArrayList<Location> getSelection(String name){
		return pSelection.get(name);
	}
	
	public boolean checkComplete(Arena ar){
		if(ar.getBoundMax() == null || ar.getBoundMin() == null || ar.getWorld() == null){//Checking if is setup
			return false;
		}
		if(ar.name == null){//must be named
			return false;
		}
		if(ar.allowedModes.size() == 0){//at least one mode must be selected
			return false;
		}
		if(ar.Lobby == null || ar.Bench == null){//if either location is null
			return false;
		}
		if(ar.rSpawn == null || ar.bSpawn == null){//spawns can't be null
			return false;
		}
		if(ar.allowedModes.contains(GameMode.CTF) && (ar.rFlag == null || ar.bFlag == null)){//flags can't be null if CTF is allowed
			return false;
		}
		if(ar.allowedModes.contains(GameMode.CONTROL) && ar.Forts.size()!=3){//if you don't have 3 forts with control enabled
			return false;
		}
		return true;
	}
	
	

}
