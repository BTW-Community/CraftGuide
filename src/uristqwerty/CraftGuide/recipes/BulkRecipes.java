package uristqwerty.CraftGuide.recipes;

import java.lang.reflect.Field;
import java.util.List;

import net.minecraft.src.FCBetterThanWolves;
import net.minecraft.src.FCCraftingManagerBulk;
import net.minecraft.src.FCCraftingManagerBulkRecipe;
import net.minecraft.src.FCCraftingManagerCauldronStoked;
import net.minecraft.src.FCCraftingManagerMillStone;
import net.minecraft.src.ItemStack;
import uristqwerty.CraftGuide.api.CraftGuideAPIObject;
import uristqwerty.CraftGuide.api.ItemSlot;
import uristqwerty.CraftGuide.api.RecipeGenerator;
import uristqwerty.CraftGuide.api.RecipeProvider;
import uristqwerty.CraftGuide.api.RecipeTemplate;
import uristqwerty.CraftGuide.api.Slot;
import uristqwerty.CraftGuide.api.SlotType;

public class BulkRecipes extends CraftGuideAPIObject implements RecipeProvider {

	private int inputWidth = 2;
	private final int inputHeight = 3;
		
	private int outputWidth = 1;
	private final int outputHeight = 3;
	
	private ItemStack[] machines;
	private Slot[] slots;
	
	private FCCraftingManagerBulk craftingManager;
	
	private int xOffset;
	
	public BulkRecipes(int inputWidth, int outputWidth, ItemStack[] machines, FCCraftingManagerBulk craftingManager, int xOffset) {
		this.inputWidth = inputWidth;
		this.outputWidth = outputWidth;
		this.machines = machines;
		this.craftingManager = craftingManager;
		this.xOffset = xOffset;
	}
		
	public BulkRecipes(int inputWidth, int outputWidth, ItemStack machine, FCCraftingManagerBulk craftingManager, int xOffset) {
		this(inputWidth, outputWidth, new ItemStack[] {null, machine, null}, craftingManager, xOffset);
	}
	
	public BulkRecipes(int inputWidth, int outputWidth, ItemStack[] machines, FCCraftingManagerBulk craftingManager) {
		this(inputWidth, outputWidth, machines, craftingManager, 3);
	}
	
	public BulkRecipes(int inputWidth, int outputWidth, ItemStack machine, FCCraftingManagerBulk craftingManager) {
		this(inputWidth, outputWidth, new ItemStack[] {null, machine, null}, craftingManager);
	}

	@Override
	public void generateRecipes(RecipeGenerator generator) {
		createSlots();
		RecipeTemplate template = generator.createRecipeTemplate(slots, machines[1]);
		
		try {
			Field recipesField = FCCraftingManagerBulk.class.getDeclaredField("m_recipes");
			recipesField.setAccessible(true);
			List<FCCraftingManagerBulkRecipe> recipes = (List<FCCraftingManagerBulkRecipe>) recipesField.get(craftingManager);
			
			for (FCCraftingManagerBulkRecipe recipe : recipes) {
				ItemStack[] crafting = new ItemStack[slots.length];
				
				List<ItemStack> inputs = recipe.getCraftingIngrediantList();
				for (int i = 0; i < inputs.size() && i < inputWidth*inputHeight; i++) {
					crafting[i] = inputs.get(i);
				}

				for (int i = 0; i < machines.length; i++) {
					crafting[inputWidth*inputHeight + i] = machines[i];
				}
				
				List<ItemStack> outputs = recipe.getCraftingOutputList();
				// Condensing is required for the Companion Cube MillStone recipe
				BTWRecipes.condenseItemStackList(outputs);
				for (int row = 0; row < outputHeight && row < outputs.size(); row++) {
					crafting[inputWidth*inputHeight + machines.length + row] = outputs.get(row);
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
		slots = new ItemSlot[inputWidth*inputHeight + machines.length + outputHeight];
		
		for (int row = 0; row < inputHeight; row++) {
			for (int col = 0; col < inputWidth; col++) {
				slots[row * inputWidth + col] = new ItemSlot(col * 18 + xOffset, row * 18 + 3, 16, 16, true);
			}
		}
		for (int row = 0; row < machines.length; row++) {
			slots[inputWidth*inputHeight + row] = new ItemSlot(inputWidth * 18 + xOffset, row * 18 + 3, 16, 16).setSlotType(SlotType.MACHINE_SLOT);
		}
		for (int row = 0; row < outputHeight; row++) {
			slots[inputWidth*inputHeight + machines.length + row] = new ItemSlot(inputWidth * 18 + xOffset + 18, row * 18 + 3, 16, 16, true).setSlotType(SlotType.OUTPUT_SLOT);
		}
	}
}
