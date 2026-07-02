package net.quedoom.giant_potato.screen.custom;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.quedoom.giant_potato.GiantPotato;

public class FoundryScreen extends AbstractContainerScreen<FoundryScreenHandler> {
    private static final ResourceLocation GUI_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(GiantPotato.MOD_ID, "textures/gui/foundry/foundry.png");
    private static final ResourceLocation MASH_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(GiantPotato.MOD_ID, "textures/gui/foundry/mash_bar.png");
    private static final ResourceLocation PROGRESS_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(GiantPotato.MOD_ID, "textures/gui/foundry/progress.png");

    public FoundryScreen(FoundryScreenHandler handler, Inventory inventory, Component title) {
        super(handler, inventory, title);
    }

    @Override
    protected void init() {
        super.init();

        // Get rid of the Title and Inventory Title
        titleLabelY = 1000;
        inventoryLabelY = 1000;
    }

    @Override
    protected void renderBg(GuiGraphics context, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, GUI_TEXTURE);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        context.blit(GUI_TEXTURE, x, y, 0, 0, imageWidth, imageHeight);
        renderProgressArrow(context, x, y);
        renderValueMash(context, x, y);

    }

    private void renderValueMash(GuiGraphics context, int x, int y) {
        context.blit(MASH_TEXTURE, x + 12, y + 12 + 58 - menu.getScaledMashDisplay(), 0,
                58 - menu.getScaledMashDisplay(), 16, menu.getScaledMashDisplay(), 16, 58);
    }

    private void renderProgressArrow(GuiGraphics context, int x, int y) {
        if(menu.isCrafting()){
            context.blit(PROGRESS_TEXTURE, x + 102, y + 34, 0, 0,
                    menu.getScaledArrowProgress(), 16, 24, 16);
        }
    }

    @Override
    public void render(GuiGraphics context, int mouseX, int mouseY, float delta) {
        renderBackground(context, mouseX, mouseY, delta);
        super.render(context, mouseX, mouseY, delta);
        renderTooltip(context, mouseX, mouseY);
    }
}
