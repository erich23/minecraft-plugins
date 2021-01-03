package io.github.erich23;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LightningStrike;
import org.bukkit.entity.Player;
import org.bukkit.entity.Trident;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class MySpigotPlugin extends JavaPlugin implements Listener {
	
	HashMap<String,Integer> tridentComboCounter = new HashMap<String,Integer>();
	
	// Fired when plugin is first enabled
    @Override
    public void onEnable() {
    	getServer().getPluginManager().registerEvents(this, this);
    }
    // Fired when plugin is disabled
    @Override
    public void onDisable() {

    }
    
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
    	Player player = e.getPlayer();
    	tridentComboCounter.put(player.getName(), 0);
    }
    
    
    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e) {
      // check if entity is damaged by a Player
      if (e.getDamager() instanceof Trident) {
    	  Trident trident = (Trident) e.getDamager();
    	  if(trident.getShooter() instanceof Player) {
    		  Player shooter = (Player)trident.getShooter();
    		  if(tridentComboCounter.containsKey(shooter.getName())){
    			  Integer comboCount = tridentComboCounter.get(shooter.getName());
    			  comboCount += 1;
    			  //combo hit
    			  if(comboCount == 5) {
    				  List<Player> onlinePlayers = new ArrayList<Player>(Bukkit.getOnlinePlayers());
        			  for(int i=0; i<onlinePlayers.size();i+=1) {
        				  onlinePlayers.get(i).addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20, 99));
        			  }
    				  Entity ent = e.getEntity();
    	    		  World world = ent.getWorld();
    	    		  //strike lightning
    	    		  for(int i=0; i<100; i+=1) {
    	    			  Location loc = ent.getLocation();
    	    			  world.strikeLightning(loc);
    	    		  }
    	    		  //set on fire
    	    		  ent.setFireTicks(80);
    	    		  Location loc = ent.getLocation();
    	    		  world.createExplosion(loc, 20);
    	    		  //150% damage
    	    		  e.setDamage(e.getDamage()*1.5);
    	    		  //reset combo
    	    		  comboCount = 0;
    			  }
    			  //else build up
    			  else if(comboCount == 4) {
    					  shooter.getWorld().playSound(shooter.getLocation(), Sound.ITEM_TOTEM_USE, 1.0F, 2.0F);
    			  }
    			  tridentComboCounter.put(shooter.getName(), comboCount);
    		  }
    	  }
      } 
      else if(e.getDamager() instanceof LightningStrike && e.getEntity() instanceof Player){
      	 e.setCancelled(true);
      }
      else {
        return;
      }
      
    }
    
    @EventHandler
    public void onProjectileHit(ProjectileHitEvent e) {
    	//reset combo
    	if(e.getEntity() instanceof Trident) {
    		Trident trident = (Trident) e.getEntity();
    		if (trident.getShooter() instanceof Player) {
    			Player shooter = (Player) trident.getShooter();
    			if (e.getHitEntity() instanceof Block) {
    				if(tridentComboCounter.containsKey(shooter.getName())){
    					tridentComboCounter.put(shooter.getName(), 0);
    				}
        		}
    		}
    	}
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    	if (cmd.getName().equalsIgnoreCase("trident")) { // If the player typed /basic then do the following, note: If you only registered this executor for one command, you don't need this
    		if(sender instanceof Player) {
    			Player player = (Player) sender;
    			ItemStack item = new ItemStack(Material.TRIDENT, 1);
            	item.addUnsafeEnchantment(Enchantment.LOYALTY, 3);
            	item.addUnsafeEnchantment(Enchantment.CHANNELING, 1);
            	item.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 5);
            	item.addUnsafeEnchantment(Enchantment.ARROW_DAMAGE, 3);
            	item.addUnsafeEnchantment(Enchantment.FIRE_ASPECT, 1);
            	item.addUnsafeEnchantment(Enchantment.LOOT_BONUS_MOBS, 3);
            	item.addUnsafeEnchantment(Enchantment.FROST_WALKER, 1);
            	item.addUnsafeEnchantment(Enchantment.DURABILITY, 30);
            	player.getInventory().addItem(item);
            	return true;
    		}
    		
    	} 
    	if (cmd.getName().equalsIgnoreCase("pickaxe")) { // If the player typed /basic then do the following, note: If you only registered this executor for one command, you don't need this
    		if(sender instanceof Player) {
    			Player player = (Player) sender;
    			ItemStack item = new ItemStack(Material.DIAMOND_PICKAXE, 1);
            	item.addUnsafeEnchantment(Enchantment.DIG_SPEED, 200);
            	item.addUnsafeEnchantment(Enchantment.DURABILITY, 200);
            	item.addUnsafeEnchantment(Enchantment.LOOT_BONUS_BLOCKS, 10);
            	item.addUnsafeEnchantment(Enchantment.KNOCKBACK, 100);
            	player.getInventory().addItem(item);
            	return true;
    		}
    		
    	} 
    	if (cmd.getName().equalsIgnoreCase("spawnmob")) {
    		if(sender instanceof Player) {
    			if(args.length > 0 && args[0].equals("dragon")) {
    				Player player = (Player) sender;
        			Location location = player.getLocation();
        			location.add(1, 1, 0);
        			
        			player.getWorld().spawnEntity(location, EntityType.ENDER_DRAGON);
        			return true;
    			}
    			if(args.length > 0 && args[0].equals("ravager")) {
    				Player player = (Player) sender;
        			Location location = player.getLocation();
        			location.add(1, 1, 0);
        			player.getWorld().spawnEntity(location, EntityType.RAVAGER);
        			return true;
    			}
    			if(args.length > 0 && args[0].equals("pillager")) {
    				Player player = (Player) sender;
        			Location location = player.getLocation();
        			location.add(1, 1, 0);
        			player.getWorld().spawnEntity(location, EntityType.PILLAGER);
        			return true;
    			}
    		}
    	}
    	//If this has happened the function will return true. 
            // If this hasn't happened the value of false will be returned.
    	return false; 
    }
    
    
    
//    @EventHandler
//    public void onPlayerLogin(PlayerInteractEvent event) {
//    	Player player = event.getPlayer();
//    	ItemStack item = new ItemStack(Material.TRIDENT, 1);
//    	item.addUnsafeEnchantment(Enchantment.LOYALTY, 3);
//    	item.addUnsafeEnchantment(Enchantment.CHANNELING, 1);
//    	item.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 5);
//    	item.addUnsafeEnchantment(Enchantment.ARROW_DAMAGE, 3);
//    	item.addUnsafeEnchantment(Enchantment.FIRE_ASPECT, 1);
//    	item.addUnsafeEnchantment(Enchantment.LOOT_BONUS_MOBS, 3);
//    	item.addUnsafeEnchantment(Enchantment.FROST_WALKER, 1);
//    	item.addUnsafeEnchantment(Enchantment.DURABILITY, 30);
//    	player.getInventory().addItem(item);
//    }
}
