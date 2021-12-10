package uristqwerty.CraftGuide.recipes;

import java.lang.reflect.Field;
import java.util.ArrayList;

import net.minecraft.src.FCBetterThanWolves;
import net.minecraft.src.FCCraftingManagerHopperFilter;
import net.minecraft.src.FCCraftingManagerHopperFilterRecipe;
import net.minecraft.src.ItemStack;
import uristqwerty.CraftGuide.api.CraftGuideAPIObject;
import uristqwerty.CraftGuide.api.ItemSlot;
import uristqwerty.CraftGuide.api.RecipeGenerator;
import uristqwerty.CraftGuide.api.RecipeProvider;
import uristqwerty.CraftGuide.api.RecipeTemplate;
import uristqwerty.CraftGuide.api.Slot;
import uristqwerty.CraftGuide.api.SlotType;

public class HopperFilterRecipes extends CraftGuideAPIObject implements RecipeProvider {
	private Slot[] slots;
	
	private ItemStack hopper = new ItemStack(FCBetterThanWolves.fcHopper);
	private ItemStack urn = new ItemStack(FCBetterThanWolves.fcItemUrn);
	private ItemStack soulUrn = new ItemStack(FCBetterThanWolves.fcItemSoulUrn);

	@Override
	public void generateRecipes(RecipeGenerator generator) {
		createSlots();
		RecipeTemplate template = generator.createRecipeTemplate(slots, hopper);
		
		try {
			Field recipesField = FCCraftingManagerHopperFilter.class.getDeclaredField("recipes");
			recipesField.setAccessible(true);
			ArrayList<FCCraftingManagerHopperFilterRecipe> recipes = (ArrayList<FCCraftingManagerHopperFilterRecipe>) recipesField.get(FCCraftingManagerHopperFilter.instance);
			
			for (FCCraftingManagerHopperFilterRecipe recipe : recipes) {
				ItemStack[] crafting = new ItemStack[slots.length];
				
				crafting[0] = recipe.getInput();
				crafting[2] = recipe.getFilterUsed();
				crafting[3] = hopper;
				crafting[4] = recipe.getFilteredOutput();
				
				if (recipe.getContainsSouls()) {
					crafting[1] = urn;
					crafting[5] = soulUrn;
				} else {
					crafting[5] = recipe.getHopperOutput();
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
		slots = new ItemSlot[6];
		int xOffset = 14;
		
		for (int i = 0; i < 2; i++) {
			slots[i] = new ItemSlot(xOffset, i * 18 + 3, 16, 16, true);
		}
		for (int i = 0; i < 2; i++) {
			slots[2 + i] = new ItemSlot(18 + xOffset, i * 18 + 3, 16, 16).setSlotType(SlotType.MACHINE_SLOT);
		}
		for (int i = 0; i < 2; i++) {
			slots[4 + i] = new ItemSlot(36 + xOffset, i * 18 + 3, 16, 16, true).setSlotType(SlotType.OUTPUT_SLOT);
		}
	}
}
