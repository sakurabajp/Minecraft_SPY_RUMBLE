package net.cherryleaves.minecraft_spy_rumble;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class Game {
    public void Start(){
        final ScoreboardManager managerW = Bukkit.getScoreboardManager();
        final ScoreboardManager managerV = Bukkit.getScoreboardManager();

        final Scoreboard scoreboardW = Objects.requireNonNull(managerW).getMainScoreboard();
        final Scoreboard scoreboardV = Objects.requireNonNull(managerV).getMainScoreboard();

        List<Player> Players = new ArrayList<>(Bukkit.getOnlinePlayers());
        if (scoreboardW.getTeam("wolf") != null) {
            Objects.requireNonNull(scoreboardW.getTeam("wolf")).unregister();
        }
        if (scoreboardV.getTeam("villager") != null) {
            Objects.requireNonNull(scoreboardV.getTeam("villager")).unregister();
        }
        Team teamW = scoreboardW.registerNewTeam("wolf");
        Team teamV = scoreboardV.registerNewTeam("villager");
        // teamM.setSuffix("[←この人は狂人です]");
        // teamW.setSuffix("[←この人は人狼です]");
        // teamV.setSuffix("[←この人は村人です]");
        for (Player playerACC : Bukkit.getOnlinePlayers()) {
            // playerACC.sendMessage("貴方を村人チームに追加しました");
            teamV.addPlayer(playerACC);
        }
        for (int i = Minecraft_SPY_RUMBLE.BWPCount; i > 0; i += -1) {
            Random random = new Random();
            Player WolfTeamPlayers = Players.get(random.nextInt(Players.size()));
            if (teamW.hasEntry(WolfTeamPlayers.getName())) {
                // WolfTeamPlayers.sendMessage("貴方はすでに人狼チームに所属しているため再抽選が行われます");
                return;
            }
            teamW.addPlayer(WolfTeamPlayers);
            // WolfTeamPlayers.sendMessage("貴方は人狼に選ばれました");
        }
        for (Player playerALL : Bukkit.getOnlinePlayers()) {
            Minecraft_SPY_RUMBLE.PlayerCount += 1;
            playerALL.setGameMode(GameMode.SURVIVAL);
            playerALL.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, 10, 80, true, false));
            playerALL.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 10, 80, true, false));
            playerALL.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 100, 5, true, false));
            playerALL.playSound(playerALL.getLocation(), Sound.ENTITY_WITHER_SPAWN, 0.5f, 1.0f);
            playerALL.getInventory().clear();
            playerALL.getActivePotionEffects().clear();
            playerALL.sendMessage(ChatColor.BOLD + "" + ChatColor.DARK_GREEN + "-----------------------------------------------------");
            playerALL.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "ゲームスタート！");
            playerALL.sendMessage("");
            playerALL.sendMessage(ChatColor.AQUA + "制限時間は" + ChatColor.RESET + ChatColor.GOLD + "3時間" + ChatColor.RESET + ChatColor.AQUA + "です");
            playerALL.sendMessage("");
            if (teamV.hasEntry(playerALL.getName())) {
                playerALL.sendMessage(ChatColor.DARK_AQUA + "あなたは" + ChatColor.GREEN + "村人陣営" + ChatColor.DARK_AQUA + "です");
            }
            if (teamW.hasEntry(playerALL.getName())) {
                playerALL.sendMessage(ChatColor.DARK_AQUA + "あなたは" + ChatColor.RED + "人狼陣営" + ChatColor.DARK_AQUA + "です");
                playerALL.sendMessage(ChatColor.DARK_AQUA + "仲間は" + ChatColor.RED + teamW.getEntries() + ChatColor.DARK_AQUA + "です");
            }
            playerALL.sendMessage(ChatColor.BOLD + "" + ChatColor.DARK_GREEN + "-----------------------------------------------------");
        }
    }
}