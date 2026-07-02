package net.quedoon.giant_potato.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import java.util.List;

public record FoundryRecipe(ItemStack output, NonNullList<Ingredient> ingredients, int maxProgress) implements Recipe<FoundryRecipeInput> {
    @Override
    public NonNullList<Ingredient> getIngredients() {
        return ingredients;
    }

    @Override
    public boolean matches(FoundryRecipeInput input, Level world) {
        if (world.isClientSide) {
            return false;
        }
        for (int i = 0; i < ingredients.size(); i++) {
            if (!ingredients.get(i).test(input.getItem(i))) {
                return false;
            }
        }
        return true;
    }

    @Override
    public ItemStack assemble(FoundryRecipeInput recipeInput, HolderLookup.Provider provider) {
        return output.copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return false;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider registriesLookup) {
        return output;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.FOUNDRY_SERIALIZER;
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipes.FOUNDRY_TYPE;
    }

    public static class Serializer implements RecipeSerializer<FoundryRecipe> {
        private static final MapCodec<FoundryRecipe> CODEC = RecordCodecBuilder.mapCodec(
                instance -> instance.group(
                                ItemStack.STRICT_CODEC.fieldOf("result").forGetter(recipe -> recipe.output),
                                Ingredient.CODEC_NONEMPTY
                                        .listOf()
                                        .fieldOf("ingredients")
                                        .flatXmap(
                                                ingredients -> {
                                                    Ingredient[] ingredients2 = (Ingredient[])ingredients.stream().filter(ingredient -> !ingredient.isEmpty()).toArray(Ingredient[]::new);
                                                    if (ingredients2.length == 0) {
                                                        return DataResult.error(() -> "No ingredients for foundry recipe");
                                                    } else {
                                                        return ingredients2.length > 3
                                                                ? DataResult.error(() -> "Too many ingredients for foundry recipe")
                                                                : DataResult.success(NonNullList.of(Ingredient.EMPTY, ingredients2));
                                                    }
                                                },
                                                DataResult::success
                                        )
                                        .forGetter(recipe -> recipe.ingredients),
                                Codec.INT.optionalFieldOf("time", 72).forGetter(recipe -> recipe.maxProgress)
                        )
                        .apply(instance, FoundryRecipe::new)
        );
        public static final StreamCodec<RegistryFriendlyByteBuf, FoundryRecipe> PACKET_CODEC = StreamCodec.of(
                FoundryRecipe.Serializer::write, FoundryRecipe.Serializer::read
        );

        @Override
        public MapCodec<FoundryRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, FoundryRecipe> streamCodec() {
            return PACKET_CODEC;
        }

        private static FoundryRecipe read(RegistryFriendlyByteBuf buf) {
            int i = buf.readVarInt();
            NonNullList<Ingredient> defaultedList = NonNullList.withSize(i, Ingredient.EMPTY);
            defaultedList.replaceAll(empty -> Ingredient.CONTENTS_STREAM_CODEC.decode(buf));
            ItemStack itemStack = ItemStack.STREAM_CODEC.decode(buf);
            int l = buf.readVarInt();
            return new FoundryRecipe(itemStack, defaultedList, l);
        }

        private static void write(RegistryFriendlyByteBuf buf, FoundryRecipe recipe) {
            buf.writeVarInt(recipe.ingredients.size());

            for (Ingredient ingredient : recipe.ingredients) {
                Ingredient.CONTENTS_STREAM_CODEC.encode(buf, ingredient);
            }

            ItemStack.STREAM_CODEC.encode(buf, recipe.output);

            buf.writeVarInt(recipe.maxProgress);
        }
    }
}
