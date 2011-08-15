package Infinity.Snow.Game.Timers;

//An active game object is created when a new game is started.
//They function to initiate timers, configure settings and
//and also to deal with game setup and team making

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.block.Chest;
import Infinity.Snow.Game.Mechanics.Fort;
import Infinity.Snow.Game.Mechanics.gameObject;
import Infinity.Snow.Main.ConfigManager.GameMode;
import Infinity.Snow.Game.Arena;
import Infinity.Snow.Game.GameManager;
import Infinity.Snow.Main.ConfigManager.objectType;
import Infinity.Snow.Main.ConfigManager.arenaState;
import Infinity.Snow.Main.ConfigManager.restriction;
import Infinity.Snow.Main.ConfigManager.GameTeam;

public class ActiveGame {

	public Arena arena;
	public GameMode mode;
	public GameManager GM;
	public ArrayList<Player> players = new ArrayList<Player>();
	public ArrayList<Player>deSpawnedPlayers = new ArrayList<Player>();
	public ArrayList<Player>frozenPlayers = new ArrayList<Player>();
	public ArrayList<restriction> gameRestrictions = new ArrayList<restriction>();
	public ArrayList<Integer> timerID = new ArrayList<Integer>();
	
	private HashMap<GameTeam, ArrayList<Player>>teams = new HashMap<GameTeam, ArrayList<Player>>();
	private HashMap<Player, ItemStack[]>inventoryCache = new HashMap<Player, ItemStack[]>();
	private HashMap<Player, Integer>playerDamage = new HashMap<Player, Integer>();
	
	public Player rFlagHolder, bFlagHolder;
	
	private int rScore = 0;
	private int bScore = 0;
	
	

	public ActiveGame(Arena ar, GameMode Mode, Player host, GameManager gm) {
		arena = ar;
		players.add(host);
		mode = Mode;
		ar.currentState = arenaState.LOADING;
		GM = gm;
		GM.addPlayer(host);
		//Send server message about new game host?
	}

	public void setRestrictions(ActiveGame ag) {//Sets restrictions
		switch (ag.mode) {
		case CLASSIC:
			ag.gameRestrictions.add(restriction.NO_PROJECTILES);
			ag.gameRestrictions.add(restriction.NO_BREAKING);
			break;
		case CLASSICG:
			ag.gameRestrictions.remove(restriction.NO_PROJECTILES);
			break;
		case CTF:
			ag.gameRestrictions.add(restriction.NO_BREAKING);
			ag.gameRestrictions.add(restriction.NO_BUILDING);
			break;
		case ELIM:
			ag.gameRestrictions.add(restriction.NO_BREAKING);
			ag.gameRestrictions.add(restriction.NO_BUILDING);
			break;
		case DEATHMTCH:
			ag.gameRestrictions.add(restriction.NO_RESPAWN);
			ag.gameRestrictions.add(restriction.NO_BREAKING);
			ag.gameRestrictions.add(restriction.NO_BUILDING);
			break;
		case CONTROL:
			ag.gameRestrictions.add(restriction.NO_BREAKING);
			break;
		default:
			ag.gameRestrictions.clear();
		}
	}

	public void setTeams(){//should be called upon when the game goes active
		for(Player plr: players){
			if(teams.get(GameTeam.BLUE).size() >= teams.get(GameTeam.RED).size()){
				teams.get(GameTeam.RED).add(plr);
			}else{
				teams.get(GameTeam.BLUE).add(plr);
			}
		}
	}
	
	public void removeInventory(Player plr){//Removes the player's inventory and caches it
		inventoryCache.put(plr, plr.getInventory().getContents());
		plr.getInventory().clear();
	}
	
	public void returnInventory(Player plr){//Return the player's inventory
		plr.getInventory().setContents(inventoryCache.get(plr));
	}
	
	public void playerRegister(Player plr) {// call when a player requests to join a game
		if (players.size() < GM.configMgr.getDoubleOption("MAX_PLAYERS")
				&& arena.currentState == arenaState.LOADING) {
			GM.addPlayer(plr);// add to game manager's cache
			plr.teleport(arena.Lobby);
			removeInventory(plr);
			players.add(plr);
			
		}
	}

	public void playerLeave(Player plr) {// should be called when player quits
											// the game
		if (players.contains(plr)) {// only if the player is a part of this
										// game
			returnInventory(plr);
			players.remove(plr);// remove the person from the hashmap
			if (players.size() == 0) {
				GM.endGame(this);// if nobody's left the end the game
			} else if (players.size() <= 1
					&& arena.currentState == arenaState.IN_GAME) {
				GM.endGame(this);
			}

		}
	}

	public void gameEnd(){//intermediate for returning inventories called by timers and by onScore
		for(int i: timerID){//Cancels any still running tasks
			GM.configMgr.plugin.getServer().getScheduler().cancelTask(i);
		}
		for(Player plr: players){
			plr.teleport(arena.Lobby);//Teleport them back to the lobby
			returnInventory(plr);
		}
		GM.endGame(this);
	}
	

	public GameTeam getTeam(Player plr) {// returns the team of the player
		for(GameTeam gt: EnumSet.allOf(GameTeam.class)){
			if(teams.get(gt).contains(plr)){
				return gt;
			}
		}
		return null;
	}


	public GameTeam checkWinning() {// Returns winning team's color and null if tied or Control
									
		if (mode == GameMode.CLASSICG || mode == GameMode.ELIM || mode == GameMode.CTF || mode == GameMode.DEATHMTCH || mode == GameMode.CONTROL) {
			if (bScore > rScore) {
				return GameTeam.BLUE;
			} else if (bScore < rScore) {
				return GameTeam.RED;
			} else {
				return null;
			}
		}else if(mode == GameMode.DEATHMTCH){
			if(teams.get(GameTeam.RED).size() > teams.get(GameTeam.BLUE).size()){
				return GameTeam.RED;
			}else if(teams.get(GameTeam.RED).size() < teams.get(GameTeam.BLUE).size()){
				return GameTeam.BLUE;
			}else{
				return null;
			}
		}else{
			return null;
		}
	}


	public void refillChests(){//fills all the game chests with snowballs
		for(gameObject go: arena.gameObjects){
			if( go.type == objectType.CHEST){
				ItemStack is = new ItemStack(332);is.setAmount(64);
				((Chest)go.L.getBlock()).getInventory().addItem(is);
				((Chest)go.L.getBlock()).getInventory().addItem(is);
				
			}
		}
	}
	

	public void respawnPlayer(Player plr) {//Respawns a player back at their spawn
		if(getTeam(plr)==GameTeam.RED){//Move them back to the spawn
			plr.teleport(arena.rSpawn);
		}else{
			plr.teleport(arena.bSpawn);
		}
		for(Fort f: arena.Forts){//Quick update for Control mode
			f.updateController(this);
		}
		
	}
	

	public void despawnPlayer(Player plr) {//Sends a player to the "bench" where they are no longer a part of the game
		
		plr.teleport(arena.Bench);
		teams.get(getTeam(plr)).remove(plr);
		deSpawnedPlayers.add(plr);
	}


	public void broadcast(String msg){//Sends a message to all players in this game
		for(Player plr:players){
			plr.sendMessage("[Sno]"+msg);
		}
	}
	

	public void addScore(GameTeam gt, int score){
		if(gt == GameTeam.RED){
			rScore+=score;
		}else{
			bScore+=score;
		}
		onScore(gt);
	}

	public void onScore(GameTeam gt){
		if(mode == GameMode.CTF){
			if(gt == GameTeam.RED && rScore>=GM.configMgr.getIntOption("CTF_EG_SCORE")){
				gameEnd();
			}else if(gt == GameTeam.BLUE && bScore>=GM.configMgr.getIntOption("CTF_EG_SCORE")){
				gameEnd();
			}
		}
		if(mode == GameMode.ELIM){
			if(gt == GameTeam.RED && rScore>=GM.configMgr.getIntOption("ELIM_EG_SCORE")){
				gameEnd();
			}else if(gt == GameTeam.BLUE && bScore>=GM.configMgr.getIntOption("ELIM_EG_SCORE")){
				gameEnd();
			}
		}
		if(mode == GameMode.DEATHMTCH){
			if(gt == GameTeam.RED && teams.get(GameTeam.BLUE).size()==0){
				gameEnd();
			}else if(gt == GameTeam.BLUE && teams.get(GameTeam.RED).size()==0){
				gameEnd();
			}
		}
	}
	

	public void playerHitCallback(EntityDamageByEntityEvent event) {
		Player victim = ((Player) event.getEntity());
		Player shooter = ((Player) ((Projectile) event.getDamager()).getShooter());
		if (getTeam(victim) != getTeam(shooter)) {
			if (mode == GameMode.CLASSICG) {
				playerDamage.put(victim, playerDamage.get(victim) + 1);
				addScore(getTeam(shooter),1);
				if (playerDamage.get(victim) % GM.configMgr.getIntOption("CLASSIC_HITS_RESPAWN") == 0) {
					respawnPlayer(victim);
				}
			} else if (mode == GameMode.CTF) {
				playerDamage.put(victim, playerDamage.get(victim) + 1);
				if (playerDamage.get(victim) % GM.configMgr.getIntOption("CTF_HITS_RESPAWN") == 0) {
					respawnPlayer(victim);
				}
			} else if (mode == GameMode.ELIM) {
				playerDamage.put(victim, playerDamage.get(victim) + 1);
				addScore(getTeam(shooter),1);
				respawnPlayer(victim);// just respawn

			} else if (mode == GameMode.DEATHMTCH) {
				playerDamage.put(victim, playerDamage.get(victim) + 1);
				addScore(getTeam(shooter),1);
				despawnPlayer(victim);// despawn to lobby
			} else if (mode == GameMode.CONTROL) {
				playerDamage.put(victim, playerDamage.get(victim) + 1);
				if (playerDamage.get(victim) % GM.configMgr.getIntOption("CONTROL_HITS_RESPAWN") == 0) {
					respawnPlayer(victim);
				}
			}
		}
	}

	public void startGame() {// starts the game
		
		broadcast("The game will now begin");
		
		setTeams();
		
		for(Player plr: players){
			ItemStack is = new ItemStack(332);//Starts players with their snowballs
			for(int i = GM.configMgr.getIntOption("SNOWBALL_STARTING_AMOUNT"); i>0; i-=64){
				if(i>64){
					is.setAmount(64);
				}else{
					is.setAmount(i);
				}
				plr.getInventory().addItem(is);
				
			}
			
			playerDamage.put( plr, 0);//Set all damage counters to 0
			
			if(getTeam(plr) == GameTeam.RED){//sends them to their respective spawns
				plr.teleport(arena.rSpawn);
			}else{
				plr.teleport(arena.bSpawn);
			}
		}
		
		
		ChestRefillTimer C = new ChestRefillTimer(this);
		timerID.add(GM.configMgr.plugin.getServer().getScheduler().scheduleAsyncRepeatingTask(GM.configMgr.plugin, C,(long) (20 * GM.configMgr.getDoubleOption("SNOWBALL_REFILL_DELAY")),(long) (20 * GM.configMgr.getDoubleOption("SNOWBALL_REFILL_DELAY"))));
		
		for(gameObject go: arena.gameObjects){
			if(go.type == objectType.CHEST){
				go.L.getBlock().setTypeId(54);
			}
			if(go.type == objectType.FREEZE_TRAP){
				go.L.getBlock().setTypeId(70);
			}
		}
		
		
		
		
		
		if (mode == GameMode.CLASSIC) {
			arena.currentState = arenaState.IN_GAME;
			if (GM.configMgr.getDoubleOption("CLASSIC_GAME_TIME") > 0) {// if there is build time start the build timer
				setRestrictions(this);
				ClassicBuildTimer T = new ClassicBuildTimer(this);
				timerID.add(GM.configMgr.plugin.getServer().getScheduler().scheduleAsyncDelayedTask(GM.configMgr.plugin, T,(long) (20 * GM.configMgr.getDoubleOption("CLASSIC_BUILD_TIME"))));
			} else {// otherwise go straight to the game
				setRestrictions(this);
				ClassicGameTimer T = new ClassicGameTimer(this);
				timerID.add(GM.configMgr.plugin.getServer().getScheduler().scheduleAsyncDelayedTask(GM.configMgr.plugin, T,(long) (20 * GM.configMgr.getDoubleOption("CLASSIC_GAME_TIME"))));
			}
		} else if (mode == GameMode.CTF) {
			setRestrictions(this);
			arena.currentState = arenaState.IN_GAME;
			arena.bFlag.getBlock().setTypeId(50);
			arena.rFlag.getBlock().setTypeId(50);
			CTFGameTimer T = new CTFGameTimer(this);// CTF timer
			timerID.add(GM.configMgr.plugin.getServer().getScheduler().scheduleAsyncDelayedTask(GM.configMgr.plugin, T,(long) (20 * GM.configMgr.getDoubleOption("CTF_GAME_TIME"))));
		} else if (mode == GameMode.ELIM) {
			setRestrictions(this);
			arena.currentState = arenaState.IN_GAME;
			ELIMGameTimer T = new ELIMGameTimer(this);// ELIM timer
			timerID.add(GM.configMgr.plugin.getServer().getScheduler().scheduleAsyncDelayedTask(GM.configMgr.plugin, T,(long) (20 * GM.configMgr.getDoubleOption("ELIM_GAME_TIME"))));
		} else if (mode == GameMode.DEATHMTCH) {
			setRestrictions(this);
			arena.currentState = arenaState.IN_GAME;
			DEATHMTCHGameTimer T = new DEATHMTCHGameTimer(this);
			timerID.add(GM.configMgr.plugin.getServer().getScheduler().scheduleAsyncDelayedTask(GM.configMgr.plugin, T,(long) (20 * GM.configMgr.getDoubleOption("DEATHMTCH_GAME_TIME"))));
		}else if (mode == GameMode.CONTROL) {
			setRestrictions(this);
			arena.currentState = arenaState.IN_GAME;
			CONTROLGameTimer T = new CONTROLGameTimer(this);// Control timer
			CONTROLKeeper K = new CONTROLKeeper(this);// Control timer
			timerID.add(GM.configMgr.plugin.getServer().getScheduler().scheduleAsyncRepeatingTask(GM.configMgr.plugin, K, 0, (long) (20 * GM.configMgr.getDoubleOption("CONTROL_DELAY_TIME"))));
			timerID.add(GM.configMgr.plugin.getServer().getScheduler().scheduleAsyncDelayedTask(GM.configMgr.plugin, T,(long) (20 * GM.configMgr.getDoubleOption("CONTROL_GAME_TIME"))));
			
		}
	}

}
