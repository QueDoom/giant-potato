package net.quedoom.giant_potato.block.entity.data.hitbox;

import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.quedoom.giant_potato.GiantPotato;
import net.quedoom.giant_potato.block.entity.util.block_entity.pipe.AbstractPipeBlockEntity;
import net.quedoom.giant_potato.block.entity.util.AbstractInteractionHitbox;
import net.quedoom.giant_potato.util.GetStringFromDirection;
import org.joml.Vector3f;


@SuppressWarnings({"unused"})
public class AbstractPipeHitbox extends AbstractInteractionHitbox {
private final AbstractPipeBlockEntity blockEntity;
    private final Direction side;

    public AbstractPipeHitbox(AbstractPipeBlockEntity blockEntity, AABB box, Vector3f debugColor, Direction side) {
        super(box, debugColor);
        this.blockEntity = blockEntity;
        this.side = side;
    }

    @Override
    public ResourceLocation getIdentifier() {
        return ResourceLocation.fromNamespaceAndPath(GiantPotato.MOD_ID, "abstract_pipe_" + GetStringFromDirection.minecraftDir(side));
    }

    public static ResourceLocation getIdentifier(Direction dir) {
        return ResourceLocation.fromNamespaceAndPath(GiantPotato.MOD_ID, "abstract_pipe_" + GetStringFromDirection.minecraftDir(dir));
    }

    public AbstractPipeBlockEntity getBlockEntity() {
        return blockEntity;
    }

    @Override
    public InteractionResult interact(AbstractPipeBlockEntity blockEntity, Vec3 actualPos, Player player, InteractionHand hand) {
        blockEntity.toggleSideFromHitbox(blockEntity, actualPos, player, hand, side);
        return InteractionResult.PASS;
    }
}
