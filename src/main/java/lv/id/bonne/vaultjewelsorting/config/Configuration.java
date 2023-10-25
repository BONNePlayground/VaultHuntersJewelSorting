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
                Arrays.asList(SortingHelper.JewelOptions.NAME.name(),
                    SortingHelper.JewelOptions.ATTRIBUTE.name(),
                    SortingHelper.JewelOptions.ATTRIBUTE_VALUE.name(),
                    SortingHelper.JewelOptions.SIZE.name(),
                    SortingHelper.JewelOptions.LEVEL.name()),
                entry -> entry instanceof String value &&
                    Enums.getIfPresent(SortingHelper.JewelOptions.class, value.toUpperCase()).isPresent());

        this.jewelSortingByAmount = this.builder.
            comment("The order of Jewels if they are sorted by the amount/size.").
            comment("Supported Values: NAME, ATTRIBUTE, ATTRIBUTE_VALUE, SIZE, LEVEL, ATTRIBUTE_WEIGHT").
            defineList("jewel_sorting_by_amount",
                Collections.emptyList(),
                entry -> entry instanceof String value &&
                    Enums.getIfPresent(SortingHelper.JewelOptions.class, value.toUpperCase()).isPresent());

        this.jewelSortingByMod = this.builder.
            comment("The order of Jewels if they are sorted by the mod.").
            comment("Supported Values: NAME, ATTRIBUTE, ATTRIBUTE_VALUE, SIZE, LEVEL, ATTRIBUTE_WEIGHT").
            defineList("jewel_sorting_by_mod",
                Collections.emptyList(),
                entry -> entry instanceof String value &&
                    Enums.getIfPresent(SortingHelper.JewelOptions.class, value.toUpperCase()).isPresent());

        this.builder.pop();

        this.builder.comment("This category holds options how Gear are sorted");
        this.builder.push("Gear Sorting");

        this.gearSortingByName = this.builder.
            comment("The order of Gear if they are sorted by the name.").
            comment("Supported Values: NAME, STATE, RARITY, LEVEL, MODEL").
            defineList("gear_sorting_by_name",
                Arrays.asList(SortingHelper.GearOptions.NAME.name(),
                    SortingHelper.GearOptions.STATE.name(),
                    SortingHelper.GearOptions.RARITY.name(),
                    SortingHelper.GearOptions.LEVEL.name(),
                    SortingHelper.GearOptions.MODEL.name()),
                entry -> entry instanceof String value &&
                    Enums.getIfPresent(SortingHelper.GearOptions.class, value.toUpperCase()).isPresent());

        this.gearSortingByAmount = this.builder.
            comment("The order of Gear if they are sorted by the amount/size.").
            comment("Supported Values: NAME, STATE, RARITY, LEVEL, MODEL").
            defineList("gear_sorting_by_amount",
                Collections.emptyList(),
                entry -> entry instanceof String value &&
                    Enums.getIfPresent(SortingHelper.GearOptions.class, value.toUpperCase()).isPresent());

        this.gearSortingByMod = this.builder.
            comment("The order of Gear if they are sorted by the mod.").
            comment("Supported Values: NAME, STATE, RARITY, LEVEL, MODEL").
            defineList("gear_sorting_by_mod",
                Collections.emptyList(),
                entry -> entry instanceof String value &&
                    Enums.getIfPresent(SortingHelper.GearOptions.class, value.toUpperCase()).isPresent());

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

        this.builder.comment("This category holds options how Inscriptions are sorted");
        this.builder.push("Inscription Sorting");

        this.inscriptionSortingByName = this.builder.
            comment("The order of Inscriptions if they are sorted by the name.").
            comment("Supported Values: NAME, INSTABILITY, TIME, COMPLETION, ROOMS").
            defineList("inscription_sorting_by_name",
                Arrays.asList(SortingHelper.InscriptionOptions.NAME.name(),
                    SortingHelper.InscriptionOptions.ROOMS.name(),
                    SortingHelper.InscriptionOptions.TIME.name(),
                    SortingHelper.InscriptionOptions.COMPLETION.name(),
                    SortingHelper.InscriptionOptions.INSTABILITY.name()),
                entry -> entry instanceof String value &&
                    Enums.getIfPresent(SortingHelper.InscriptionOptions.class, value.toUpperCase()).isPresent());

        this.inscriptionSortingByAmount = this.builder.
            comment("The order of Inscriptions if they are sorted by the amount/size.").
            comment("Supported Values: NAME, INSTABILITY, TIME, COMPLETION, ROOMS").
            defineList("inscription_sorting_by_amount",
                Collections.emptyList(),
                entry -> entry instanceof String value &&
                    Enums.getIfPresent(SortingHelper.InscriptionOptions.class, value.toUpperCase()).isPresent());

        this.inscriptionSortingByMod = this.builder.
            comment("The order of Inscriptions if they are sorted by the mod.").
            comment("Supported Values: NAME, INSTABILITY, TIME, COMPLETION, ROOMS").
            defineList("inscription_sorting_by_mod",
                Collections.emptyList(),
                entry -> entry instanceof String value &&
                    Enums.getIfPresent(SortingHelper.InscriptionOptions.class, value.toUpperCase()).isPresent());

        this.builder.pop();

        this.builder.comment("This category holds options how Vault Crystals are sorted");
        this.builder.push("Vault Crystal Sorting");

        this.vaultCrystalSortingByName = this.builder.
            comment("The order of Vault Crystal if they are sorted by the name.").
            comment("Supported Values: NAME, LEVEL, TYPE").
            defineList("crystal_sorting_by_name",
                Arrays.asList(SortingHelper.CrystalOptions.NAME.name(),
                    SortingHelper.CrystalOptions.TYPE.name(),
                    SortingHelper.CrystalOptions.LEVEL.name()),
                entry -> entry instanceof String value &&
                    Enums.getIfPresent(SortingHelper.CrystalOptions.class, value.toUpperCase()).isPresent());

        this.vaultCrystalSortingByAmount = this.builder.
            comment("The order of Vault Crystal if they are sorted by the amount/size.").
            comment("Supported Values: NAME, LEVEL, TYPE").
            defineList("crystal_sorting_by_amount",
                Collections.emptyList(),
                entry -> entry instanceof String value &&
                    Enums.getIfPresent(SortingHelper.CrystalOptions.class, value.toUpperCase()).isPresent());

        this.vaultCrystalSortingByMod = this.builder.
            comment("The order of Vault Crystal if they are sorted by the mod.").
            comment("Supported Values: NAME, LEVEL, TYPE").
            defineList("crystal_sorting_by_mod",
                Collections.emptyList(),
                entry -> entry instanceof String value &&
                    Enums.getIfPresent(SortingHelper.CrystalOptions.class, value.toUpperCase()).isPresent());

        this.builder.pop();

        Configuration.GENERAL_SPEC = this.builder.build();
    }


    /**
     * Gets jewel sorting by name.
     *
     * @return the jewel sorting by name
     */
    public List<SortingHelper.JewelOptions> getJewelSortingByName()
    {
        return this.convertStringToJewelEnum(this.jewelSortingByName.get());
    }


    /**
     * Gets jewel sorting by amount.
     *
     * @return the jewel sorting by amount
     */
    public List<SortingHelper.JewelOptions> getJewelSortingByAmount()
    {
        return this.convertStringToJewelEnum(this.jewelSortingByAmount.get());
    }


    /**
     * Gets jewel sorting by mod.
     *
     * @return the jewel sorting by mod
     */
    public List<SortingHelper.JewelOptions> getJewelSortingByMod()
    {
        return this.convertStringToJewelEnum(this.jewelSortingByMod.get());
    }


    /**
     * Gets gear sorting by name.
     *
     * @return the gear sorting by name
     */
    public List<SortingHelper.GearOptions> getGearSortingByName()
    {
        return this.convertStringToGearEnum(this.gearSortingByName.get());
    }


    /**
     * Gets gear sorting by amount.
     *
     * @return the gear sorting by amount
     */
    public List<SortingHelper.GearOptions> getGearSortingByAmount()
    {
        return this.convertStringToGearEnum(this.gearSortingByAmount.get());
    }


    /**
     * Gets gear sorting by mod.
     *
     * @return the gear sorting by mod
     */
    public List<SortingHelper.GearOptions> getGearSortingByMod()
    {
        return this.convertStringToGearEnum(this.gearSortingByMod.get());
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
     * Inscription sorting by name.
     *
     * @return the Inscription sorting by name
     */
    public List<SortingHelper.InscriptionOptions> getInscriptionSortingByName()
    {
        return this.convertStringToInscriptionEnum(this.inscriptionSortingByName.get());
    }


    /**
     * Gets inscription sorting by amount.
     *
     * @return the inscription sorting by amount
     */
    public List<SortingHelper.InscriptionOptions> getInscriptionSortingByAmount()
    {
        return this.convertStringToInscriptionEnum(this.inscriptionSortingByAmount.get());
    }


    /**
     * Gets inscription sorting by mod.
     *
     * @return the inscription sorting by mod
     */
    public List<SortingHelper.InscriptionOptions> getInscriptionSortingByMod()
    {
        return this.convertStringToInscriptionEnum(this.inscriptionSortingByMod.get());
    }


    /**
     * Gets vault crystal sorting by name.
     *
     * @return the vault crystal sorting by name
     */
    public List<SortingHelper.CrystalOptions> getVaultCrystalSortingByName()
    {
        return this.convertStringToCrystalEnum(this.vaultCrystalSortingByName.get());
    }


    /**
     * Gets vault crystal sorting by amount.
     *
     * @return the vault crystal sorting by amount
     */
    public List<SortingHelper.CrystalOptions> getVaultCrystalSortingByAmount()
    {
        return this.convertStringToCrystalEnum(this.vaultCrystalSortingByAmount.get());
    }


    /**
     * Gets vault crystal sorting by mod.
     *
     * @return the vault crystal sorting by mod
     */
    public List<SortingHelper.CrystalOptions> getVaultCrystalSortingByMod()
    {
        return this.convertStringToCrystalEnum(this.vaultCrystalSortingByMod.get());
    }


    /**
     * This method converts String list to Enum list.
     * @param value The string list that need to be converted.
     * @return Converted Enum list.
     */
    private List<SortingHelper.JewelOptions> convertStringToJewelEnum(List<? extends String> value)
    {
        return value.stream().
            map(String::toUpperCase).
            filter(upperCase -> Enums.getIfPresent(SortingHelper.JewelOptions.class, upperCase).isPresent()).
            map(SortingHelper.JewelOptions::valueOf).
            distinct().
            toList();
    }


    /**
     * This method converts String list to Enum list.
     * @param value The string list that need to be converted.
     * @return Converted Enum list.
     */
    private List<SortingHelper.GearOptions> convertStringToGearEnum(List<? extends String> value)
    {
        return value.stream().
            map(String::toUpperCase).
            filter(upperCase -> Enums.getIfPresent(SortingHelper.GearOptions.class, upperCase).isPresent()).
            map(SortingHelper.GearOptions::valueOf).
            distinct().
            toList();
    }


    /**
     * This method converts String list to Enum list.
     * @param value The string list that need to be converted.
     * @return Converted Enum list.
     */
    private List<SortingHelper.InscriptionOptions> convertStringToInscriptionEnum(List<? extends String> value)
    {
        return value.stream().
            map(String::toUpperCase).
            filter(upperCase -> Enums.getIfPresent(SortingHelper.InscriptionOptions.class, upperCase).isPresent()).
            map(SortingHelper.InscriptionOptions::valueOf).
            distinct().
            toList();
    }


    /**
     * This method converts String list to Enum list.
     * @param value The string list that need to be converted.
     * @return Converted Enum list.
     */
    private List<SortingHelper.CrystalOptions> convertStringToCrystalEnum(List<? extends String> value)
    {
        return value.stream().
            map(String::toUpperCase).
            filter(upperCase -> Enums.getIfPresent(SortingHelper.CrystalOptions.class, upperCase).isPresent()).
            map(SortingHelper.CrystalOptions::valueOf).
            distinct().
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

    /**
     * The config value for inscription sorting by name.
     */
    private final ForgeConfigSpec.ConfigValue<List<? extends String>> inscriptionSortingByName;

    /**
     * The config value for inscription sorting by amount.
     */
    private final ForgeConfigSpec.ConfigValue<List<? extends String>> inscriptionSortingByAmount;

    /**
     * The config value for inscription sorting by mod.
     */
    private final ForgeConfigSpec.ConfigValue<List<? extends String>> inscriptionSortingByMod;

    /**
     * The config value for vault crystal sorting by name.
     */
    private final ForgeConfigSpec.ConfigValue<List<? extends String>> vaultCrystalSortingByName;

    /**
     * The config value for vault crystal sorting by amount.
     */
    private final ForgeConfigSpec.ConfigValue<List<? extends String>> vaultCrystalSortingByAmount;

    /**
     * The config value for vault crystal sorting by mod.
     */
    private final ForgeConfigSpec.ConfigValue<List<? extends String>> vaultCrystalSortingByMod;

    /**
     * The general config spec.
     */
    public static ForgeConfigSpec GENERAL_SPEC;
}
