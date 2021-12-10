package uristqwerty.CraftGuide.recipes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import net.minecraft.src.FCBetterThanWolves;
import net.minecraft.src.FCCraftingManagerCauldron;
import net.minecraft.src.FCCraftingManagerCauldronStoked;
import net.minecraft.src.FCCraftingManagerCrucible;
import net.minecraft.src.FCCraftingManagerCrucibleStoked;
import net.minecraft.src.FCCraftingManagerMillStone;
import net.minecraft.src.ItemStack;

public class BTWRecipes {
	// Used for stoked fire recipes
	ItemStack bellows = new ItemStack(FCBetterThanWolves.fcBellows);
	ItemStack hibachi = new ItemStack(FCBetterThanWolves.fcBBQ);
	
	ItemStack millStone = new ItemStack(FCBetterThanWolves.fcMillStone);
	ItemStack cauldron = new ItemStack(FCBetterThanWolves.fcCauldron);
	ItemStack crucible = new ItemStack(FCBetterThanWolves.fcCrucible);
	
	public BTWRecipes() {
		new BulkRecipes(1, 1, millStone, FCCraftingManagerMillStone.getInstance(), 13);
		new BulkRecipes(2, 1, cauldron, FCCraftingManagerCauldron.getInstance());
		new BulkRecipes(2, 1, new ItemStack[] {bellows, cauldron, hibachi}, FCCraftingManagerCauldronStoked.getInstance());
		new BulkRecipes(2, 1, crucible, FCCraftingManagerCrucible.getInstance());
		new BulkRecipes(2, 1, new ItemStack[] {bellows, crucible, hibachi}, FCCraftingManagerCrucibleStoked.getInstance());
		new AnvilRecipes();
		new HopperFilterRecipes();
		new KilnRecipes();
		new PistonPackingRecipes();
		new SawRecipes();
	}
	
	/*
	 * Groups together items of the same type and damage.
	 */
	public static void condenseItemStackList(List<ItemStack> list) {
		if (list.size() <= 1) {
			return;
		}
		
		// Hash items without their stackSize, which we count separately and add as the value.
		HashMap<List<Integer>, Integer> hash = new HashMap();
		for (ItemStack itemStack : list) {
			List<Integer> item = new ArrayList(2);
			item.add(0, itemStack.itemID);
			item.add(1, itemStack.getItemDamage());
			int stackSize = itemStack.stackSize;
			if (hash.containsKey(item)) {
				stackSize += hash.get(item);
			}
			hash.put(item, stackSize);
		}
		list.clear();
		
		// Go through the hashed keys and create an ItemStack with the calculated stackSize.
		Iterator<Entry<List<Integer>, Integer>> iterator = hash.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<List<Integer>, Integer> entry = iterator.next();
			List<Integer> item = entry.getKey();
			list.add(new ItemStack(item.get(0), entry.getValue(), item.get(1)));
		}
	}
}
