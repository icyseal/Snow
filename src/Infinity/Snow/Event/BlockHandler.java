package Infinity.Snow.Event;

//Will eventually do callbacks to other classes for
//dealing with block place/removed events


import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import Infinity.Snow.Main.ConfigManager;

public class BlockHandler extends BlockListener{

	ConfigManager configMgr;
	
	public BlockHandler(ConfigManager configmgr) {
		configMgr = configmgr;
	}
	
	public void onBlockBreakEvent(BlockBreakEvent event){
		if(configMgr.gameMgr.getPlayerIsPlaying(event.getPlayer()) != null ){
			Player plr = event.getPlayer();
			if(!configMgr.permissionsMgr.ingameModerator(plr)){
				event.setCancelled(true);
			}
		}
	}
	
	public void onBlockPlaceEvent(BlockPlaceEvent event){
		if(configMgr.gameMgr.getPlayerIsPlaying(event.getPlayer()) != null ){
			Player plr = event.getPlayer();
			if(!configMgr.permissionsMgr.ingameModerator(plr)){
				event.setCancelled(true);
			}
			
		}
	}

}
