package Infinity.Snow.Event;

//Will eventually call back to other classes when
//players leave or move

import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import Infinity.Snow.Main.ConfigManager.restriction;
import Infinity.Snow.Game.Timers.ActiveGame;
import Infinity.Snow.Main.ConfigManager;
import Infinity.Snow.Main.ConfigManager.GameMode;
import Infinity.Snow.Main.ConfigManager.GameTeam;

public class PlayerHandler extends PlayerListener{

	ConfigManager configMgr;
	
	public PlayerHandler(ConfigManager config) {
		configMgr = config;
	}
	
	public void onPlayerInteractEvent(PlayerInteractEvent event){
		
		if(event.getAction() == Action.RIGHT_CLICK_BLOCK ){
			Player plr = event.getPlayer();
			
			
			if(configMgr.gameMgr.getPlayerIsPlaying(event.getPlayer())!=null){
				ActiveGame aGame = configMgr.gameMgr.getPlayerIsPlaying(plr);
				if(aGame.gameRestrictions.contains(restriction.NO_BREAKING) && 
						!configMgr.permissionsMgr.ingameModerator(plr)){
					
					if(event.getClickedBlock().getTypeId() == configMgr.getIntOption("CTF_FLAG_ID") && aGame.mode == GameMode.CTF){
						if(event.getClickedBlock().getLocation().equals(aGame.arena.bFlag)){
							if(aGame.getTeam(plr) == GameTeam.RED && aGame.bFlagHolder==null){
								aGame.rFlagHolder = plr;
								aGame.arena.bFlag.getBlock().setTypeId(0);
								aGame.broadcast(plr+" has snatched the flag!");
							}else if(aGame.getTeam(plr) == GameTeam.BLUE && aGame.rFlagHolder==plr){
								aGame.addScore(GameTeam.BLUE, 1);
								aGame.arena.rFlag.getBlock().setTypeId(50);
								aGame.arena.bFlag.getBlock().setTypeId(50);
								aGame.broadcast(plr+" has scored a point for the blue team!");
							}
						}else if(event.getClickedBlock().getLocation().equals(aGame.arena.rFlag)){
							if(aGame.getTeam(plr) == GameTeam.BLUE && aGame.rFlagHolder==null){
								aGame.bFlagHolder = plr;
								aGame.arena.rFlag.getBlock().setTypeId(0);
								aGame.broadcast(plr+" has snatched the flag!");
							}else if(aGame.getTeam(plr) == GameTeam.RED && aGame.rFlagHolder==plr){
								aGame.addScore(GameTeam.RED, 1);
								aGame.arena.rFlag.getBlock().setTypeId(50);
								aGame.arena.bFlag.getBlock().setTypeId(50);
								aGame.broadcast(plr+" has scored a point for the red team!");
							}
						}
					}
				}
				
			}else{
				
				if(configMgr.eventHandler.setupHandles.containsKey(plr.getName())){
					if(event.getItem().getTypeId() == 280){
						configMgr.eventHandler.addLocation(plr.getName(), event.getClickedBlock().getLocation());
					}
				}
				
			}
		}
	}
	
	public void onPlayerQuitEvent(PlayerQuitEvent event){
		if(configMgr.gameMgr.getPlayerIsPlaying(event.getPlayer()) != null ){
			Player plr = event.getPlayer();
			configMgr.gameMgr.removePlayer(plr);
			configMgr.gameMgr.getPlayerIsPlaying(event.getPlayer()).playerLeave(plr);
		}
	}
	
	public void onPlayerKickEvent(PlayerKickEvent event){
		if(configMgr.gameMgr.getPlayerIsPlaying(event.getPlayer()) != null ){
			Player plr = event.getPlayer();
			configMgr.gameMgr.removePlayer(plr);
			configMgr.gameMgr.getPlayerIsPlaying(event.getPlayer()).playerLeave(plr);
		}
	}
	
	public void onPlayerMoveEvent(PlayerMoveEvent event){
		if(configMgr.gameMgr.getPlayerIsPlaying(event.getPlayer()) != null ){
			Player plr = event.getPlayer();
			if(!configMgr.gameMgr.getPlayerIsPlaying(plr).arena.isInside(event.getTo())){
				event.setTo(event.getFrom());
			}
			
		}
	}

}
