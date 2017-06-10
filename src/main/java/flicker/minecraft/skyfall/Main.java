package flicker.minecraft.skyfall;


import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener {
	
	public FileConfiguration config = getConfig();
	
	private void SetupConfig()
	{
		config.addDefault("Void", "Ground");
		config.addDefault("Skylands","world");
		config.options().copyDefaults(true);
		saveConfig();
	}
	
	@Override
	public void onEnable()
	{		
		SetupConfig();
		getServer().getPluginManager().registerEvents(this, this);
	}
	
	@EventHandler(ignoreCancelled=true)
	public void onDamageTaken(EntityDamageEvent event)
	{
		//If a player took damage...
		if (event.getEntityType() != EntityType.PLAYER) return;
		
		//Find the player...
		Player player = (Player)event.getEntity();
		
		//player.sendMessage("Skyfall Debug: Took Damage");
		
		//If the player took void damage...
		if(event.getCause() == DamageCause.VOID)
		{
			//player.sendMessage("Skyfall Debug: Took Void Damage");
			//If the player is below 0 y.
			if(player.getLocation().getY() < 0)
			{
				//player.sendMessage("Skyfall Debug: In Void");
				//Find the desination world name in the config.
				String destinationName = config.getString(player.getWorld().getName());
				if(destinationName == null) return; //No destination.
				
				//player.sendMessage("Skyfall Debug: Found destination" + destinationName);
				
				//Find the world to teleport to.
				World destinationWorld = Bukkit.getServer().getWorld(destinationName);
				
				//Construct the destination location (new world, XYZ pos).
				Location destinationLocation = new Location(destinationWorld, player.getLocation().getX(), 500, player.getLocation().getZ());
				
				//Teleport the player to the new world.
				//This doesn't actually teleport them to the sky, presumably because changing dimension moves them to the lowest clear block.
				player.teleport(destinationLocation);
				
				//This time, they're already in the new world, so they should go to the sky!
				player.teleport(destinationLocation);
				
				//Make sure they don't actually take damage yet.
				event.setCancelled(true);
			}
		}
	}
}
