package uristqwerty.CraftGuide.recipes;

import btw.item.BTWItems;
import btw.item.items.WoolItem;
import net.minecraft.src.ItemStack;
import uristqwerty.CraftGuide.api.*;

// TODO: Figure out how to change color of knitting.
// Ideally would like it to be white.

public class KnittingRecipes extends CraftGuideAPIObject implements RecipeProvider {
    private final ItemStack needles = new ItemStack(BTWItems.knittingNeedles);
    private final ItemStack knitting = new ItemStack(BTWItems.knitting);

    @Override
    public void generateRecipes(RecipeGenerator generator) {
        Slot[] slots = createSlots();
        RecipeTemplate template = generator.createRecipeTemplate(slots, null).setSize(4 * 18 + 8, 3 * 18 + 4);

        for (int i = 0; i < WoolItem.woolColors.length; i++) {
            ItemStack[] crafting = new ItemStack[slots.length];
            ItemStack wool = new ItemStack(BTWItems.wool, 1, i);

            crafting[0] = wool;
            crafting[1] = wool;
            crafting[2] = needles;
            crafting[4] = knitting;

            generator.addRecipe(template, crafting);
        }
    }

    // Taken from DefaultRecipeProvider and modified.
    private Slot[] createSlots() {
        return new ItemSlot[]{
                new ItemSlot(12, 12, 16, 16).drawOwnBackground(),
                new ItemSlot(30, 12, 16, 16).drawOwnBackground(),
                new ItemSlot(12, 30, 16, 16).drawOwnBackground(),
                new ItemSlot(30, 30, 16, 16).drawOwnBackground(),
                new ItemSlot(59, 21, 16, 16, true).drawOwnBackground().setSlotType(SlotType.OUTPUT_SLOT),
        };
    }
}
