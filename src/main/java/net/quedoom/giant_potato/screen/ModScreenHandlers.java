package net.quedoom.giant_potato.screen;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.MenuType;
import net.quedoom.giant_potato.GiantPotato;
import net.quedoom.giant_potato.screen.custom.FoundryScreenHandler;
import net.quedoom.giant_potato.screen.custom.MashTankScreenHandler;

public class ModScreenHandlers {

    public static final MenuType<FoundryScreenHandler> FOUNDRY_SCREEN_HANDLER =
            Registry.register(BuiltInRegistries.MENU, ResourceLocation.fromNamespaceAndPath(GiantPotato.MOD_ID, "foundry_screen_handler"),
                    new ExtendedScreenHandlerType<>(FoundryScreenHandler::new, BlockPos.STREAM_CODEC));

    public static final MenuType<MashTankScreenHandler> MASH_TANK_SCREEN_HANDLER =
            Registry.register(BuiltInRegistries.MENU, ResourceLocation.fromNamespaceAndPath(GiantPotato.MOD_ID, "mash_tank_screen_handler"),
                    new ExtendedScreenHandlerType<>(MashTankScreenHandler::new, BlockPos.STREAM_CODEC));


    public static void registerScreenHandlers() {
        GiantPotato.LOGGER.info("Registering Mod Screen Handlers for " + GiantPotato.MOD_ID);
    }
}
