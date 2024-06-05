//
// Created by BONNe
// Copyright - 2023
//


package lv.id.bonne.vaulthunters.jewelsorting.utils;


import org.jetbrains.annotations.Nullable;
import java.util.*;

import iskallia.vault.VaultMod;
import iskallia.vault.gear.VaultGearState;
import iskallia.vault.gear.attribute.VaultGearAttribute;
import iskallia.vault.gear.attribute.VaultGearModifier;
import iskallia.vault.gear.data.AttributeGearData;
import iskallia.vault.gear.data.GearDataCache;
import iskallia.vault.gear.data.VaultGearData;
import iskallia.vault.gear.trinket.TrinketEffect;
import iskallia.vault.init.ModGearAttributes;
import iskallia.vault.init.ModItems;
import iskallia.vault.item.crystal.CrystalData;
import iskallia.vault.item.data.InscriptionData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;


/**
 * This class helps to sort Jewel items in correct order.
 */
public class SortingHelper
{
    /**
     * Indicates if item with given id has custom sorting.
     * @param id The id of item.
     * @return {@code true} if sortable, otherwise {@code false}
     */
    public static boolean isSortable(ResourceLocation id)
    {
        return SortingHelper.VAULT_CHARMS.contains(id) ||
            SortingHelper.VAULT_GEAR_SET.contains(id) ||
            SortingHelper.CUSTOM_SORTING.contains(id);
    }


    /**
     * This method compares two given registry names.
     * @param leftReg The left registry name.
     * @param rightReg The right registry name.
     * @param ascending The ascending or descending order.
     * @return The comparison of two given registry names.
     */
    public static int compareRegistryNames(ResourceLocation leftReg,
        ResourceLocation rightReg,
        boolean ascending)
    {
        String leftName = VAULT_GEAR_SET.contains(leftReg) ?
            VaultMod.sId("gear") :
            VAULT_CHARMS.contains(leftReg) ?
                VaultMod.sId("charm"): leftReg.toString();

        String rightName = VAULT_GEAR_SET.contains(rightReg) ?
            VaultMod.sId("gear") :
            VAULT_CHARMS.contains(rightReg) ?
                VaultMod.sId("charm"): rightReg.toString();

        return ascending ?
            SortingHelper.compareString(leftName, rightName) :
            SortingHelper.compareString(rightName, leftName);
    }


    /**
     * This method compares two given jewels by their sorting order.
     *
     * @param leftName the left name
     * @param leftData the left data
     * @param rightName the right name
     * @param rightData the right data
     * @param sortingOrder the sorting order
     * @param ascending the ascending
     * @return the comparison of two given jewels.
     */
    public static int compareJewels(
        String leftName,
        VaultGearData leftData,
        String rightName,
        VaultGearData rightData,
        List<JewelOptions> sortingOrder,
        boolean ascending)
    {
        // Get all affixes for left item.
        List<VaultGearModifier<?>> leftAffixes = new ArrayList<>();
        leftAffixes.addAll(leftData.getModifiers(VaultGearModifier.AffixType.PREFIX));
        leftAffixes.addAll(leftData.getModifiers(VaultGearModifier.AffixType.SUFFIX));

        // Get all affixes for right item.
        List<VaultGearModifier<?>> rightAffixes = new ArrayList<>();
        rightAffixes.addAll(rightData.getModifiers(VaultGearModifier.AffixType.PREFIX));
        rightAffixes.addAll(rightData.getModifiers(VaultGearModifier.AffixType.SUFFIX));

        int returnValue = 0;

        // Get compared affix.
        VaultGearAttribute<?> leftAttribute = leftAffixes.size() == 1 ? leftAffixes.get(0).getAttribute() : null;
        VaultGearAttribute<?> rightAttribute = rightAffixes.size() == 1 ? rightAffixes.get(0).getAttribute() : null;

        for (int i = 0, sortingOrderSize = sortingOrder.size(); returnValue == 0 && i < sortingOrderSize; i++)
        {
            JewelOptions sortOptions = sortingOrder.get(i);

            returnValue = switch (sortOptions) {
                case NAME -> SortingHelper.compareString(leftName, rightName);
                case ATTRIBUTE -> SortingHelper.compareAttribute(leftAttribute, rightAttribute);
                case ATTRIBUTE_VALUE -> SortingHelper.compareAttributeValue(leftData, leftAttribute, rightData, rightAttribute);
                case SIZE -> SortingHelper.compareSizeAttribute(leftData, rightData);
                case ATTRIBUTE_WEIGHT -> SortingHelper.compareAttributeValueWeight(leftData, leftAttribute, rightData, rightAttribute);
                case LEVEL -> SortingHelper.compareLevel(leftData, rightData);
            };
        }

        return ascending ? returnValue : -returnValue;
    }


    /**
     * This method compares two given jewels by their sorting order.
     *
     * @param leftName the left name
     * @param leftData the left data
     * @param rightName the right name
     * @param rightData the right data
     * @param sortingOrder the sorting order
     * @param ascending the ascending
     * @return the comparison of two given jewels.
     */
    public static int compareJewels(
        String leftName,
        GearDataCache leftData,
        String rightName,
        GearDataCache rightData,
        List<JewelOptions> sortingOrder,
        boolean ascending)
    {
        int returnValue = 0;

        IExtraGearDataCache leftExtraCache = (IExtraGearDataCache) leftData;
        IExtraGearDataCache rightExtraCache = (IExtraGearDataCache) rightData;

        for (int i = 0, sortingOrderSize = sortingOrder.size(); returnValue == 0 && i < sortingOrderSize; i++)
        {
            JewelOptions sortOptions = sortingOrder.get(i);

            returnValue = switch (sortOptions) {
                case NAME -> SortingHelper.compareString(leftName, rightName);
                case ATTRIBUTE -> SortingHelper.compareIntegerValue(leftExtraCache.getExtraFirstAttributeIndex(),
                    rightExtraCache.getExtraFirstAttributeIndex());
                case ATTRIBUTE_VALUE -> SortingHelper.compareDoubleValue(leftExtraCache.getExtraFirstAttributeValue(),
                    rightExtraCache.getExtraFirstAttributeValue());
                case SIZE -> SortingHelper.compareIntegerValue(leftExtraCache.getExtraJewelSize(),
                    rightExtraCache.getExtraJewelSize());
                case ATTRIBUTE_WEIGHT -> SortingHelper.compareAttributeValueWeight(leftExtraCache, rightExtraCache);
                case LEVEL -> SortingHelper.compareIntegerValue(leftExtraCache.getExtraGearLevel(),
                    rightExtraCache.getExtraGearLevel());
            };
        }

        return ascending ? returnValue : -returnValue;
    }


    /**
     * This method compares two given jewels by their sorting order.
     *
     * @param leftName the left name
     * @param leftData the left data
     * @param rightName the right name
     * @param rightData the right data
     * @param sortingOrder the sorting order
     * @param ascending the ascending
     * @return the comparison of two given jewels.
     */
    public static int compareJewels(
        String leftName,
        CompoundTag leftData,
        String rightName,
        CompoundTag rightData,
        List<JewelOptions> sortingOrder,
        boolean ascending)
    {
        int returnValue = 0;
        for (int i = 0, sortingOrderSize = sortingOrder.size(); returnValue == 0 && i < sortingOrderSize; i++)
        {
            JewelOptions sortOptions = sortingOrder.get(i);

            returnValue = switch (sortOptions) {
                case NAME -> SortingHelper.compareString(leftName, rightName);
                case ATTRIBUTE -> SortingHelper.compareIntegerValue(leftData.getInt(EXTRA_ATTRIBUTE_INDEX),
                    rightData.getInt(EXTRA_ATTRIBUTE_INDEX));
                case ATTRIBUTE_VALUE -> SortingHelper.compareDoubleValue(leftData.getDouble(EXTRA_ATTRIBUTE_VALUE),
                    rightData.getDouble(EXTRA_ATTRIBUTE_VALUE));
                case SIZE -> SortingHelper.compareIntegerValue(leftData.getInt(EXTRA_JEWEL_SIZE),
                    rightData.getInt(EXTRA_JEWEL_SIZE));
                case ATTRIBUTE_WEIGHT ->
                {
                    double leftWeight = leftData.getDouble(EXTRA_ATTRIBUTE_VALUE) / leftData.getInt(EXTRA_JEWEL_SIZE);
                    double rightWeight = rightData.getDouble(EXTRA_ATTRIBUTE_VALUE) / rightData.getInt(EXTRA_JEWEL_SIZE);

                    yield SortingHelper.compareDoubleValue(leftWeight, rightWeight);
                }
                case LEVEL -> SortingHelper.compareIntegerValue(leftData.getInt(EXTRA_GEAR_LEVEL),
                    rightData.getInt(EXTRA_GEAR_LEVEL));
            };
        }

        return ascending ? returnValue : -returnValue;
    }


    /**
     * This method compares two given vault gear by their sorting order.
     *
     * @param leftName the left name
     * @param leftData the left data
     * @param rightName the right name
     * @param rightData the right data
     * @param sortingOrder the sorting order
     * @param ascending the ascending
     * @return the comparison of two given vault gear items.
     */
    public static int compareVaultGear(String leftName,
        VaultGearData leftData,
        String rightName,
        VaultGearData rightData,
        List<GearOptions> sortingOrder,
        boolean ascending)
    {
        // Start comparing with the current vault gear state.
        int returnValue = 0;

        for (int i = 0, sortingOrderSize = sortingOrder.size(); returnValue == 0 && i < sortingOrderSize; i++)
        {
            GearOptions sortOptions = sortingOrder.get(i);

            returnValue = switch (sortOptions) {
                case NAME -> SortingHelper.compareString(leftName, rightName);
                case STATE -> SortingHelper.compareState(leftData, rightData);
                case RARITY -> SortingHelper.compareRarity(leftData, rightData);
                case LEVEL -> SortingHelper.compareLevel(leftData, rightData);
                case MODEL -> SortingHelper.compareModel(leftData, rightData);
            };
        }

        return ascending ? returnValue : -returnValue;
    }


    /**
     * This method compares two given vault gear by their sorting order.
     *
     * @param leftName the left name
     * @param leftData the left data
     * @param rightName the right name
     * @param rightData the right data
     * @param sortingOrder the sorting order
     * @param ascending the ascending
     * @return the comparison of two given vault gear items.
     */
    public static int compareInscriptions(String leftName,
        InscriptionData leftData,
        String rightName,
        InscriptionData rightData,
        List<InscriptionOptions> sortingOrder,
        boolean ascending)
    {
        // Start comparing with the current vault gear state.
        int returnValue = 0;

        CompoundTag leftTag = leftData.serializeNBT();
        CompoundTag rightTag = rightData.serializeNBT();

        for (int i = 0, sortingOrderSize = sortingOrder.size(); returnValue == 0 && i < sortingOrderSize; i++)
        {
            InscriptionOptions sortOptions = sortingOrder.get(i);

            returnValue = switch (sortOptions) {
                case NAME -> SortingHelper.compareString(leftName, rightName);
                case INSTABILITY -> SortingHelper.compareInstability(leftTag, rightTag);
                case COMPLETION -> SortingHelper.compareCompletion(leftTag, rightTag);
                case TIME -> SortingHelper.compareTime(leftTag, rightTag);
                case ROOMS -> SortingHelper.compareRooms(leftData, rightData);
            };
        }

        return ascending ? returnValue : -returnValue;
    }


    /**
     * This method compares two given infused catalyst by their sorting order.
     *
     * @param leftName the left name
     * @param leftData the left data
     * @param rightName the right name
     * @param rightData the right data
     * @param sortingOrder the sorting order
     * @param ascending the ascending
     * @return the comparison of two given infused catalyst items.
     */
    public static int compareCatalysts(String leftName,
        CompoundTag leftData,
        String rightName,
        CompoundTag rightData,
        List<CatalystOptions> sortingOrder,
        boolean ascending)
    {
        int returnValue = 0;
        for (int i = 0, sortingOrderSize = sortingOrder.size(); returnValue == 0 && i < sortingOrderSize; i++)
        {
            CatalystOptions sortOptions = sortingOrder.get(i);

            returnValue = switch (sortOptions) {
                case NAME -> SortingHelper.compareString(leftName, rightName);
                case SIZE -> SortingHelper.compareIntegerValue(
                    leftData.contains(SIZE) ? leftData.getInt(SIZE) : -1,
                    rightData.contains(SIZE) ? rightData.getInt(SIZE) : -1);
                case MODIFIER -> SortingHelper.compareModifiers(
                    leftData.getList(MODIFIERS, CompoundTag.TAG_STRING),
                    rightData.getList(MODIFIERS, CompoundTag.TAG_STRING));
            };
        }

        return ascending ? returnValue : -returnValue;
    }


    /**
     * This method compares two given vault crystals by their sorting order.
     * @param leftName the left name
     * @param leftData the left data
     * @param rightName the right name
     * @param rightData the right data
     * @param sortingOrder the sorting order
     * @param ascending the ascending
     * @return the comparison of two given vault crystals items.
     */
    public static int compareVaultCrystals(String leftName,
        CrystalData leftData,
        String rightName,
        CrystalData rightData,
        List<CrystalOptions> sortingOrder,
        boolean ascending)
    {
        int returnValue = 0;

        for (int i = 0, sortingOrderSize = sortingOrder.size(); returnValue == 0 && i < sortingOrderSize; i++)
        {
            CrystalOptions sortOptions = sortingOrder.get(i);

            returnValue = switch (sortOptions) {
                case NAME -> SortingHelper.compareString(leftName, rightName);
                case LEVEL -> Integer.compare(leftData.getProperties().getLevel().orElse(-1),
                    rightData.getProperties().getLevel().orElse(-1));
                case TYPE -> SortingHelper.compareString(
                    leftData.getObjective().getClass().getName(),
                    rightData.getObjective().getClass().getName());
            };
        }

        return ascending ? returnValue : -returnValue;
    }


    /**
     * This method compares two given vault trinkets by their sorting order.
     * @param leftName the left name
     * @param leftData the left data
     * @param leftTag the left tag
     * @param rightName the right name
     * @param rightData the right data
     * @param rightTag the right tag
     * @param sortingOrder the sorting order
     * @param ascending the ascending
     * @return the comparison of two given vault trinkets items.
     */
    public static int compareTrinkets(String leftName,
        AttributeGearData leftData,
        CompoundTag leftTag,
        String rightName,
        AttributeGearData rightData,
        CompoundTag rightTag,
        List<TrinketOptions> sortingOrder,
        boolean ascending)
    {
        int returnValue = Boolean.compare(isIdentified(leftData), isIdentified(rightData));

        if (!isIdentified(leftData) && returnValue == 0)
        {
            // Exit fast. Unidentified items are not comparable.
            return 0;
        }

        for (int i = 0, sortingOrderSize = sortingOrder.size(); returnValue == 0 && i < sortingOrderSize; i++)
        {
            TrinketOptions sortOptions = sortingOrder.get(i);

            returnValue = switch (sortOptions) {
                case NAME -> SortingHelper.compareString(leftName, rightName);
                case USES -> Integer.compare(getRemainingUses(leftTag), getRemainingUses(rightTag));
                case TYPE ->
                {
                    Optional<TrinketEffect<?>> leftValue = leftData.getFirstValue(ModGearAttributes.TRINKET_EFFECT);
                    Optional<TrinketEffect<?>> rightValue = rightData.getFirstValue(ModGearAttributes.TRINKET_EFFECT);

                    if (leftValue.isPresent() && rightValue.isPresent())
                    {
                        yield SortingHelper.compareString(
                            leftValue.get().getTrinketConfig().getName(),
                            rightValue.get().getTrinketConfig().getName());
                    }
                    else
                    {
                        yield Boolean.compare(leftValue.isPresent(), rightValue.isPresent());
                    }
                }
                case SLOT ->
                {
                    Optional<TrinketEffect<?>> leftValue = leftData.getFirstValue(ModGearAttributes.TRINKET_EFFECT);
                    Optional<TrinketEffect<?>> rightValue = rightData.getFirstValue(ModGearAttributes.TRINKET_EFFECT);

                    if (leftValue.isPresent() && rightValue.isPresent())
                    {
                        if (leftValue.get().getConfig().hasCuriosSlot() && rightValue.get().getConfig().hasCuriosSlot())
                        {
                            yield SortingHelper.compareString(
                                leftValue.get().getConfig().getCuriosSlot(),
                                rightValue.get().getConfig().getCuriosSlot());
                        }
                        else
                        {
                            yield Boolean.compare(leftValue.get().getConfig().hasCuriosSlot(),
                                rightValue.get().getConfig().hasCuriosSlot());
                        }
                    }
                    else
                    {
                        yield Boolean.compare(leftValue.isPresent(), rightValue.isPresent());
                    }
                }
            };
        }

        return ascending ? returnValue : -returnValue;
    }


    /**
     * This method compares two given vault dolls by their sorting order.
     * @param leftName the left name
     * @param leftTag the left data
     * @param rightName the right name
     * @param rightTag the right data
     * @param sortingOrder the sorting order
     * @param ascending the ascending
     * @return the comparison of two given vault dolls items.
     */
    public static int compareVaultDolls(String leftName,
        CompoundTag leftTag,
        String rightName,
        CompoundTag rightTag,
        List<DollOptions> sortingOrder,
        boolean ascending)
    {
        int returnValue = 0;

        for (int i = 0, sortingOrderSize = sortingOrder.size(); returnValue == 0 && i < sortingOrderSize; i++)
        {
            DollOptions sortOptions = sortingOrder.get(i);

            returnValue = switch (sortOptions) {
                case NAME -> SortingHelper.compareString(leftName, rightName);
                case OWNER -> SortingHelper.compareString(getDollName(leftTag), getDollName(rightTag));
                case XP -> Double.compare(getDollXP(leftTag), getDollXP(rightTag));
                case LOOT -> Double.compare(getDollLoot(leftTag), getDollLoot(rightTag));
                case COMPLETED -> Boolean.compare(getDollCompletion(leftTag), getDollCompletion(rightTag));
            };
        }

        return ascending ? returnValue : -returnValue;
    }


    /**
     * This method compares two given vault charms by their sorting order.
     * @param leftName the left name
     * @param leftData the left data
     * @param leftTag the left tag
     * @param rightName the right name
     * @param rightData the right data
     * @param rightTag the right tag
     * @param sortingOrder the sorting order
     * @param ascending the ascending
     * @return the comparison of two given vault charms items.
     */
    public static int compareCharms(String leftName,
        AttributeGearData leftData,
        CompoundTag leftTag,
        String rightName,
        AttributeGearData rightData,
        CompoundTag rightTag,
        List<CharmOptions> sortingOrder,
        boolean ascending)
    {
        int returnValue = Boolean.compare(isIdentified(leftData), isIdentified(rightData));

        if (!isIdentified(leftData) && returnValue == 0)
        {
            // Exit fast. Unidentified items are not comparable.
            return 0;
        }

        for (int i = 0, sortingOrderSize = sortingOrder.size(); returnValue == 0 && i < sortingOrderSize; i++)
        {
            CharmOptions sortOptions = sortingOrder.get(i);

            returnValue = switch (sortOptions) {
                case NAME -> SortingHelper.compareString(leftName, rightName);
                case USES -> Integer.compare(getRemainingUses(leftTag), getRemainingUses(rightTag));
                case VALUE -> Float.compare(getCharmValue(leftTag), getCharmValue(rightTag));
            };
        }

        return ascending ? returnValue : -returnValue;
    }


    /**
     * This method compares relic fragments by their "VaultModelId" tag value.
     * @param leftTag The left tag of relic fragment
     * @param rightTag The right tag of relic fragment
     * @param ascending the order of sort
     * @return the comparison of two given relic fragment tags.
     */
    public static int compareRelicFragments(@Nullable CompoundTag leftTag,
        @Nullable CompoundTag rightTag,
        boolean ascending)
    {
        int returnValue;

        if (leftTag != null && rightTag != null)
        {
            returnValue = SortingHelper.compareString(
                leftTag.getString(VAULT_MODEL_ID),
                rightTag.getString(VAULT_MODEL_ID));
        }
        else if (leftTag != null)
        {
            returnValue = 1;

        }
        else
        {
            returnValue = -1;
        }

        return ascending ? returnValue : -returnValue;
    }


    /**
     * This method compares respec flasks by their "Ability" tag value.
     * @param leftTag The left tag of relic fragment
     * @param rightTag The right tag of relic fragment
     * @param ascending the order of sort
     * @return the comparison of two given respec flasks tags.
     */
    public static int compareRespecFlasks(@Nullable CompoundTag leftTag,
        @Nullable CompoundTag rightTag,
        boolean ascending)
    {
        int returnValue;

        if (leftTag != null && rightTag != null)
        {
            returnValue = SortingHelper.compareString(
                leftTag.getString(ABILITY),
                rightTag.getString(ABILITY));
        }
        else if (leftTag != null)
        {
            returnValue = 1;

        }
        else
        {
            returnValue = -1;
        }

        return ascending ? returnValue : -returnValue;
    }


    /**
     * This method compares faced focus by their "modTag" tag value.
     * @param leftTag The left tag of relic fragment
     * @param rightTag The right tag of relic fragment
     * @param ascending the order of sort
     * @return the comparison of two given faced focus tags.
     */
    public static int compareFacedFocus(@Nullable CompoundTag leftTag,
        @Nullable CompoundTag rightTag,
        boolean ascending)
    {
        int returnValue;

        if (leftTag != null && rightTag != null)
        {
            returnValue = SortingHelper.compareString(
                leftTag.getString(MOD_TAG),
                rightTag.getString(MOD_TAG));
        }
        else if (leftTag != null)
        {
            returnValue = 1;

        }
        else
        {
            returnValue = -1;
        }

        return ascending ? returnValue : -returnValue;
    }


    /**
     * This method compares augments by their "theme" tag value.
     * @param leftTag The left tag of augment
     * @param rightTag The right tag of augment
     * @param ascending the order of sort
     * @return the comparison of two given augments tags.
     */
    public static int compareAugments(@Nullable CompoundTag leftTag,
        @Nullable CompoundTag rightTag,
        boolean ascending)
    {
        int returnValue;

        if (leftTag != null && rightTag != null)
        {
            returnValue = SortingHelper.compareString(
                leftTag.getString(THEME),
                rightTag.getString(THEME));
        }
        else if (leftTag != null)
        {
            returnValue = 1;

        }
        else
        {
            returnValue = -1;
        }

        return ascending ? returnValue : -returnValue;
    }


// ---------------------------------------------------------------------
// Section: Internal Sorting Methods
// ---------------------------------------------------------------------


    /**
     * This method compares two given strings.
     * @param leftName Left item name.
     * @param rightName Right item name.
     * @return Returns comparison of two given strings.
     */
    private static int compareString(String leftName, String rightName)
    {
        return leftName.compareTo(rightName);
    }


    /**
     * This method compares two given attributes.
     * @param leftAttribute Left item attribute.
     * @param rightAttribute Right item attribute.
     * @return Returns:
     *      -1 if leftAttribute internal index is smaller than rightAttribute.
     *      +1 if leftAttribute internal index is bigger than rightAttribute.
     *      0 if leftAttribute internal index is equal to rightAttribute.
     */
    private static int compareAttribute(VaultGearAttribute<?> leftAttribute, VaultGearAttribute<?> rightAttribute)
    {
        return Integer.compare(AttributeHelper.getAttributeIndex(leftAttribute),
            AttributeHelper.getAttributeIndex(rightAttribute));
    }


    /**
     * This method compares two given items by their attribute value. This method can compare only items with equal
     * attributes.
     * @param leftData Left item data.
     * @param leftAttribute Left item attribute.
     * @param rightData Right item data.
     * @param rightAttribute Right item attribute.
     * @return Returns:
     *     -1 if leftData attribute value is smaller than rightData attribute value.
     *     +1 if leftData attribute value is bigger than rightData attribute value.
     *     0 if leftData attribute value is equal to rightData attribute value.
     */
    private static int compareAttributeValue(VaultGearData leftData,
        VaultGearAttribute<?> leftAttribute,
        VaultGearData rightData,
        VaultGearAttribute<?> rightAttribute)
    {
        // Comparing by attribute value can happen only on equal attributes.
        if (SortingHelper.compareAttribute(leftAttribute, rightAttribute) != 0 || leftAttribute == null)
        {
            return 0;
        }

        if (AttributeHelper.isFloatAttribute(leftAttribute))
        {
            // Compare items by float attribute value.
            Optional<Float> leftValue = (Optional<Float>) leftData.getFirstValue(leftAttribute);
            Optional<Float> rightValue = (Optional<Float>) rightData.getFirstValue(leftAttribute);

            return leftValue.orElse(0F).compareTo(rightValue.orElse(0F));
        }
        else if (AttributeHelper.isIntegerAttribute(leftAttribute))
        {
            // Compare items by integer attribute value.
            Optional<Integer> leftValue = (Optional<Integer>) leftData.getFirstValue(leftAttribute);
            Optional<Integer> rightValue = (Optional<Integer>) rightData.getFirstValue(leftAttribute);

            return leftValue.orElse(0).compareTo(rightValue.orElse(0));
        }
        else if (AttributeHelper.isDoubleAttribute(leftAttribute))
        {
            // Compare items by double attribute value.
            Optional<Double> leftValue = (Optional<Double>) leftData.getFirstValue(leftAttribute);
            Optional<Double> rightValue = (Optional<Double>) rightData.getFirstValue(leftAttribute);

            return leftValue.orElse(0D).compareTo(rightValue.orElse(0D));
        }
        else
        {
            return 0;
        }
    }


    /**
     * This method compares two given items by their attribute value weight. Attribute value weight is calculated by
     * dividing attribute value by jewel size.
     * @param leftData Left item data.
     * @param leftAttribute Left item attribute.
     * @param rightData Right item data.
     * @param rightAttribute Right item attribute.
     * @return Returns:
     *     -1 if leftData attribute value weight is smaller than rightData attribute value weight.
     *     +1 if leftData attribute value weight is bigger than rightData attribute value weight.
     *     0 if leftData attribute value weight is equal to rightData attribute value weight.
     */
    private static int compareAttributeValueWeight(VaultGearData leftData,
        VaultGearAttribute<?> leftAttribute,
        VaultGearData rightData,
        VaultGearAttribute<?> rightAttribute)
    {
        // Comparing by attribute value can happen only on equal attributes.
        if (SortingHelper.compareAttribute(leftAttribute, rightAttribute) != 0 || leftAttribute == null)
        {
            return 0;
        }

        int leftSize = leftData.getFirstValue(ModGearAttributes.JEWEL_SIZE).orElse(0);
        int rightSize = rightData.getFirstValue(ModGearAttributes.JEWEL_SIZE).orElse(0);

        if (leftSize == 0 && rightSize == 0)
        {
            // No size, no comparison
            return 0;
        }
        else if (leftSize == 0)
        {
            // No size for first one.
            return -1;
        }
        else if (rightSize == 0)
        {
            // No size for second one.
            return 1;
        }

        // Now actually compare.

        if (AttributeHelper.isFloatAttribute(leftAttribute))
        {
            float leftValue = ((Optional<Float>) leftData.getFirstValue(leftAttribute)).orElse(0F);
            float rightValue = ((Optional<Float>) rightData.getFirstValue(leftAttribute)).orElse(0F);

            // Compare items by float attribute value weight.
            return Float.compare(leftValue / leftSize, rightValue / rightSize);
        }
        else if (AttributeHelper.isIntegerAttribute(leftAttribute))
        {
            int leftValue = ((Optional<Integer>) leftData.getFirstValue(leftAttribute)).orElse(0);
            int rightValue = ((Optional<Integer>) rightData.getFirstValue(leftAttribute)).orElse(0);

            // Compare items by integer attribute value weight.
            return Integer.compare(leftValue / leftSize, rightValue / rightSize);
        }
        else if (AttributeHelper.isDoubleAttribute(leftAttribute))
        {
            double leftValue = ((Optional<Double>) leftData.getFirstValue(leftAttribute)).orElse(0D);
            double rightValue = ((Optional<Double>) rightData.getFirstValue(leftAttribute)).orElse(0D);

            // Compare items by integer attribute value weight.
            return Double.compare(leftValue / leftSize, rightValue / rightSize);
        }
        else
        {
            return 0;
        }
    }


    /**
     * This method compares two given items by their JEWEL_SIZE attribute.
     * @param leftData Left item.
     * @param rightData Right item.
     * @return Returns:
     *      -1 if leftData jewel size value is smaller than rightData jewel size value.
     *      +1 if leftData jewel size value is bigger than rightData jewel size value.
     *      0 if leftData jewel size value is equal to rightData jewel size value.
     */
    private static int compareSizeAttribute(VaultGearData leftData, VaultGearData rightData)
    {
        // If we reach this part and still do not have a valid order, then compare by jewel size.
        Optional<Integer> leftSize = leftData.getFirstValue(ModGearAttributes.JEWEL_SIZE);
        Optional<Integer> rightSize = rightData.getFirstValue(ModGearAttributes.JEWEL_SIZE);

        return leftSize.orElse(0).compareTo(rightSize.orElse(0));
    }


    /**
     * This method compares item level value of two given items.
     * @param leftData Left item.
     * @param rightData Right item.
     * @return Returns:
     *      -1 if leftData item level value is smaller than rightData item level value.
     *      +1 if leftData item level value is bigger than rightData item level value.
     *      0 if leftData item level value is equal to rightData item level value.
     */
    private static int compareLevel(VaultGearData leftData, VaultGearData rightData)
    {
        // If we reach this part and still do not have a valid order, then compare by jewel level.
        return Integer.compare(leftData.getItemLevel(), rightData.getItemLevel());
    }


    /**
     * This method compares that state of two given items.
     * @param leftData The data of the left item.
     * @param rightData The data of the right item.
     * @return The comparison of State value.
     */
    private static int compareState(VaultGearData leftData, VaultGearData rightData)
    {
        return leftData.getState().compareTo(rightData.getState());
    }


    /**
     * This method compares rarity of two given items.
     * @param leftData The data of the left item.
     * @param rightData The data of the right item.
     * @return The comparison of Rarity value.
     */
    private static int compareRarity(VaultGearData leftData, VaultGearData rightData)
    {
        if (leftData.getState() != rightData.getState())
        {
            // Non-equal states are not comparable by rarity.
            return SortingHelper.compareState(leftData, rightData);
        }

        if (leftData.getState().equals(VaultGearState.IDENTIFIED))
        {
            // If item is identified, then compare by item rarity.

            return leftData.getRarity().compareTo(rightData.getRarity());
        }
        else
        {
            // Find ModGearAttributes.GEAR_ROLL_TYPE attribute and compare it.
            Optional<String> leftValue = leftData.getFirstValue(ModGearAttributes.GEAR_ROLL_TYPE);
            Optional<String> rightValue = rightData.getFirstValue(ModGearAttributes.GEAR_ROLL_TYPE);

            if (leftValue.isPresent() && rightValue.isPresent())
            {
                String leftRoll = leftValue.get();
                String rightRoll = rightValue.get();

                if (!leftRoll.equals(rightRoll))
                {
                    int leftIndex = AttributeHelper.getRollIndex(leftValue.get());
                    int rightIndex = AttributeHelper.getRollIndex(rightValue.get());

                    if (leftIndex == rightIndex)
                    {
                        // sort by name
                        return SortingHelper.compareString(leftRoll, rightRoll);
                    }
                    else
                    {
                        // Sort by index
                        return Integer.compare(leftIndex, rightIndex);
                    }
                }
            }
        }

        // Gear does not contain data about rarity.
        return 0;
    }


    /**
     * This method compares model of two given items.
     * @param leftData The data of the left item.
     * @param rightData The data of the right item.
     * @return Gear Name comparison.
     */
    private static int compareModel(VaultGearData leftData, VaultGearData rightData)
    {
        if (leftData.getState() != rightData.getState())
        {
            // Non-equal states are not comparable by rarity.
            return SortingHelper.compareState(leftData, rightData);
        }

        if (leftData.getState().equals(VaultGearState.IDENTIFIED))
        {
            // Find ModGearAttributes.GEAR_MODEL attribute and compare it.
            Optional<ResourceLocation> leftValue = leftData.getFirstValue(ModGearAttributes.GEAR_MODEL);
            Optional<ResourceLocation> rightValue = rightData.getFirstValue(ModGearAttributes.GEAR_MODEL);

            if (leftValue.isPresent() && rightValue.isPresent())
            {
                return leftValue.get().compareTo(rightValue.get());
            }
        }

        // Gear does not contain data about rarity.
        return 0;
    }


    /**
     * This method compares two given inscription data by their instability.
     * @param leftData Left inscription data.
     * @param rightData Right inscription data.
     * @return Returns the comparison of two given inscription data by their instability.
     */
    private static int compareInstability(CompoundTag leftData, CompoundTag rightData)
    {
        return leftData.contains("instability") && rightData.contains("instability") ?
            Float.compare(leftData.getFloat("instability"), rightData.getFloat("instability")) :
            0;
    }


    /**
     * This method compares two given inscription data by their completion.
     * @param leftData Left inscription data.
     * @param rightData Right inscription data.
     * @return Returns the comparison of two given inscription data by their completion.
     */
    private static int compareCompletion(CompoundTag leftData, CompoundTag rightData)
    {
        return leftData.contains("completion") && rightData.contains("completion") ?
            Float.compare(leftData.getFloat("completion"), rightData.getFloat("completion")) :
            0;
    }


    /**
     * This method compares two given inscription data by their time.
     * @param leftData Left inscription data.
     * @param rightData Right inscription data.
     * @return Returns the comparison of two given inscription data by their time.
     */
    private static int compareTime(CompoundTag leftData, CompoundTag rightData)
    {
        return leftData.contains("time") && rightData.contains("time") ?
            Integer.compare(leftData.getInt("time"), rightData.getInt("time")) :
            0;
    }


    /**
     * This method compares two given inscription data by their rooms.
     * @param leftData Left inscription data.
     * @param rightData Right inscription data.
     * @return Returns the comparison of two given inscription data by their rooms.
     */
    private static int compareRooms(InscriptionData leftData, InscriptionData rightData)
    {
        int leftRooms = leftData.getEntries().size();
        int rightRooms = rightData.getEntries().size();

        if (leftRooms != rightRooms)
        {
            return Integer.compare(leftRooms, rightRooms);
        }
        else if (leftRooms == 1)
        {
            InscriptionData.Entry leftEntry = leftData.getEntries().get(0);
            InscriptionData.Entry rightEntry = rightData.getEntries().get(0);

            String leftRoomName = leftEntry.toRoomEntry().getName().getString();
            String rightRoomName = rightEntry.toRoomEntry().getName().getString();

            return leftRoomName.compareTo(rightRoomName);
        }
        else
        {
            // Equal number of rooms. Return 0.
            return 0;
        }
    }


    /**
     * This method compares 2 given integer values.
     * @param leftValue The left value.
     * @param rightValue The right value.
     * @return Returns if left value is larger then right value.
     */
    private static int compareIntegerValue(Integer leftValue, Integer rightValue)
    {
        if (leftValue == null && rightValue != null)
        {
            return -1;
        }
        else if (leftValue != null && rightValue == null)
        {
            return 1;
        }
        else if (leftValue == null)
        {
            return 0;
        }
        else
        {
            return Integer.compare(leftValue, rightValue);
        }
    }


    /**
     * This method compares 2 given double values.
     * @param leftValue The left value.
     * @param rightValue The right value.
     * @return Returns if left value is larger then right value.
     */
    private static int compareDoubleValue(Double leftValue, Double rightValue)
    {
        if (leftValue == null && rightValue != null)
        {
            return -1;
        }
        else if (leftValue != null && rightValue == null)
        {
            return 1;
        }
        else if (leftValue == null)
        {
            return 0;
        }
        else
        {
            return Double.compare(leftValue, rightValue);
        }
    }


    /**
     * This method compares 2 given IExtraGearDataCache values.
     * @param leftValue The left value.
     * @param rightValue The right value.
     * @return Returns if left value is larger then right value.
     */
    private static int compareAttributeValueWeight(IExtraGearDataCache leftValue, IExtraGearDataCache rightValue)
    {
        Integer leftSize = leftValue.getExtraJewelSize();
        Integer rightSize = rightValue.getExtraJewelSize();

        if (leftSize == null && rightSize == null)
        {
            // No size, no comparison
            return 0;
        }
        else if (leftSize == null || leftSize == 0)
        {
            // No size for first one.
            return -1;
        }
        else if (rightSize == null || rightSize == 0)
        {
            // No size for second one.
            return 1;
        }

        // Now actually compare.

        Double leftAttributeValue = leftValue.getExtraFirstAttributeValue();
        Double rightAttributeValue = rightValue.getExtraFirstAttributeValue();

        if (leftAttributeValue == null && rightAttributeValue == null)
        {
            // No attribute value, no comparison
            return 0;
        }
        else if (leftAttributeValue == null)
        {
            // No attribute value for first one.
            return -1;
        }
        else if (rightAttributeValue == null)
        {
            // No attribute value for second one.
            return 1;
        }

        return Double.compare(leftAttributeValue / leftSize, rightAttributeValue / rightSize);
    }


    /**
     * This method returns remaining uses from given tag.
     * @param tag The tag of the item.
     * @return The remaining uses from given tag.
     */
    private static int getRemainingUses(CompoundTag tag)
    {
        return Math.max(getVaultUses(tag) - getUsedVaults(tag), 0);
    }


    /**
     * This method returns vaultUses from given tag.
     * @param tag The tag of the item.
     * @return The vaultUses from given tag.
     */
    private static int getVaultUses(CompoundTag tag)
    {
        return tag.contains("vaultUses") ? tag.getInt("vaultUses") : 0;
    }


    /**
     * This method returns amount of used vaults.
     * @param tag The tag of the item.
     * @return The amount of used vaults.
     */
    private static int getUsedVaults(CompoundTag tag)
    {
        return tag.contains("usedVaults") ? tag.getList("usedVaults", 10).size() : 0;
    }


    /**
     * This method returns name of doll owner.
     * @param tag The tag of the item.
     * @return The doll owner name.
     */
    private static String getDollName(CompoundTag tag)
    {
        return tag != null && tag.contains("playerProfile") && tag.getCompound("playerProfile").contains("Name") ?
            tag.getCompound("playerProfile").getString("Name") : "";
    }


    /**
     * This method returns name of doll xp %.
     * @param tag The tag of the item.
     * @return The doll xp %.
     */
    private static float getDollXP(CompoundTag tag)
    {
        return tag != null && tag.contains("xpPercent") ? tag.getFloat("xpPercent") : 0f;
    }


    /**
     * This method returns name of doll loot %.
     * @param tag The tag of the item.
     * @return The doll loot %.
     */
    private static float getDollLoot(CompoundTag tag)
    {
        return tag != null && tag.contains("lootPercent") ? tag.getFloat("lootPercent") : 0f;
    }


    /**
     * This method returns name of doll completion status.
     * @param tag The tag of the item.
     * @return The doll completion status.
     */
    private static boolean getDollCompletion(CompoundTag tag)
    {
        return tag != null && tag.contains("vaultUUID");
    }


    /**
     * This method returns name of charm value %.
     * @param tag The tag of the item.
     * @return The charm value %.
     */
    private static float getCharmValue(CompoundTag tag)
    {
        return tag.contains("charmValue") ? tag.getFloat("charmValue") : 0f;
    }


    /**
     * This method checks if given gear is identified.
     * @param data The data of the gear.
     * @return True if gear is identified.
     */
    private static boolean isIdentified(AttributeGearData data)
    {
        return data.getFirstValue(ModGearAttributes.STATE).
            orElse(VaultGearState.UNIDENTIFIED) == VaultGearState.IDENTIFIED;
    }


    /**
     * This method compares to given modifier list to each other.
     * Assumption is made here, that ListTag#get(<number>) returns empty string for non-existent
     * index.
     * @param leftList The list of modifiers for first object.
     * @param rightList The list of modifiers for second object.
     * @return Comparison between 2 lists.
     */
    private static int compareModifiers(ListTag leftList, ListTag rightList)
    {
        int i = 0;
        int max = Math.max(leftList.size(), rightList.size());

        String leftMain = leftList.getString(i);
        String rightMain = rightList.getString(i++);

        while (leftMain.equals(rightMain) && i < max)
        {
            leftMain = leftList.getString(i);
            rightMain = leftList.getString(i++);
        }

        return leftMain.compareTo(rightMain);
    }


// ---------------------------------------------------------------------
// Section: Enum for sorting order
// ---------------------------------------------------------------------


    /**
     * This enum holds all possible values for JEWEL sorting order.
     */
    public enum JewelOptions
    {
        /**
         * The name of the item
         */
        NAME,
        /**
         * The name of the jewel attribute
         */
        ATTRIBUTE,
        /**
         * The value of the jewel attribute
         */
        ATTRIBUTE_VALUE,
        /**
         * The size of the jewel
         */
        SIZE,
        /**
         * The weight of the jewel attribute - attribute value / size
         */
        ATTRIBUTE_WEIGHT,
        /**
         * The level of the item
         */
        LEVEL
    }


    /**
     * This enum holds all possible values for GEAR sorting order.
     */
    public enum GearOptions
    {
        /**
         * The name of the item
         */
        NAME,
        /**
         * The level of the item
         */
        LEVEL,
        /**
         * The rarity of the item
         */
        RARITY,
        /**
         * The state of the gear. (IDENTIFIED, UNIDENTIFIED, etc.)
         */
        STATE,
        /**
         * The model fo the gear.
         */
        MODEL
    }


    /**
     * This enum holds all possible values for INSCRIPTION sorting order.
     */
    public enum InscriptionOptions
    {
        /**
         * The name of the inscription
         */
        NAME,
        /**
         * The completion amount of inscription.
         */
        COMPLETION,
        /**
         * The time of inscription.
         */
        TIME,
        /**
         * The instability of inscription.
         */
        INSTABILITY,
        /**
         * The amount of rooms in inscription.
         */
        ROOMS
    }


    /**
     * This enum holds all possible values for crystal sorting order
     */
    public enum CrystalOptions
    {
        /**
         * The name of item
         */
        NAME,
        /**
         * Level of the crystal
         */
        LEVEL,
        /**
         * Type of the crystal
         */
        TYPE
    }


    /**
     * This enum holds all possible values for trinket sorting order
     */
    public enum TrinketOptions
    {
        /**
         * The name of item
         */
        NAME,
        /**
         * Type of the trinket
         */
        TYPE,
        /**
         * The slot of trinket
         */
        SLOT,
        /**
         * The uses left on trinket
         */
        USES
    }


    /**
     * This enum holds all possible values for vault doll sorting order
     */
    public enum DollOptions
    {
        /**
         * The name of item
         */
        NAME,
        /**
         * The name of doll owner
         */
        OWNER,
        /**
         * The status of doll
         */
        COMPLETED,
        /**
         * The xp % of doll
         */
        XP,
        /**
         * The loot % of doll
         */
        LOOT
    }


    /**
     * This enum holds all possible values for vault charm sorting order
     */
    public enum CharmOptions
    {
        /**
         * The name of item
         */
        NAME,
        /**
         * The god affinity value of charm
         */
        VALUE,
        /**
         * The uses of charm
         */
        USES
    }


    /**
     * This enum holds all possible values for infused catalyst sorting order
     */
    public enum CatalystOptions
    {
        /**
         * The name of item
         */
        NAME,
        /**
         * The size of catalyst
         */
        SIZE,
        /**
         * The modifier of catalyst
         */
        MODIFIER
    }


    /**
     * The modifiers variable
     */
    public static final String MODIFIERS = "modifiers";

    /**
     * The size variable
     */
    public static final String SIZE = "size";

    /**
     * The vault model id variable
     */
    public static final String VAULT_MODEL_ID = "VaultModelId";

    /**
     * The ability variable
     */
    public static final String ABILITY = "Ability";

    /**
     * The mod tag variable
     */
    public static final String MOD_TAG = "modTag";

    /**
     * The theme variable
     */
    public static final String THEME = "theme";

    /**
     * The name of the cache.
     */
    public static final String EXTRA_ATTRIBUTE_INDEX = "extra_attribute_index";

    /**
     * The name of the cache.
     */
    public static final String EXTRA_ATTRIBUTE_VALUE = "extra_attribute_value";

    /**
     * The name of the cache.
     */
    public static final String EXTRA_JEWEL_SIZE = "extra_jewel_size";

    /**
     * The name of the cache.
     */
    public static final String EXTRA_GEAR_LEVEL = "extra_gear_level";

    /**
     * The name of the cache.
     */
    public static final String EXTRA_CACHE_VERSION = "extra_cache_version";

    /**
     * The set of items that are considered vault gear.
     */
    public static final Set<ResourceLocation> VAULT_GEAR_SET = new HashSet<>();

    /**
     * The set of items that are considered vault charms.
     */
    public static final Set<ResourceLocation> VAULT_CHARMS = new HashSet<>();

    /**
     * The set of items that can be sorted by this algorithm.
     */
    public static final Set<ResourceLocation> CUSTOM_SORTING = new HashSet<>();

    // Put all vault gear items into the set.
    static
    {
        VAULT_GEAR_SET.add(ModItems.HELMET.getRegistryName());
        VAULT_GEAR_SET.add(ModItems.CHESTPLATE.getRegistryName());
        VAULT_GEAR_SET.add(ModItems.LEGGINGS.getRegistryName());
        VAULT_GEAR_SET.add(ModItems.BOOTS.getRegistryName());

        VAULT_GEAR_SET.add(ModItems.SWORD.getRegistryName());
        VAULT_GEAR_SET.add(ModItems.AXE.getRegistryName());

        VAULT_GEAR_SET.add(ModItems.WAND.getRegistryName());
        VAULT_GEAR_SET.add(ModItems.SHIELD.getRegistryName());

        VAULT_GEAR_SET.add(ModItems.MAGNET.getRegistryName());

        VAULT_GEAR_SET.add(ModItems.IDOL_BENEVOLENT.getRegistryName());
        VAULT_GEAR_SET.add(ModItems.IDOL_OMNISCIENT.getRegistryName());
        VAULT_GEAR_SET.add(ModItems.IDOL_TIMEKEEPER.getRegistryName());
        VAULT_GEAR_SET.add(ModItems.IDOL_MALEVOLENCE.getRegistryName());

        VAULT_GEAR_SET.add(ModItems.FOCUS.getRegistryName());

        VAULT_CHARMS.add(ModItems.SMALL_CHARM.getRegistryName());
        VAULT_CHARMS.add(ModItems.LARGE_CHARM.getRegistryName());
        VAULT_CHARMS.add(ModItems.GRAND_CHARM.getRegistryName());
        VAULT_CHARMS.add(ModItems.MAJESTIC_CHARM.getRegistryName());

        CUSTOM_SORTING.add(ModItems.JEWEL.getRegistryName());
        CUSTOM_SORTING.add(ModItems.INSCRIPTION.getRegistryName());
        CUSTOM_SORTING.add(ModItems.VAULT_CRYSTAL.getRegistryName());
        CUSTOM_SORTING.add(ModItems.TRINKET.getRegistryName());
        CUSTOM_SORTING.add(ModItems.VAULT_DOLL.getRegistryName());
        CUSTOM_SORTING.add(ModItems.VAULT_CATALYST_INFUSED.getRegistryName());
        CUSTOM_SORTING.add(ModItems.RELIC_FRAGMENT.getRegistryName());
        CUSTOM_SORTING.add(ModItems.RESPEC_FLASK.getRegistryName());
        CUSTOM_SORTING.add(ModItems.FACETED_FOCUS.getRegistryName());
        CUSTOM_SORTING.add(ModItems.AUGMENT.getRegistryName());
    }
}
