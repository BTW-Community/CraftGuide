package uristqwerty.CraftGuide.recipes;

import java.lang.reflect.Field;
import java.util.List;

import net.minecraft.src.FCBetterThanWolves;
import net.minecraft.src.FCCraftingManagerBulk;
import net.minecraft.src.FCCraftingManagerBulkRecipe;
import net.minecraft.src.FCCraftingManagerCauldron;
import net.minecraft.src.ItemStack;
import uristqwerty.CraftGuide.api.CraftGuideAPIObject;
import uristqwerty.CraftGuide.api.ItemSlot;
import uristqwerty.CraftGuide.api.RecipeGenerator;
import uristqwerty.CraftGuide.api.RecipeProvider;
import uristqwerty.CraftGuide.api.RecipeTemplate;
import uristqwerty.CraftGuide.api.Slot;
import uristqwerty.CraftGuide.api.SlotType;

public class CauldronRecipes extends CraftGuideAPIObject implements RecipeProvider {
	
	private final int inputWidth = 2;
	private final int inputHeight = 3;
	private final int outputHeight = 3;
	private Slot[] slots = new ItemSlot[inputWidth*inputHeight + outputHeight + 1];
		
	@Override
	public void generateRecipes(RecipeGenerator generator) {
		createSlots();
		
		ItemStack machine = new ItemStack(FCBetterThanWolves.fcCauldron);
		RecipeTemplate template = generator.createRecipeTemplate(slots, machine);
		
		try {
			Field recipesField = FCCraftingManagerBulk.class.getDeclaredField("m_recipes");
			recipesField.setAccessible(true);
			List<FCCraftingManagerBulkRecipe> recipes = (List<FCCraftingManagerBulkRecipe>) recipesField.get(FCCraftingManagerCauldron.getInstance());
			
			for (FCCraftingManagerBulkRecipe recipe : recipes) {
				ItemStack[] crafting = new ItemStack[slots.length];
				
				List<ItemStack> inputs = recipe.getCraftingIngrediantList();
				for (int i = 0; i < inputs.size() && i < inputWidth*inputHeight; i++) {
					crafting[i] = inputs.get(i);
				}
				
				crafting[inputWidth*inputHeight] = machine;
				
				List<ItemStack> outputs = recipe.getCraftingOutputList();
				for (int row = 0; row < outputHeight && row < outputs.size(); row++) {
					crafting[inputWidth*inputHeight + row + 1] = outputs.get(row);
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
		for (int row = 0; row < inputHeight; row++) {
			for (int col = 0; col < inputWidth; col++) {
				slots[row * inputWidth + col] = new ItemSlot(col * 18 + 3, row * 18 + 3, 16, 16, true);
			}
		}
		slots[inputWidth*inputHeight] = new ItemSlot(39, 21, 16, 16).setSlotType(SlotType.MACHINE_SLOT);
		for (int row = 0; row < outputHeight; row++) {
			slots[inputWidth*inputHeight + row + 1] = new ItemSlot(59, row * 18 + 3, 16, 16, true).setSlotType(SlotType.OUTPUT_SLOT);
		}
	}
}
