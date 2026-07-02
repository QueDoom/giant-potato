package net.quedoom.giant_potato;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.quedoom.giant_potato.block.ModBlocks;
import net.quedoom.giant_potato.block.entity.ModBlockEntities;
import net.quedoom.giant_potato.block.entity.renderer.AbstractPipeBlockEntityRenderer;
import net.quedoom.giant_potato.block.entity.renderer.MashTankBlockEntityRenderer;
import net.quedoom.giant_potato.fluid.ModFluids;
import net.quedoom.giant_potato.fluid.render.MashFluidRenderHandler;
import net.quedoom.giant_potato.fluid.render.PoisonousMashFluidRenderHandler;
import net.quedoom.giant_potato.fluid.render.SewerWaterFluidRenderHandler;
import net.quedoom.giant_potato.screen.ModScreenHandlers;
import net.quedoom.giant_potato.screen.custom.CrusherScreen;
import net.quedoom.giant_potato.screen.custom.FoundryScreen;
import net.quedoom.giant_potato.screen.custom.MashTankScreen;
import net.quedoom.giant_potato.zapi.gecko.render.CrusherWheelBlockRenderer;
import net.quedoom.giant_potato.zapi.gecko.render.FoundryBlockRenderer;

public class GiantPotatoClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BlockEntityRenderers.register(ModBlockEntities.FOUNDRY_BE, FoundryBlockRenderer::new);
        BlockEntityRenderers.register(ModBlockEntities.CRUSHER_WHEEL_BE, CrusherWheelBlockRenderer::new);

        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.MASH_TANK, RenderType.cutout());
        BlockEntityRenderers.register(ModBlockEntities.MASH_TANK_BE, MashTankBlockEntityRenderer::new);
        BlockEntityRenderers.register(ModBlockEntities.MASH_PIPE_BE, AbstractPipeBlockEntityRenderer::new);
        BlockEntityRenderers.register(ModBlockEntities.MASH_PIPE_OUTPUT_BE, AbstractPipeBlockEntityRenderer::new);

        MenuScreens.register(ModScreenHandlers.FOUNDRY_SCREEN_HANDLER, FoundryScreen::new);
        MenuScreens.register(ModScreenHandlers.CRUSHER_SCREEN_HANDLER, CrusherScreen::new);
        MenuScreens.register(ModScreenHandlers.MASH_TANK_SCREEN_HANDLER, MashTankScreen::new);

//        FluidRenderHandlerRegistry.INSTANCE.register(ModFluids.MASH, ModFluids.MASH_FLOWING_UNUSED,
//                SimpleFluidRenderHandler.coloredWater(0xf3f300ff));
//        BlockRenderLayerMap.INSTANCE.putFluids(RenderLayer.getTranslucent(),
//                ModFluids.MASH,ModFluids.MASH_FLOWING_UNUSED);

        FluidRenderHandlerRegistry.INSTANCE.register(ModFluids.MASH, ModFluids.MASH_FLOWING_UNUSED, new MashFluidRenderHandler());
        FluidRenderHandlerRegistry.INSTANCE.register(ModFluids.POISONOUS_MASH, ModFluids.POISONOUS_MASH_FLOWING_UNUSED, new PoisonousMashFluidRenderHandler());
        FluidRenderHandlerRegistry.INSTANCE.register(ModFluids.SEWER_WATER, ModFluids.FLOWING_SEWER_WATER, new SewerWaterFluidRenderHandler());

        //ClientboundPayloadRegistrar.register();
    }
}

