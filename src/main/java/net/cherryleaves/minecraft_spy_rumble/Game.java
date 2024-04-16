package net.cherryleaves.minecraft_spy_rumble;

import org.bukkit.*;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import static net.cherryleaves.minecraft_spy_rumble.Minecraft_SPY_RUMBLE.ParallelTaskCount;

public class Game {
    public void Start() {
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
        for (Player playerACC : Bukkit.getOnlinePlayers()) {
            teamV.addPlayer(playerACC);
        }
        for (int i = Minecraft_SPY_RUMBLE.BWPCount; i > 0; i -= 1) {
            Random random = new Random();
            Player WolfTeamPlayers = Players.get(random.nextInt(Players.size()));
            if (teamW.hasEntry(WolfTeamPlayers.getName())) {
                return;
            }
            teamW.addPlayer(WolfTeamPlayers);
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
            playerALL.sendMessage(ChatColor.AQUA + "同時タスク出現数は" + ChatColor.RESET + ChatColor.GOLD + ParallelTaskCount + ChatColor.RESET + ChatColor.AQUA + "個です");
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
        for (Entity entity : Bukkit.getWorlds().get(0).getEntities()) {
            // エンティティがアーマースタンドであり、かつ指定されたタグを持つかどうかを確認します。
            if (entity instanceof ArmorStand && entity.getScoreboardTags().contains("TaskPoint")) {
                ArmorStand armorStand = (ArmorStand) entity;
                // アーマースタンドを削除します。
                armorStand.setGlowing(false);
            }
        }
        for (int i = ParallelTaskCount; i > 0; i -= 1) {
            onArmorStand();
        }
        new Minecraft_SPY_RUMBLE().PlayerSneak();
    }

    // 過去の自分さん、このメソッドで何がしたいのかくらいメモしててくれても良かったんじゃないですかね
    public void onArmorStand() {
        World currentWorld = Bukkit.getWorlds().get(0); // 現在のワールドはリストの先頭にあると仮定
        List<ArmorStand> armorStands = (List<ArmorStand>) currentWorld.getEntitiesByClass(ArmorStand.class);
        ArmorStand randomArmorStand = armorStands.get(new Random().nextInt(armorStands.size()));
        if (randomArmorStand != null && randomArmorStand.getScoreboardTags().contains("TaskPoint")) {
            if(!randomArmorStand.getScoreboardTags().contains("SelectedTaskPoint")) {
                randomArmorStand.addScoreboardTag("SelectedTaskPoint");
                randomArmorStand.setGlowing(true);
            }
            else{
                onArmorStand();
            }
        }
    }
    public void outArmorStand(Entity e){
        String tag = e.getScoreboardTags().toString();
        if(e.getType().equals(EntityType.ARMOR_STAND)){
            if(tag.equals("SelectedTaskPoint")) {
                e.setGlowing(false);
                e.removeScoreboardTag("SelectedTaskPoint");
            }
        }
    }
}