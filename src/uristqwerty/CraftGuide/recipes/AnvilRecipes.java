package uristqwerty.CraftGuide.recipes;

import java.lang.reflect.Field;
import java.util.List;

import net.minecraft.src.FCBetterThanWolves;
import net.minecraft.src.FCCraftingManagerAnvil;
import net.minecraft.src.FCCraftingManagerBulk;
import net.minecraft.src.FCCraftingManagerBulkRecipe;
import net.minecraft.src.IRecipe;
import net.minecraft.src.ItemStack;
import net.minecraft.src.ShapedRecipes;
import net.minecraft.src.ShapelessRecipes;
import uristqwerty.CraftGuide.api.CraftGuideAPIObject;
import uristqwerty.CraftGuide.api.ItemSlot;
import uristqwerty.CraftGuide.api.RecipeGenerator;
import uristqwerty.CraftGuide.api.RecipeProvider;
import uristqwerty.CraftGuide.api.RecipeTemplate;
import uristqwerty.CraftGuide.api.Slot;
import uristqwerty.CraftGuide.api.SlotType;

public class AnvilRecipes extends CraftGuideAPIObject implements RecipeProvider {
	
	private Slot[] slotsShaped;
	private Slot[] slotsShapeless;
	private ItemStack anvil = new ItemStack(FCBetterThanWolves.fcAnvil);

	@Override
	public void generateRecipes(RecipeGenerator generator) {
		slotsShaped = createSlots(true);
		slotsShapeless = createSlots(false);
		RecipeTemplate templateShaped = generator.createRecipeTemplate(slotsShaped, anvil).setSize(96, 78);
		RecipeTemplate templateShapeless = generator.createRecipeTemplate(slotsShapeless, anvil).setSize(96, 78);
		
		List<IRecipe> recipes = FCCraftingManagerAnvil.getInstance().getRecipeList();
		for (IRecipe recipe : recipes) {
			try {
				Field recipeField;
				if (recipe instanceof ShapelessRecipes) {
					recipeField = ShapelessRecipes.class.getDeclaredField("recipeItems");
				} else {
					recipeField = ShapedRecipes.class.getDeclaredField("recipeItems");
				}
				recipeField.setAccessible(true);
				ItemStack[] inputs = (ItemStack[]) recipeField.get(recipe);
				
				ItemStack[] crafting = new ItemStack[slotsShaped.length];
				for (int i = 0; i < inputs.length; i++) {
					crafting[i] = inputs[i];
				}
				
				crafting[16] = anvil;
				crafting[17] = recipe.getRecipeOutput();
				
				if (recipe instanceof ShapelessRecipes) {
					generator.addRecipe(templateShapeless, crafting);
				} else {
					generator.addRecipe(templateShaped, crafting);
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
	
	private Slot[] createSlots(boolean drawOwnBackground) {
		Slot[] slots = new ItemSlot[18];
		
		for (int row = 0; row < 4; row++) {
			for (int col = 0; col < 4; col++) {
				slots[row * 4 + col] = new ItemSlot(col * 18 + 3, row * 18 + 3, 16, 16, true).drawOwnBackground(drawOwnBackground);
			}
		}
		slots[16] = new ItemSlot(75, 39, 16, 16).setSlotType(SlotType.MACHINE_SLOT);
		slots[17] = new ItemSlot(75, 21, 16, 16, true).setSlotType(SlotType.OUTPUT_SLOT).drawOwnBackground();
		return slots;
	}
}
