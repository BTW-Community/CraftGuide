package uristqwerty.CraftGuide.recipes;

import btw.entity.mob.villager.VillagerEntity;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.MerchantRecipe;
import uristqwerty.CraftGuide.api.*;

import java.util.Map;
import java.util.Random;
import java.util.Set;

public class MerchantRecipes extends CraftGuideAPIObject implements RecipeProvider {
    private final Set<Integer> professions = VillagerEntity.professionMap.keySet();
    private final Map<Integer, VillagerEntity.WeightedMerchantEntry> defaultTradeByProfessionList = VillagerEntity.defaultTradeByProfessionList;

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

        Slot[] slots = BTWRecipes.createSlots(2, 1, 1);
        RecipeTemplate template = generator.createRecipeTemplate(slots, new ItemStack(Item.emerald)).setSize(3 * 18 + 6, 2 * 18 + 4);

        try {
            ItemStack[] crafting = new ItemStack[slots.length];

            MerchantRecipe recipe = entry.generateRecipe(new Random());

            ItemStack itemBuy = recipe.getItemToBuy();
            ItemStack itemSecondBuy = recipe.getSecondItemToBuy();
            ItemStack itemSell = recipe.getItemToSell();

            if (entry instanceof VillagerEntity.WeightMerchantEnchantmentEntry) {
                VillagerEntity.WeightMerchantEnchantmentEntry enchantmentEntry = (VillagerEntity.WeightMerchantEnchantmentEntry) entry;
                itemSecondBuy = itemStack(itemSecondBuy, enchantmentEntry.maxEmeraldCount);
            } else if (entry instanceof VillagerEntity.WeightedMerchantRecipeEntry) {
                VillagerEntity.WeightedMerchantRecipeEntry recipeEntry = (VillagerEntity.WeightedMerchantRecipeEntry) entry;
                itemBuy = itemStack(itemBuy, recipeEntry.input1MaxCount);
                itemSecondBuy = itemStack(itemSecondBuy, recipeEntry.input2MaxCount);
                itemSell = itemStack(itemSell, recipeEntry.resultMaxCount);
            }

            crafting[0] = itemBuy;
            crafting[1] = itemSecondBuy;
            crafting[2] = new ItemStack(Item.monsterPlacer, Math.abs(entry.level), 600 + profession);
            crafting[3] = itemSell;

            generator.addRecipe(template, crafting);
        } catch (IllegalArgumentException | SecurityException e) {
            e.printStackTrace();
        }
	}

    private ItemStack itemStack(ItemStack itemStack, int size) {
        return itemStack == null ? null : new ItemStack(itemStack.getItem(), size, itemStack.getItemDamage());
    }
}
