package net.cherryleaves.minecraft_spy_rumble;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class Player_Task implements Listener {

    public BukkitRunnable TimerM;

    @EventHandler
    public void PlayerShiftEvent(PlayerToggleSneakEvent e) {
        Player p = e.getPlayer();
        if (!p.isSneaking()) {
            p.sendMessage(ChatColor.AQUA + "すにーくいべんとえなぶる！");

            // スニーク中に1tick事に数値を減らしてタスク進捗を管理。これ自体は別メゾットに置いたほうがいいかも。
            TimerM = new BukkitRunnable() {
                @Override
                public void run() {
                }
            };
            TimerM.runTaskTimer((Plugin) this, 0, 2); // 20 ticks = 1 second
        }
            /*for (Entity entity : p.getNearbyEntities(2, 2, 2)) {
                if (entity instanceof ArmorStand) {
                    Location armorStandLocation = entity.getLocation();
                    if (p.getLocation().distanceSquared(armorStandLocation) <= 2 * 2 ){
                    }
                }
            }*/
        else if(p.isSneaking()){
            p.sendMessage(ChatColor.RED + "すにーくいべんとでぃさぶる！");
        }
    }
}
