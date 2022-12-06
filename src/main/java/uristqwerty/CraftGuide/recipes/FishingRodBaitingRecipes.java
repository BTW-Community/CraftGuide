package uristqwerty.CraftGuide.recipes;

import btw.item.BTWItems;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import uristqwerty.CraftGuide.api.*;

public class FishingRodBaitingRecipes extends CraftGuideAPIObject implements RecipeProvider {
    private final ItemStack oyster = new ItemStack(BTWItems.creeperOysters);
    private final ItemStack wing = new ItemStack(BTWItems.batWing);
    private final ItemStack wart = new ItemStack(BTWItems.witchWart);
    private final ItemStack eye = new ItemStack(Item.spiderEye);
    private final ItemStack flesh = new ItemStack(Item.rottenFlesh);

    @Override
    public void generateRecipes(RecipeGenerator generator) {
        Slot[] slots = createSlots();
        RecipeTemplate template = generator.createRecipeTemplate(slots, null).setSize(4 * 18 + 8, 3 * 18 + 4);

        for (ItemStack bait : new ItemStack[]{oyster, wing, wart, eye, flesh}) {
            ItemStack[] crafting = new ItemStack[slots.length];

            crafting[0] = new ItemStack(Item.fishingRod);
            crafting[1] = bait;
            crafting[4] = new ItemStack(BTWItems.baitedFishingRod);

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
