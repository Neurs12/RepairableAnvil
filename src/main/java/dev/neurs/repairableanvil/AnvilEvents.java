package dev.neurs.repairableanvil;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

public class AnvilEvents implements Listener {
    Random random = new Random();
    ArrayList<Material> anvilStates = new ArrayList<>();

    AnvilEvents() {
        anvilStates.add(Material.ANVIL);
        anvilStates.add(Material.CHIPPED_ANVIL);
        anvilStates.add(Material.DAMAGED_ANVIL);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Block clickedBlock = event.getClickedBlock();
        if (clickedBlock == null) return;

        Material type = clickedBlock.getType();
        if (!anvilStates.contains(type)) return;
        if (event.getHand() != EquipmentSlot.HAND || event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        Player player = event.getPlayer();
        if (!player.isSneaking()) return;

        ItemStack itemInHand = player.getInventory().getItemInMainHand();
        if (itemInHand.getType() != Material.IRON_INGOT) return;

        Location location = clickedBlock.getLocation();
        location.setX(location.getX() + 0.5);
        location.setY(location.getY() + 1);
        location.setZ(location.getZ() + 0.5);

        if (type.equals(anvilStates.get(0))) {
            return;
        }

        itemInHand.setAmount(itemInHand.getAmount() - 1);

        // Repair a chipped anvil, have a 15% chance of getting fixed to normal anvil
        if (type.equals(anvilStates.get(1)) && random.nextDouble() <= 0.15) {
            Objects.requireNonNull(location.getWorld()).spawnParticle(Particle.VILLAGER_HAPPY, location, 10, 0.5, 0.5, 0.5);
            location.getWorld().playSound(location, Sound.ENTITY_VILLAGER_WORK_WEAPONSMITH, 10, 1);

            BlockState state = clickedBlock.getState();
            state.setBlockData(state.getBlockData());
            state.setType(Material.ANVIL);
            state.update(true);

            return;
        }

        // Repair a damaged anvil, have a 10% chance of getting fixed to chipped anvil
        if (type.equals(anvilStates.get(2)) && random.nextDouble() <= 0.1) {
            Objects.requireNonNull(location.getWorld()).spawnParticle(Particle.VILLAGER_HAPPY, location, 10, 0.5, 0.5, 0.5);
            location.getWorld().playSound(location, Sound.ENTITY_VILLAGER_WORK_WEAPONSMITH, 10, 1);

            BlockState state = clickedBlock.getState();
            state.setBlockData(state.getBlockData());
            state.setType(Material.CHIPPED_ANVIL);
            state.update(true);

            return;
        }

        Objects.requireNonNull(location.getWorld()).spawnParticle(Particle.ENCHANTMENT_TABLE, location, 10, 0, 0, 0);
        location.getWorld().playSound(location, Sound.BLOCK_ANVIL_BREAK, 5, 1);
    }
}
