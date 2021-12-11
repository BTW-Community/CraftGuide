package uristqwerty.CraftGuide.recipes;

import java.lang.reflect.Field;
import java.util.List;

import net.minecraft.src.FCCraftingManagerBulk;
import net.minecraft.src.FCCraftingManagerBulkRecipe;
import net.minecraft.src.ItemStack;
import uristqwerty.CraftGuide.api.CraftGuideAPIObject;
import uristqwerty.CraftGuide.api.ItemSlot;
import uristqwerty.CraftGuide.api.RecipeGenerator;
import uristqwerty.CraftGuide.api.RecipeProvider;
import uristqwerty.CraftGuide.api.RecipeTemplate;
import uristqwerty.CraftGuide.api.Slot;
import uristqwerty.CraftGuide.api.SlotType;

public class BulkRecipes extends CraftGuideAPIObject implements RecipeProvider {
	private ItemStack[] machines;
	private FCCraftingManagerBulk craftingManager;
	
	private int inputSize;
	private int inputArea;
	private int inputW;
	private int inputH;
	
	private int outputSize;
	private int outputArea;
	private int outputW;
	private int outputH;
	
	public BulkRecipes(ItemStack[] machines, FCCraftingManagerBulk craftingManager) {
		this.machines = machines;
		this.craftingManager = craftingManager;
	}
		
	public BulkRecipes(ItemStack machine, FCCraftingManagerBulk craftingManager) {
		this(new ItemStack[] {null, machine, null}, craftingManager);
	}

	@Override
	public void generateRecipes(RecipeGenerator generator) {
		try {
			Field recipesField = FCCraftingManagerBulk.class.getDeclaredField("m_recipes");
			recipesField.setAccessible(true);
			List<FCCraftingManagerBulkRecipe> recipes = (List<FCCraftingManagerBulkRecipe>) recipesField.get(craftingManager);
			
			// Determine the maximum input and output lengths of the recipes.
			// Having different sizes looks jarring, especially when comparing
			// recipes of the same machine, so we use the same size for all recipes.
			for (FCCraftingManagerBulkRecipe recipe : recipes) {
				List<ItemStack> inputs = recipe.getCraftingIngrediantList();
				List<ItemStack> outputs = recipe.getCraftingOutputList();
				// Condensing is required for the Companion Cube MillStone recipe,
				// which outputs multiple single items of the same type for some reason.
				BTWRecipes.condenseItemStackList(outputs);
				
				inputSize = Math.max(inputSize, inputs.size());
				outputSize = Math.max(outputSize, outputs.size());
			}
			
			inputW = (int) Math.ceil(inputSize / 3.0);
			inputH = Math.min(inputSize, 3);
			inputArea = inputW * inputH;
			
			outputW = (int) Math.ceil(outputSize / 3.0);
			outputH = Math.min(outputSize, 3);
			outputArea = outputW * outputH;
			
			System.out.println(String.format("\n(%d) %d * %d = %d, (%d) %d * %d = %d", inputSize, inputW, inputH, inputArea, outputSize, outputW, outputH, outputArea));
			
			Slot[] slots = createSlots();
			RecipeTemplate template = generator.createRecipeTemplate(slots, machines[1]).setSize((inputW + 1 + outputH) * 18 + 6, 3 * 18 + 6);
			
			for (FCCraftingManagerBulkRecipe recipe : recipes) {
				List<ItemStack> inputs = recipe.getCraftingIngrediantList();
				List<ItemStack> outputs = recipe.getCraftingOutputList();
				BTWRecipes.condenseItemStackList(outputs);
				
				ItemStack[] crafting = new ItemStack[slots.length];
				
				for (int i = 0; i < inputs.size(); i++) {
					crafting[i] = inputs.get(i);
				}
				for (int i = 0; i < 3; i++) {
					crafting[inputArea + i] = machines[i];
				}
				for (int i = 0; i < outputs.size(); i++) {
					crafting[inputArea + 3 + i] = outputs.get(i);
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
	
	private Slot[] createSlots() {
		Slot[] slots = new ItemSlot[inputArea + 3 + outputArea];
		
		int inputShift = (3 - inputH) * 9;
		int outputShift = (3 - outputH) * 9;
		
		for (int col = 0; col < inputW; col++) {
			for (int row = 0; row < inputH; row++) {
				slots[inputH * col + row] = new ItemSlot(col * 18, row * 18 + inputShift, 16, 16, true);
			}
		}
		
		for (int row = 0; row < 3; row++) {
			slots[inputArea + row] = new ItemSlot(inputW * 18, row * 18, 16, 16).setSlotType(SlotType.MACHINE_SLOT);
		}
		for (int col = 0; col < outputW; col++) {
			for (int row = 0; row < outputH; row++) {
				slots[inputArea + 3 + outputH * col + row] = new ItemSlot((inputW + 1 + col) * 18, row * 18 + outputShift, 16, 16, true).setSlotType(SlotType.OUTPUT_SLOT);
			}
		}
		
		return slots;
	}
}
