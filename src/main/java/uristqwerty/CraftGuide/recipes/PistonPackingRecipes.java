package uristqwerty.CraftGuide.recipes;

import java.lang.reflect.Field;
import java.util.ArrayList;

import btw.crafting.manager.PistonPackingCraftingManager;
import btw.crafting.recipe.types.PistonPackingRecipe;
import net.minecraft.src.Block;
import net.minecraft.src.ItemStack;
import uristqwerty.CraftGuide.api.CraftGuideAPIObject;
import uristqwerty.CraftGuide.api.RecipeGenerator;
import uristqwerty.CraftGuide.api.RecipeProvider;
import uristqwerty.CraftGuide.api.RecipeTemplate;
import uristqwerty.CraftGuide.api.Slot;

public class PistonPackingRecipes extends CraftGuideAPIObject implements RecipeProvider {
	private ItemStack piston = new ItemStack(Block.pistonBase);

	@Override
	public void generateRecipes(RecipeGenerator generator) {
		
		try {
			Field recipesField = PistonPackingRecipe.class.getDeclaredField("recipes");
			recipesField.setAccessible(true);
			ArrayList<PistonPackingRecipe> recipes = (ArrayList<PistonPackingRecipe>) recipesField.get(PistonPackingCraftingManager.instance);
			
			int inputSize = 0;
			for (PistonPackingRecipe recipe : recipes) {
				ItemStack[] inputs = recipe.getInput();
				inputSize = Math.max(inputSize, inputs.length);
			}
			Slot[] slots = BTWRecipes.createSlots(inputSize, 1, 1);
			
			int inputW = (int) Math.ceil(inputSize / 3.0);
			int inputH = Math.min(inputSize, 3);
			int maxHeight = Math.max(inputH, 1);
			
			RecipeTemplate template = generator.createRecipeTemplate(slots, piston).setSize((inputW + 2) * 18 + 6, maxHeight * 18 + 4);
			
			for (PistonPackingRecipe recipe : recipes) {
				ItemStack[] crafting = new ItemStack[slots.length];

				ItemStack[] inputs = recipe.getInput();

				for (int i = 0; i < inputs.length; i++) {
					crafting[i] = inputs[i];
				}
				crafting[inputSize] = piston;
				crafting[inputSize + 1] = new ItemStack(recipe.getOutput(), 1, recipe.getOutputMetadata());

				generator.addRecipe(template, crafting);
			}
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}
}
