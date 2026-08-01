package com.yun.colorist.block.entity;

import com.yun.colorist.Colorist;
import com.yun.colorist.registry.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class MagicTableBlockEntity extends BlockEntity {

    private ItemStack displayItem = ItemStack.EMPTY;

    public MagicTableBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.MAGIC_TABLE, pos, state);
        Colorist.LOGGER.debug("MagicTableBlockEntity created at {}", pos);
    }

    public ItemStack getDisplayItem() {
        return displayItem;
    }

    public void setDisplayItem(ItemStack stack) {
        Colorist.LOGGER.debug("MagicTableBlockEntity.setDisplayItem: {} -> {} at {}", this.displayItem.getItem(), stack.getItem(), getBlockPos());
        this.displayItem = stack.copy();
        setChanged();
        if (getLevel() != null) {
            getLevel().sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
        }
    }

    public boolean hasItem() {
        return !displayItem.isEmpty();
    }
}
