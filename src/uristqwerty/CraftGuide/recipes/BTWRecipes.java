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
import uristqwerty.CraftGuide.api.ItemSlot;
import uristqwerty.CraftGuide.api.Slot;
import uristqwerty.CraftGuide.api.SlotType;

public class BTWRecipes {
	// Used for stoked fire recipes
	ItemStack bellows = new ItemStack(FCBetterThanWolves.fcBellows);
	ItemStack hibachi = new ItemStack(FCBetterThanWolves.fcBBQ);
	
	ItemStack millStone = new ItemStack(FCBetterThanWolves.fcMillStone);
	ItemStack cauldron = new ItemStack(FCBetterThanWolves.fcCauldron);
	ItemStack crucible = new ItemStack(FCBetterThanWolves.fcCrucible);
	
	public BTWRecipes() {
		new BulkRecipes(FCCraftingManagerMillStone.getInstance(), millStone);
		new BulkRecipes(FCCraftingManagerCauldron.getInstance(), cauldron);
		new BulkRecipes(FCCraftingManagerCauldronStoked.getInstance(), 1, bellows, cauldron, hibachi);
		new BulkRecipes(FCCraftingManagerCrucible.getInstance(), crucible);
		new BulkRecipes(FCCraftingManagerCrucibleStoked.getInstance(), 1, bellows, crucible, hibachi);
		
		new AnvilRecipes();
		new HopperFilterRecipes();
		new KilnRecipes();
		new PistonPackingRecipes();
		new SawRecipes();
		new TurntableRecipes();
		
		new LogChoppingRecipes();
		new FishingRodBaitingRecipes();
		new KnittingRecipes();
		
		new MerchantRecipes();
	}
	
	public static Slot[] createSlots(int inputSize, int machineH, int outputSize, boolean drawQuantity) {
		int inputW = (int) Math.ceil(inputSize / 3.0);
		int inputH = Math.min(inputSize, 3);
		int inputArea = inputW * inputH;
		
		int outputW = (int) Math.ceil(outputSize / 3.0);
		int outputH = Math.min(outputSize, 3);
		int outputArea = outputW * outputH;
		
		Slot[] slots = new ItemSlot[inputArea + machineH + outputArea];
		
		int maxHeight = Math.max(Math.max(inputH, outputH), machineH);
		
		int inputShift = (maxHeight - inputH) * 9;
		int outputShift = (maxHeight - outputH) * 9;
		int machineShift = (maxHeight - machineH) * 9;
		
		for (int col = 0; col < inputW; col++) {
			for (int row = 0; row < inputH; row++) {
				slots[inputH * col + row] = new ItemSlot(col * 18 + 3, row * 18 + 3 + inputShift, 16, 16, drawQuantity).drawOwnBackground();
			}
		}
		for (int row = 0; row < machineH; row++) {
			slots[inputArea + row] = new ItemSlot(inputW * 18 + 3, row * 18 + 3 + machineShift, 16, 16).setSlotType(SlotType.MACHINE_SLOT);
		}
		for (int col = 0; col < outputW; col++) {
			for (int row = 0; row < outputH; row++) {
				slots[inputArea + machineH + outputH * col + row] = new ItemSlot((inputW + 1 + col) * 18 + 3, row * 18 + 3 + outputShift, 16, 16, drawQuantity).drawOwnBackground().setSlotType(SlotType.OUTPUT_SLOT);
			}
		}
		
		return slots;
	}
	
	public static Slot[] createSlots(int inputSize, int machineH, int outputSize) {
		return createSlots(inputSize, machineH, outputSize, true);
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
