package me.itay.revertentitydropspatch;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class RevertEntityDropsPatch extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onEntityDeath(EntityDeathEvent event) {
        List<ItemStack> droppedItems = new ArrayList<>();
        Entity entity = event.getEntity();
        if (entity instanceof Item) {
            return;
        }
        droppedItems.addAll(event.getDrops());
        event.getDrops().clear();
        Location deathLocation = entity.getLocation();

        Bukkit.getScheduler().runTaskLater(this, () -> {
            for (ItemStack itemStack : droppedItems) {
                // Spawn the dropped item at the death location
                Item item = deathLocation.getWorld().dropItemNaturally(deathLocation, itemStack);
                item.setPickupDelay(40); // Set pickup delay to prevent immediate pickup by players
            }
        }, 1);
    }
}
