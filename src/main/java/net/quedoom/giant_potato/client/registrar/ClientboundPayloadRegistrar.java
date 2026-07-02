package net.quedoom.giant_potato.client.registrar;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.quedoom.giant_potato.block.entity.custom.CrusherWheelBlockEntity;
import net.quedoom.giant_potato.client.packet.ClientboundSetCrusherWheelStatePayload;

public class ClientboundPayloadRegistrar {
    public static void register() {
        registerSetCrusherWheelPayload();



    }

    private static void registerSetCrusherWheelPayload() {
        ClientPlayNetworking.registerGlobalReceiver(ClientboundSetCrusherWheelStatePayload.ID, (payload, context) -> {
            ClientLevel world = context.client().level;

            if (world == null) {
                return;
            }

            BlockPos pos = payload.pos();
            BlockEntity entity = world.getBlockEntity(pos);
            if (entity instanceof CrusherWheelBlockEntity blockEntity) {
                blockEntity.setActive(true);
            }
        });
    }
}
