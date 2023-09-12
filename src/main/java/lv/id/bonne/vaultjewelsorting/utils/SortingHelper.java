//
// Created by BONNe
// Copyright - 2023
//


package lv.id.bonne.vaultjewelsorting.utils;


import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import iskallia.vault.dynamodel.model.armor.ArmorPieceModel;
import iskallia.vault.gear.VaultGearState;
import iskallia.vault.gear.attribute.VaultGearAttribute;
import iskallia.vault.gear.attribute.VaultGearModifier;
import iskallia.vault.gear.data.VaultGearData;
import iskallia.vault.gear.item.VaultGearItem;
import iskallia.vault.init.ModDynamicModels;
import iskallia.vault.init.ModGearAttributes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;


/**
 * This class helps to sort Jewel items in correct order.
 */
public class SortingHelper
{
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
        List<SortOptions> sortingOrder,
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
            SortOptions sortOptions = sortingOrder.get(i);

            returnValue = switch (sortOptions) {
                case NAME -> SortingHelper.compareString(leftName, rightName);
                case ATTRIBUTE -> SortingHelper.compareAttribute(leftAttribute, rightAttribute);
                case ATTRIBUTE_VALUE -> SortingHelper.compareAttributeValue(leftData, leftAttribute, rightData, rightAttribute);
                case SIZE -> SortingHelper.compareSizeAttribute(leftData, rightData);
                case ATTRIBUTE_WEIGHT -> SortingHelper.compareAttributeValueWeight(leftData, leftAttribute, rightData, rightAttribute);
                case LEVEL -> SortingHelper.compareLevel(leftData, rightData);
                default -> 0;
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
        List<SortOptions> sortingOrder,
        boolean ascending)
    {
        // Start comparing with the current vault gear state.
        int returnValue = 0;

        for (int i = 0, sortingOrderSize = sortingOrder.size(); returnValue == 0 && i < sortingOrderSize; i++)
        {
            SortOptions sortOptions = sortingOrder.get(i);

            returnValue = switch (sortOptions) {
                case NAME -> SortingHelper.compareString(leftName, rightName);
                case STATE -> SortingHelper.compareState(leftData, rightData);
                case RARITY -> SortingHelper.compareRarity(leftData, rightData);
                case LEVEL -> SortingHelper.compareLevel(leftData, rightData);
                case MODEL -> SortingHelper.compareModel(leftData, rightData);
                default -> 0;
            };
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
                        return leftRoll.compareTo(rightRoll);
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


// ---------------------------------------------------------------------
// Section: Enum for sorting order
// ---------------------------------------------------------------------


    /**
     * This enum holds all possible values for sorting order.
     */
    public enum SortOptions
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
}
