package com.yun.colorist.event;

import com.yun.colorist.component.MagicAttrData;
import com.yun.colorist.component.MagicBookData;
import com.yun.colorist.registry.ModComponents;
import com.yun.colorist.registry.ModItems;
import com.yun.colorist.util.AttrUtil;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class InventoryHandler {

    private static final Map<UUID, Boolean> HAS_BOOK = new HashMap<>();

    public static void register() {
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
                updatePlayer(player);
            }
        });

        ServerPlayerEvents.AFTER_RESPAWN.register((oldPlayer, newPlayer, alive) -> {
            HAS_BOOK.remove(newPlayer.getUuid());
            ItemStack book = findBook(newPlayer);
            if (!book.isEmpty()) {
                MagicBookData data = book.getOrDefault(ModComponents.MAGIC_BOOK, MagicBookData.DEFAULT);
                double hp = AttrUtil.calculateValue(data.attr(), false).hp();
                double base = newPlayer.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH).getBaseValue();
                newPlayer.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH).setBaseValue(base + hp);
                newPlayer.setHealth((float) (base + hp));
                HAS_BOOK.put(newPlayer.getUuid(), true);
            }
        });
    }

    private static void updatePlayer(ServerPlayerEntity player) {
        ItemStack book = findBook(player);
        boolean hadBook = HAS_BOOK.getOrDefault(player.getUuid(), false);
        boolean hasBook = !book.isEmpty();
        MagicBookData data = hasBook ? book.getOrDefault(ModComponents.MAGIC_BOOK, MagicBookData.DEFAULT) : null;
        double hp = data != null ? AttrUtil.calculateValue(data.attr(), false).hp() : 0;
        double base = player.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH).getBaseValue();

        if (hasBook && !hadBook) {
            player.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH).setBaseValue(base + hp);
            HAS_BOOK.put(player.getUuid(), true);
        } else if (!hasBook && hadBook) {
            player.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH).setBaseValue(base - hp);
            HAS_BOOK.put(player.getUuid(), false);
        }
    }

    private static ItemStack findBook(PlayerEntity player) {
        for (int i = 0; i < player.getInventory().size(); i++) {
            ItemStack stack = player.getInventory().getStack(i);
            if (stack.isOf(ModItems.MAGIC_BOOK)) return stack;
        }
        return ItemStack.EMPTY;
    }
}
