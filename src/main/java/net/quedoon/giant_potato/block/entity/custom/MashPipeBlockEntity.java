package net.quedoon.giant_potato.block.entity.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.quedoon.giant_potato.block.ModBlocks;
import net.quedoon.giant_potato.block.entity.ModBlockEntities;
import net.quedoon.giant_potato.block.entity.util.block_entity.pipe.AbstractMashPipeBlockEntity;
import net.quedoon.giant_potato.block.entity.util.block.AbstractPipeBlock;

public class MashPipeBlockEntity extends AbstractMashPipeBlockEntity {
    public MashPipeBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.MASH_PIPE_BE, pos, state, ((AbstractPipeBlock) ModBlocks.MASH_PIPE), ((AbstractPipeBlock) ModBlocks.MASH_PIPE_OUTPUT));
    }

}
