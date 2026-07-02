package net.quedoom.giant_potato.block.custom;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.quedoom.giant_potato.block.entity.custom.MashPipeBlockEntity;
import net.quedoom.giant_potato.block.entity.util.block.AbstractPipeBlock;
import org.jetbrains.annotations.Nullable;

public class MashPipeBlock extends AbstractPipeBlock {
    public static final MapCodec<MashPipeBlock> CODEC = simpleCodec(MashPipeBlock::new);

    public MashPipeBlock(Properties settings) {
        super(settings);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new MashPipeBlockEntity(pos, state);
    }


}
