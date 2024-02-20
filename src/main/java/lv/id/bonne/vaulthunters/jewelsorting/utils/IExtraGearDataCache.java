//
// Created by BONNe
// Copyright - 2023
//


package lv.id.bonne.vaulthunters.jewelsorting.utils;


import org.jetbrains.annotations.Nullable;
import java.util.function.Function;

import lv.id.bonne.vaulthunters.jewelsorting.VaultJewelSorting;
import net.minecraft.nbt.Tag;


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


    /**
     * This method returns cached value of a requested tag without applying it.
     * @param key String value of a tag.
     * @param nbtRead Function to read a tag.
     * @param <T> Type of a tag.
     * @return cached value of a requested tag.
     */
    default <T> T getCachedValue(String key,  Function<Tag, T> nbtRead)
    {
        return null;
    }


    /**
     * This method returns true if cache is invalid.
     * @return true if cache is valid.
     */
    default boolean isInvalidCache()
    {
        return !VaultJewelSorting.VAULT_MOD_VERSION.equals(
            this.getCachedValue(SortingHelper.EXTRA_CACHE_VERSION, Tag::getAsString));
    }
}
