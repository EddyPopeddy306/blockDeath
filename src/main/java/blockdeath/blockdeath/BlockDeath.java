package blockdeath.blockdeath;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Boat;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;

public final class BlockDeath extends JavaPlugin implements Listener, CommandExecutor {

    public static int isStarted = 0;

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this,this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
        if(command.getName().equalsIgnoreCase("blockdeath")){
            if(args.length == 0){
                sender.sendMessage(ChatColor.RED + "Please use /blockdeath start/stop");
                return false;
            }
            if(args.length == 1){
                switch(args[0].toLowerCase()){
                    case "start": isStarted = 1;
                        Bukkit.broadcastMessage(ChatColor.GREEN + "Deathblocks started!"); return true;
                    case "stop": isStarted = 0;
                        Bukkit.broadcastMessage(ChatColor.DARK_GREEN + "Deathblocks stopped!"); return true;
                    default: sender.sendMessage(ChatColor.RED + "Please use /blockdeath start/stop");
                        return false;
                }
            }else{
                sender.sendMessage(ChatColor.RED + "Please use /blockdeath start/stop");
                return false;
            }
        }
        return false;
    }

    @EventHandler
    public void playerMovement(PlayerMoveEvent event){
        if (event.getFrom().getZ() != event.getTo().getZ() && event.getFrom().getX() != event.getTo().getX()) {
            if(isStarted == 1){
                Player p = event.getPlayer();
                Location l = p.getLocation();
                Location d = new Location(l.getWorld(), l.getX(), l.getY()-0.01, l.getZ());
                switch(d.getBlock().getType()){
                    case AIR:
                    case CAVE_AIR:
                    case VOID_AIR:
                    case KELP:
                    case KELP_PLANT:
                    case WATER:
                    case LAVA:
                    case GRASS:
                    case TALL_GRASS:
                    case SEAGRASS:
                    case TALL_SEAGRASS: return;
                    default:
                }
            /*if(p.getVehicle() instanceof Boat){ too op maybe
                return;
            }*/
                if(!d.getBlock().hasMetadata("safe")){
                    System.out.println(d.getBlock().toString());
                    p.damage(400);
                }
            }
        }

    }

    @EventHandler
    public void onBuild(BlockPlaceEvent event){
            event.getBlock().setMetadata("safe",new FixedMetadataValue(this,true));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
