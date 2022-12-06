package uristqwerty.CraftGuide.recipes;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import btw.block.BTWBlocks;
import btw.crafting.manager.SawCraftingManager;
import btw.crafting.recipe.types.SawRecipe;
import net.minecraft.src.Block;
import net.minecraft.src.BlockHalfSlab;
import net.minecraft.src.ItemStack;
import uristqwerty.CraftGuide.api.CraftGuideAPIObject;
import uristqwerty.CraftGuide.api.RecipeGenerator;
import uristqwerty.CraftGuide.api.RecipeProvider;
import uristqwerty.CraftGuide.api.RecipeTemplate;
import uristqwerty.CraftGuide.api.Slot;

public class SawRecipes extends CraftGuideAPIObject implements RecipeProvider {
	private ItemStack saw = new ItemStack(BTWBlocks.saw);

	@Override
	public void generateRecipes(RecipeGenerator generator) {;
		try {
			Field recipesField = SawCraftingManager.class.getDeclaredField("recipes");
			recipesField.setAccessible(true);
			ArrayList<SawRecipe> recipes = (ArrayList<SawRecipe>) recipesField.get(SawCraftingManager.instance);
			
			int outputSize = 0;
			for (SawRecipe recipe : recipes) {
				ItemStack[] outputs = recipe.getOutput();
				outputSize = Math.max(outputSize, outputs.length);
			}
			Slot[] slots = BTWRecipes.createSlots(1, 1, outputSize);
			
			int outputW = (int) Math.ceil(outputSize / 3.0);
			int outputH = Math.min(outputSize, 3);
			int maxHeight = Math.max(outputH, 1);
			
			RecipeTemplate template = generator.createRecipeTemplate(slots, saw).setSize((2 + outputW) * 18 + 6, maxHeight * 18 + 4);
			
			for (SawRecipe recipe : recipes) {
				Block inputBlock = recipe.getInputblock();
				int metadata = recipe.getInputMetadata()[0];
				
				int inputID;
				try {
					inputID = inputBlock.idPicked(null, 0, 0, 0);
				} catch (NullPointerException e) {
					inputID = inputBlock.idDropped(metadata, null, 1);
				}
				
				metadata = inputBlock.damageDropped(metadata);
				
				int quantity = 1;
				if (inputBlock instanceof BlockHalfSlab) {
					quantity = inputBlock.renderAsNormalBlock() ? 2 : 1;
				}
				
				ItemStack[] crafting = new ItemStack[slots.length];
				
				crafting[0] = new ItemStack(inputID, quantity, metadata);
				crafting[1] = saw;
				ItemStack[] outputs = recipe.getOutput();
				for (int i = 0; i < outputs.length; i++) {
					crafting[2 + i] = outputs[i];
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
