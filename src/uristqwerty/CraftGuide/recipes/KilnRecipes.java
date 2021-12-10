package uristqwerty.CraftGuide.recipes;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;

import net.minecraft.src.Block;
import net.minecraft.src.FCBetterThanWolves;
import net.minecraft.src.FCCraftingManagerKiln;
import net.minecraft.src.FCCraftingManagerKilnRecipe;
import net.minecraft.src.ItemStack;
import uristqwerty.CraftGuide.api.CraftGuideAPIObject;
import uristqwerty.CraftGuide.api.ItemSlot;
import uristqwerty.CraftGuide.api.RecipeGenerator;
import uristqwerty.CraftGuide.api.RecipeProvider;
import uristqwerty.CraftGuide.api.RecipeTemplate;
import uristqwerty.CraftGuide.api.Slot;
import uristqwerty.CraftGuide.api.SlotType;

public class KilnRecipes extends CraftGuideAPIObject implements RecipeProvider {
	private Slot[] slots;
	
	private ItemStack bellows = new ItemStack(FCBetterThanWolves.fcBellows);
	private ItemStack brick = new ItemStack(Block.brick);
	private ItemStack hibachi = new ItemStack(FCBetterThanWolves.fcBBQ);
	private ItemStack[] machines = new ItemStack[] {bellows, brick, hibachi};

	@Override
	public void generateRecipes(RecipeGenerator generator) {
		createSlots();
		RecipeTemplate template = generator.createRecipeTemplate(slots, brick);
		
		try {
			Field recipesField = FCCraftingManagerKiln.class.getDeclaredField("recipes");
			recipesField.setAccessible(true);
			ArrayList<FCCraftingManagerKilnRecipe> recipes = (ArrayList<FCCraftingManagerKilnRecipe>) recipesField.get(FCCraftingManagerKiln.instance);
			
			HashSet<ItemStack> completedRecipes = new HashSet();
			
			for (FCCraftingManagerKilnRecipe recipe : recipes) {
				// TODO: Find out why some Unfired Pottery blocks don't render properly.
				// Might be connected to why there are multiple bread/cookie recipes?
				
				ItemStack input = new ItemStack(recipe.getInputblock(), 1, recipe.getInputMetadata());
				
				// Skip duplicates.
				if (!completedRecipes.add(input)) {
					continue;
				}
				
				ItemStack[] crafting = new ItemStack[slots.length];
				crafting[0] = input;
				for (int i = 0; i < 3; i++) {
					crafting[1 + i] = machines[i];
				}
				ItemStack[] output = recipe.getOutput();
				int j = output.length == 1 ? 1 : 0;
				for (int i = 0; i < 3 && i < output.length; i++) {
					crafting[4 + i + j] = output[i];
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
	
	private void createSlots() {
		slots = new ItemSlot[7];
		int xOffset = 14;
		
		slots[0] = new ItemSlot(xOffset, 21, 16, 16, true);
		
		for (int i = 0; i < 3; i++) {
			slots[1 + i] = new ItemSlot(18 + xOffset, i * 18 + 3, 16, 16).setSlotType(SlotType.MACHINE_SLOT);
		}
		for (int i = 0; i < 3; i++) {
			slots[4 + i] = new ItemSlot(36 + xOffset, i * 18 + 3, 16, 16, true).setSlotType(SlotType.OUTPUT_SLOT);
		}
	}
}
