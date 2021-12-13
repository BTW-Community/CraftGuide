package uristqwerty.CraftGuide.recipes;

import net.minecraft.src.FCBetterThanWolves;
import net.minecraft.src.FCItemWool;
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
	private ItemStack knitting = new ItemStack(FCBetterThanWolves.fcItemKnitting);

	@Override
	public void generateRecipes(RecipeGenerator generator) {
		Slot[] slots = createSlots();
		RecipeTemplate template = generator.createRecipeTemplate(slots, null).setSize(4 * 18 + 8, 3 * 18 + 4);
		
		for (int i = 0; i < FCItemWool.m_iWoolColors.length; i++) {
			ItemStack[] crafting = new ItemStack[slots.length];
			ItemStack wool = new ItemStack(FCBetterThanWolves.fcItemWool, 1, i);
			
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
