//
// Created by BONNe
// Copyright - 2023
//


package lv.id.bonne.vaulthunters.jewelsorting.utils;


import org.jetbrains.annotations.Nullable;


/**
 * This interface is used to create extra methods for accessing data in GearDataCache.
 */
public interface IExtraGearDataCache
{
    /**
     * This method returns first attribute index.
     * @return first attribute index.
     */
    @Nullable
    default Integer getExtraFirstAttributeIndex()
    {
        return null;
    }


    /**
     * This method returns first attribute value.
     * @return first attribute value.
     */
    @Nullable
    default Double getExtraFirstAttributeValue()
    {
        return null;
    }


    /**
     * This method returns Jewel Size value.
     * @return Jewel Size value.
     */
    @Nullable
    default Integer getExtraJewelSize()
    {
        return null;
    }


    /**
     * This method returns Gear Level value.
     * @return Gear Level value.
     */
    default int getExtraGearLevel()
    {
        return 0;
    }
}
