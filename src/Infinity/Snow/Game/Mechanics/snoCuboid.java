package Infinity.Snow.Game.Mechanics;

//The essential abstract class for any of the
//classes that act in an area.

import org.bukkit.World;
import org.bukkit.Location;

public abstract class snoCuboid {
	protected World world;
	protected Location corOne, corTwo;
	
	public boolean isInside(Location l){
		return l.toVector().isInAABB(corOne.toVector(), corTwo.toVector());
	}
	
	public void setBounds(Location lOne, Location lTwo){
		corOne = lOne; corTwo = lTwo;
	}
	
	public Location getBoundMin(){
		return corOne;
	}
	
	public Location getBoundMax(){
		return corTwo;
	}
	
	public World getWorld(){
		return world;
	}
	
	public void setWorld(World w){
		world = w;
	}
	
}
