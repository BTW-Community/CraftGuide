package uristqwerty.CraftGuide.recipes;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.src.Block;
import net.minecraft.src.CraftingManager;
import net.minecraft.src.FCBetterThanWolves;
import net.minecraft.src.FCCraftingRecipeLogChopping;
import net.minecraft.src.FCItemAxe;
import net.minecraft.src.IRecipe;
import net.minecraft.src.Item;
import net.minecraft.src.ItemAxe;
import net.minecraft.src.ItemStack;
import uristqwerty.CraftGuide.api.CraftGuideAPIObject;
import uristqwerty.CraftGuide.api.ItemSlot;
import uristqwerty.CraftGuide.api.RecipeGenerator;
import uristqwerty.CraftGuide.api.RecipeProvider;
import uristqwerty.CraftGuide.api.RecipeTemplate;
import uristqwerty.CraftGuide.api.Slot;
import uristqwerty.CraftGuide.api.SlotType;

public class LogChoppingRecipes extends CraftGuideAPIObject implements RecipeProvider {
	@Override
	public void generateRecipes(RecipeGenerator generator) {
		List<ItemStack> axes = new ArrayList();
		for (int id = 0; id < Item.itemsList.length; id++) {
			Item item = Item.itemsList[id];
			if (item instanceof FCItemAxe) {
				axes.add(new ItemStack(item));
			}
		}
		
		List<IRecipe> recipes = CraftingManager.getInstance().getRecipeList();
		
		int outputSize = 0;
		for (IRecipe recipe : recipes) {
			if (recipe instanceof FCCraftingRecipeLogChopping) {
				FCCraftingRecipeLogChopping logRecipe = (FCCraftingRecipeLogChopping) recipe;
				
				int outputLen = logRecipe.getSecondaryOutput().length;
				if (logRecipe.getHasLowQualityOutputs()) {
					outputLen = Math.max(outputLen, logRecipe.getSecondaryOutputLowQuality().length);
				}
				
				outputSize = Math.max(outputSize, outputLen + 1);
			}
		}
		int outputW = (int) Math.ceil(outputSize / 3.0);
		
		Slot[] slots = createSlots(outputSize);
		RecipeTemplate template = generator.createRecipeTemplate(slots, null).setSize((3 + outputW) * 18 + 6, 3 * 18 + 4);
		
		for (IRecipe recipe : recipes) {
			if (recipe instanceof FCCraftingRecipeLogChopping) {
				FCCraftingRecipeLogChopping logRecipe = (FCCraftingRecipeLogChopping) recipe;
				ItemStack[] crafting = new ItemStack[slots.length];
				
				for (ItemStack axe : axes) {
					crafting[0] = axe;
					crafting[1] = logRecipe.getInput();
					
					ItemStack[] secondaryOutputs;
					if (isLowQualityAxe(axe) && logRecipe.getHasLowQualityOutputs()) {
						crafting[4] = logRecipe.getRecipeOutputLowQuality();
						secondaryOutputs = logRecipe.getSecondaryOutputLowQuality();
					} else {
						crafting[4] = logRecipe.getRecipeOutput();
						secondaryOutputs = logRecipe.getSecondaryOutput();
					}
					
					if (logRecipe.HasSecondaryOutput()) {
						for (int i = 0; i < secondaryOutputs.length; i++) {
							crafting[5 + i] = secondaryOutputs[i];
						}
					}
					
					generator.addRecipe(template, crafting);
				}
			}
		}
	}
	
	private Slot[] createSlots(int outputSize) {
		int outputW = (int) Math.ceil(outputSize / 3.0);
		int outputH = Math.min(outputSize, 3);
		int outputArea = outputW * outputH;
		
		Slot[] slots = new ItemSlot[4 + outputArea];
		
		for (int col = 0; col < 2; col++) {
			for (int row = 0; row < 2; row++) {
				slots[2 * col + row] = new ItemSlot(col * 18 + 12, row * 18 + 12, 16, 16).drawOwnBackground();
			}
		}
		for (int col = 0; col < outputW; col++) {
			for (int row = 0; row < outputH; row++) {
				slots[4 + outputH * col + row] = new ItemSlot((3 + col) * 18 + 3, row * 18 + 3, 16, 16, true).drawOwnBackground().setSlotType(SlotType.OUTPUT_SLOT);
			}
		}
		
		return slots;
	}
	
	private boolean isAxe(ItemStack stack) {
		return stack.getItem() instanceof FCItemAxe;
	}
	
	private boolean isLowQualityAxe(ItemStack stack) {
		return isAxe(stack) && ((FCItemAxe) stack.getItem()).GetConsumesHungerOnZeroHardnessVegetation();
	}
}
