package Infinity.Snow.Game.Timers;

//The class that contains the timers and their
//code to perform upon completing


import Infinity.Snow.Game.Mechanics.Fort;
import Infinity.Snow.Main.ConfigManager.GameMode;
import Infinity.Snow.Main.ConfigManager.GameTeam;

public class TimerManager {

}

class ClassicBuildTimer extends TimerManager implements Runnable {

	public ActiveGame caller;

	public ClassicBuildTimer(ActiveGame ag) {
		caller = ag;
	}

	@Override
	public void run() {
		ClassicGameTimer T = new ClassicGameTimer(caller);
		caller.timerID.add(caller.GM.configMgr.plugin.getServer().getScheduler().scheduleAsyncDelayedTask(caller.GM.configMgr.plugin, T,
						(long) (20 * caller.GM.configMgr.getDoubleOption("CLASSIC_GAME_TIME"))));
		caller.mode = GameMode.CLASSICG;
		caller.setRestrictions(caller);

	}
}

class ClassicGameTimer extends TimerManager implements Runnable {

	public ActiveGame caller;
	
	public ClassicGameTimer(ActiveGame ag) {
		caller = ag;
	}

	@Override
	public void run() {
		caller.gameEnd();
		
	}
}

class CTFGameTimer extends TimerManager implements Runnable {

	public ActiveGame caller;
	
	public CTFGameTimer(ActiveGame ag) {
		caller = ag;
	}

	@Override
	public void run() {
		caller.gameEnd();
	}
}

class ELIMGameTimer extends TimerManager implements Runnable {

	public ActiveGame caller;

	public ELIMGameTimer(ActiveGame ag) {
		caller = ag;
		
	}

	@Override
	public void run() {
		caller.gameEnd();
	}
}

class DEATHMTCHGameTimer extends TimerManager implements Runnable {

	public ActiveGame caller;


	public DEATHMTCHGameTimer(ActiveGame ag) {
		caller = ag;
	}

	@Override
	public void run() {
		caller.gameEnd();
	}

}

class CONTROLGameTimer extends TimerManager implements Runnable {

	public ActiveGame caller;


	public CONTROLGameTimer(ActiveGame ag) {
		caller = ag;
	}

	@Override
	public void run() {

		caller.gameEnd();
	}

}

class CONTROLKeeper extends TimerManager implements Runnable {//should be run as repeating so to add score

	public ActiveGame caller;
	

	public CONTROLKeeper(ActiveGame ag) {
		caller = ag;
	}

	@Override
	public void run() {
		boolean check = true; GameTeam team = GameTeam.RED;
		for(Fort f:caller.arena.Forts){
			f.updateController(caller);
			if( f.getController() != team ){
				check = false;
			}
		}
		if(check){caller.addScore(GameTeam.RED, 1);}else{
			check = true; team = GameTeam.BLUE;
			for(Fort f:caller.arena.Forts){
				f.updateController(caller);
				if( f.getController() != team ){
					check = false;
				}
			}
			if(check){caller.addScore(GameTeam.BLUE, 1);}
		}
		
	}

}

class ChestRefillTimer extends TimerManager implements Runnable {

	public ActiveGame caller;

	public ChestRefillTimer(ActiveGame ag) {
		caller = ag;
	}

	@Override
	public void run() {
		caller.refillChests();
	}

}
/*class PlateTimer extends TimerManager implements Runnable {

	public ActiveGame caller;
	public int id;
	public String name;

	public PlateTimer(ActiveGame ag, String plr) {
		caller = ag;
		name = plr;
	}

	@Override
	public void run() {
		caller.frozenPlayers.remove(name);
		
	}

}*/