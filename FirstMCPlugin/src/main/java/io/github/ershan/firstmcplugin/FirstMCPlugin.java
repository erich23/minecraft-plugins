package io.github.ershan.firstmcplugin;

import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;


public final class FirstMCPlugin extends JavaPlugin {
	 @Override
	 public void onEnable() {
		 // TODO Insert logic to be performed when the plugin is enabled
	 }

	 @EventHandler
	 public void onPlayerJoin(PlayerJoinEvent event) {
		 getLogger().info(event.getPlayer().getDisplayName() + " has joined!");
		 event.getPlayer().getInventory().addItem(new ItemStack(Material.DIAMOND_AXE, 1));
	 }

	 @Override
	 public void onDisable() {
		 // TODO Insert logic to be performed when the plugin is disabled
	 }
	 
	@EventHandler
	 public void onPlayerInteractBlock(PlayerInteractEvent event) {
	     Player player = event.getPlayer();
	     player.getWorld().strikeLightning(player.getTargetBlock((Set<Material>) null, 200).getLocation());
//	     if (player.getInventory().getItemInMainHand().getType() == Material.DIAMOND_AXE) {
//	         // Creates a bolt of lightning at a given location. In this case, that location is where the player is looking.
//	         // Can only create lightning up to 200 blocks away.
//	         player.getWorld().strikeLightning(player.getTargetBlock((Set<Material>) null, 200).getLocation());
//	     }
	 }

}
