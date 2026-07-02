package net.quedoom.giant_potato.client.packet;

import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.quedoom.giant_potato.GiantPotato;

public record ClientboundSetCrusherWheelStatePayload(BlockPos pos) implements CustomPacketPayload {
    public static final ResourceLocation SET_CRUSHER_WHEEL_STATE_PAYLOAD_ID = ResourceLocation.fromNamespaceAndPath(GiantPotato.MOD_ID, "set_crusher_wheel_state");
    public static final CustomPacketPayload.Type<ClientboundSetCrusherWheelStatePayload> ID = new CustomPacketPayload.Type<>(SET_CRUSHER_WHEEL_STATE_PAYLOAD_ID);
    public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundSetCrusherWheelStatePayload> CODEC = StreamCodec.composite(BlockPos.STREAM_CODEC, ClientboundSetCrusherWheelStatePayload::pos, ClientboundSetCrusherWheelStatePayload::new);


    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}
