//
// Created by BONNe
// Copyright - 2023
//


package lv.id.bonne.vaultjewelsorting.utils;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import iskallia.vault.gear.attribute.VaultGearAttribute;
import iskallia.vault.gear.attribute.VaultGearModifier;
import iskallia.vault.gear.data.VaultGearData;
import iskallia.vault.init.ModGearAttributes;


/**
 * This class helps to sort Jewel items in correct order.
 */
public class SortingHelper
{
    /**
     * Compare jewels by their Attribute Count, Attribute, Attribute value, Jewel Size, Jewel Level.
     *
     * @param leftData the left data
     * @param rightData the right data
     * @param ascending the ascending
     * @return the int
     */
    public static int compareJewels(VaultGearData leftData, VaultGearData rightData, boolean ascending)
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

        if (leftAffixes.size() == 1 && rightAffixes.size() == 1)
        {
            // If there is just 1 attribute in both lists, then compare them by attribute index.

            VaultGearAttribute<?> leftAttribute = leftAffixes.get(0).getAttribute();
            VaultGearAttribute<?> rightAttribute = rightAffixes.get(0).getAttribute();

            int leftIndex = AttributeHelper.getAttributeIndex(leftAttribute);
            int rightIndex = AttributeHelper.getAttributeIndex(rightAttribute);

            if (leftIndex != rightIndex)
            {
                // If attribute indexes are different, then return them based on required order.
                return ascending ?
                    Integer.compare(leftIndex, rightIndex) :
                    Integer.compare(rightIndex, leftIndex);
            }
            else if (AttributeHelper.isFloatAttribute(leftAttribute))
            {
                // Compare items by float attribute value.
                Optional<Float> leftValue = (Optional<Float>) leftData.getFirstValue(leftAttribute);
                Optional<Float> rightValue = (Optional<Float>) rightData.getFirstValue(leftAttribute);

                returnValue = ascending ?
                    leftValue.orElse(0F).compareTo(rightValue.orElse(0F)) :
                    rightValue.orElse(0F).compareTo(leftValue.orElse(0F));
            }
            else if (AttributeHelper.isIntegerAttribute(leftAttribute))
            {
                // Compare items by integer attribute value.
                Optional<Integer> leftValue = (Optional<Integer>) leftData.getFirstValue(leftAttribute);
                Optional<Integer> rightValue = (Optional<Integer>) rightData.getFirstValue(leftAttribute);

                returnValue = ascending ?
                    leftValue.orElse(0).compareTo(rightValue.orElse(0)) :
                    rightValue.orElse(0).compareTo(leftValue.orElse(0));
            }
            else if (AttributeHelper.isDoubleAttribute(leftAttribute))
            {
                // Compare items by double attribute value.
                Optional<Double> leftValue = (Optional<Double>) leftData.getFirstValue(leftAttribute);
                Optional<Double> rightValue = (Optional<Double>) rightData.getFirstValue(leftAttribute);

                returnValue = ascending ?
                    leftValue.orElse(0D).compareTo(rightValue.orElse(0D)) :
                    rightValue.orElse(0D).compareTo(leftValue.orElse(0D));
            }
        }
        else
        {
            // Compare by the number of affixes
            returnValue = Integer.compare(leftAffixes.size(), rightAffixes.size());
        }

        if (returnValue == 0)
        {
            // If we reach this part and still do not have a valid order, then compare by jewel size.
            Optional<Integer> leftSize = leftData.getFirstValue(ModGearAttributes.JEWEL_SIZE);
            Optional<Integer> rightSize = rightData.getFirstValue(ModGearAttributes.JEWEL_SIZE);

            returnValue = ascending ?
                leftSize.orElse(0).compareTo(rightSize.orElse(0)) :
                rightSize.orElse(0).compareTo(leftSize.orElse(0));

        }

        if (returnValue == 0)
        {
            // If we reach this part and still do not have a valid order, then compare by jewel level.
            int leftLevel = leftData.getItemLevel();
            int rightLevel = rightData.getItemLevel();

            returnValue = ascending ?
                Integer.compare(leftLevel, rightLevel) :
                Integer.compare(rightLevel, leftLevel);
        }

        return returnValue;
    }


    /**
     * Compare jewels by their Jewel Size, Attribute Count, Attribute, Attribute value, Jewel Level.
     *
     * @param leftData the left data
     * @param rightData the right data
     * @param ascending the ascending
     * @return the int
     */
    public static int compareJewelsSize(VaultGearData leftData, VaultGearData rightData, boolean ascending)
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

        // If we reach this part and still do not have a valid order, then compare by jewel size.
        Optional<Integer> leftSize = leftData.getFirstValue(ModGearAttributes.JEWEL_SIZE);
        Optional<Integer> rightSize = rightData.getFirstValue(ModGearAttributes.JEWEL_SIZE);

        returnValue = ascending ?
            leftSize.orElse(0).compareTo(rightSize.orElse(0)) :
            rightSize.orElse(0).compareTo(leftSize.orElse(0));

        if (returnValue == 0)
        {
            if (leftAffixes.size() == 1 && rightAffixes.size() == 1)
            {
                // If there is just 1 attribute in both lists, then compare them by attribute index.

                VaultGearAttribute<?> leftAttribute = leftAffixes.get(0).getAttribute();
                VaultGearAttribute<?> rightAttribute = rightAffixes.get(0).getAttribute();

                int leftIndex = AttributeHelper.getAttributeIndex(leftAttribute);
                int rightIndex = AttributeHelper.getAttributeIndex(rightAttribute);

                if (leftIndex != rightIndex)
                {
                    // If attribute indexes are different, then return them based on required order.
                    return ascending ?
                        Integer.compare(leftIndex, rightIndex) :
                        Integer.compare(rightIndex, leftIndex);
                }
                else if (AttributeHelper.isFloatAttribute(leftAttribute))
                {
                    // Compare items by float attribute value.
                    Optional<Float> leftValue = (Optional<Float>) leftData.getFirstValue(leftAttribute);
                    Optional<Float> rightValue = (Optional<Float>) rightData.getFirstValue(leftAttribute);

                    returnValue = ascending ?
                        leftValue.orElse(0F).compareTo(rightValue.orElse(0F)) :
                        rightValue.orElse(0F).compareTo(leftValue.orElse(0F));
                }
                else if (AttributeHelper.isIntegerAttribute(leftAttribute))
                {
                    // Compare items by integer attribute value.
                    Optional<Integer> leftValue = (Optional<Integer>) leftData.getFirstValue(leftAttribute);
                    Optional<Integer> rightValue = (Optional<Integer>) rightData.getFirstValue(leftAttribute);

                    returnValue = ascending ?
                        leftValue.orElse(0).compareTo(rightValue.orElse(0)) :
                        rightValue.orElse(0).compareTo(leftValue.orElse(0));
                }
                else if (AttributeHelper.isDoubleAttribute(leftAttribute))
                {
                    // Compare items by double attribute value.
                    Optional<Double> leftValue = (Optional<Double>) leftData.getFirstValue(leftAttribute);
                    Optional<Double> rightValue = (Optional<Double>) rightData.getFirstValue(leftAttribute);

                    returnValue = ascending ?
                        leftValue.orElse(0D).compareTo(rightValue.orElse(0D)) :
                        rightValue.orElse(0D).compareTo(leftValue.orElse(0D));
                }
            }
            else
            {
                // Compare by the number of affixes
                returnValue = Integer.compare(leftAffixes.size(), rightAffixes.size());
            }
        }

        if (returnValue == 0)
        {
            // If we reach this part and still do not have a valid order, then compare by jewel level.
            int leftLevel = leftData.getItemLevel();
            int rightLevel = rightData.getItemLevel();

            returnValue = ascending ?
                Integer.compare(leftLevel, rightLevel) :
                Integer.compare(rightLevel, leftLevel);
        }

        return returnValue;
    }
}
