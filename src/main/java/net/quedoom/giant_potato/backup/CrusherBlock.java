//package net.quedoom.giant_potato.backup;
//
//import com.mojang.serialization.MapCodec;
//import net.minecraft.core.BlockPos;
//import net.minecraft.core.Direction;
//import net.minecraft.world.Containers;
//import net.minecraft.world.level.Level;
//import net.minecraft.world.level.block.BaseEntityBlock;
//import net.minecraft.world.level.block.entity.BlockEntity;
//import net.minecraft.world.level.block.entity.BlockEntityTicker;
//import net.minecraft.world.level.block.entity.BlockEntityType;
//import net.minecraft.world.level.block.state.BlockState;
//import net.minecraft.world.phys.shapes.VoxelShape;
//import net.quedoom.giant_potato.block.entity.ModBlockEntities;
//import net.quedoom.giant_potato.block.entity.util.block.machine.AbstractMashMachineBlock;
//import net.quedoom.giant_potato.block.util.ModBlockHitboxes;
//import org.jetbrains.annotations.Nullable;
//
//public class CrusherBlock extends AbstractMashMachineBlock {
//    public static final MapCodec<CrusherBlock> CODEC = simpleCodec(CrusherBlock::new);
//
//    @Override
//    protected VoxelShape getShape() {
//        return ModBlockHitboxes.getCrusherHitbox();
//    }
//
//    public CrusherBlock(Properties settings) {
//        super(settings);
//        this.registerDefaultState(this.getStateDefinition().any().setValue(FACING, Direction.NORTH));
//    }
//
//
//    @Override
//    protected MapCodec<? extends BaseEntityBlock> codec() {
//        return CODEC;
//    }
//
//    @Override
//    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
//        return new CrusherBlockEntity(pos, state);
//    }
//
//    @Override
//    protected void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean moved) {
//        if(state.getBlock() != newState.getBlock()) {
//            BlockEntity blockEntity = world.getBlockEntity(pos);
//            if (blockEntity instanceof CrusherBlockEntity CrusherBlockEntity) {
//                Containers.dropContents(world, pos, CrusherBlockEntity);
//                world.updateNeighbourForOutputSignal(pos, this);
//            }
//
//            super.onRemove(state, world, pos, newState, moved);
//        }
//    }
//
//
////    @Override
////    protected ItemActionResult onUseWithItem(ItemStack stack, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
////        if (!world.isClient()) {
////            NamedScreenHandlerFactory screenHandlerFactory = ((CrusherBlockEntity) world.getBlockEntity(pos));
////            if (screenHandlerFactory != null) {
////                player.openHandledScreen(screenHandlerFactory);
////                return ItemActionResult.SUCCESS;
////            }
////        }
////        return ItemActionResult.FAIL;
////    }
//
//    @Override
//    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
//        return createTickerHelper(type, ModBlockEntities.CRUSHER_BE, (world1, pos, state1, blockEntity) -> blockEntity.tick(world1, pos, state1));
//    }
//}
