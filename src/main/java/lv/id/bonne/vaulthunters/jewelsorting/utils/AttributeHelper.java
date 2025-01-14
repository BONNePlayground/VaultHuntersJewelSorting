//
// Created by BONNe
// Copyright - 2023
//


package lv.id.bonne.vaulthunters.jewelsorting.utils;


import java.util.ArrayList;
import java.util.List;

import iskallia.vault.gear.attribute.VaultGearAttribute;
import lv.id.bonne.vaulthunters.jewelsorting.VaultJewelSorting;
import net.minecraftforge.event.RegistryEvent;


/**
 * The class that helps to deal with Vault Hunters Gear Attributes.
 */
public class AttributeHelper
{
    /**
     * This method populates all VaultGearAttributes into custom lists.
     */
    public static void registerAttributes(RegistryEvent.Register<VaultGearAttribute<?>> event)
    {
        event.getRegistry().getValues().forEach(attribute ->
        {
            if (!MOD_GEAR_ATTRIBUTE.contains(attribute))
            {
                MOD_GEAR_ATTRIBUTE.add(attribute);
            }
        });
    }


    /**
     * Is float attribute boolean.
     *
     * @param attribute the attribute
     * @return the boolean
     */
    public static boolean isFloatAttribute(VaultGearAttribute<?> attribute)
    {
        try
        {
            return attribute != null && attribute.getType().cast(0) instanceof Float;
        }
        catch (ClassCastException ignore)
        {
            return false;
        }
    }


    /**
     * Is integer attribute boolean.
     *
     * @param attribute the attribute
     * @return the boolean
     */
    public static boolean isIntegerAttribute(VaultGearAttribute<?> attribute)
    {
        try
        {
            return attribute != null && attribute.getType().cast(0) instanceof Integer;
        }
        catch (ClassCastException ignore)
        {
            return false;
        }
    }



    /**
     * Is double attribute boolean.
     *
     * @param attribute the attribute
     * @return the boolean
     */
    public static boolean isDoubleAttribute(VaultGearAttribute<?> attribute)
    {
        try
        {
            return attribute != null && attribute.getType().cast(0) instanceof Double;
        }
        catch (ClassCastException ignore)
        {
            return false;
        }
    }


    /**
     * Gets attribute index.
     *
     * @param attribute the attribute
     * @return the attribute index
     */
    public static int getAttributeIndex(VaultGearAttribute<?> attribute)
    {
        return attribute == null ? -1 : MOD_GEAR_ATTRIBUTE.indexOf(attribute);
    }


    /**
     * Gets roll by index.
     * @param name The roll name.
     * @return The index of the roll.
     */
    public static int getRollIndex(String name)
    {
        return VaultJewelSorting.CONFIGURATION.getRarityOrder().indexOf(name);
    }


// ---------------------------------------------------------------------
// Section: Variables
// ---------------------------------------------------------------------


    /**
     * List that holds all VaultGearAttributes.
     */
    private static final List<VaultGearAttribute<?>> MOD_GEAR_ATTRIBUTE = new ArrayList<>();
}
