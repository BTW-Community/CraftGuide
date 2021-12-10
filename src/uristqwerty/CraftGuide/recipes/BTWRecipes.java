package uristqwerty.CraftGuide.recipes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;

public class BTWRecipes {
	public BTWRecipes() {
		new MillStoneRecipes();
		new CauldronRecipes();
		new CauldronStokedRecipes();
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
