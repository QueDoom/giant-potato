package net.quedoon.giant_potato.screen.custom;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.screen.v1.Screens;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleVariantStorage;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Item;
import net.quedoon.giant_potato.GiantPotato;
import net.quedoon.giant_potato.screen.render.FluidStackRenderer;
import net.quedoon.giant_potato.util.MouseUtil;

import java.util.Optional;

public class MashTankScreen extends AbstractContainerScreen<MashTankScreenHandler> {
    private static final ResourceLocation GUI_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(GiantPotato.MOD_ID,"textures/gui/mash_tank/mash_tank.png");
    private static final ResourceLocation MASH_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(GiantPotato.MOD_ID, "textures/gui/common/mash_bar.png");
    private FluidStackRenderer fluidStackRenderer;

    public MashTankScreen(MashTankScreenHandler handler, Inventory inventory, Component title) {
        super(handler, inventory, title);
    }

    @Override
    protected void init() {
        super.init();

        assignFluidStackRenderer();
    }

    private void assignFluidStackRenderer() {
        fluidStackRenderer = new FluidStackRenderer((FluidConstants.BUCKET / 81) * 64, true, 16, 64);
    }

    private void renderFluidTooltip(GuiGraphics context, int mouseX, int mouseY, int x, int y, int offsetX, int offsetY, FluidStackRenderer renderer) {
        if(isMouseAboveArea(mouseX, mouseY, x, y, offsetX, offsetY, renderer)) {
            context.renderTooltip(Screens.getTextRenderer(this), renderer.getTooltip(menu.blockEntity.fluidStorage, Item.TooltipContext.EMPTY),
                    Optional.empty(), mouseX - x, mouseY - y);
        }
    }

    @Override
    protected void renderLabels(GuiGraphics context, int mouseX, int mouseY) {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        renderFluidTooltip(context, mouseX, mouseY, x, y, 80, 8, fluidStackRenderer);
    }

    @Override
    protected void renderBg(GuiGraphics context, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, GUI_TEXTURE);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        context.blit(GUI_TEXTURE, x, y, 0, 0, imageWidth, imageHeight);


        fluidStackRenderer.drawFluid(context, menu.blockEntity.fluidStorageHalf, x + 80, y + 40, 16, 32,
                (FluidConstants.BUCKET / 81) * 16);
        fluidStackRenderer.drawFluid(context, menu.blockEntity.fluidStorageHalfMinus, x + 80, y + 8, 16, 32,
                (FluidConstants.BUCKET / 81) * 16);
        //renderValueMash(context, x, y);
    }

    private void renderValueMash(GuiGraphics context, int x, int y) {
        context.blit(MASH_TEXTURE, x + 80, y + 8 + 58 - menu.getScaledMashDisplay(), 0,
                58 - menu.getScaledMashDisplay(), 16, menu.getScaledMashDisplay(), 16, 58);
    }

    @Override
    public void render(GuiGraphics context, int mouseX, int mouseY, float delta) {
        renderBackground(context, mouseX, mouseY, delta);
        super.render(context, mouseX, mouseY, delta);
        renderTooltip(context, mouseX, mouseY);
    }


    private boolean isMouseAboveArea(int pMouseX, int pMouseY, int x, int y, int offsetX, int offsetY, FluidStackRenderer renderer) {
        return MouseUtil.isMouseOver(pMouseX, pMouseY, x + offsetX, y + offsetY, renderer.getWidth(), renderer.getHeight());
    }
}
