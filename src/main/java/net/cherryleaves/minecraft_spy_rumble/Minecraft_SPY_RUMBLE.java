package net.cherryleaves.minecraft_spy_rumble;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
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
        getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        super.onDisable();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent Player) {
        if (Player.getPlayer().isOp()) {
            ItemStack OperateBook = new ItemStack(Material.BOOK);
            ItemMeta OperateBookMeta = OperateBook.getItemMeta(); // metaを登録
            Objects.requireNonNull(OperateBookMeta).setDisplayName(ChatColor.BOLD + "設定本");
            OperateBook.setItemMeta(OperateBookMeta);
            Player.getPlayer().getInventory().addItem(OperateBook);
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event){
        Action a = event.getAction();
        Player p = event.getPlayer();
        if ((a == Action.RIGHT_CLICK_AIR) || (a == Action.RIGHT_CLICK_BLOCK) && (event.getItem() != null) && (event.getItem().getType() == Material.BOOK)){
            p.playSound(event.getPlayer().getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 1, 1);
            p.openInventory(SettingGUI);
            SettingGUICreate();
        }
    }

    Inventory SettingGUI = Bukkit.createInventory(null, 9, ChatColor.BOLD + "設定");

    public void SettingGUICreate(){
        SettingGUI.setItem(1, new ItemStack(Material.WITHER_SKELETON_SKULL));
    }
}
