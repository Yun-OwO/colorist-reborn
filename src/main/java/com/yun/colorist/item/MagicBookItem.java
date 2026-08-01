package com.yun.colorist.item;

import com.yun.colorist.Colorist;
import com.yun.colorist.component.MagicAttrData;
import com.yun.colorist.component.MagicBookData;
import com.yun.colorist.event.MagicBookHandler;
import com.yun.colorist.registry.ModComponents;
import com.yun.colorist.registry.ModItems;
import com.yun.colorist.registry.ModPayloads;
import com.yun.colorist.util.AttrUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class MagicBookItem extends Item {
    public MagicBookItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        Colorist.LOGGER.debug("MagicBookItem.use called by {} hand={}", player.getName().getString(), hand);

        if (level.isClientSide()) {
            Colorist.LOGGER.debug("Client side, returning SUCCESS");
            return InteractionResult.SUCCESS;
        }

        MagicBookData data = stack.getOrDefault(ModComponents.MAGIC_BOOK, MagicBookData.DEFAULT);
        if (data.attrs().isEmpty()) {
            Colorist.LOGGER.debug("Magic book has no attributes, returning PASS");
            return InteractionResult.PASS;
        }

        MagicAttrData attr = data.attr();
        double cost = AttrUtil.calculateValue(attr, false).cost();
        Colorist.LOGGER.debug("Book attr level={}, cost={}", attr.level(), cost);
        if (attr.level() < cost) {
            Colorist.LOGGER.warn("Level too low: {} < {}", attr.level(), cost);
            player.displayClientMessage(Component.translatable("message.colorist.level_low"), false);
            return InteractionResult.FAIL;
        }

        int firstLevel = data.attrs().get(0).level();
        int newLevel = (int) Math.max(0, firstLevel - cost);
        Colorist.LOGGER.debug("First attr level: {} -> {} (cost={})", firstLevel, newLevel, cost);
        MagicAttrData first = data.attrs().get(0);
        MagicAttrData updatedFirst = new MagicAttrData(first.r(), first.g(), first.b(), first.brightness(), first.darkness(), newLevel, first.color());

        java.util.List<MagicAttrData> newAttrs = new java.util.ArrayList<>(data.attrs());
        newAttrs.set(0, updatedFirst);
        MagicAttrData combined = AttrUtil.combine(newAttrs);
        MagicBookData newData = new MagicBookData(newAttrs, combined, data.hasHpBonus());
        stack.set(ModComponents.MAGIC_BOOK, newData);

        int damage = (int) ((1 - Math.max(combined.level() / (cost * 100), 0)) * 1000);
        stack.setDamageValue(damage);
        Colorist.LOGGER.debug("Setting damage to {}", damage);

        ModPayloads.sendMagicStart(player, combined);
        if (player instanceof ServerPlayer serverPlayer) {
            Colorist.LOGGER.debug("Starting cast for player {}", player.getName().getString());
            MagicBookHandler.startCast(serverPlayer);
        }

        Colorist.LOGGER.info("MagicBook cast successful for {}", player.getName().getString());
        return InteractionResult.SUCCESS;
    }
}
