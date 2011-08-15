package Infinity.Snow.Main;

//Manages permissions, compatible with 
//Permissions PermissionsEX and Bukkit Permissions


import org.bukkit.entity.Player;


import com.nijiko.permissions.PermissionHandler;
import com.nijikokun.bukkit.Permissions.Permissions;

import ru.tehkode.permissions.PermissionManager;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class PermissionsManager {

	ConfigManager configMgr;

	public enum PermissionType {
		PEREX, PERMISSIONS, BUKKIT
	}

	public static PermissionType perType;
	private static Object pHandle;

	public PermissionsManager( ConfigManager config) {
		
		configMgr = config;
		
	}

	public void initPermissons() {
		if (configMgr.plugin.getServer().getPluginManager().getPlugin("PermissionsEx") != null) {
			pHandle = (PermissionManager) PermissionsEx.getPermissionManager();
			perType = PermissionType.PEREX;
			configMgr.plugin.log("Found PermissionsEX");

		} else if (configMgr.plugin.getServer().getPluginManager().getPlugin("Permissions") != null) {
			pHandle = (PermissionHandler) ((Permissions) configMgr.plugin.getServer()
					.getPluginManager().getPlugin("Permissions")).getHandler();
			perType = PermissionType.PERMISSIONS;
			configMgr.plugin.log("Found Permissions.");

		} else {
			perType = PermissionType.BUKKIT;
			configMgr.plugin.log("Using bukkit permissions");
		}

	}

	private static boolean permission(Player player, String permission) {

		switch (perType) {
		case PEREX:
			return ((PermissionManager) pHandle).has(player, permission);
		case PERMISSIONS:
			return ((PermissionHandler) pHandle).has(player, permission);
		case BUKKIT:
			return player.hasPermission(permission);
		default:
			return true;
		}

	}

	public boolean doSetup(Player p) {

		return permission(p, "snow.setup.do");

	}
	public boolean editSetup(Player p) {

		return permission(p, "snow.setup.edit");

	}
	public boolean ingameModerator(Player p) {

		return permission(p, "snow.game.moderator");

	}
	

}
