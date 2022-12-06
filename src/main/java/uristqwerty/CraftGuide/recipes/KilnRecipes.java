package uristqwerty.CraftGuide.recipes;

import btw.block.BTWBlocks;
import btw.crafting.manager.KilnCraftingManager;
import btw.crafting.recipe.types.KilnRecipe;
import btw.item.items.PlaceAsBlockItem;
import net.minecraft.src.Block;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import uristqwerty.CraftGuide.api.*;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class KilnRecipes extends CraftGuideAPIObject implements RecipeProvider {
    private final ItemStack bellows = new ItemStack(BTWBlocks.bellows);
    private final ItemStack kiln = new ItemStack(BTWBlocks.kiln);
    private final ItemStack hibachi = new ItemStack(BTWBlocks.hibachi);
    private final ItemStack[] machines = new ItemStack[]{bellows, kiln, hibachi};

    @Override
    public void generateRecipes(RecipeGenerator generator) {
        try {
            Field recipesField = KilnCraftingManager.class.getDeclaredField("recipes");
            recipesField.setAccessible(true);
            ArrayList<KilnRecipe> recipes = (ArrayList<KilnRecipe>) recipesField.get(KilnCraftingManager.instance);

            int outputSize = 0;
            for (KilnRecipe recipe : recipes) {
                ItemStack[] outputs = recipe.getOutput();
                outputSize = Math.max(outputSize, outputs.length);
            }
            Slot[] slots = BTWRecipes.createSlots(1, 3, outputSize);
            int outputW = (int) Math.ceil(outputSize / 3.0);
            RecipeTemplate template = generator.createRecipeTemplate(slots, kiln).setSize((2 + outputW) * 18 + 6, 3 * 18 + 4);

            for (KilnRecipe recipe : recipes) {
                Block inputBlock = recipe.getInputblock();
                int metadata = recipe.getInputMetadata()[0];

                int inputID;
                try {
                    inputID = inputBlock.idPicked(null, 0, 0, 0);
                } catch (NullPointerException e) {
                    inputID = inputBlock.idDropped(metadata, null, 1);
                }

                // Ignore things that cannot be placed.
                if (!(Item.itemsList[inputID] instanceof PlaceAsBlockItem || Block.blocksList[inputID] != null)) {
                    continue;
                }

                metadata = inputBlock.damageDropped(metadata);

                ItemStack input = new ItemStack(inputID, 1, metadata);
                ItemStack[] crafting = new ItemStack[slots.length];

                crafting[0] = input;
                for (int i = 0; i < 3; i++) {
                    crafting[1 + i] = machines[i];
                }
                ItemStack[] output = recipe.getOutput();
                for (int i = 0; i < 3 && i < output.length; i++) {
                    crafting[4 + i] = output[i];
                }

                generator.addRecipe(template, crafting);
            }
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }
	}
}
