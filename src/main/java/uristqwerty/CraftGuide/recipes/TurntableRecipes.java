package uristqwerty.CraftGuide.recipes;

import btw.block.BTWBlocks;
import btw.crafting.manager.TurntableCraftingManager;
import btw.crafting.recipe.types.TurntableRecipe;
import net.minecraft.src.Block;
import net.minecraft.src.ItemStack;
import uristqwerty.CraftGuide.api.*;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class TurntableRecipes extends CraftGuideAPIObject implements RecipeProvider {
    private final ItemStack turntable = new ItemStack(BTWBlocks.turntable);

    @Override
    public void generateRecipes(RecipeGenerator generator) {
        try {
            Field recipesField = TurntableCraftingManager.class.getDeclaredField("recipes");
            recipesField.setAccessible(true);
            ArrayList<TurntableRecipe> recipes = (ArrayList<TurntableRecipe>) recipesField.get(TurntableCraftingManager.instance);

            int outputSize = 1;
            for (TurntableRecipe recipe : recipes) {
                ItemStack[] outputs = recipe.getItemsEjected();
                if (outputs == null) {
                    continue;
                }
                outputSize = Math.max(outputSize, outputs.length + 1);
            }
            Slot[] slots = BTWRecipes.createSlots(1, 1, outputSize);

            int outputW = (int) Math.ceil(outputSize / 3.0);
            int outputH = Math.min(outputSize, 3);
            int maxHeight = Math.max(outputH, 1);

            RecipeTemplate template = generator.createRecipeTemplate(slots, turntable).setSize((2 + outputW) * 18 + 6, maxHeight * 18 + 4);

            for (TurntableRecipe recipe : recipes) {
                Block inputBlock = recipe.getInputblock();
                int metadata = recipe.getInputMetadata()[0];

                int inputID;
                try {
                    inputID = inputBlock.idPicked(null, 0, 0, 0);
                } catch (NullPointerException e) {
                    inputID = inputBlock.idDropped(metadata, null, 1);
                }

                metadata = inputBlock.damageDropped(metadata);

                Block outputBlock = recipe.getOutputBlock();
                ItemStack output;
                if (outputBlock == null) {
                    output = null;
                } else {
                    output = new ItemStack(recipe.getOutputBlock(), 1, recipe.getOutputMetadata());
                }

                ItemStack[] crafting = new ItemStack[slots.length];

                crafting[0] = new ItemStack(inputID, 1, metadata);
                crafting[1] = turntable;
                crafting[2] = output;

                ItemStack[] ejected = recipe.getItemsEjected();
                for (int i = 0; ejected != null && i < ejected.length; i++) {
                    crafting[3 + i] = ejected[i];
                }

                generator.addRecipe(template, crafting);
            }
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }
	}
}
