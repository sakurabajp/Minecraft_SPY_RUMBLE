package net.cherryleaves.minecraft_spy_rumble;

import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Objects;

public final class Minecraft_SPY_RUMBLE extends JavaPlugin implements Listener {
    public BukkitRunnable task;
    @Override
    public void onEnable() {
        // Plugin startup logic
        super.onEnable();
        ConsoleCommandSender console = getServer().getConsoleSender();
        console.sendMessage(ChatColor.GREEN + "ーーーーーーーーーーーーーーーーーーーーーーーーーーーーーー");
        console.sendMessage(ChatColor.AQUA + "Minecraft Spy Rumble plugin activated!!!!!!!!!!!");
        console.sendMessage("");
        console.sendMessage(ChatColor.GREEN + "このゲームは【Spy Rumble】というゲームをモチーフにし、");
        console.sendMessage(ChatColor.GREEN + "我々制作陣がこるんの名の下勝手にMinecraft化したゲームである");
        console.sendMessage("");
        console.sendMessage(ChatColor.GREEN + "java素人の人が書いたコードですので、");
        console.sendMessage(ChatColor.GREEN + "修正等ありましたら是非Githubにプルリク投げて下さい");
        console.sendMessage("");
        console.sendMessage(ChatColor.GREEN + "ーーーーーーーーーーーーーーーーーーーーーーーーーーーーーー");
        Objects.requireNonNull(getCommand("task-spawn")).setExecutor(this);
        Objects.requireNonNull(getCommand("start")).setExecutor(this);
        Objects.requireNonNull(getCommand("stop-game")).setExecutor(this);
        getServer().getPluginManager().registerEvents(this, this);
        getServer().getPluginManager().registerEvents(new ItemSpawnStand(), this);
        getServer().getPluginManager().registerEvents(new Player_Task(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        super.onDisable();
        task.cancel();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("task-spawn")) {
            if (!(sender instanceof Player) || !sender.isOp()) {
                sender.sendMessage(ChatColor.RED + "You don't have permission to use this command.");
                return true;
            }
            Player admin = ((Player) sender).getPlayer();
            // GUiを開く
            assert admin != null;
            admin.playSound(admin.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 0.7f, 0.8f);
            new ItemSpawnStand().getItem(admin);
        }
        if (command.getName().equalsIgnoreCase("start")) {
            if (!(sender instanceof Player) || !sender.isOp()) {
                sender.sendMessage(ChatColor.RED + "You don't have permission to use this command.");
                return true;
            }
            Player admin = ((Player) sender).getPlayer();
            // GUiを開く
            assert admin != null;
            admin.playSound(admin.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 0.7f, 0.8f);
            GiveBook(admin);
        }
        if (command.getName().equalsIgnoreCase("stop-game")) {
            if (!(sender instanceof Player) || !sender.isOp()) {
                sender.sendMessage(ChatColor.RED + "You don't have permission to use this command.");
                return true;
            }
            Objects.requireNonNull(((Player) sender).getPlayer()).sendMessage("ゲームを強制中断させました");
            ((Player) sender).getPlayer().playSound(((Player) sender).getLocation(), Sound.ITEM_GOAT_HORN_SOUND_0, 1, 1);
            Objects.requireNonNull(Objects.requireNonNull(Bukkit.getScoreboardManager()).getMainScoreboard().getTeam("wolf")).unregister();
            Objects.requireNonNull(Objects.requireNonNull(Bukkit.getScoreboardManager()).getMainScoreboard().getTeam("villager")).unregister();
            Bukkit.getServer().getWorlds().forEach(world -> {
                world.getEntities().forEach(entity -> {
                    if (entity instanceof ArmorStand) {
                        ArmorStand armorStand = (ArmorStand) entity;
                        if (armorStand.getScoreboardTags().contains("TaskPoint")) {
                            armorStand.setGlowing(true);
                            if (armorStand.getScoreboardTags().contains("SelectedTaskPoint")) {
                                armorStand.removeScoreboardTag("SelectedTaskPoint");
                            }
                        }
                    }
                });
            });
        }
        return false;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent Player) {
        Player.getPlayer().getInventory().clear();
        if (Player.getPlayer().isOp()) {
            GiveBook(Player.getPlayer());
            new ItemSpawnStand().getItem(Player.getPlayer());
        }
    }

    public void GiveBook(Player p){
        ItemStack OperateBook = new ItemStack(Material.BOOK);
        ItemMeta OperateBookMeta = OperateBook.getItemMeta(); // metaを登録
        Objects.requireNonNull(OperateBookMeta).setDisplayName(ChatColor.BOLD + "設定本");
        OperateBook.setItemMeta(OperateBookMeta);
        Objects.requireNonNull(p.getPlayer()).getInventory().addItem(OperateBook);
    }

    // BeforeWolfPlayerCountの略
    public static int BWPCount = 1;
    // BeforeVillagerPlayerCountの略
    public static int BVPCount;
    // そもそもの全体人数
    public static int PlayerCount;
    // 同時に出現するタスクの数
    public static int ParallelTaskCount = 3;
    Inventory SettingGUI = Bukkit.createInventory(null, 9, ChatColor.BOLD + "設定");
    int armorStandCount = 0;


    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Action a = event.getAction();
        Player p = event.getPlayer();
        if (((a == Action.RIGHT_CLICK_AIR) || (a == Action.RIGHT_CLICK_BLOCK)) && (event.getItem() != null) && (event.getItem().getType() == Material.BOOK)) {
            p.playSound(event.getPlayer().getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
            SettingGUICreate();
            p.openInventory(SettingGUI);
        }
    }

    public void SettingGUICreate() {
        // 人狼の数減らし
        ItemStack WolfCountDownItem = new ItemStack(Material.BLUE_CANDLE);
        ItemMeta WolfCountDownItemMeta = WolfCountDownItem.getItemMeta();
        Objects.requireNonNull(WolfCountDownItemMeta).setDisplayName(ChatColor.BLUE + "人狼の数を減らす");
        WolfCountDownItem.setItemMeta(WolfCountDownItemMeta);
        SettingGUI.setItem(0, WolfCountDownItem);
        // 人狼の数表示
        ItemStack WolfPlayerCountItem = new ItemStack(Material.WITHER_SKELETON_SKULL);
        ItemMeta WolfPlayerCountItemMeta = WolfPlayerCountItem.getItemMeta();
        Objects.requireNonNull(WolfPlayerCountItemMeta).setDisplayName(ChatColor.DARK_AQUA + "人狼の数は" + ChatColor.GOLD + BWPCount + ChatColor.DARK_AQUA + "人です");
        WolfPlayerCountItem.setItemMeta(WolfPlayerCountItemMeta);
        SettingGUI.setItem(1, WolfPlayerCountItem);
        // 人狼の数増やし
        ItemStack WolfCountUpItem = new ItemStack(Material.RED_CANDLE);
        ItemMeta WolfCountUpItemMeta = WolfCountUpItem.getItemMeta();
        Objects.requireNonNull(WolfCountUpItemMeta).setDisplayName(ChatColor.RED + "人狼の数を増やす");
        WolfCountUpItem.setItemMeta(WolfCountUpItemMeta);
        SettingGUI.setItem(2, WolfCountUpItem);
        // タスクの数減らし
        ItemStack TaskCountDownItem = new ItemStack(Material.LIGHT_BLUE_CANDLE);
        ItemMeta TaskCountDownItemMeta = TaskCountDownItem.getItemMeta();
        Objects.requireNonNull(TaskCountDownItemMeta).setDisplayName(ChatColor.RED + "同時に出現するタスクの数を減らす");
        TaskCountDownItem.setItemMeta(TaskCountDownItemMeta);
        SettingGUI.setItem(4, TaskCountDownItem);
        // タスクの数表示
        ItemStack TaskCountItem = new ItemStack(Material.IRON_PICKAXE);
        ItemMeta TaskCountItemMeta = TaskCountItem.getItemMeta();
        Objects.requireNonNull(TaskCountItemMeta).setDisplayName(ChatColor.DARK_AQUA + "同時に出現するタスクの数は" + ChatColor.GOLD + ParallelTaskCount + ChatColor.DARK_AQUA + "個です");
        TaskCountItem.setItemMeta(TaskCountItemMeta);
        SettingGUI.setItem(5, TaskCountItem);
        // タスクの数増やし
        ItemStack TaskCountUpItem = new ItemStack(Material.MAGENTA_CANDLE);
        ItemMeta TaskCountUpItemMeta = TaskCountUpItem.getItemMeta();
        Objects.requireNonNull(TaskCountUpItemMeta).setDisplayName(ChatColor.RED + "同時に出現するタスクの数を増やす");
        TaskCountUpItem.setItemMeta(TaskCountUpItemMeta);
        SettingGUI.setItem(6, TaskCountUpItem);
        // ゲームスタートボタン
        ItemStack GameStartItem = new ItemStack(Material.TOTEM_OF_UNDYING);
        ItemMeta GameStartItemMeta = GameStartItem.getItemMeta();
        Objects.requireNonNull(GameStartItemMeta).setDisplayName(ChatColor.BLUE + "ゲームスタート！");
        GameStartItem.setItemMeta(GameStartItemMeta);
        SettingGUI.setItem(8, GameStartItem);
    }

    @EventHandler
    public void onPlayerInventoryClickEvent(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player) {
            Player player = (Player) event.getWhoClicked();
            // クリックされたGUIを取得する
            Inventory clickedInventory = event.getClickedInventory();
            if (clickedInventory == SettingGUI) {
                ItemStack clickedItem = event.getCurrentItem();
                if (clickedItem != null && clickedItem.getType().equals(Material.RED_CANDLE)) {
                    BWPCount += 1;
                    player.playSound(player.getLocation(), Sound.BLOCK_DISPENSER_DISPENSE, 1.0f, 1.0f);
                    SettingGUICreate();
                    player.openInventory(SettingGUI);
                }
                if (clickedItem != null && clickedItem.getType().equals(Material.BLUE_CANDLE)) {
                    if (BWPCount > 1) {
                        BWPCount -= 1;
                        player.playSound(player.getLocation(), Sound.BLOCK_DISPENSER_DISPENSE, 1.0f, 1.0f);
                        SettingGUICreate();
                        player.openInventory(SettingGUI);
                    }
                    else {
                        player.sendMessage(ChatColor.RED + "人狼の数を1未満にすることはできません");
                        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 1.0f, 0.01f);
                        event.setCancelled(true);
                    }
                }
                if (clickedItem != null && clickedItem.getType().equals(Material.MAGENTA_CANDLE)) {
                    ParallelTaskCount += 1;
                    player.playSound(player.getLocation(), Sound.BLOCK_DISPENSER_DISPENSE, 1.0f, 1.0f);
                    SettingGUICreate();
                    player.openInventory(SettingGUI);
                }
                if (clickedItem != null && clickedItem.getType().equals(Material.LIGHT_BLUE_CANDLE)) {
                    if (ParallelTaskCount > 0) {
                        ParallelTaskCount -= 1;
                        player.playSound(player.getLocation(), Sound.BLOCK_DISPENSER_DISPENSE, 1.0f, 1.0f);
                        SettingGUICreate();
                        player.openInventory(SettingGUI);
                    }
                    else {
                        player.sendMessage(ChatColor.RED + "同時に出現するタスクの数を0未満にすることはできません");
                        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 1.0f, 0.01f);
                    }
                }
                if (clickedItem != null && clickedItem.getType().equals(Material.TOTEM_OF_UNDYING)) {
                    armorStandCount = 0;
                    for (ArmorStand armorStand : Bukkit.getWorlds().get(0).getEntitiesByClass(ArmorStand.class)) {
                        if (armorStand.getScoreboardTags().contains("TaskPoint")) {
                            armorStandCount++;
                        }
                    }
                    if(armorStandCount >= ParallelTaskCount) {
                        player.closeInventory();
                        new Game().Start();
                    }
                    else{
                        player.sendMessage(ChatColor.RED + "設置してあるアーマースタンドの数を超えて設定することはできません。");
                        player.playSound(player, Sound.BLOCK_NOTE_BLOCK_BASS, 1f, 1f);
                    }
                }
                event.setCancelled(true);
            }
        }
    }

    public void PlayerSneak() {
        task = new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (player.isSneaking()) {
                        // ゲーム開始時にスコアボード再作成してここで、任意のプレイヤースニーク時に減らす
                        Objects.requireNonNull(player.getScoreboard().getObjective("scoreboardA")).getScore(player.getPlayer()).setScore(-1);
                    }
                }
            }
        };task.runTaskTimer(this, 0L, 1L);
    }
}
