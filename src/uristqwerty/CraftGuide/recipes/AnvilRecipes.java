package uristqwerty.CraftGuide.recipes;

import java.lang.reflect.Field;
import java.util.List;

import net.minecraft.src.FCBetterThanWolves;
import net.minecraft.src.FCCraftingManagerAnvil;
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
	private ItemStack anvil = new ItemStack(FCBetterThanWolves.fcAnvil);

	@Override
	public void generateRecipes(RecipeGenerator generator) {
		Slot[] slots = createSlots();
		RecipeTemplate template = generator.createRecipeTemplate(slots, anvil).setSize(96, 76);
		
		List<IRecipe> recipes = FCCraftingManagerAnvil.getInstance().getRecipeList();
		for (IRecipe recipe : recipes) {
			try {
				Field recipeField;
				if (recipe instanceof ShapelessRecipes) {
					recipeField = getPrivateField(ShapelessRecipes.class, "recipeItems", "b");
				} else {
					recipeField = getPrivateField(ShapedRecipes.class, "recipeItems", "d");
				}
				recipeField.setAccessible(true);
				ItemStack[] inputs = (ItemStack[]) recipeField.get(recipe);
				
				ItemStack[] crafting = new ItemStack[slots.length];
				for (int i = 0; i < inputs.length; i++) {
					crafting[i] = inputs[i];
				}
				
				crafting[16] = anvil;
				crafting[17] = recipe.getRecipeOutput();
				
				generator.addRecipe(template, crafting);
				
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}
	
	private Slot[] createSlots() {
		Slot[] slots = new ItemSlot[18];
		
		for (int row = 0; row < 4; row++) {
			for (int col = 0; col < 4; col++) {
				slots[row * 4 + col] = new ItemSlot(col * 18 + 3, row * 18 + 3, 16, 16, true).drawOwnBackground();
			}
		}
		slots[16] = new ItemSlot(75, 39, 16, 16).setSlotType(SlotType.MACHINE_SLOT);
		slots[17] = new ItemSlot(75, 21, 16, 16, true).drawOwnBackground().setSlotType(SlotType.OUTPUT_SLOT);
		return slots;
	}
	
	private <T> Field getPrivateField(Class<? extends T> recipeClass, String name, String obfName) {
		Field field = null;
		try {
			field = recipeClass.getDeclaredField(name);
		} catch (NoSuchFieldException e) {
			try {
				field = recipeClass.getDeclaredField(obfName);
			} catch (NoSuchFieldException e1) {
				e1.printStackTrace();
			} catch (SecurityException e1) {
				e1.printStackTrace();
			}
		}
		field.setAccessible(true);
		return field;
	}
}
