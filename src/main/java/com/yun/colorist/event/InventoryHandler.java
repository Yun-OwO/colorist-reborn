package com.yun.colorist.event;

import com.yun.colorist.component.MagicBookData;
import com.yun.colorist.registry.ModComponents;
import com.yun.colorist.registry.ModItems;
import com.yun.colorist.util.AttrUtil;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class InventoryHandler {

    private static final Map<UUID, Boolean> HAS_BOOK = new HashMap<>();

    public static void register() {
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            for (ServerPlayer player : server.getPlayerList().getPlayers()) {
                updatePlayer(player);
            }
        });

        ServerPlayerEvents.AFTER_RESPAWN.register((oldPlayer, newPlayer, alive) -> {
            HAS_BOOK.remove(newPlayer.getUUID());
            ItemStack book = findBook(newPlayer);
            if (!book.isEmpty()) {
                MagicBookData data = book.getOrDefault(ModComponents.MAGIC_BOOK, MagicBookData.DEFAULT);
                double hp = AttrUtil.calculateValue(data.attr(), false).hp();
                double base = newPlayer.getAttribute(Attributes.MAX_HEALTH).getBaseValue();
                newPlayer.getAttribute(Attributes.MAX_HEALTH).setBaseValue(base + hp);
                newPlayer.setHealth((float) (base + hp));
                HAS_BOOK.put(newPlayer.getUUID(), true);
            }
        });
    }

    private static void updatePlayer(ServerPlayer player) {
        ItemStack book = findBook(player);
        boolean hadBook = HAS_BOOK.getOrDefault(player.getUUID(), false);
        boolean hasBook = !book.isEmpty();
        MagicBookData data = hasBook ? book.getOrDefault(ModComponents.MAGIC_BOOK, MagicBookData.DEFAULT) : null;
        double hp = data != null ? AttrUtil.calculateValue(data.attr(), false).hp() : 0;
        double base = player.getAttribute(Attributes.MAX_HEALTH).getBaseValue();

        if (hasBook && !hadBook) {
            player.getAttribute(Attributes.MAX_HEALTH).setBaseValue(base + hp);
            HAS_BOOK.put(player.getUUID(), true);
        } else if (!hasBook && hadBook) {
            player.getAttribute(Attributes.MAX_HEALTH).setBaseValue(base - hp);
            HAS_BOOK.put(player.getUUID(), false);
        }
    }

    private static ItemStack findBook(Player player) {
        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            ItemStack stack = player.getInventory().getItem(i);
            if (stack.is(ModItems.MAGIC_BOOK)) return stack;
        }
        return ItemStack.EMPTY;
    }
}
