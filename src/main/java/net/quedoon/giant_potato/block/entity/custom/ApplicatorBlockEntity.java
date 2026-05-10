//package net.quedoon.giant_potato.block.entity.custom;
//
//import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
//import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleVariantStorage;
//import net.minecraft.block.BlockState;
//import net.minecraft.block.entity.BlockEntityType;
//import net.minecraft.entity.player.PlayerEntity;
//import net.minecraft.entity.player.PlayerInventory;
//import net.minecraft.item.ItemStack;
//import net.minecraft.recipe.RecipeEntry;
//import net.minecraft.screen.ScreenHandler;
//import net.minecraft.text.Text;
//import net.minecraft.util.math.BlockPos;
//import net.minecraft.world.World;
//import net.quedoon.giant_potato.block.entity.util.block_entity.mashines.AbstractMashMachineBlockEntity;
//import org.jetbrains.annotations.Nullable;
//
//import java.util.Optional;
//
//public class ApplicatorBlockEntity extends AbstractMashMachineBlockEntity<>{
//    public ApplicatorBlockEntity(BlockEntityType type, BlockPos pos, BlockState state, int inventorySize, int maxMash) {
//        super(type, pos, state, inventorySize, maxMash);
//    }
//
//    @Override
//    public int getMaxProgress() {
//        return 100;
//    }
//
//    @Override
//    public String getName() {
//        return "applicator";
//    }
//
//    @Override
//    public void tick(World world, BlockPos pos, BlockState state) {
//
//    }
//
//    @Override
//    protected Optional<RecipeEntry> getCurrentRecipe() {
//        return Optional.empty();
//    }
//
//    @Override
//    protected ItemStack getRecipeOutput() {
//        return null;
//    }
//
//    @Override
//    public Text getDisplayName() {
//        return null;
//    }
//
//    @Override
//    public @Nullable ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
//        return null;
//    }
//}
