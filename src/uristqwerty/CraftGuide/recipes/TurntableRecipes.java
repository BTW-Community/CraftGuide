package uristqwerty.CraftGuide.recipes;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;

import net.minecraft.src.Block;
import net.minecraft.src.BlockSand;
import net.minecraft.src.FCBetterThanWolves;
import net.minecraft.src.FCBlockUnfiredPottery;
import net.minecraft.src.FCCraftingManagerSaw;
import net.minecraft.src.FCCraftingManagerSawRecipe;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import uristqwerty.CraftGuide.api.CraftGuideAPIObject;
import uristqwerty.CraftGuide.api.ItemSlot;
import uristqwerty.CraftGuide.api.RecipeGenerator;
import uristqwerty.CraftGuide.api.RecipeProvider;
import uristqwerty.CraftGuide.api.RecipeTemplate;
import uristqwerty.CraftGuide.api.Slot;
import uristqwerty.CraftGuide.api.SlotType;

public class TurntableRecipes extends CraftGuideAPIObject implements RecipeProvider {
	private ItemStack turntable = new ItemStack(FCBetterThanWolves.fcTurntable);

	@Override
	public void generateRecipes(RecipeGenerator generator) {
		Slot[] slots = BTWRecipes.createSlots(1, 1, 2);
		RecipeTemplate template = generator.createRecipeTemplate(slots, turntable).setSize(3 * 18 + 6, 2 * 18 + 6);
		
		generator.addRecipe(template, new ItemStack[] {
			new ItemStack(FCBetterThanWolves.fcBlockUnfiredClay),
			turntable,
			new ItemStack(FCBetterThanWolves.fcUnfiredPottery, 1, FCBlockUnfiredPottery.m_iSubtypeCrucible),
			new ItemStack(Item.clay),
		});
		generator.addRecipe(template, new ItemStack[] {
			new ItemStack(FCBetterThanWolves.fcUnfiredPottery, 1, FCBlockUnfiredPottery.m_iSubtypeCrucible),
			turntable,
			new ItemStack(FCBetterThanWolves.fcUnfiredPottery, 1, FCBlockUnfiredPottery.m_iSubtypePlanter),
			new ItemStack(Item.clay, 2),
		});
		generator.addRecipe(template, new ItemStack[] {
			new ItemStack(FCBetterThanWolves.fcUnfiredPottery, 1, FCBlockUnfiredPottery.m_iSubtypePlanter),
			turntable,
			new ItemStack(FCBetterThanWolves.fcUnfiredPottery, 1, FCBlockUnfiredPottery.m_iSubtypeVase),
			new ItemStack(Item.clay, 2),
		});
		generator.addRecipe(template, new ItemStack[] {
			new ItemStack(FCBetterThanWolves.fcUnfiredPottery, 1, FCBlockUnfiredPottery.m_iSubtypeVase),
			turntable,
			new ItemStack(FCBetterThanWolves.fcUnfiredPottery, 1, FCBlockUnfiredPottery.m_iSubtypeUrn),
			new ItemStack(Item.clay, 2),
		});
	}
}
