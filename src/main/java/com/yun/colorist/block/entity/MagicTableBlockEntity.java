package com.yun.colorist.block.entity;

import com.yun.colorist.registry.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class MagicTableBlockEntity extends BlockEntity {

    private ItemStack displayItem = ItemStack.EMPTY;

    public MagicTableBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.MAGIC_TABLE, pos, state);
    }

    public ItemStack getDisplayItem() {
        return displayItem;
    }

    public void setDisplayItem(ItemStack stack) {
        this.displayItem = stack.copy();
        setChanged();
        if (level != null) {
            level.sendBlockUpdated(pos, getBlockState(), getBlockState(), 3);
        }
    }

    public boolean hasItem() {
        return !displayItem.isEmpty();
    }
}
