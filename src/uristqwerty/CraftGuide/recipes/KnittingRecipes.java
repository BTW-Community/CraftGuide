package uristqwerty.CraftGuide.recipes;

import net.minecraft.src.FCBetterThanWolves;
import net.minecraft.src.ItemStack;
import uristqwerty.CraftGuide.api.CraftGuideAPIObject;
import uristqwerty.CraftGuide.api.ItemSlot;
import uristqwerty.CraftGuide.api.RecipeGenerator;
import uristqwerty.CraftGuide.api.RecipeProvider;
import uristqwerty.CraftGuide.api.RecipeTemplate;
import uristqwerty.CraftGuide.api.Slot;
import uristqwerty.CraftGuide.api.SlotType;

// TODO: Figure out how to change color of knitting.
// Ideally would like it to be white.

public class KnittingRecipes extends CraftGuideAPIObject implements RecipeProvider {
	private ItemStack needles = new ItemStack(FCBetterThanWolves.fcItemKnittingNeedles);
	private ItemStack wool = new ItemStack(FCBetterThanWolves.fcItemWool);
	private ItemStack knitting = new ItemStack(FCBetterThanWolves.fcItemKnitting);

	@Override
	public void generateRecipes(RecipeGenerator generator) {
		Slot[] slots = createSlots();
		RecipeTemplate template = generator.createRecipeTemplate(slots, null).setSize(4 * 18 + 6, 2 * 18 + 6);
		
		ItemStack[] crafting = new ItemStack[slots.length];
		
		crafting[0] = wool;
		crafting[1] = wool;
		crafting[2] = needles;
		crafting[4] = knitting;
		
		generator.addRecipe(template, crafting);
	}
	
	// Taken from DefaultRecipeProvider and modified.
	private Slot[] createSlots() {
		return new ItemSlot[]{
			new ItemSlot(12, 12, 16, 16),
			new ItemSlot(30, 12, 16, 16),
			new ItemSlot(12, 30, 16, 16),
			new ItemSlot(30, 30, 16, 16),
			new ItemSlot(59, 21, 16, 16, true).setSlotType(SlotType.OUTPUT_SLOT),
		};
	}
}
