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
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class MagicTableBlock extends Block {

    public MagicTableBlock(AbstractBlock.Settings settings) {
        super(settings);
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (world.isClient) return ActionResult.SUCCESS;
        BlockEntity be = world.getBlockEntity(pos);
        if (!(be instanceof MagicTableBlockEntity table)) return ActionResult.PASS;

        ItemStack held = player.getStackInHand(Hand.MAIN_HAND);
        ItemStack display = table.getDisplayItem();

        if (!display.isEmpty() && !held.isEmpty()) {
            return handleProcess(world, pos, player, table, display, held);
        }

        if (display.isEmpty() && !held.isEmpty()) {
            table.setDisplayItem(held.split(1));
            return ActionResult.SUCCESS;
        }

        if (!display.isEmpty() && held.isEmpty()) {
            player.getInventory().offerOrDrop(display.copy());
            table.setDisplayItem(ItemStack.EMPTY);
            return ActionResult.SUCCESS;
        }

        return ActionResult.PASS;
    }

    private ActionResult handleProcess(World world, BlockPos pos, PlayerEntity player, MagicTableBlockEntity table, ItemStack display, ItemStack held) {
        if (display.isOf(ModItems.MAGIC_PAPER)) {
            return processPaper(world, pos, player, table, display, held);
        } else if (display.isOf(ModItems.MAGIC_BOOK)) {
            return processBook(world, pos, player, table, display, held);
        } else {
            player.sendMessage(Text.translatable("message.colorist.cannot_dye"), false);
            return ActionResult.FAIL;
        }
    }

    private ActionResult processPaper(World world, BlockPos pos, PlayerEntity player, MagicTableBlockEntity table, ItemStack paper, ItemStack held) {
        MagicPaperData data = paper.getOrDefault(ModComponents.MAGIC_PAPER, MagicPaperData.DEFAULT);
        String dyeColor = ColorUtil.DYE_COLORS.get(Identifier.of(held.getItem().toString()).getPath());
        if (dyeColor != null) {
            String newColor = ColorUtil.merge(data.attr(), dyeColor, 1f / data.level());
            int newLevel = data.level() + 1;
            ItemStack newPaper = paper.copy();
            newPaper.set(ModComponents.MAGIC_PAPER, new MagicPaperData(newLevel, newColor));
            table.setDisplayItem(newPaper);
            held.decrement(1);
            player.sendMessage(Text.translatable("message.colorist.dye_success").setStyle(net.minecraft.text.Style.EMPTY.withColor(net.minecraft.util.Formatting.byName(newColor))), false);
            return ActionResult.SUCCESS;
        } else if (held.isOf(ModItems.MAGIC_CRYSTAL)) {
            ItemStack newPaper = paper.copy();
            newPaper.set(ModComponents.MAGIC_PAPER, new MagicPaperData(data.level() + 5, data.attr()));
            table.setDisplayItem(newPaper);
            held.decrement(1);
            player.sendMessage(Text.translatable("message.colorist.add_success"), false);
            return ActionResult.SUCCESS;
        }
        player.sendMessage(Text.translatable("message.colorist.cannot_dye"), false);
        return ActionResult.FAIL;
    }

    private ActionResult processBook(World world, BlockPos pos, PlayerEntity player, MagicTableBlockEntity table, ItemStack book, ItemStack held) {
        MagicBookData data = book.getOrDefault(ModComponents.MAGIC_BOOK, MagicBookData.DEFAULT);
        List<MagicAttrData> attrs = new ArrayList<>(data.attrs());
        if (held.isOf(ModItems.MAGIC_PAPER)) {
            if (attrs.size() >= AttrUtil.MAX_ATTRS) {
                player.sendMessage(Text.translatable("message.colorist.book_full"), false);
                return ActionResult.FAIL;
            }
            MagicPaperData paperData = held.getOrDefault(ModComponents.MAGIC_PAPER, MagicPaperData.DEFAULT);
            MagicAttrData newAttr = AttrUtil.calculateFromPaper(paperData.level(), paperData.attr());
            attrs.add(newAttr);
            MagicAttrData combined = AttrUtil.combine(attrs);
            ItemStack newBook = book.copy();
            newBook.set(ModComponents.MAGIC_BOOK, new MagicBookData(attrs, combined, data.hasHpBonus()));
            table.setDisplayItem(newBook);
            held.decrement(1);
            player.sendMessage(Text.translatable("message.colorist.insert_success", attrs.size(), AttrUtil.MAX_ATTRS), false);
            return ActionResult.SUCCESS;
        } else if (held.isOf(ModItems.MAGIC_CRYSTAL)) {
            if (attrs.isEmpty()) {
                player.sendMessage(Text.translatable("message.colorist.book_empty"), false);
                return ActionResult.FAIL;
            }
            MagicAttrData first = new MagicAttrData(attrs.get(0).r(), attrs.get(0).g(), attrs.get(0).b(), attrs.get(0).brightness(), attrs.get(0).darkness(), attrs.get(0).level() + 5, attrs.get(0).color());
            attrs.set(0, first);
            MagicAttrData combined = AttrUtil.combine(attrs);
            ItemStack newBook = book.copy();
            newBook.set(ModComponents.MAGIC_BOOK, new MagicBookData(attrs, combined, data.hasHpBonus()));
            table.setDisplayItem(newBook);
            held.decrement(1);
            player.sendMessage(Text.translatable("message.colorist.add_success"), false);
            return ActionResult.SUCCESS;
        }
        player.sendMessage(Text.translatable("message.colorist.cannot_insert"), false);
        return ActionResult.FAIL;
    }

    @Override
    public BlockState onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        if (!world.isClient) {
            BlockEntity be = world.getBlockEntity(pos);
            if (be instanceof MagicTableBlockEntity table && table.hasItem()) {
                ItemStack stack = table.getDisplayItem();
                if (!stack.isEmpty() && !player.isCreative()) {
                    dropStack(world, pos, stack);
                }
            }
        }
        return super.onBreak(world, pos, state, player);
    }
}
