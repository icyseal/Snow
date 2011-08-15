package Infinity.Snow.Event;

//Will eventually perform callbacks for entities
//specifically entitydamagedbyentity which is called
//when a player gets hit by a snowball

import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.entity.EntityListener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import Infinity.Snow.Main.ConfigManager;

public class EntityHandler extends EntityListener {

	ConfigManager configMgr;
	
	public EntityHandler(ConfigManager configmgr) {
		configMgr = configmgr;
	}
	
	public void onEntityDamageByEntity(EntityDamageByEntityEvent event){
		if(event.getEntity() instanceof Player && event.getDamager() instanceof Snowball){
			configMgr.plugin.gameMgr.getPlayerIsPlaying(((Player)event.getEntity())).playerHitCallback(event);
		}
	}

}
