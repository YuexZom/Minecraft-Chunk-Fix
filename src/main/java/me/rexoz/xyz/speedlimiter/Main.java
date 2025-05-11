package me.rexoz.xyz.speedlimiter;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.UUID;

public class Main extends JavaPlugin {

    private final HashMap<UUID, Location> lastLocations = new HashMap<>();
    private double speedLimit;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        speedLimit = getConfig().getDouble("speed-limit");

        // OP OYUNCULAR ETKİLENMİYO BEYLER

        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (!player.isOp()) {
                        if (player.isFlying()) {
                            Location lastLoc = lastLocations.get(player.getUniqueId());
                            Location currentLoc = player.getLocation();

                            if (lastLoc != null) {
                                double distance = lastLoc.distance(currentLoc);

                                if (distance > speedLimit) {
                                    player.teleport(lastLoc);
                                    player.sendMessage("§e§l⚡ §4§lR.A.C §7> §4§lR.A.C §cBir değişiklik tespit etti!");

                                    player.setFlying(false);
                                    player.setAllowFlight(false);
                                }
                            }

                            lastLocations.put(player.getUniqueId(), currentLoc.clone());
                        } else {
                            lastLocations.remove(player.getUniqueId());
                        }
                    }
                }
            }
        }.runTaskTimer(this, 0L, 5L);
    }
}
