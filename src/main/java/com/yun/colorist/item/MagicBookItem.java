package com.yun.colorist.item;

import com.yun.colorist.component.MagicAttrData;
import com.yun.colorist.component.MagicBookData;
import com.yun.colorist.registry.ModComponents;
import com.yun.colorist.registry.ModItems;
import com.yun.colorist.util.AttrUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class MagicBookItem extends Item {
    public MagicBookItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getStackInHand(hand);
        if (world.isClient) return TypedActionResult.success(stack);

        MagicBookData data = stack.getOrDefault(ModComponents.MAGIC_BOOK, MagicBookData.DEFAULT);
        if (data.attrs().isEmpty()) return TypedActionResult.pass(stack);

        MagicAttrData attr = data.attr();
        double cost = AttrUtil.calculateValue(attr, false).cost();
        if (attr.level() < cost) {
            player.sendMessage(net.minecraft.text.Text.translatable("message.colorist.level_low"), false);
            return TypedActionResult.fail(stack);
        }

        int firstLevel = data.attrs().get(0).level();
        int newLevel = (int) Math.max(0, firstLevel - cost);
        MagicAttrData first = data.attrs().get(0);
        MagicAttrData updatedFirst = new MagicAttrData(first.r(), first.g(), first.b(), first.brightness(), first.darkness(), newLevel, first.color());

        java.util.List<MagicAttrData> newAttrs = new java.util.ArrayList<>(data.attrs());
        newAttrs.set(0, updatedFirst);
        MagicAttrData combined = AttrUtil.combine(newAttrs);
        MagicBookData newData = new MagicBookData(newAttrs, combined, data.hasHpBonus());
        stack.set(ModComponents.MAGIC_BOOK, newData);

        stack.setDamage((int) ((1 - Math.max(combined.level() / (cost * 100), 0)) * 1000));
        com.yun.colorist.registry.ModPayloads.sendMagicStart(player, combined);

        return TypedActionResult.success(stack);
    }
}
