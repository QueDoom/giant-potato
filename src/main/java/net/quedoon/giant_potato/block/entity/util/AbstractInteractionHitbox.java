package net.quedoon.giant_potato.block.entity.util;

import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.quedoon.giant_potato.block.entity.util.block_entity.pipe.AbstractPipeBlockEntity;
import net.quedoon.giant_potato.util.ShapeUtil;
import org.joml.Vector3f;

public abstract class AbstractInteractionHitbox {
    public static final Vector3f RED = new Vector3f(0.7f, 0.1f, 0.3f);
    public static final Vector3f YELLOW = new Vector3f(0.922f, 0.91f, 0.376f);

    protected final AABB originalBox;

    protected AABB box;
    protected Vector3f debugColor;

    public AbstractInteractionHitbox(AABB box, Vector3f debugColor) {
        this.originalBox = box;
        this.box = originalBox;
        this.debugColor = debugColor;
    }

    public abstract ResourceLocation getIdentifier();

    /**
     * Default Boxes are only aligned towards {@link Direction#NORTH}
     */
    public AABB getOriginalBox() {
        return originalBox;
    }

    /**
     * Default Boxes are only aligned towards {@link Direction#NORTH}
     */
    public AABB getBox() {
        return box;
    }

    public AABB getRotatedBox(Direction facing) {
        return ShapeUtil.rotateBox(getBox(), facing);
    }

    public Vector3f getDebugColor() {
        return debugColor;
    }

    public InteractionResult interact(AbstractPipeBlockEntity blockEntity, Vec3 actualPos, Player player, InteractionHand hand) {
        return InteractionResult.SUCCESS;
    }

    public InteractionResult attack(AbstractPipeBlockEntity blockEntity, Vec3 actualPos, Player player, ItemStack stack) {
        return InteractionResult.PASS;
    }
}
