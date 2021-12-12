package uristqwerty.CraftGuide.recipes;

import java.util.List;

import net.minecraft.src.Block;
import net.minecraft.src.CraftingManager;
import net.minecraft.src.FCBetterThanWolves;
import net.minecraft.src.FCRecipesLogChopping;
import net.minecraft.src.IRecipe;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import uristqwerty.CraftGuide.api.CraftGuideAPIObject;
import uristqwerty.CraftGuide.api.ItemSlot;
import uristqwerty.CraftGuide.api.RecipeGenerator;
import uristqwerty.CraftGuide.api.RecipeProvider;
import uristqwerty.CraftGuide.api.RecipeTemplate;
import uristqwerty.CraftGuide.api.Slot;
import uristqwerty.CraftGuide.api.SlotType;

public class LogChoppingRecipes extends CraftGuideAPIObject implements RecipeProvider {
	private ItemStack axeStone = new ItemStack(Item.axeStone);
	private ItemStack axeGold = new ItemStack(Item.axeGold);
	
	private ItemStack axeIron = new ItemStack(Item.axeIron);
	private ItemStack axeDiamond = new ItemStack(Item.axeDiamond);
	private ItemStack axeRefined = new ItemStack(FCBetterThanWolves.fcItemRefinedAxe);
	private ItemStack axeBattle = new ItemStack(FCBetterThanWolves.fcItemBattleAxe);

	@Override
	public void generateRecipes(RecipeGenerator generator) {
		Slot[] slots = createSlots();
		RecipeTemplate template = generator.createRecipeTemplate(slots, null).setSize(5 * 18 + 6, 3 * 18 + 6);
		
		// Low quality axe.
		for (ItemStack axe : new ItemStack[] {axeStone, axeGold}) {
			// Vanilla logs.
			for (int i = 0; i < 4; i++) {
				ItemStack[] crafting = new ItemStack[slots.length];
				
				crafting[0] = axe;
				crafting[1] = new ItemStack(Block.wood, 1, i);
				crafting[4] = new ItemStack(Item.stick, 2);
				crafting[5] = new ItemStack(FCBetterThanWolves.fcItemSawDust, 4);
				crafting[6] = new ItemStack(FCBetterThanWolves.fcItemBark, 1, i);
				
				generator.addRecipe(template, crafting);
			}
			
			// Blood wood log.
			ItemStack[] crafting = new ItemStack[slots.length];
			
			crafting[0] = axe;
			crafting[1] = new ItemStack(FCBetterThanWolves.fcBloodWood);
			crafting[4] = new ItemStack(Item.stick, 2);
			crafting[5] = new ItemStack(FCBetterThanWolves.fcItemSawDust, 3);
			crafting[6] = new ItemStack(FCBetterThanWolves.fcItemSoulDust);
			crafting[7] = new ItemStack(FCBetterThanWolves.fcItemBark, 1, 4);
			
			generator.addRecipe(template, crafting);
		}
		
		// High quality axe.
		for (ItemStack axe : new ItemStack[] {axeIron, axeDiamond, axeRefined, axeBattle}) {
			// Vanilla logs
			for (int i = 0; i < 4; i++) {
				ItemStack[] crafting = new ItemStack[slots.length];
				
				crafting[0] = axe;
				crafting[1] = new ItemStack(Block.wood, 1, i);
				crafting[4] = new ItemStack(Block.planks, 2, i);
				crafting[5] = new ItemStack(FCBetterThanWolves.fcItemSawDust, 2);
				crafting[6] = new ItemStack(FCBetterThanWolves.fcItemBark, 1, i);
				
				generator.addRecipe(template, crafting);
			}
			
			// Blood wood log.
			ItemStack[] crafting = new ItemStack[slots.length];
			
			crafting[0] = axe;
			crafting[1] = new ItemStack(FCBetterThanWolves.fcBloodWood);
			crafting[4] = new ItemStack(Block.planks, 2, 4);
			crafting[5] = new ItemStack(FCBetterThanWolves.fcItemSawDust);
			crafting[6] = new ItemStack(FCBetterThanWolves.fcItemSoulDust);
			crafting[7] = new ItemStack(FCBetterThanWolves.fcItemBark, 1, 4);
			
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
			new ItemSlot(59, 3, 16, 16, true).setSlotType(SlotType.OUTPUT_SLOT),
			new ItemSlot(59, 21, 16, 16, true).setSlotType(SlotType.OUTPUT_SLOT),
			new ItemSlot(59, 39, 16, 16, true).setSlotType(SlotType.OUTPUT_SLOT),
			new ItemSlot(77, 3, 16, 16, true).setSlotType(SlotType.OUTPUT_SLOT),
		};
	}
}
