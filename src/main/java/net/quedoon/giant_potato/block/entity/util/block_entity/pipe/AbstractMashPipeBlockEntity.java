package net.quedoon.giant_potato.block.entity.util.block_entity.pipe;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.quedoon.giant_potato.block.entity.util.block.AbstractPipeBlock;

public abstract class AbstractMashPipeBlockEntity extends AbstractFluidPipeBlockEntity {
    public AbstractMashPipeBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state, AbstractPipeBlock notOutput, AbstractPipeBlock output) {
        super(type, pos, state, notOutput, output);
    }


}
