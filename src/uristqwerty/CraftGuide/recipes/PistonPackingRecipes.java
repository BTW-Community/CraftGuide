package uristqwerty.CraftGuide.recipes;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;

import net.minecraft.src.Block;
import net.minecraft.src.FCBetterThanWolves;
import net.minecraft.src.FCCraftingManagerKiln;
import net.minecraft.src.FCCraftingManagerKilnRecipe;
import net.minecraft.src.FCCraftingManagerPistonPacking;
import net.minecraft.src.FCCraftingManagerPistonPackingRecipe;
import net.minecraft.src.ItemStack;
import uristqwerty.CraftGuide.api.CraftGuideAPIObject;
import uristqwerty.CraftGuide.api.ItemSlot;
import uristqwerty.CraftGuide.api.RecipeGenerator;
import uristqwerty.CraftGuide.api.RecipeProvider;
import uristqwerty.CraftGuide.api.RecipeTemplate;
import uristqwerty.CraftGuide.api.Slot;
import uristqwerty.CraftGuide.api.SlotType;

public class PistonPackingRecipes extends CraftGuideAPIObject implements RecipeProvider {
	private Slot[] slots;
	private ItemStack piston = new ItemStack(Block.pistonBase);

	@Override
	public void generateRecipes(RecipeGenerator generator) {
		createSlots();
		RecipeTemplate template = generator.createRecipeTemplate(slots, piston);
		
		try {
			Field recipesField = FCCraftingManagerPistonPacking.class.getDeclaredField("recipes");
			recipesField.setAccessible(true);
			ArrayList<FCCraftingManagerPistonPackingRecipe> recipes = (ArrayList<FCCraftingManagerPistonPackingRecipe>) recipesField.get(FCCraftingManagerPistonPacking.instance);
			
			for (FCCraftingManagerPistonPackingRecipe recipe : recipes) {
				ItemStack[] crafting = new ItemStack[slots.length];
				ItemStack[] inputs = recipe.getInput();
				int j = inputs.length == 1 ? 1 : 0;
				for (int i = 0; i < 3 && i < inputs.length; i++) {
					crafting[i + j] = inputs[i];
				}
				crafting[3] = piston;
				crafting[4] = new ItemStack(recipe.getOutput(), 1, recipe.getOutputMetadata());
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
		slots = new ItemSlot[5];
		int xOffset = 14;
		
		for (int i = 0; i < 3; i++) {
			slots[i] = new ItemSlot(xOffset, i * 18 + 3, 16, 16);
		}
		slots[3] = new ItemSlot(18 + xOffset, 21, 16, 16).setSlotType(SlotType.MACHINE_SLOT);
		slots[4] = new ItemSlot(36 + xOffset, 21, 16, 16).setSlotType(SlotType.OUTPUT_SLOT);
	}
}
