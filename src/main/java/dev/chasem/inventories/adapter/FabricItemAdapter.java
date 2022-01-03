package dev.chasem.inventories.adapter;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import gg.inventories.adapters.items.ItemAdapter;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtil;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;

import java.util.List;
import java.util.Map;

public class FabricItemAdapter extends ItemAdapter<ItemStack> {

    static JsonObject airJson = new JsonObject();

    static {
        airJson.addProperty("type", "ignore?");
        airJson.addProperty("unlocalizedName", Registry.ITEM.getId(Items.AIR).toString());
        airJson.addProperty("source", Registry.ITEM.getId(Items.AIR).getNamespace());
        airJson.addProperty("itemName", Registry.ITEM.getId(Items.AIR).getPath());
    }

    @Override
    public JsonObject toJson(ItemStack stack) {
        JsonObject itemJson = new JsonObject();

        // Material type
        itemJson.addProperty("type", "ignore");

        itemJson.addProperty("unlocalizedName", Registry.ITEM.getId(stack.getItem()).toString());
        itemJson.addProperty("source", Registry.ITEM.getId(stack.getItem()).getNamespace());
        itemJson.addProperty("itemName", Registry.ITEM.getId(stack.getItem()).getPath());

        if (stack.getItem() == Items.AIR) {
            return itemJson;
        }

        if (stack.getDamage() > 0) {
            itemJson.addProperty("data", stack.getDamage());
            itemJson.addProperty("durability", stack.getDamage());
        }

        if (stack.getCount() != 1) {
            itemJson.addProperty("amount", stack.getCount());
        }

        if (stack.getMaxDamage() > 0) {
            itemJson.addProperty("maxDurability", stack.getMaxDamage());
        }
//        if (stack.hasItemMeta()) {
        JsonObject metaJson = new JsonObject();

//        ItemMeta meta = stack.getItemMeta();

        if (stack.hasCustomName()) {
            String displayName = stack.getName().getString();
            itemJson.addProperty("displayName", displayName);
        }

        //TODO ?
//        if (meta.hasLocalizedName()) {
//            metaJson.addProperty("localizedName", meta.getLocalizedName());
//        }

        List<String> loreList = getLore(stack);

        if (loreList.size() > 0) {
            JsonArray lore = new JsonArray();
            loreList.forEach(line -> lore.add(new JsonPrimitive(line)));
            metaJson.add("lore", lore);
        }

        if (stack.hasEnchantments()) {
            JsonArray enchants = new JsonArray();
            //this.stackTagCompound != null ? this.stackTagCompound.getTagList("ench", 10) : new NBTTagList()

            Map<Enchantment, Integer> enchantMap = EnchantmentHelper.get(stack);
            for (Enchantment enchantment : enchantMap.keySet()) {
                String enchant = enchantment.getName(enchantMap.get(enchantment).intValue()).getString();
                enchants.add(new JsonPrimitive(enchant));
            }

            metaJson.add("enchants", enchants);
        }

        // TODO FLAGS
//        stack.getItem().
//        if (!meta.getItemFlags().isEmpty()) {
//            JsonArray flags = new JsonArray();
//
//            meta.getItemFlags().forEach(itemFlag -> flags.add(new JsonPrimitive(itemFlag.name())));
//
//            metaJson.add("flags", flags);
//        }

        if (stack.getItem() == Items.PLAYER_HEAD) {
//            ItemHead skull = ((ItemSkull) stack.getItem());
            //TODO SKULL.
//            if (skullMeta.hasOwner()) {
//                JsonObject skullData = new JsonObject();
//                skullData.addProperty("owner", skullMeta.getOwner());
//                skullData.addProperty("metaType", "SKULL");
//
//                metaJson.add("extraMeta", skullData);
//            }
        } else if (stack.getItem() == Items.BLACK_BANNER) {

//          TODO
//            if (bannerMeta.numberOfPatterns() > 0) {
//                //TODO:
//            }

        } else if (stack.getItem() == Items.ENCHANTED_BOOK) {

            JsonObject esData = new JsonObject();

            esData.addProperty("metaType", "ENCHANTMENT_STORAGE");
            JsonArray enchants = new JsonArray();
            Map<Enchantment, Integer> enchantMap = EnchantmentHelper.get(stack);
            for (Enchantment enchantment : enchantMap.keySet()) {
                String enchant = enchantment.getName(enchantMap.get(enchantment).intValue()).getString();
                enchants.add(new JsonPrimitive(enchant));
            }

            metaJson.add("enchants", enchants);

            esData.add("storedEnchants", enchants);

            metaJson.add("extraMeta", esData);

//        }
//        else if (meta instanceof BookMeta bookMeta) {
//
//            JsonObject bookData = new JsonObject();
//            bookData.addProperty("metaType", "BOOK_META");
//
//            if (bookMeta.hasAuthor() || bookMeta.hasPages() || bookMeta.hasTitle()) {
//                if (bookMeta.hasTitle()) {
//                    bookData.addProperty("title", bookMeta.getTitle());
//                }
//
//                if (bookMeta.hasAuthor()) {
//                    bookData.addProperty("author", bookMeta.getAuthor());
//                }
//
//                if (bookMeta.hasPages()) {
//                    JsonArray pages = new JsonArray();
//                    bookMeta.getPages().forEach(str -> pages.add(new JsonPrimitive(str)));
//                    bookData.add("pages", pages);
//                }
//            }
//
//            metaJson.add("extraMeta", bookData);
//
//        } else if (meta instanceof FireworkMeta) {
//
//        } else if (meta instanceof FireworkEffectMeta) {

        } else if (stack.getItem() == Items.POTION || stack.getItem() == Items.SPLASH_POTION || stack.getItem() == Items.LINGERING_POTION) {
            JsonObject potionData = new JsonObject();
            potionData.addProperty("metaType", "POTION_META");

            Potion potion = PotionUtil.getPotion(stack);

            boolean potionUpgraded = false;
            for (StatusEffectInstance effect : potion.getEffects()) {
                System.out.println("POTION : " + effect.getAmplifier());
                if (effect.getAmplifier() > 1) {
                    potionUpgraded = true;
                    break;
                }
            }

            potionData.addProperty("potionType", potion.finishTranslationKey(""));
            potionData.addProperty("potionLevel", potionUpgraded ? 2 : 1);

            // TODO
//            if (potionMeta.hasCustomEffects()) {
//                JsonArray customEffects = new JsonArray();
//                potionMeta.getCustomEffects().forEach(potionEffect -> {
//                    customEffects.add(new JsonPrimitive(potionEffect.getType().getName()
//                            + ":" + potionEffect.getAmplifier()
//                            + ":" + potionEffect.getDuration() / 20));
//                });
//
//                potionData.add("customEffects", customEffects);
//            }

            metaJson.add("extraMeta", potionData);
//        } else if (meta instanceof MapMeta) {
//
//        } else if (meta instanceof CrossbowMeta) {
//
//        } else if (meta instanceof TropicalFishBucketMeta) {
//
//        } else if (meta instanceof SpawnEggMeta) {
//
//        } else if (meta instanceof LeatherArmorMeta) {

        } else if (Registry.ITEM.getId(stack.getItem()).getPath().contains("shulker")) {
            JsonObject blockStateJson = new JsonObject();

            blockStateJson.addProperty("metaType", "SHULKER_BOX");
            JsonArray shulkerJson = new JsonArray();
            NbtCompound nbt = stack.getNbt();
            if (nbt.contains("BlockEntityTag")) {
                NbtCompound blocktag = stack.getNbt().getCompound("BlockEntityTag");
                ShulkerBoxBlockEntity box = new ShulkerBoxBlockEntity(DyeColor.BLACK, new BlockPos(0, 0, 0), Blocks.SHULKER_BOX.getDefaultState());
                box.readInventoryNbt(blocktag);

                for (int i = 0; i < box.size(); i++) {
                    ItemStack shulkerStack = box.getStack(i);
                    if (shulkerStack == null) {
                        shulkerJson.add(airJson);
                    } else {
                        shulkerJson.add(this.toJson(shulkerStack));
                    }
                }

            }
            blockStateJson.add("inventory", shulkerJson);

            metaJson.add("extraMeta", blockStateJson);
        }
        itemJson.add("itemMeta", metaJson);
//        }

        if (!itemJson.has("displayName")) {
            String displayName = stack.getName().getString();
            itemJson.addProperty("displayName", displayName);
        }

        //TODO: Item json

        return itemJson;
    }

    public List<String> getLore(ItemStack stack) {
        ImmutableList.Builder<String> loreBuilder = ImmutableList.builder();
        if (stack.hasNbt() && stack.getNbt().contains("Lore")) {
            NbtList loreNbt = stack.getNbt().getList("Lore", 8);
            for (int i = 0; i < loreNbt.size(); i++) {
                NbtElement currentLore = loreNbt.get(i);
                loreBuilder.add(currentLore.asString());
            }
        }
        List<String> lore = loreBuilder.build();

        return lore;
    }
}
