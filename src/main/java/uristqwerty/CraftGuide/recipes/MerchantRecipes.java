package uristqwerty.CraftGuide.recipes;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import btw.entity.mob.villager.VillagerEntity;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityList;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Item;
import net.minecraft.src.MerchantRecipe;
import net.minecraft.src.World;
import uristqwerty.CraftGuide.api.CraftGuideAPIObject;
import uristqwerty.CraftGuide.api.RecipeGenerator;
import uristqwerty.CraftGuide.api.RecipeProvider;
import uristqwerty.CraftGuide.api.RecipeTemplate;
import uristqwerty.CraftGuide.api.Slot;

public class MerchantRecipes extends CraftGuideAPIObject implements RecipeProvider {
	private Set<Integer> professions = VillagerEntity.professionMap.keySet();
	
	private Map<Integer, VillagerEntity.WeightedMerchantEntry> defaultTradeByProfessionList = VillagerEntity.defaultTradeByProfessionList;
	
	@Override
	public void generateRecipes(RecipeGenerator generator) {
		for (int profession : professions) {
			Set<VillagerEntity.WeightedMerchantEntry> entries = VillagerEntity.tradeByProfessionList.get(profession);
			for (VillagerEntity.WeightedMerchantEntry entry : entries) {
				addEntryRecipe(generator, entry, profession);
			}
			
			for (int level = 1; level < 5; level++) {
				VillagerEntity.WeightedMerchantEntry entry = VillagerEntity.levelUpTradeByProfessionList.get(profession).get(level);
				addEntryRecipe(generator, entry, profession);
			}
		}
	}
	
	private void addEntryRecipe(RecipeGenerator generator, VillagerEntity.WeightedMerchantEntry entry, int profession) {
		if (entry == null) return;
		
		Slot[] slots = BTWRecipes.createSlots(2, 1, 1, false);
		RecipeTemplate template = generator.createRecipeTemplate(slots, new ItemStack(Item.emerald)).setSize(3 * 18 + 6, 2 * 18 + 4);
		
		try {
			MerchantRecipe recipe = entry.generateRecipe(new Random());
			ItemStack[] crafting = new ItemStack[slots.length];
			
			crafting[0] = recipe.getItemToBuy();
			crafting[1] = recipe.getSecondItemToBuy();
			crafting[2] = new ItemStack(Item.monsterPlacer, 1, 600 + profession);
			crafting[3] = recipe.getItemToSell();
			
			generator.addRecipe(template, crafting);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}
	}
}
