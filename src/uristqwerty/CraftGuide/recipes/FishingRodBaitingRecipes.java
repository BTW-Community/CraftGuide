package uristqwerty.CraftGuide.recipes;

import net.minecraft.src.FCBetterThanWolves;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import uristqwerty.CraftGuide.api.CraftGuideAPIObject;
import uristqwerty.CraftGuide.api.ItemSlot;
import uristqwerty.CraftGuide.api.RecipeGenerator;
import uristqwerty.CraftGuide.api.RecipeProvider;
import uristqwerty.CraftGuide.api.RecipeTemplate;
import uristqwerty.CraftGuide.api.Slot;
import uristqwerty.CraftGuide.api.SlotType;

public class FishingRodBaitingRecipes extends CraftGuideAPIObject implements RecipeProvider {
	private ItemStack oyster = new ItemStack(FCBetterThanWolves.fcItemCreeperOysters);
	private ItemStack wing = new ItemStack(FCBetterThanWolves.fcItemBatWing);
	private ItemStack wart = new ItemStack(FCBetterThanWolves.fcItemWitchWart);
	private ItemStack eye = new ItemStack(Item.spiderEye);
	private ItemStack flesh = new ItemStack(Item.rottenFlesh);

	@Override
	public void generateRecipes(RecipeGenerator generator) {
		Slot[] slots = createSlots();
		RecipeTemplate template = generator.createRecipeTemplate(slots, null).setSize(4 * 18 + 6, 2 * 18 + 6);
		
		for (ItemStack bait : new ItemStack[] {oyster, wing, wart, eye, flesh}) {
			ItemStack[] crafting = new ItemStack[slots.length];
			
			crafting[0] = new ItemStack(Item.fishingRod);
			crafting[1] = bait;
			crafting[4] = new ItemStack(FCBetterThanWolves.fcItemFishingRodBaited);
			
			generator.addRecipe(template, crafting);
		}
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
