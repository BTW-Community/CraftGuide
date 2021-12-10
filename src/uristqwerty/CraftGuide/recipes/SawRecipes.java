package uristqwerty.CraftGuide.recipes;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;

import net.minecraft.src.FCBetterThanWolves;
import net.minecraft.src.FCCraftingManagerSaw;
import net.minecraft.src.FCCraftingManagerSawRecipe;
import net.minecraft.src.ItemStack;
import uristqwerty.CraftGuide.api.CraftGuideAPIObject;
import uristqwerty.CraftGuide.api.ItemSlot;
import uristqwerty.CraftGuide.api.RecipeGenerator;
import uristqwerty.CraftGuide.api.RecipeProvider;
import uristqwerty.CraftGuide.api.RecipeTemplate;
import uristqwerty.CraftGuide.api.Slot;
import uristqwerty.CraftGuide.api.SlotType;

public class SawRecipes extends CraftGuideAPIObject implements RecipeProvider {
	private Slot[] slots;
	private ItemStack saw = new ItemStack(FCBetterThanWolves.fcSaw);

	@Override
	public void generateRecipes(RecipeGenerator generator) {
		createSlots();
		RecipeTemplate template = generator.createRecipeTemplate(slots, saw);
		
		try {
			Field recipesField = FCCraftingManagerSaw.class.getDeclaredField("recipes");
			recipesField.setAccessible(true);
			ArrayList<FCCraftingManagerSawRecipe> recipes = (ArrayList<FCCraftingManagerSawRecipe>) recipesField.get(FCCraftingManagerSaw.instance);
			
			// TODO: Figure out why some 'weird' recipes appear, i.e. wood type changing when sawed.
			for (FCCraftingManagerSawRecipe recipe : recipes) {
				ItemStack[] crafting = new ItemStack[slots.length];
				
				crafting[0] = new ItemStack(recipe.getInputblock(), 1, recipe.getInputMetadata());
				crafting[1] = saw;
				ItemStack[] outputs = recipe.getOutput();
				int j = outputs.length == 1 ? 1 : 0;
				for (int i = 0; i < 3 && i < outputs.length; i++) {
					crafting[2 + i + j] = outputs[i];
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
		slots = new ItemSlot[5];
		int xOffset = 14;
		
		slots[0] = new ItemSlot(xOffset, 21, 16, 16);
		slots[1] = new ItemSlot(18 + xOffset, 21, 16, 16).setSlotType(SlotType.MACHINE_SLOT);
		for (int i = 0; i < 3; i++) {
			slots[2 + i] = new ItemSlot(36 + xOffset, i * 18 + 3, 16, 16, true).setSlotType(SlotType.OUTPUT_SLOT);
		}
	}
}
