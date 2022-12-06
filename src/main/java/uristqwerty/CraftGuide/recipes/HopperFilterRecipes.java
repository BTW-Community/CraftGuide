package uristqwerty.CraftGuide.recipes;

import java.lang.reflect.Field;
import java.util.ArrayList;

import btw.block.BTWBlocks;
import btw.crafting.manager.HopperFilteringCraftingManager;
import btw.crafting.recipe.types.HopperFilterRecipe;
import btw.item.BTWItems;
import net.minecraft.src.ItemStack;
import uristqwerty.CraftGuide.api.CraftGuideAPIObject;
import uristqwerty.CraftGuide.api.RecipeGenerator;
import uristqwerty.CraftGuide.api.RecipeProvider;
import uristqwerty.CraftGuide.api.RecipeTemplate;
import uristqwerty.CraftGuide.api.Slot;

public class HopperFilterRecipes extends CraftGuideAPIObject implements RecipeProvider {
	private ItemStack hopper = new ItemStack(BTWBlocks.hopper);
	private ItemStack urn = new ItemStack(BTWItems.urn);
	private ItemStack soulUrn = new ItemStack(BTWItems.soulUrn);

	@Override
	public void generateRecipes(RecipeGenerator generator) {
		Slot[] slots = BTWRecipes.createSlots(2, 2, 2);
		RecipeTemplate template = generator.createRecipeTemplate(slots, hopper).setSize(3 * 18 + 6, 2 * 18 + 4);
		
		try {
			Field recipesField = HopperFilteringCraftingManager.class.getDeclaredField("recipes");
			recipesField.setAccessible(true);
			ArrayList<HopperFilterRecipe> recipes = (ArrayList<HopperFilterRecipe>) recipesField.get(HopperFilteringCraftingManager.instance);
			
			for (HopperFilterRecipe recipe : recipes) {
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
