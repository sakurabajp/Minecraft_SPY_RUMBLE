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
        final ScoreboardManager managerM = Bukkit.getScoreboardManager();

        final Scoreboard scoreboardW = Objects.requireNonNull(managerW).getMainScoreboard();
        final Scoreboard scoreboardV = Objects.requireNonNull(managerV).getMainScoreboard();
        final Scoreboard scoreboardM = Objects.requireNonNull(managerM).getMainScoreboard();

        List<Player> Players = new ArrayList<>(Bukkit.getOnlinePlayers());
        if (scoreboardW.getTeam("wolf") != null) {
            Objects.requireNonNull(scoreboardW.getTeam("wolf")).unregister();
        }
        if (scoreboardV.getTeam("villager") != null) {
            Objects.requireNonNull(scoreboardV.getTeam("villager")).unregister();
        }
        if (scoreboardM.getTeam("madman") != null) {
            Objects.requireNonNull(scoreboardM.getTeam("madman")).unregister();
        }
        Team teamW = scoreboardW.registerNewTeam("wolf");
        Team teamM = scoreboardM.registerNewTeam("madman");
        Team teamV = scoreboardV.registerNewTeam("villager");
        // teamM.setSuffix("[←この人は狂人です]");
        // teamW.setSuffix("[←この人は人狼です]");
        // teamV.setSuffix("[←この人は村人です]");
        for (Player playerACC : Bukkit.getOnlinePlayers()) {
            // playerACC.sendMessage("貴方を村人チームに追加しました");
            teamV.addPlayer(playerACC);
        }
        for (int i = BeforeWolfPlayerCount; i > 0; i += -1) {
            Random random = new Random();
            Player WolfTeamPlayers = Players.get(random.nextInt(Players.size()));
            if (teamW.hasEntry(WolfTeamPlayers.getName())) {
                // WolfTeamPlayers.sendMessage("貴方はすでに人狼チームに所属しているため再抽選が行われます");
                return;
            }
            teamW.addPlayer(WolfTeamPlayers);
            // WolfTeamPlayers.sendMessage("貴方は人狼に選ばれました");
        }
        for (int i = BeforeMadmanPlayerCount; i > 0; i += -1) {
            Random random = new Random();
            Player MadmanTeamPlayers = Players.get(random.nextInt(Players.size()));
            if (teamM.hasEntry(MadmanTeamPlayers.getName())) {
                // MadmanTeamPlayers.sendMessage("貴方はすでに狂人チームに所属しているため再抽選が行われます");
                return;
            } else if (teamW.hasEntry(MadmanTeamPlayers.getName())) {
                // MadmanTeamPlayers.sendMessage("貴方はすでに狂人チームに所属しているため再抽選が行われます");
                return;
            }
            teamM.addPlayer(MadmanTeamPlayers);
            // MadmanTeamPlayers.sendMessage("貴方は狂人に選ばれました");
        }
        for (Player playerALL5 : Bukkit.getOnlinePlayers()) {
            ALLPlayerCount++;
            playerALL5.setGameMode(GameMode.SURVIVAL);
            playerALL5.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, 10, 80, true, false));
            playerALL5.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 10, 80, true, false));
            playerALL5.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 100, 5, true, false));
            playerALL5.playSound(playerALL5.getLocation(), Sound.ENTITY_WITHER_SPAWN, 0.5f, 1.0f);
            playerALL5.getInventory().clear();
            playerALL5.getActivePotionEffects().clear();
            sendTitle(playerALL5, "&6ゲームスタート！", "", 10, 40, 10);
            playerALL5.sendMessage(ChatColor.BOLD + "" + ChatColor.DARK_GREEN + "-----------------------------------------------------");
            playerALL5.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "ゲームスタート！");
            playerALL5.sendMessage("");
            playerALL5.sendMessage(ChatColor.AQUA + "制限時間は" + ChatColor.RESET + ChatColor.GOLD + "3時間" + ChatColor.RESET + ChatColor.AQUA + "です");
            playerALL5.sendMessage("");
            if (teamV.hasEntry(playerALL5.getName())) {
                playerALL5.sendMessage(ChatColor.DARK_AQUA + "あなたは" + ChatColor.GREEN + "村人陣営" + ChatColor.DARK_AQUA + "です");
            }
            if (teamW.hasEntry(playerALL5.getName())) {
                playerALL5.sendMessage(ChatColor.DARK_AQUA + "あなたは" + ChatColor.RED + "人狼陣営" + ChatColor.DARK_AQUA + "です");
                playerALL5.sendMessage(ChatColor.DARK_AQUA + "仲間は" + ChatColor.RED + teamW.getEntries() + ChatColor.DARK_AQUA + "です");
            }
            if (teamM.hasEntry(playerALL5.getName())) {
                playerALL5.sendMessage(ChatColor.DARK_AQUA + "あなたは" + ChatColor.LIGHT_PURPLE + "狂人陣営" + ChatColor.DARK_AQUA + "です");
            }
            playerALL5.sendMessage(ChatColor.BOLD + "" + ChatColor.DARK_GREEN + "-----------------------------------------------------");
            for (Player playerAdminTAG1 : Bukkit.getOnlinePlayers()) {
                if (playerALL5.getScoreboardTags().contains("Admin1")) {
                    playerAdminTAG1.teleport(playerALL5.getLocation());
                }
            }
            playerALL5.removeScoreboardTag("Admin1");
            playerALL5.setStatistic(org.bukkit.Statistic.DEATHS, 0);
        }
    }
}
