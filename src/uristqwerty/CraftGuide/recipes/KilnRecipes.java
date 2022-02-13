package uristqwerty.CraftGuide.recipes;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import net.minecraft.src.Block;
import net.minecraft.src.FCBetterThanWolves;
import net.minecraft.src.FCCraftingManagerKiln;
import net.minecraft.src.FCCraftingManagerKilnRecipe;
import net.minecraft.src.FCItemPlacesAsBlock;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import uristqwerty.CraftGuide.api.CraftGuideAPIObject;
import uristqwerty.CraftGuide.api.RecipeGenerator;
import uristqwerty.CraftGuide.api.RecipeProvider;
import uristqwerty.CraftGuide.api.RecipeTemplate;
import uristqwerty.CraftGuide.api.Slot;

public class KilnRecipes extends CraftGuideAPIObject implements RecipeProvider {
	private ItemStack bellows = new ItemStack(FCBetterThanWolves.fcBellows);
	private ItemStack kiln = new ItemStack(FCBetterThanWolves.fcKiln);
	private ItemStack hibachi = new ItemStack(FCBetterThanWolves.fcBBQ);
	private ItemStack[] machines = new ItemStack[] {bellows, kiln, hibachi};

	@Override
	public void generateRecipes(RecipeGenerator generator) {
		try {
			Field recipesField = FCCraftingManagerKiln.class.getDeclaredField("recipes");
			recipesField.setAccessible(true);
			ArrayList<FCCraftingManagerKilnRecipe> recipes = (ArrayList<FCCraftingManagerKilnRecipe>) recipesField.get(FCCraftingManagerKiln.instance);
			
			int outputSize = 0;
			for (FCCraftingManagerKilnRecipe recipe : recipes) {
				ItemStack[] outputs = recipe.getOutput();
				outputSize = Math.max(outputSize, outputs.length);
			}
			Slot[] slots = BTWRecipes.createSlots(1, 3, outputSize);
			int outputW = (int) Math.ceil(outputSize / 3.0);
			RecipeTemplate template = generator.createRecipeTemplate(slots, kiln).setSize((2 + outputW) * 18 + 6, 3 * 18 + 4);
			
			for (FCCraftingManagerKilnRecipe recipe : recipes) {
				Block inputBlock = recipe.getInputblock();
				int metadata = recipe.getInputMetadata()[0];

				int inputID;
				try {
					inputID = inputBlock.idPicked(null, 0, 0, 0);
				} catch (NullPointerException e) {
					inputID = inputBlock.idDropped(metadata, null, 1);
				}
				
				// Ignore things that cannot be placed.
				if (!(Item.itemsList[inputID] instanceof FCItemPlacesAsBlock || Block.blocksList[inputID] instanceof Block)) {
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
