package net.cherryleaves.minecraft_spy_rumble;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
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

import java.util.Objects;

public final class Minecraft_SPY_RUMBLE extends JavaPlugin implements Listener {
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
        getServer().getPluginManager().registerEvents(this, this);
        getServer().getPluginManager().registerEvents(new ItemSpawnStand(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        super.onDisable();
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
        return false;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent Player) {
        Player.getPlayer().getInventory().clear();
        if (Player.getPlayer().isOp()) {
            ItemStack OperateBook = new ItemStack(Material.BOOK);
            ItemMeta OperateBookMeta = OperateBook.getItemMeta(); // metaを登録
            Objects.requireNonNull(OperateBookMeta).setDisplayName(ChatColor.BOLD + "設定本");
            OperateBook.setItemMeta(OperateBookMeta);
            Player.getPlayer().getInventory().addItem(OperateBook);
            new ItemSpawnStand().getItem(Player.getPlayer());
        }
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
                    player.closeInventory();
                    new Game().Start();
                }
                event.setCancelled(true);
            }
        }
    }
}
