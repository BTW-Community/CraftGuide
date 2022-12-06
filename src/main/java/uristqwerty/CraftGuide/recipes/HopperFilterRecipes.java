package uristqwerty.CraftGuide.recipes;

import btw.block.BTWBlocks;
import btw.crafting.manager.HopperFilteringCraftingManager;
import btw.crafting.recipe.types.HopperFilterRecipe;
import btw.item.BTWItems;
import net.minecraft.src.ItemStack;
import uristqwerty.CraftGuide.api.*;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class HopperFilterRecipes extends CraftGuideAPIObject implements RecipeProvider {
    private final ItemStack hopper = new ItemStack(BTWBlocks.hopper);
    private final ItemStack urn = new ItemStack(BTWItems.urn);
    private final ItemStack soulUrn = new ItemStack(BTWItems.soulUrn);

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
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }
	}
}
