package net.quedoon.giant_potato.block.util;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class PosDirection {
    private final BlockPos POS;
    private final Direction DIRECTION;


    public PosDirection(BlockPos posOrigin, Direction direction) {
        this.POS = posOrigin;
        this.DIRECTION = direction;
    }

    public BlockPos getOriginPosition() {
        return POS;
    }

    public BlockState getOrigin(World world) {
        return world.getBlockState(POS);
    }

    public Direction getDirection() {
        return DIRECTION;
    }

    public BlockState getState(World world) {
        return world.getBlockState(POS.offset(DIRECTION));
    }

}
