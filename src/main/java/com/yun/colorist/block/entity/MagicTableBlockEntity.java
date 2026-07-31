package com.yun.colorist.block.entity;

import com.yun.colorist.registry.ModBlockEntities;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.math.BlockPos;

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
        markDirty();
        if (world != null) {
            world.updateListeners(pos, getCachedState(), getCachedState(), 0);
        }
    }

    public boolean hasItem() {
        return !displayItem.isEmpty();
    }

    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
        super.readNbt(nbt, registries);
        if (nbt.contains("displayItem", NbtCompound.COMPOUND_TYPE)) {
            displayItem = ItemStack.fromNbt(registries, nbt.getCompound("displayItem")).orElse(ItemStack.EMPTY);
        } else {
            displayItem = ItemStack.EMPTY;
        }
    }

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
        super.writeNbt(nbt, registries);
        if (!displayItem.isEmpty()) {
            nbt.put("displayItem", displayItem.toNbt(registries));
        }
    }
}
