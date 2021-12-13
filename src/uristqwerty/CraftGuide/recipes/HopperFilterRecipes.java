package uristqwerty.CraftGuide.recipes;

import java.lang.reflect.Field;
import java.util.ArrayList;

import net.minecraft.src.FCBetterThanWolves;
import net.minecraft.src.FCCraftingManagerHopperFilter;
import net.minecraft.src.FCCraftingManagerHopperFilterRecipe;
import net.minecraft.src.ItemStack;
import uristqwerty.CraftGuide.api.CraftGuideAPIObject;
import uristqwerty.CraftGuide.api.RecipeGenerator;
import uristqwerty.CraftGuide.api.RecipeProvider;
import uristqwerty.CraftGuide.api.RecipeTemplate;
import uristqwerty.CraftGuide.api.Slot;

public class HopperFilterRecipes extends CraftGuideAPIObject implements RecipeProvider {
	private ItemStack hopper = new ItemStack(FCBetterThanWolves.fcHopper);
	private ItemStack urn = new ItemStack(FCBetterThanWolves.fcItemUrn);
	private ItemStack soulUrn = new ItemStack(FCBetterThanWolves.fcItemSoulUrn);

	@Override
	public void generateRecipes(RecipeGenerator generator) {
		Slot[] slots = BTWRecipes.createSlots(2, 2, 2);
		RecipeTemplate template = generator.createRecipeTemplate(slots, hopper).setSize(3 * 18 + 6, 2 * 18 + 4);
		
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
}
