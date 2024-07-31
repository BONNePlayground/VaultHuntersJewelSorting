//
// Created by BONNe
// Copyright - 2023
//


package lv.id.bonne.vaulthunters.jewelsorting.config;


import com.google.common.base.Enums;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import lv.id.bonne.vaulthunters.jewelsorting.utils.SortingHelper;
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
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

        builder.comment("This category holds options how Jewels are sorted");
        builder.push("Jewel Sorting");

        this.jewelSortingByName = builder.
            comment("The order of Jewels if they are sorted by the name.").
            comment("Supported Values: NAME, ATTRIBUTE, ATTRIBUTE_VALUE, SIZE, LEVEL, ATTRIBUTE_WEIGHT, CUTS").
            defineList("jewel_sorting_by_name",
                Arrays.asList(SortingHelper.JewelOptions.NAME.name(),
                    SortingHelper.JewelOptions.ATTRIBUTE.name(),
                    SortingHelper.JewelOptions.ATTRIBUTE_VALUE.name(),
                    SortingHelper.JewelOptions.SIZE.name(),
                    SortingHelper.JewelOptions.LEVEL.name(),
                    SortingHelper.JewelOptions.CUTS.name()),
                entry -> entry instanceof String value &&
                    Enums.getIfPresent(SortingHelper.JewelOptions.class, value.toUpperCase()).isPresent());

        this.jewelSortingByAmount = builder.
            comment("The order of Jewels if they are sorted by the amount/size.").
            comment("Supported Values: NAME, ATTRIBUTE, ATTRIBUTE_VALUE, SIZE, LEVEL, ATTRIBUTE_WEIGHT, CUTS").
            defineList("jewel_sorting_by_amount",
                Collections.emptyList(),
                entry -> entry instanceof String value &&
                    Enums.getIfPresent(SortingHelper.JewelOptions.class, value.toUpperCase()).isPresent());

        this.jewelSortingByMod = builder.
            comment("The order of Jewels if they are sorted by the mod.").
            comment("Supported Values: NAME, ATTRIBUTE, ATTRIBUTE_VALUE, SIZE, LEVEL, ATTRIBUTE_WEIGHT, CUTS").
            defineList("jewel_sorting_by_mod",
                Collections.emptyList(),
                entry -> entry instanceof String value &&
                    Enums.getIfPresent(SortingHelper.JewelOptions.class, value.toUpperCase()).isPresent());

        builder.pop();

        builder.comment("This category holds options how Gear are sorted");
        builder.push("Gear Sorting");

        this.gearSortingByName = builder.
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

        this.gearSortingByAmount = builder.
            comment("The order of Gear if they are sorted by the amount/size.").
            comment("Supported Values: NAME, STATE, RARITY, LEVEL, MODEL").
            defineList("gear_sorting_by_amount",
                Collections.emptyList(),
                entry -> entry instanceof String value &&
                    Enums.getIfPresent(SortingHelper.GearOptions.class, value.toUpperCase()).isPresent());

        this.gearSortingByMod = builder.
            comment("The order of Gear if they are sorted by the mod.").
            comment("Supported Values: NAME, STATE, RARITY, LEVEL, MODEL").
            defineList("gear_sorting_by_mod",
                Collections.emptyList(),
                entry -> entry instanceof String value &&
                    Enums.getIfPresent(SortingHelper.GearOptions.class, value.toUpperCase()).isPresent());

        this.rarityOrder = builder.
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

        builder.pop();

        builder.comment("This category holds options how Inscriptions are sorted");
        builder.push("Inscription Sorting");

        this.inscriptionSortingByName = builder.
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

        this.inscriptionSortingByAmount = builder.
            comment("The order of Inscriptions if they are sorted by the amount/size.").
            comment("Supported Values: NAME, INSTABILITY, TIME, COMPLETION, ROOMS").
            defineList("inscription_sorting_by_amount",
                Collections.emptyList(),
                entry -> entry instanceof String value &&
                    Enums.getIfPresent(SortingHelper.InscriptionOptions.class, value.toUpperCase()).isPresent());

        this.inscriptionSortingByMod = builder.
            comment("The order of Inscriptions if they are sorted by the mod.").
            comment("Supported Values: NAME, INSTABILITY, TIME, COMPLETION, ROOMS").
            defineList("inscription_sorting_by_mod",
                Collections.emptyList(),
                entry -> entry instanceof String value &&
                    Enums.getIfPresent(SortingHelper.InscriptionOptions.class, value.toUpperCase()).isPresent());

        builder.pop();

        builder.comment("This category holds options how Vault Crystals are sorted");
        builder.push("Vault Crystal Sorting");

        this.vaultCrystalSortingByName = builder.
            comment("The order of Vault Crystal if they are sorted by the name.").
            comment("Supported Values: NAME, LEVEL, TYPE").
            defineList("crystal_sorting_by_name",
                Arrays.asList(SortingHelper.CrystalOptions.NAME.name(),
                    SortingHelper.CrystalOptions.TYPE.name(),
                    SortingHelper.CrystalOptions.LEVEL.name()),
                entry -> entry instanceof String value &&
                    Enums.getIfPresent(SortingHelper.CrystalOptions.class, value.toUpperCase()).isPresent());

        this.vaultCrystalSortingByAmount = builder.
            comment("The order of Vault Crystal if they are sorted by the amount/size.").
            comment("Supported Values: NAME, LEVEL, TYPE").
            defineList("crystal_sorting_by_amount",
                Collections.emptyList(),
                entry -> entry instanceof String value &&
                    Enums.getIfPresent(SortingHelper.CrystalOptions.class, value.toUpperCase()).isPresent());

        this.vaultCrystalSortingByMod = builder.
            comment("The order of Vault Crystal if they are sorted by the mod.").
            comment("Supported Values: NAME, LEVEL, TYPE").
            defineList("crystal_sorting_by_mod",
                Collections.emptyList(),
                entry -> entry instanceof String value &&
                    Enums.getIfPresent(SortingHelper.CrystalOptions.class, value.toUpperCase()).isPresent());

        builder.pop();

        builder.comment("This category holds options how Trinkets are sorted");
        builder.push("Trinkets Sorting");

        this.trinketSortingByName = builder.
            comment("The order of Trinkets if they are sorted by the name.").
            comment("Supported Values: NAME, SLOT, TYPE, USES").
            defineList("trinket_sorting_by_name",
                Arrays.asList(SortingHelper.TrinketOptions.NAME.name(),
                    SortingHelper.TrinketOptions.SLOT.name(),
                    SortingHelper.TrinketOptions.TYPE.name(),
                    SortingHelper.TrinketOptions.USES.name()),
                entry -> entry instanceof String value &&
                    Enums.getIfPresent(SortingHelper.TrinketOptions.class, value.toUpperCase()).isPresent());

        this.trinketSortingByAmount = builder.
            comment("The order of Trinkets if they are sorted by the amount/size.").
            comment("Supported Values: NAME, SLOT, TYPE, USES").
            defineList("trinket_sorting_by_amount",
                Collections.emptyList(),
                entry -> entry instanceof String value &&
                    Enums.getIfPresent(SortingHelper.TrinketOptions.class, value.toUpperCase()).isPresent());

        this.trinketSortingByMod = builder.
            comment("The order of Trinkets if they are sorted by the mod.").
            comment("Supported Values: NAME, SLOT, TYPE, USES").
            defineList("trinket_sorting_by_mod",
                Collections.emptyList(),
                entry -> entry instanceof String value &&
                    Enums.getIfPresent(SortingHelper.TrinketOptions.class, value.toUpperCase()).isPresent());

        builder.pop();

        builder.comment("This category holds options how Vault Dolls are sorted");
        builder.push("Vault Doll Sorting");

        this.dollSortingByName = builder.
            comment("The order of dolls if they are sorted by the name.").
            comment("Supported Values: NAME, OWNER, COMPLETED, XP, LOOT").
            defineList("vault_doll_sorting_by_name",
                Arrays.asList(SortingHelper.DollOptions.NAME.name(),
                    SortingHelper.DollOptions.COMPLETED.name(),
                    SortingHelper.DollOptions.OWNER.name(),
                    SortingHelper.DollOptions.LOOT.name(),
                    SortingHelper.DollOptions.XP.name()),
                entry -> entry instanceof String value &&
                    Enums.getIfPresent(SortingHelper.DollOptions.class, value.toUpperCase()).isPresent());

        this.dollSortingByAmount = builder.
            comment("The order of dolls if they are sorted by the amount/size.").
            comment("Supported Values: NAME, OWNER, COMPLETED, XP, LOOT").
            defineList("vault_doll_sorting_by_amount",
                Collections.emptyList(),
                entry -> entry instanceof String value &&
                    Enums.getIfPresent(SortingHelper.DollOptions.class, value.toUpperCase()).isPresent());

        this.dollSortingByMod = builder.
            comment("The order of dolls if they are sorted by the mod.").
            comment("Supported Values: NAME, OWNER, COMPLETED, XP, LOOT").
            defineList("vault_doll_sorting_by_mod",
                Collections.emptyList(),
                entry -> entry instanceof String value &&
                    Enums.getIfPresent(SortingHelper.DollOptions.class, value.toUpperCase()).isPresent());

        builder.pop();

        builder.comment("This category holds options how Vault Charms are sorted");
        builder.push("Vault Charm Sorting");

        this.charmSortingByName = builder.
            comment("The order of charms if they are sorted by the name.").
            comment("Supported Values: NAME, USES, VALUE").
            defineList("charm_sorting_by_name",
                Arrays.asList(SortingHelper.CharmOptions.NAME.name(),
                    SortingHelper.CharmOptions.USES.name(),
                    SortingHelper.CharmOptions.VALUE.name()),
                entry -> entry instanceof String value &&
                    Enums.getIfPresent(SortingHelper.CharmOptions.class, value.toUpperCase()).isPresent());

        this.charmSortingByAmount = builder.
            comment("The order of charms if they are sorted by the amount/size.").
            comment("Supported Values: NAME, USES, VALUE").
            defineList("charm_sorting_by_amount",
                Collections.emptyList(),
                entry -> entry instanceof String value &&
                    Enums.getIfPresent(SortingHelper.CharmOptions.class, value.toUpperCase()).isPresent());

        this.charmSortingByMod = builder.
            comment("The order of charms if they are sorted by the mod.").
            comment("Supported Values: NAME, USES, VALUE").
            defineList("charm_sorting_by_mod",
                Collections.emptyList(),
                entry -> entry instanceof String value &&
                    Enums.getIfPresent(SortingHelper.CharmOptions.class, value.toUpperCase()).isPresent());

        builder.pop();

        builder.comment("This category holds options how Infused Catalysts are sorted");
        builder.push("Infused Catalysts Sorting");

        this.catalystSortingByName = builder.
            comment("The order of Infused Catalysts if they are sorted by the name.").
            comment("Supported Values: NAME, SIZE, MODIFIER").
            defineList("catalyst_sorting_by_name",
                Arrays.asList(SortingHelper.CatalystOptions.NAME.name(),
                    SortingHelper.CatalystOptions.MODIFIER.name(),
                    SortingHelper.CatalystOptions.SIZE.name()),
                entry -> entry instanceof String value &&
                    Enums.getIfPresent(SortingHelper.CatalystOptions.class, value.toUpperCase()).isPresent());

        this.catalystSortingByAmount = builder.
            comment("The order of Infused Catalysts if they are sorted by the amount/size.").
            comment("Supported Values: NAME, SIZE, MODIFIER").
            defineList("catalyst_sorting_by_amount",
                Collections.emptyList(),
                entry -> entry instanceof String value &&
                    Enums.getIfPresent(SortingHelper.CatalystOptions.class, value.toUpperCase()).isPresent());

        this.catalystSortingByMod = builder.
            comment("The order of Infused Catalysts if they are sorted by the mod.").
            comment("Supported Values: NAME, SIZE, MODIFIER").
            defineList("catalyst_sorting_by_mod",
                Collections.emptyList(),
                entry -> entry instanceof String value &&
                    Enums.getIfPresent(SortingHelper.CatalystOptions.class, value.toUpperCase()).isPresent());

        builder.pop();

        Configuration.GENERAL_SPEC = builder.build();
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
     * Gets Trinket sorting by name.
     *
     * @return the Trinket sorting by name
     */
    public List<SortingHelper.TrinketOptions> getTrinketSortingByName()
    {
        return this.convertStringToTrinketEnum(this.trinketSortingByName.get());
    }


    /**
     * Gets Trinket sorting by amount.
     *
     * @return the Trinket sorting by amount
     */
    public List<SortingHelper.TrinketOptions> getTrinketSortingByAmount()
    {
        return this.convertStringToTrinketEnum(this.trinketSortingByAmount.get());
    }


    /**
     * Gets Trinket sorting by mod.
     *
     * @return the Trinket sorting by mod
     */
    public List<SortingHelper.TrinketOptions> getTrinketSortingByMod()
    {
        return this.convertStringToTrinketEnum(this.trinketSortingByMod.get());
    }


    /**
     * Gets Doll sorting by name.
     *
     * @return the Doll sorting by name
     */
    public List<SortingHelper.DollOptions> getDollSortingByName()
    {
        return this.convertStringToDollEnum(this.dollSortingByName.get());
    }


    /**
     * Gets Doll sorting by amount.
     *
     * @return the Doll sorting by amount
     */
    public List<SortingHelper.DollOptions> getDollSortingByAmount()
    {
        return this.convertStringToDollEnum(this.dollSortingByAmount.get());
    }


    /**
     * Gets Doll sorting by mod.
     *
     * @return the Doll sorting by mod
     */
    public List<SortingHelper.DollOptions> getDollSortingByMod()
    {
        return this.convertStringToDollEnum(this.dollSortingByMod.get());
    }


    /**
     * Gets Doll sorting by name.
     *
     * @return the Doll sorting by name
     */
    public List<SortingHelper.CharmOptions> getCharmSortingByName()
    {
        return this.convertStringToCharmEnum(this.charmSortingByName.get());
    }


    /**
     * Gets Charm sorting by amount.
     *
     * @return the Charm sorting by amount
     */
    public List<SortingHelper.CharmOptions> getCharmSortingByAmount()
    {
        return this.convertStringToCharmEnum(this.charmSortingByAmount.get());
    }


    /**
     * Gets Charm sorting by mod.
     *
     * @return the Charm sorting by mod
     */
    public List<SortingHelper.CharmOptions> getCharmSortingByMod()
    {
        return this.convertStringToCharmEnum(this.charmSortingByMod.get());
    }


    /**
     * Gets catalyst sorting by name.
     *
     * @return the catalyst sorting by name
     */
    public List<SortingHelper.CatalystOptions> getCatalystSortingByName()
    {
        return this.convertStringToCatalystEnum(this.catalystSortingByName.get());
    }


    /**
     * Gets catalyst sorting by amount.
     *
     * @return the catalyst sorting by amount
     */
    public List<SortingHelper.CatalystOptions> getCatalystSortingByAmount()
    {
        return this.convertStringToCatalystEnum(this.catalystSortingByAmount.get());
    }


    /**
     * Gets catalyst sorting by mod.
     *
     * @return the catalyst sorting by mod
     */
    public List<SortingHelper.CatalystOptions> getCatalystSortingByMod()
    {
        return this.convertStringToCatalystEnum(this.catalystSortingByMod.get());
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


    /**
     * This method converts String list to Enum list.
     * @param value The string list that need to be converted.
     * @return Converted Enum list.
     */
    private List<SortingHelper.TrinketOptions> convertStringToTrinketEnum(List<? extends String> value)
    {
        return value.stream().
            map(String::toUpperCase).
            filter(upperCase -> Enums.getIfPresent(SortingHelper.TrinketOptions.class, upperCase).isPresent()).
            map(SortingHelper.TrinketOptions::valueOf).
            distinct().
            toList();
    }


    /**
     * This method converts String list to Enum list.
     * @param value The string list that need to be converted.
     * @return Converted Enum list.
     */
    private List<SortingHelper.DollOptions> convertStringToDollEnum(List<? extends String> value)
    {
        return value.stream().
            map(String::toUpperCase).
            filter(upperCase -> Enums.getIfPresent(SortingHelper.DollOptions.class, upperCase).isPresent()).
            map(SortingHelper.DollOptions::valueOf).
            distinct().
            toList();
    }


    /**
     * This method converts String list to Enum list.
     * @param value The string list that need to be converted.
     * @return Converted Enum list.
     */
    private List<SortingHelper.CharmOptions> convertStringToCharmEnum(List<? extends String> value)
    {
        return value.stream().
            map(String::toUpperCase).
            filter(upperCase -> Enums.getIfPresent(SortingHelper.CharmOptions.class, upperCase).isPresent()).
            map(SortingHelper.CharmOptions::valueOf).
            distinct().
            toList();
    }


    /**
     * This method converts String list to Enum list.
     * @param value The string list that need to be converted.
     * @return Converted Enum list.
     */
    private List<SortingHelper.CatalystOptions> convertStringToCatalystEnum(List<? extends String> value)
    {
        return value.stream().
            map(String::toUpperCase).
            filter(upperCase -> Enums.getIfPresent(SortingHelper.CatalystOptions.class, upperCase).isPresent()).
            map(SortingHelper.CatalystOptions::valueOf).
            distinct().
            toList();
    }


// ---------------------------------------------------------------------
// Section: Variables
// ---------------------------------------------------------------------


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
     * The config value for trinket sorting by name.
     */
    private final ForgeConfigSpec.ConfigValue<List<? extends String>> trinketSortingByName;

    /**
     * The config value for trinket sorting by amount.
     */
    private final ForgeConfigSpec.ConfigValue<List<? extends String>> trinketSortingByAmount;

    /**
     * The config value for trinket sorting by mod.
     */
    private final ForgeConfigSpec.ConfigValue<List<? extends String>> trinketSortingByMod;

    /**
     * The config value for vault doll sorting by name.
     */
    private final ForgeConfigSpec.ConfigValue<List<? extends String>> dollSortingByName;

    /**
     * The config value for vault doll sorting by amount.
     */
    private final ForgeConfigSpec.ConfigValue<List<? extends String>> dollSortingByAmount;

    /**
     * The config value for vault doll sorting by mod.
     */
    private final ForgeConfigSpec.ConfigValue<List<? extends String>> dollSortingByMod;

    /**
     * The config value for vault charm sorting by name.
     */
    private final ForgeConfigSpec.ConfigValue<List<? extends String>> charmSortingByName;

    /**
     * The config value for vault charm sorting by amount.
     */
    private final ForgeConfigSpec.ConfigValue<List<? extends String>> charmSortingByAmount;

    /**
     * The config value for vault charm sorting by mod.
     */
    private final ForgeConfigSpec.ConfigValue<List<? extends String>> charmSortingByMod;


    /**
     * The config value for infused catalyst sorting by name.
     */
    private final ForgeConfigSpec.ConfigValue<List<? extends String>> catalystSortingByName;

    /**
     * The config value for infused catalyst sorting by amount.
     */
    private final ForgeConfigSpec.ConfigValue<List<? extends String>> catalystSortingByAmount;

    /**
     * The config value for infused catalyst sorting by mod.
     */
    private final ForgeConfigSpec.ConfigValue<List<? extends String>> catalystSortingByMod;

    /**
     * The general config spec.
     */
    public static ForgeConfigSpec GENERAL_SPEC;
}
