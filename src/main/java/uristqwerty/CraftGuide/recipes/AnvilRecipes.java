package uristqwerty.CraftGuide.recipes;

import btw.block.BTWBlocks;
import btw.crafting.manager.SoulforgeCraftingManager;
import net.minecraft.src.IRecipe;
import net.minecraft.src.ItemStack;
import net.minecraft.src.ShapedRecipes;
import net.minecraft.src.ShapelessRecipes;
import uristqwerty.CraftGuide.api.*;

import java.lang.reflect.Field;
import java.util.List;

public class AnvilRecipes extends CraftGuideAPIObject implements RecipeProvider {
    private final ItemStack anvil = new ItemStack(BTWBlocks.soulforge);

    @Override
    public void generateRecipes(RecipeGenerator generator) {
        Slot[] slots = createSlots();
        RecipeTemplate template = generator.createRecipeTemplate(slots, anvil).setSize(96, 76);

        List<IRecipe> recipes = SoulforgeCraftingManager.getInstance().getRecipeList();
        for (IRecipe recipe : recipes) {
            try {
                ItemStack[] crafting = new ItemStack[slots.length];

                if (recipe instanceof ShapelessRecipes) {
                    ItemStack[] inputs = (ItemStack[]) getPrivateField(ShapelessRecipes.class, recipe, "recipeItems", "b");

                    System.arraycopy(inputs, 0, crafting, 0, inputs.length);
                } else {
                    ItemStack[] inputs = (ItemStack[]) getPrivateField(ShapedRecipes.class, recipe, "recipeItems", "d");

                    int width = (Integer) getPrivateField(ShapedRecipes.class, recipe, "recipeWidth", "b");
                    int height = (Integer) getPrivateField(ShapedRecipes.class, recipe, "recipeHeight", "c");

                    for (int row = 0; row < height; row++) {
                        if (width >= 0) System.arraycopy(inputs, row * width, crafting, row * 4, width);
                    }
                }

                crafting[16] = anvil;
                crafting[17] = recipe.getRecipeOutput();

                generator.addRecipe(template, crafting);

            } catch (SecurityException | IllegalArgumentException | IllegalAccessException e) {
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

    private <T> Object getPrivateField(Class<? extends T> recipeClass, T object, String name, String obfName) throws IllegalArgumentException, IllegalAccessException {
        Field field = null;
        try {
            field = recipeClass.getDeclaredField(name);
        } catch (NoSuchFieldException e) {
            try {
                field = recipeClass.getDeclaredField(obfName);
            } catch (NoSuchFieldException | SecurityException e1) {
                e1.printStackTrace();
            }
        }
        assert field != null;
        field.setAccessible(true);
        return field.get(object);
    }
}
