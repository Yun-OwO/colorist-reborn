package com.yun.colorist.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.yun.colorist.Colorist;
import com.yun.colorist.block.entity.MagicTableBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class MagicTableRenderer implements BlockEntityRenderer<MagicTableBlockEntity, MagicTableRenderState> {

    public MagicTableRenderer(BlockEntityRendererProvider.Context context) {
        Colorist.LOGGER.debug("MagicTableRenderer created");
    }

    @Override
    public MagicTableRenderState createRenderState() {
        return new MagicTableRenderState();
    }

    @Override
    public void extractRenderState(MagicTableBlockEntity blockEntity, MagicTableRenderState renderState, float partialTick, Vec3 offset, ModelFeatureRenderer.CrumblingOverlay crumblingOverlay) {
        renderState.displayItem = blockEntity.getDisplayItem();
        Colorist.LOGGER.debug("MagicTableRenderer.extractRenderState: item={} at {} offset={}", renderState.displayItem.getItem(), blockEntity.getBlockPos(), offset);
    }

    @Override
    public void submit(MagicTableRenderState renderState, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState cameraRenderState) {
        ItemStack display = renderState.displayItem;
        if (display.isEmpty()) {
            Colorist.LOGGER.debug("MagicTableRenderer.submit: empty display item, skipping");
            return;
        }

        Colorist.LOGGER.debug("MagicTableRenderer.submit: rendering {} (lightCoords={})", display.getItem(), renderState.lightCoords);

        // Resolve item model and render using the new pipeline
        ItemStackRenderState itemState = new ItemStackRenderState();
        var resolver = Minecraft.getInstance().getItemModelResolver();
        if (resolver == null) {
            Colorist.LOGGER.warn("ItemModelResolver is null, cannot render");
            return;
        }
        if (Minecraft.getInstance().player == null) {
            Colorist.LOGGER.warn("Player is null, cannot render");
            return;
        }
        resolver.updateForNonLiving(
                itemState, display, ItemDisplayContext.GROUND, Minecraft.getInstance().player
        );

        poseStack.pushPose();
        float time;
        var level = Minecraft.getInstance().level;
        if (level != null) {
            time = (level.getGameTime() + Minecraft.getInstance().getDeltaTracker().getGameTimeDeltaTicks()) * 0.05f;
        } else {
            time = 0f;
        }
        float y = Mth.sin(time) * 0.05f + 0.25f;
        poseStack.translate(0.5, y + 1.0, 0.5);
        poseStack.scale(0.6f, 0.6f, 0.6f);
        poseStack.mulPose(com.mojang.math.Axis.YP.rotation(time));

        // Use the packed light from the render state
        int packedLight = renderState.lightCoords;
        Colorist.LOGGER.trace("Submitting item model at pos (0.5, {}, 0.5) with light={}", y + 1.0, packedLight);
        itemState.submit(poseStack, collector, packedLight, 0, 0);

        poseStack.popPose();
    }
}