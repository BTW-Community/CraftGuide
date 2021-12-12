package uristqwerty.CraftGuide.recipes;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import net.minecraft.src.Block;
import net.minecraft.src.FCBetterThanWolves;
import net.minecraft.src.FCCraftingManagerSaw;
import net.minecraft.src.FCCraftingManagerSawRecipe;
import net.minecraft.src.ItemStack;
import uristqwerty.CraftGuide.api.CraftGuideAPIObject;
import uristqwerty.CraftGuide.api.RecipeGenerator;
import uristqwerty.CraftGuide.api.RecipeProvider;
import uristqwerty.CraftGuide.api.RecipeTemplate;
import uristqwerty.CraftGuide.api.Slot;

public class SawRecipes extends CraftGuideAPIObject implements RecipeProvider {
	private ItemStack saw = new ItemStack(FCBetterThanWolves.fcSaw);

	@Override
	public void generateRecipes(RecipeGenerator generator) {;
		try {
			Field recipesField = FCCraftingManagerSaw.class.getDeclaredField("recipes");
			recipesField.setAccessible(true);
			ArrayList<FCCraftingManagerSawRecipe> recipes = (ArrayList<FCCraftingManagerSawRecipe>) recipesField.get(FCCraftingManagerSaw.instance);
			
			int outputSize = 0;
			for (FCCraftingManagerSawRecipe recipe : recipes) {
				ItemStack[] outputs = recipe.getOutput();
				outputSize = Math.max(outputSize, outputs.length);
			}
			Slot[] slots = BTWRecipes.createSlots(1, 1, outputSize);
			
			int outputW = (int) Math.ceil(outputSize / 3.0);
			int outputH = Math.min(outputSize, 3);
			int maxHeight = Math.max(outputH, 1);
			
			RecipeTemplate template = generator.createRecipeTemplate(slots, saw).setSize((2 + outputW) * 18 + 6, maxHeight * 18 + 6);
			
			HashSet<List<Integer>> completedInputs = new HashSet();
			for (FCCraftingManagerSawRecipe recipe : recipes) {
				Block inputBlock = recipe.getInputblock();
				int metadata = recipe.getInputMetadata();
				// We get the item dropped, otherwise it'll give us the Block IDs of the
				// actual blocks placed in the world, which aren't legitimately obtainable.
				// (i.e. various orientations of the "same" block).
				int inputID = inputBlock.idDropped(metadata, null, 1);
				metadata = inputBlock.damageDropped(metadata);
				
				// For some reason idDropped for vines is 0.
				if (inputID == 0) {
					inputID = Block.vine.blockID;
				}
				
				// Skip duplicate inputs.
				ArrayList<Integer> inputCheck = new ArrayList();
				inputCheck.add(inputID);
				inputCheck.add(metadata);
				if (!completedInputs.add(inputCheck)) {
					continue;
				}
				
				ItemStack[] crafting = new ItemStack[slots.length];
				
				crafting[0] = new ItemStack(inputID, 1, metadata);
				crafting[1] = saw;
				ItemStack[] outputs = recipe.getOutput();
				for (int i = 0; i < outputs.length; i++) {
					crafting[2 + i] = outputs[i];
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
