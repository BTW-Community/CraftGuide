package uristqwerty.CraftGuide.recipes;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import net.minecraft.src.Block;
import net.minecraft.src.FCBetterThanWolves;
import net.minecraft.src.FCBlockUnfiredPottery;
import net.minecraft.src.FCCraftingManagerBulkRecipe;
import net.minecraft.src.FCCraftingManagerKiln;
import net.minecraft.src.FCCraftingManagerKilnRecipe;
import net.minecraft.src.FCItemPlacesAsBlock;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import uristqwerty.CraftGuide.api.CraftGuideAPIObject;
import uristqwerty.CraftGuide.api.ItemSlot;
import uristqwerty.CraftGuide.api.RecipeGenerator;
import uristqwerty.CraftGuide.api.RecipeProvider;
import uristqwerty.CraftGuide.api.RecipeTemplate;
import uristqwerty.CraftGuide.api.Slot;
import uristqwerty.CraftGuide.api.SlotType;

public class KilnRecipes extends CraftGuideAPIObject implements RecipeProvider {
	private ItemStack bellows = new ItemStack(FCBetterThanWolves.fcBellows);
	private ItemStack brick = new ItemStack(Block.brick);
	private ItemStack hibachi = new ItemStack(FCBetterThanWolves.fcBBQ);
	private ItemStack[] machines = new ItemStack[] {bellows, brick, hibachi};

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
			RecipeTemplate template = generator.createRecipeTemplate(slots, brick);
			
			HashSet<Integer> completedInputs = new HashSet();
			for (FCCraftingManagerKilnRecipe recipe : recipes) {

				Block inputBlock = recipe.getInputblock();
				int metadata = recipe.getInputMetadata();
				// We get the item dropped, otherwise it'll give us the Block IDs of the
				// actual blocks placed in the world, which aren't legitimately obtainable.
				int inputID = inputBlock.idDropped(metadata, null, 1);
				
				// The consequence of the above method is that wet brick and
				// unfired Nether brick drop clay and Nether sludge respectively.
				if (inputID == Item.clay.itemID) {
					inputID = FCBetterThanWolves.fcItemBrickUnfired.itemID;
				}
				if (inputID == FCBetterThanWolves.fcItemNetherSludge.itemID) {
					inputID = FCBetterThanWolves.fcItemNetherBrickUnfired.itemID;
				}
				
				// Skip duplicate inputs.
				if (!completedInputs.add(inputID)) {
					continue;
				}
				
				ItemStack input = new ItemStack(inputID, 1, 0);
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
