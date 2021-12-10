package uristqwerty.CraftGuide.recipes;

import java.lang.reflect.Field;
import java.util.List;

import net.minecraft.src.FCBetterThanWolves;
import net.minecraft.src.FCCraftingManagerBulk;
import net.minecraft.src.FCCraftingManagerBulkRecipe;
import net.minecraft.src.FCCraftingManagerMillStone;
import net.minecraft.src.ItemStack;
import uristqwerty.CraftGuide.CommonUtilities;
import uristqwerty.CraftGuide.api.CraftGuideAPIObject;
import uristqwerty.CraftGuide.api.ItemSlot;
import uristqwerty.CraftGuide.api.RecipeGenerator;
import uristqwerty.CraftGuide.api.RecipeProvider;
import uristqwerty.CraftGuide.api.RecipeTemplate;
import uristqwerty.CraftGuide.api.Slot;
import uristqwerty.CraftGuide.api.SlotType;

public class MillStoneRecipes extends CraftGuideAPIObject implements RecipeProvider {	
	// TODO: Make output slots dynamic to account for extra output.
	// As of BTW CE v1.3.0, the maximum output items is 3.
	private final Slot[] slots = new ItemSlot[] {
		new ItemSlot(13, 21, 16, 16),
		new ItemSlot(31, 21, 16, 16).setSlotType(SlotType.MACHINE_SLOT),
		new ItemSlot(50, 3, 16, 16, true).setSlotType(SlotType.OUTPUT_SLOT),
		new ItemSlot(50, 21, 16, 16, true).setSlotType(SlotType.OUTPUT_SLOT),
		new ItemSlot(50, 39, 16, 16, true).setSlotType(SlotType.OUTPUT_SLOT),
	};

	@Override
	public void generateRecipes(RecipeGenerator generator) {
		ItemStack machine = new ItemStack(FCBetterThanWolves.fcMillStone);
		RecipeTemplate template = generator.createRecipeTemplate(slots, machine);
		
		try {
			Field recipesField = FCCraftingManagerBulk.class.getDeclaredField("m_recipes");
			recipesField.setAccessible(true);
			List<FCCraftingManagerBulkRecipe> recipes = (List<FCCraftingManagerBulkRecipe>) recipesField.get(FCCraftingManagerMillStone.getInstance());
			
			for (FCCraftingManagerBulkRecipe recipe : recipes) {
				ItemStack[] crafting = new ItemStack[slots.length];
				crafting[0] = recipe.GetFirstIngredient();
				crafting[1] = machine;
				
				List<ItemStack> output = recipe.getCraftingOutputList();
				// Condensing is required for the Buddy Block recipe.
				BTWRecipes.condenseItemStackList(output);
				
				for (int i = 0; i < output.size() && i < slots.length - 2; i++) {
					crafting[i + 2] = output.get(i);
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
