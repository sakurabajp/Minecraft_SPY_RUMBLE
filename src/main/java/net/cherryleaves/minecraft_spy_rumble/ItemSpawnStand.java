package net.cherryleaves.minecraft_spy_rumble;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Objects;

public class ItemSpawnStand implements Listener {

    Inventory RGUI = Bukkit.createInventory(null, 9, ChatColor.BOLD + "設定");

    public void getItem(Player player) {
        ItemStack setTaskItem = new ItemStack(Material.GOLDEN_PICKAXE);
        ItemMeta setTaskItemMeta = setTaskItem.getItemMeta();
        Objects.requireNonNull(setTaskItemMeta).setDisplayName(ChatColor.GOLD + "金のスポーン場所をセットする");
        setTaskItem.setItemMeta(setTaskItemMeta);
        // インベントリにアイテムを加える
        player.getInventory().addItem(setTaskItem);
    }

    @EventHandler
    public void setStand(PlayerInteractEvent event) {
        if (event.getPlayer().getInventory().getItemInMainHand().getType() == Material.GOLDEN_PICKAXE && Objects.requireNonNull(event.getPlayer().getInventory().getItemInMainHand().getItemMeta()).getDisplayName().equals(ChatColor.GOLD + "金のスポーン場所をセットする")) {
            if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                if (event.getHand() == EquipmentSlot.HAND) {
                    Block b = event.getClickedBlock();
                    Objects.requireNonNull(b).getWorld().spawn(b.getLocation().add(0.5, 1, 0.5), ArmorStand.class, armorStand -> {
                        armorStand.setBasePlate(true); // 防具建てが地面に設置されないようにする
                        armorStand.setCustomName(ChatColor.GOLD + "金の出現場所の候補");
                        armorStand.addScoreboardTag("TaskPoint");
                        armorStand.setInvulnerable(true);
                        event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.BLOCK_DISPENSER_DISPENSE, 1.0f, 1.0f);
                    });
                }
            }
            if (event.getAction() == Action.RIGHT_CLICK_AIR) {
                ItemStack ResetItem = new ItemStack(Material.ARMOR_STAND);
                ItemMeta ResetItemMeta = ResetItem.getItemMeta();
                Objects.requireNonNull(ResetItemMeta).setDisplayName(ChatColor.AQUA + "出現場所を全てリセットする");
                ResetItem.setItemMeta(ResetItemMeta);
                RGUI.setItem(2, ResetItem);
                ItemStack GlowItem = new ItemStack(Material.GLOW_INK_SAC);
                ItemMeta GlowItemMeta = GlowItem.getItemMeta();
                Objects.requireNonNull(GlowItemMeta).setDisplayName(ChatColor.AQUA + "スポーン場所を発光させる");
                GlowItem.setItemMeta(GlowItemMeta);
                RGUI.setItem(4, GlowItem);
                ItemStack unGlowItem = new ItemStack(Material.INK_SAC);
                ItemMeta unGlowItemMeta = unGlowItem.getItemMeta();
                Objects.requireNonNull(unGlowItemMeta).setDisplayName(ChatColor.AQUA + "スポーン場所の発光を解除");
                unGlowItem.setItemMeta(unGlowItemMeta);
                RGUI.setItem(6, unGlowItem);
                event.getPlayer().openInventory(RGUI);
                event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.BLOCK_DISPENSER_DISPENSE, 1.0f, 1.0f);
            }
            if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerInventoryClickEvent(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player) {
            // クリックされたGUIを取得する
            Inventory clickedInventory = event.getClickedInventory();
            // クリックしたプレイヤーを取得
            Player player = (Player) event.getWhoClicked();
            if (clickedInventory == RGUI) {
                ItemStack clickedItem = event.getCurrentItem();
                if (clickedItem != null && clickedItem.getType().equals(Material.ARMOR_STAND)) {
                    for (Entity entity : player.getWorld().getEntities()) {
                        // エンティティがアーマースタンドであり、かつ指定されたタグを持つかどうかを確認します。
                        if (entity instanceof ArmorStand && entity.getScoreboardTags().contains("TaskPoint")) {
                            ArmorStand armorStand = (ArmorStand) entity;
                            // アーマースタンドを削除します。
                            armorStand.remove();
                        }
                    }
                    player.closeInventory();
                    player.sendMessage(ChatColor.GOLD + "金のスポーン場所を全て削除しました");
                    player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
                }
                if (clickedItem != null && clickedItem.getType().equals(Material.GLOW_INK_SAC)) {
                    for (Entity entity : player.getWorld().getEntities()) {
                        // エンティティがアーマースタンドであり、かつ指定されたタグを持つかどうかを確認します。
                        if (entity instanceof ArmorStand && entity.getScoreboardTags().contains("TaskPoint")) {
                            ArmorStand armorStand = (ArmorStand) entity;
                            // アーマースタンドを削除します。
                            armorStand.setGlowing(true);
                        }
                    }
                    player.closeInventory();
                    player.sendMessage(ChatColor.GOLD + "金のスポーン場所を全て発光させました");
                    player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
                }
                if (clickedItem != null && clickedItem.getType().equals(Material.INK_SAC)) {
                    for (Entity entity : player.getWorld().getEntities()) {
                        // エンティティがアーマースタンドであり、かつ指定されたタグを持つかどうかを確認します。
                        if (entity instanceof ArmorStand && entity.getScoreboardTags().contains("TaskPoint")) {
                            ArmorStand armorStand = (ArmorStand) entity;
                            // アーマースタンドを削除します。
                            armorStand.setGlowing(false);
                        }
                    }
                    player.closeInventory();
                    player.sendMessage(ChatColor.GOLD + "金のスポーン場所の発光を全て解除しました");
                    player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 0.1f);
                }
                event.setCancelled(true);
            }
        }
    }
}
