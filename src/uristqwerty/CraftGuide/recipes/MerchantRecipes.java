package uristqwerty.CraftGuide.recipes;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import net.minecraft.src.Entity;
import net.minecraft.src.EntityList;
import net.minecraft.src.FCEntityVillager;
import net.minecraft.src.ItemStack;
import net.minecraft.src.FCEntityVillager.WeightedMerchantEntry;
import net.minecraft.src.Item;
import net.minecraft.src.MerchantRecipe;
import net.minecraft.src.World;
import uristqwerty.CraftGuide.api.CraftGuideAPIObject;
import uristqwerty.CraftGuide.api.RecipeGenerator;
import uristqwerty.CraftGuide.api.RecipeProvider;
import uristqwerty.CraftGuide.api.RecipeTemplate;
import uristqwerty.CraftGuide.api.Slot;

public class MerchantRecipes extends CraftGuideAPIObject implements RecipeProvider {
	private Set<Integer> professions = FCEntityVillager.professionMap.keySet();
	
	private Map<Integer, Set<WeightedMerchantEntry>> tradeByProfessionList = FCEntityVillager.tradeByProfessionList;
	private Map<Integer, Map<Integer, WeightedMerchantEntry>> levelUpTradeByProfessionList = FCEntityVillager.levelUpTradeByProfessionList;
	private Map<Integer, WeightedMerchantEntry> defaultTradeByProfessionList = FCEntityVillager.defaultTradeByProfessionList;
	
	@Override
	public void generateRecipes(RecipeGenerator generator) {
		Slot[] slots = BTWRecipes.createSlots(2, 1, 1, false);
		RecipeTemplate template = generator.createRecipeTemplate(slots, null).setSize(3 * 18 + 6, 2 * 18 + 4);
		
		for (int profession : professions) {
			Set<WeightedMerchantEntry> entries = tradeByProfessionList.get(profession);
			for (WeightedMerchantEntry entry : entries) {
				try {
					ItemStack[] crafting = new ItemStack[slots.length];
					MerchantRecipe recipe = entry.generateRecipe(new Random());
					
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
	}
}
