package uristqwerty.CraftGuide.recipes;

import java.lang.reflect.Field;
import java.util.List;

import net.minecraft.src.FCBetterThanWolves;
import net.minecraft.src.FCCraftingManagerBulk;
import net.minecraft.src.FCCraftingManagerBulkRecipe;
import net.minecraft.src.FCCraftingManagerCauldronStoked;
import net.minecraft.src.ItemStack;
import uristqwerty.CraftGuide.api.CraftGuideAPIObject;
import uristqwerty.CraftGuide.api.ItemSlot;
import uristqwerty.CraftGuide.api.RecipeGenerator;
import uristqwerty.CraftGuide.api.RecipeProvider;
import uristqwerty.CraftGuide.api.RecipeTemplate;
import uristqwerty.CraftGuide.api.Slot;
import uristqwerty.CraftGuide.api.SlotType;

public class CauldronStokedRecipes extends CraftGuideAPIObject implements RecipeProvider {
	
	private final int inputWidth = 2;
	private final int inputHeight = 3;
	private final int machineHeight = 3;
	private final int outputHeight = 3;
	private Slot[] slots = new ItemSlot[inputWidth*inputHeight + machineHeight + outputHeight];
		
	@Override
	public void generateRecipes(RecipeGenerator generator) {
		createSlots();
		
		ItemStack machine = new ItemStack(FCBetterThanWolves.fcCauldron);
		ItemStack hibachi = new ItemStack(FCBetterThanWolves.fcBBQ);
		ItemStack bellows = new ItemStack(FCBetterThanWolves.fcBellows);
		RecipeTemplate template = generator.createRecipeTemplate(slots, machine);
		
		try {
			Field recipesField = FCCraftingManagerBulk.class.getDeclaredField("m_recipes");
			recipesField.setAccessible(true);
			List<FCCraftingManagerBulkRecipe> recipes = (List<FCCraftingManagerBulkRecipe>) recipesField.get(FCCraftingManagerCauldronStoked.getInstance());
			
			for (FCCraftingManagerBulkRecipe recipe : recipes) {
				ItemStack[] crafting = new ItemStack[slots.length];
				
				List<ItemStack> inputs = recipe.getCraftingIngrediantList();
				for (int i = 0; i < inputs.size() && i < inputWidth*inputHeight; i++) {
					crafting[i] = inputs.get(i);
				}
				
				crafting[inputWidth*inputHeight] = bellows;
				crafting[inputWidth*inputHeight + 1] = machine;
				crafting[inputWidth*inputHeight + 2] = hibachi;
				
				List<ItemStack> outputs = recipe.getCraftingOutputList();
				for (int row = 0; row < outputHeight && row < outputs.size(); row++) {
					crafting[inputWidth*inputHeight + machineHeight + row] = outputs.get(row);
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
		for (int row = 0; row < machineHeight; row++) {
			slots[inputWidth*inputHeight + row] = new ItemSlot(39, row * 18 + 3, 16, 16).setSlotType(SlotType.MACHINE_SLOT);
		}
		for (int row = 0; row < outputHeight; row++) {
			slots[inputWidth*inputHeight + machineHeight + row] = new ItemSlot(59, row * 18 + 3, 16, 16, true).setSlotType(SlotType.OUTPUT_SLOT);
		}
	}
}
