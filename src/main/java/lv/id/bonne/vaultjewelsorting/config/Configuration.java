//
// Created by BONNe
// Copyright - 2023
//


package lv.id.bonne.vaultjewelsorting.config;



import com.google.common.base.Enums;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import lv.id.bonne.vaultjewelsorting.utils.SortingHelper;
import net.minecraftforge.common.ForgeConfigSpec;


/**
 * The configuration handling class. Holds all the config values.
 */
public class Configuration
{
    /**
     * The constructor for the config.
     */
    public Configuration()
    {
        this.builder = new ForgeConfigSpec.Builder();

        this.builder.comment("This category holds options how Jewels are sorted");
        this.builder.push("Jewel Sorting");

        this.jewelSortingByName = this.builder.
            comment("The order of Jewels if they are sorted by the name.").
            comment("Supported Values: NAME, ATTRIBUTE, ATTRIBUTE_VALUE, SIZE, LEVEL, ATTRIBUTE_WEIGHT").
            defineList("jewel_sorting_by_name",
                Arrays.asList(SortingHelper.SortOptions.NAME.name(),
                    SortingHelper.SortOptions.ATTRIBUTE.name(),
                    SortingHelper.SortOptions.ATTRIBUTE_VALUE.name(),
                    SortingHelper.SortOptions.SIZE.name(),
                    SortingHelper.SortOptions.LEVEL.name()),
                entry -> entry instanceof String value &&
                    Enums.getIfPresent(SortingHelper.SortOptions.class, value).isPresent());

        this.jewelSortingByAmount = this.builder.
            comment("The order of Jewels if they are sorted by the amount/size.").
            comment("Supported Values: NAME, ATTRIBUTE, ATTRIBUTE_VALUE, SIZE, LEVEL, ATTRIBUTE_WEIGHT").
            defineList("jewel_sorting_by_amount",
                Arrays.asList(SortingHelper.SortOptions.NAME.name(),
                    SortingHelper.SortOptions.SIZE.name(),
                    SortingHelper.SortOptions.ATTRIBUTE.name(),
                    SortingHelper.SortOptions.ATTRIBUTE_VALUE.name(),
                    SortingHelper.SortOptions.LEVEL.name()),
                entry -> entry instanceof String value &&
                    Enums.getIfPresent(SortingHelper.SortOptions.class, value).isPresent());

        this.jewelSortingByMod = this.builder.
            comment("The order of Jewels if they are sorted by the mod.").
            comment("Supported Values: NAME, ATTRIBUTE, ATTRIBUTE_VALUE, SIZE, LEVEL, ATTRIBUTE_WEIGHT").
            defineList("jewel_sorting_by_mod",
                Collections.emptyList(),
                entry -> entry instanceof String value &&
                    Enums.getIfPresent(SortingHelper.SortOptions.class, value).isPresent());

        this.builder.pop();

        this.builder.comment("This category holds options how Gear are sorted");
        this.builder.push("Gear Sorting");

        this.gearSortingByName = this.builder.
            comment("The order of Gear if they are sorted by the name.").
            comment("Supported Values: NAME, STATE, RARITY, LEVEL, MODEL").
            defineList("gear_sorting_by_name",
                Arrays.asList(SortingHelper.SortOptions.NAME.name(),
                    SortingHelper.SortOptions.STATE.name(),
                    SortingHelper.SortOptions.RARITY.name(),
                    SortingHelper.SortOptions.LEVEL.name()),
                entry -> entry instanceof String value &&
                    Enums.getIfPresent(SortingHelper.SortOptions.class, value).isPresent());

        this.gearSortingByAmount = this.builder.
            comment("The order of Gear if they are sorted by the amount/size.").
            comment("Supported Values: NAME, STATE, RARITY, LEVEL, MODEL").
            defineList("gear_sorting_by_amount",
                Arrays.asList(SortingHelper.SortOptions.NAME.name(),
                    SortingHelper.SortOptions.STATE.name(),
                    SortingHelper.SortOptions.LEVEL.name(),
                    SortingHelper.SortOptions.RARITY.name()),
                entry -> entry instanceof String value &&
                    Enums.getIfPresent(SortingHelper.SortOptions.class, value).isPresent());

        this.gearSortingByMod = this.builder.
            comment("The order of Gear if they are sorted by the mod.").
            comment("Supported Values: NAME, STATE, RARITY, LEVEL, MODEL").
            defineList("gear_sorting_by_mod",
                Collections.emptyList(),
                entry -> entry instanceof String value &&
                    Enums.getIfPresent(SortingHelper.SortOptions.class, value).isPresent());

        this.rarityOrder = this.builder.
            comment("The order of Rarities Pools in the sorting for unidentified gear.").
            defineList("rarity_order",
                Arrays.asList("Scrappy",
                    "Scrappy+",
                    "Common",
                    "Common+",
                    "Rare",
                    "Rare+",
                    "Epic",
                    "Epic+",
                    "Omega",
                    "Beginner",
                    "Novice",
                    "Maker",
                    "Apprentice",
                    "Smith",
                    "Artisan",
                    "Master Artisan",
                    "Gladiator"),
                entry -> true);

        this.builder.pop();

        Configuration.GENERAL_SPEC = this.builder.build();
    }


    /**
     * Gets jewel sorting by name.
     *
     * @return the jewel sorting by name
     */
    public List<SortingHelper.SortOptions> getJewelSortingByName()
    {
        return this.convertStringToEnum(this.jewelSortingByName.get());
    }


    /**
     * Gets jewel sorting by amount.
     *
     * @return the jewel sorting by amount
     */
    public List<SortingHelper.SortOptions> getJewelSortingByAmount()
    {
        return this.convertStringToEnum(this.jewelSortingByAmount.get());
    }


    /**
     * Gets jewel sorting by mod.
     *
     * @return the jewel sorting by mod
     */
    public List<SortingHelper.SortOptions> getJewelSortingByMod()
    {
        return this.convertStringToEnum(this.jewelSortingByMod.get());
    }


    /**
     * Gets gear sorting by name.
     *
     * @return the gear sorting by name
     */
    public List<SortingHelper.SortOptions> getGearSortingByName()
    {
        return this.convertStringToEnum(this.gearSortingByName.get());
    }


    /**
     * Gets gear sorting by amount.
     *
     * @return the gear sorting by amount
     */
    public List<SortingHelper.SortOptions> getGearSortingByAmount()
    {
        return this.convertStringToEnum(this.gearSortingByAmount.get());
    }


    /**
     * Gets gear sorting by mod.
     *
     * @return the gear sorting by mod
     */
    public List<SortingHelper.SortOptions> getGearSortingByMod()
    {
        return this.convertStringToEnum(this.gearSortingByMod.get());
    }


    /**
     * Gets rarity order.
     *
     * @return the rarity order
     */
    public List<? extends String> getRarityOrder()
    {
        return this.rarityOrder.get();
    }


    /**
     * This method converts String list to Enum list.
     * @param value The string list that need to be converted.
     * @return Converted Enum list.
     */
    private List<SortingHelper.SortOptions> convertStringToEnum(List<? extends String> value)
    {
        return value.stream().
            filter(text -> Enums.getIfPresent(SortingHelper.SortOptions.class, text.toUpperCase()).isPresent()).
            map(SortingHelper.SortOptions::valueOf).
            toList();
    }


// ---------------------------------------------------------------------
// Section: Variables
// ---------------------------------------------------------------------


    /**
     * The main builder for the config.
     */
    private final ForgeConfigSpec.Builder builder;

    /**
     * The config value for jewel sorting by name.
     */
    private final ForgeConfigSpec.ConfigValue<List<? extends String>> jewelSortingByName;

    /**
     * The config value for jewel sorting by amount.
     */
    private final ForgeConfigSpec.ConfigValue<List<? extends String>> jewelSortingByAmount;

    /**
     * The config value for jewel sorting by mod.
     */
    private final ForgeConfigSpec.ConfigValue<List<? extends String>> jewelSortingByMod;

    /**
     * The config value for gear sorting by name.
     */
    private final ForgeConfigSpec.ConfigValue<List<? extends String>> gearSortingByName;

    /**
     * The config value for gear sorting by amount.
     */
    private final ForgeConfigSpec.ConfigValue<List<? extends String>> gearSortingByAmount;

    /**
     * The config value for gear sorting by mod.
     */
    private final ForgeConfigSpec.ConfigValue<List<? extends String>> gearSortingByMod;

    /**
     * The config value for rarity order.
     */
    private final ForgeConfigSpec.ConfigValue<List<? extends String>> rarityOrder;


    //private final ForgeConfigSpec.ConfigValue<List<SortingHelper.SortOptions>> inscriptionSortingByName;

    //private final ForgeConfigSpec.ConfigValue<List<SortingHelper.SortOptions>> inscriptionSortingByAmount;

    //private final ForgeConfigSpec.ConfigValue<List<SortingHelper.SortOptions>> inscriptionSortingByMod;

    /**
     * The general config spec.
     */
    public static ForgeConfigSpec GENERAL_SPEC;
}
