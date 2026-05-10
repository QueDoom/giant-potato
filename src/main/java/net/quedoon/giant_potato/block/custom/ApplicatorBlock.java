//package net.quedoon.giant_potato.block.custom;
//
//import com.mojang.serialization.MapCodec;
//import net.minecraft.block.BlockEntityProvider;
//import net.minecraft.block.BlockState;
//import net.minecraft.block.BlockWithEntity;
//import net.minecraft.block.entity.BlockEntity;
//import net.minecraft.block.entity.BlockEntityTicker;
//import net.minecraft.block.entity.BlockEntityType;
//import net.minecraft.util.ItemScatterer;
//import net.minecraft.util.math.BlockPos;
//import net.minecraft.util.math.Direction;
//import net.minecraft.util.shape.VoxelShape;
//import net.minecraft.world.World;
//import net.quedoon.giant_potato.block.entity.custom.ApplicatorBlockEntity;
//import net.quedoon.giant_potato.block.entity.custom.CrusherBlockEntity;
//import net.quedoon.giant_potato.block.entity.util.block.machine.AbstractMashMachineBlock;
//import org.jetbrains.annotations.Nullable;
//
//public class ApplicatorBlock extends AbstractMashMachineBlock {
//    public static final MapCodec<CrusherBlock> CODEC = createCodec(CrusherBlock::new);
//    @Override
//    protected VoxelShape getShape() {
//        return null;
//    }
//
//    protected ApplicatorBlock(Settings settings) {
//        super(settings);
//        this.setDefaultState(this.getStateManager().getDefaultState().with(FACING, Direction.NORTH));
//    }
//
//    @Override
//    protected void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
//        if(state.getBlock() != newState.getBlock()) {
//            BlockEntity blockEntity = world.getBlockEntity(pos);
//            if (blockEntity instanceof ApplicatorBlockEntity applicatorBlockEntity) {
//                ItemScatterer.spawn(world, pos, applicatorBlockEntity);
//                world.updateComparators(pos, this);
//            }
//
//            super.onStateReplaced(state, world, pos, newState, moved);
//        }
//    }
//
//    @Override
//    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
//        return null;
//    }
//
//    @Override
//    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
//        return null;
//    }
//
//    @Override
//    protected MapCodec<? extends BlockWithEntity> getCodec() {
//        return CODEC;
//    }
//}
