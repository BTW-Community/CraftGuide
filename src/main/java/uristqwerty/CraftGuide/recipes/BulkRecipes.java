package uristqwerty.CraftGuide.recipes;

import btw.crafting.manager.BulkCraftingManager;
import btw.crafting.recipe.types.BulkRecipe;
import net.minecraft.src.ItemStack;
import uristqwerty.CraftGuide.api.*;

import java.lang.reflect.Field;
import java.util.List;

public class BulkRecipes extends CraftGuideAPIObject implements RecipeProvider {
    private final BulkCraftingManager craftingManager;
    private final int index;
    private final ItemStack[] machines;

    public BulkRecipes(BulkCraftingManager craftingManager, int index, ItemStack... machines) {
        this.craftingManager = craftingManager;
        this.index = index;
        this.machines = machines;
    }

    public BulkRecipes(BulkCraftingManager craftingManager, ItemStack... machines) {
        this(craftingManager, 0, machines);
    }

    @Override
    public void generateRecipes(RecipeGenerator generator) {
        try {
            Field recipesField = BulkCraftingManager.class.getDeclaredField("recipes");
            recipesField.setAccessible(true);
            List<BulkRecipe> recipes = (List<BulkRecipe>) recipesField.get(craftingManager);

            // Determine the maximum input and output lengths of the recipes.
            // Having different sizes looks jarring, especially when comparing
            // recipes of the same machine, so we use the same size for all recipes.
            int inputSize = 0;
            int outputSize = 0;
            for (BulkRecipe recipe : recipes) {
                List<ItemStack> inputs = recipe.getCraftingIngrediantList();
                List<ItemStack> outputs = recipe.getCraftingOutputList();
                // Condensing is required for the Companion Cube MillStone recipe,
                // which outputs multiple single items of the same type for some reason.
                BTWRecipes.condenseItemStackList(outputs);

                inputSize = Math.max(inputSize, inputs.size());
                outputSize = Math.max(outputSize, outputs.size());
            }

            int inputW = (int) Math.ceil(inputSize / 3.0);
            int inputH = Math.min(inputSize, 3);
            int inputArea = inputW * inputH;

            int outputW = (int) Math.ceil(outputSize / 3.0);
            int outputH = Math.min(outputSize, 3);

            int maxHeight = Math.max(Math.max(inputH, outputH), machines.length);

            Slot[] slots = BTWRecipes.createSlots(inputSize, machines.length, outputSize);
            RecipeTemplate template = generator.createRecipeTemplate(slots, machines[index]).setSize((inputW + 1 + outputW) * 18 + 6, maxHeight * 18 + 4);

            for (BulkRecipe recipe : recipes) {
                List<ItemStack> inputs = recipe.getCraftingIngrediantList();
                List<ItemStack> outputs = recipe.getCraftingOutputList();
                BTWRecipes.condenseItemStackList(outputs);

                ItemStack[] crafting = new ItemStack[slots.length];

                for (int i = 0; i < inputs.size(); i++) {
                    crafting[i] = inputs.get(i);
                }
                for (int i = 0; i < machines.length; i++) {
                    crafting[inputArea + i] = machines[i];
                }
                for (int i = 0; i < outputs.size(); i++) {
                    crafting[inputArea + machines.length + i] = outputs.get(i);
                }

                generator.addRecipe(template, crafting);
            }
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
