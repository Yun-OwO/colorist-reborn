package com.yun.colorist.block;

import com.yun.colorist.block.entity.MagicTableBlockEntity;
import com.yun.colorist.component.MagicAttrData;
import com.yun.colorist.component.MagicBookData;
import com.yun.colorist.component.MagicPaperData;
import com.yun.colorist.registry.ModBlocks;
import com.yun.colorist.registry.ModComponents;
import com.yun.colorist.registry.ModItems;
import com.yun.colorist.util.AttrUtil;
import com.yun.colorist.util.ColorUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public class MagicTableBlock extends Block {

    public MagicTableBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        if (level.isClientSide) return InteractionResult.SUCCESS;
        BlockEntity be = level.getBlockEntity(pos);
        if (!(be instanceof MagicTableBlockEntity table)) return InteractionResult.PASS;

        ItemStack held = player.getItemInHand(InteractionHand.MAIN_HAND);
        ItemStack display = table.getDisplayItem();

        if (!display.isEmpty() && !held.isEmpty()) {
            return handleProcess(level, pos, player, table, display, held);
        }

        if (display.isEmpty() && !held.isEmpty()) {
            table.setDisplayItem(held.split(1));
            return InteractionResult.SUCCESS;
        }

        if (!display.isEmpty() && held.isEmpty()) {
            player.getInventory().placeItemBackInInventory(display.copy());
            table.setDisplayItem(ItemStack.EMPTY);
            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }

    private InteractionResult handleProcess(Level level, BlockPos pos, Player player, MagicTableBlockEntity table, ItemStack display, ItemStack held) {
        if (display.is(ModItems.MAGIC_PAPER)) {
            return processPaper(level, pos, player, table, display, held);
        } else if (display.is(ModItems.MAGIC_BOOK)) {
            return processBook(level, pos, player, table, display, held);
        } else {
            player.displayClientMessage(Component.translatable("message.colorist.cannot_dye"), false);
            return InteractionResult.FAIL;
        }
    }

    private InteractionResult processPaper(Level level, BlockPos pos, Player player, MagicTableBlockEntity table, ItemStack paper, ItemStack held) {
        MagicPaperData data = paper.getOrDefault(ModComponents.MAGIC_PAPER, MagicPaperData.DEFAULT);
        String dyeColor = ColorUtil.DYE_COLORS.get(BuiltInRegistries.ITEM.getKey(held.getItem()).getPath());
        if (dyeColor != null) {
            String newColor = ColorUtil.merge(data.attr(), dyeColor, 1f / data.level());
            int newLevel = data.level() + 1;
            ItemStack newPaper = paper.copy();
            newPaper.set(ModComponents.MAGIC_PAPER, new MagicPaperData(newLevel, newColor));
            table.setDisplayItem(newPaper);
            held.shrink(1);
            player.displayClientMessage(Component.translatable("message.colorist.dye_success").withStyle(Style.EMPTY.withColor(ColorUtil.hexToInt(newColor))), false);
            return InteractionResult.SUCCESS;
        } else if (held.is(ModItems.MAGIC_CRYSTAL)) {
            ItemStack newPaper = paper.copy();
            newPaper.set(ModComponents.MAGIC_PAPER, new MagicPaperData(data.level() + 5, data.attr()));
            table.setDisplayItem(newPaper);
            held.shrink(1);
            player.displayClientMessage(Component.translatable("message.colorist.add_success"), false);
            return InteractionResult.SUCCESS;
        }
        player.displayClientMessage(Component.translatable("message.colorist.cannot_dye"), false);
        return InteractionResult.FAIL;
    }

    private InteractionResult processBook(Level level, BlockPos pos, Player player, MagicTableBlockEntity table, ItemStack book, ItemStack held) {
        MagicBookData data = book.getOrDefault(ModComponents.MAGIC_BOOK, MagicBookData.DEFAULT);
        List<MagicAttrData> attrs = new ArrayList<>(data.attrs());
        if (held.is(ModItems.MAGIC_PAPER)) {
            if (attrs.size() >= AttrUtil.MAX_ATTRS) {
                player.displayClientMessage(Component.translatable("message.colorist.book_full"), false);
                return InteractionResult.FAIL;
            }
            MagicPaperData paperData = held.getOrDefault(ModComponents.MAGIC_PAPER, MagicPaperData.DEFAULT);
            MagicAttrData newAttr = AttrUtil.calculateFromPaper(paperData.level(), paperData.attr());
            attrs.add(newAttr);
            MagicAttrData combined = AttrUtil.combine(attrs);
            ItemStack newBook = book.copy();
            newBook.set(ModComponents.MAGIC_BOOK, new MagicBookData(attrs, combined, data.hasHpBonus()));
            table.setDisplayItem(newBook);
            held.shrink(1);
            player.displayClientMessage(Component.translatable("message.colorist.insert_success", attrs.size(), AttrUtil.MAX_ATTRS), false);
            return InteractionResult.SUCCESS;
        } else if (held.is(ModItems.MAGIC_CRYSTAL)) {
            if (attrs.isEmpty()) {
                player.displayClientMessage(Component.translatable("message.colorist.book_empty"), false);
                return InteractionResult.FAIL;
            }
            MagicAttrData first = new MagicAttrData(attrs.get(0).r(), attrs.get(0).g(), attrs.get(0).b(), attrs.get(0).brightness(), attrs.get(0).darkness(), attrs.get(0).level() + 5, attrs.get(0).color());
            attrs.set(0, first);
            MagicAttrData combined = AttrUtil.combine(attrs);
            ItemStack newBook = book.copy();
            newBook.set(ModComponents.MAGIC_BOOK, new MagicBookData(attrs, combined, data.hasHpBonus()));
            table.setDisplayItem(newBook);
            held.shrink(1);
            player.displayClientMessage(Component.translatable("message.colorist.add_success"), false);
            return InteractionResult.SUCCESS;
        }
        player.displayClientMessage(Component.translatable("message.colorist.cannot_insert"), false);
        return InteractionResult.FAIL;
    }

    @Override
    public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        if (!level.isClientSide) {
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof MagicTableBlockEntity table && table.hasItem()) {
                ItemStack stack = table.getDisplayItem();
                if (!stack.isEmpty() && !player.isCreative()) {
                    Vec3 center = Vec3.atCenterOf(pos);
                    level.addFreshEntity(new ItemEntity(level, center.x, center.y + 0.5, center.z, stack));
                }
            }
        }
        return super.playerWillDestroy(level, pos, state, player);
    }
}
