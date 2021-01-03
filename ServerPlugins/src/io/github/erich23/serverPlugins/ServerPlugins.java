package io.github.erich23.serverPlugins;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.block.data.type.Chest.Type;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class ServerPlugins extends JavaPlugin implements Listener {

	// Fired when plugin is first enabled
    @Override
    public void onEnable() {
    	getServer().getPluginManager().registerEvents(this, this);
    }
    // Fired when plugin is disabled
    @Override
    public void onDisable() {

    }
    
    private Integer getPlayerNumItemStacksInInventory(Player p) {
    	Integer count = 0;
    	for(ItemStack i : p.getInventory().getContents()){
            if(i != null){
            	count += 1;
            }
        }
    	for(ItemStack i : p.getInventory().getArmorContents()){
            if(i != null){
            	count += 1;
            }
        }
    	return count;
    }
    
    
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
    	Player player = e.getEntity();
    	Integer playerSpace = getPlayerNumItemStacksInInventory(player);
    	//if no items then don't create chest
    	if(playerSpace == 0) {
    		return;
    	}
    	Location loc = player.getLocation();
    	player.getWorld().getBlockAt(loc).setType(Material.CHEST);
    	Chest chest = (Chest) player.getWorld().getBlockAt(loc).getState();
    	Integer chestSpace = chest.getInventory().getSize();
    	
    	Location secondChestLoc = null;
    	
    	Boolean stacked = false;
    	//single chest or double chest
    	if (playerSpace > chestSpace){
    		Material frontType = player.getWorld().getBlockAt(loc.clone().add(1,0,0)).getType();
    		Material backType = player.getWorld().getBlockAt(loc.clone().add(-1,0,0)).getType();
    		Material leftType = player.getWorld().getBlockAt(loc.clone().add(0,0,-1)).getType();
    		Material rightType = player.getWorld().getBlockAt(loc.clone().add(0,0,1)).getType();
    		
    		if(frontType == Material.AIR || frontType == Material.WATER || frontType == Material.LAVA) {
    			secondChestLoc = loc.clone().add(1,0,0);
    		}
    		else if(backType == Material.AIR || backType == Material.WATER || backType == Material.LAVA) {
    			secondChestLoc = loc.clone().add(-1,0,0);
    		}
    		else if(leftType == Material.AIR || leftType == Material.WATER || leftType == Material.LAVA) {
    			secondChestLoc = loc.clone().add(0,0,-1);
    		}
    		else if(rightType == Material.AIR || rightType == Material.WATER || rightType == Material.LAVA) {
    			secondChestLoc = loc.clone().add(0,0,1);
    		}
    		else {
    			//if they're all blocks then
    			//get priority blocks
    			Set<Material>nonoLIST = new HashSet<Material>();
    			nonoLIST.add(Material.CHEST);
    			nonoLIST.add(Material.ANCIENT_DEBRIS);
    			nonoLIST.add(Material.DIAMOND_ORE);
    			nonoLIST.add(Material.DRAGON_EGG);
    			nonoLIST.add(Material.BEDROCK);
    			nonoLIST.add(Material.DIAMOND_BLOCK);
    			nonoLIST.add(Material.EMERALD_BLOCK);
    			nonoLIST.add(Material.BEACON);
    			nonoLIST.add(Material.NETHERITE_BLOCK);
    			nonoLIST.add(Material.END_PORTAL_FRAME);
    			
    			if(!nonoLIST.contains(frontType)) {
    				secondChestLoc = loc.clone().add(1,0,0);
    			}
    			else if(!nonoLIST.contains(backType)) {
    				secondChestLoc = loc.clone().add(-1,0,0);
    			}
    			else if(!nonoLIST.contains(leftType)) {
    				secondChestLoc = loc.clone().add(0,0,-1);
    			}
    			else if(!nonoLIST.contains(rightType)){
    				secondChestLoc = loc.clone().add(0,0,1);
    			}
    			else {
    				//stack chests
    				secondChestLoc = loc.clone().add(0,1,0);
    				stacked = true;
    			}
    			
    		}
    		player.getWorld().getBlockAt(secondChestLoc).setType(Material.CHEST);
    	}
    	
    	
    	//transfer stuff over
    	ArrayList<ItemStack> allItems = new ArrayList<ItemStack>();
        for(ItemStack i : player.getInventory().getContents()){
            if(i != null){
            	allItems.add(i);
            }
        }
        
        Integer allItemsI = 0;
        Chest finalchest1 = (Chest) player.getWorld().getBlockAt(loc).getState();
        Chest finalchest2 = (Chest) player.getWorld().getBlockAt(secondChestLoc).getState();

        Integer chestSize = finalchest1.getInventory().getSize();

        while(allItemsI < allItems.size() && allItemsI < chestSize) {
        	finalchest1.getInventory().addItem(allItems.get(allItemsI));
        	allItemsI+=1;
        }
        while(allItemsI < allItems.size()) {
        	finalchest2.getInventory().addItem(allItems.get(allItemsI));
        	allItemsI+=1;
        }
        
        e.getDrops().clear();
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    	if (cmd.getName().equalsIgnoreCase("tp")) { // If the player typed /basic then do the following, note: If you only registered this executor for one command, you don't need this
    		if(sender instanceof Player) {
    			Player sender_ = (Player) sender;
    			if(args.length == 2) {
    				Player playerFrom = Bukkit.getServer().getPlayer(args[0]);
    				Player playerTo = Bukkit.getServer().getPlayer(args[1]);
    				if(playerFrom == null) {
    					sender_.sendMessage("Player: " + args[0] + " not found");
    					return false;
    				}
    				if(playerTo == null) {
    					sender_.sendMessage("Player: " + args[1] + " not found");
    					return false;
    				}
    				
    				playerFrom.teleport(playerTo.getLocation());
    				return true;
        			
        		}
        		if (args.length == 1) {
        			Player playerFrom = sender_;
        			Player playerTo = Bukkit.getServer().getPlayer(args[0]);
        			if(playerTo == null) {
    					sender_.sendMessage("Player: " + args[0] + " not found");
    					return false;
    				}
        			playerFrom.teleport(playerTo.getLocation());
    				return true;
        		}
    		}
    	}
    	
    	//If this has happened the function will return true. 
            // If this hasn't happened the value of false will be returned.
    	return false; 
    }
    
}