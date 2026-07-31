package com.yun.colorist.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.yun.colorist.block.entity.MagicTableBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class MagicTableRenderer implements BlockEntityRenderer<MagicTableBlockEntity> {

    public MagicTableRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(MagicTableBlockEntity table, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        ItemStack display = table.getDisplayItem();
        if (display.isEmpty()) return;

        poseStack.pushPose();
        float time = (table.getLevel().getGameTime() + partialTick) * 0.05f;
        float y = Mth.sin(time) * 0.05f + 0.25f;
        poseStack.translate(0.5, y + 1.0, 0.5);
        poseStack.scale(0.6f, 0.6f, 0.6f);
        poseStack.mulPose(com.mojang.math.Axis.YP.rotation(time));

        Minecraft.getInstance().getItemRenderer().renderStatic(
                display,
                ItemDisplayContext.GROUND,
                packedLight,
                OverlayTexture.NO_OVERLAY,
                poseStack,
                buffer,
                table.getLevel(),
                0
        );

        poseStack.popPose();
    }
}
