//
// Created by BONNe
// Copyright - 2023
//


package lv.id.bonne.vaulthunters.jewelsorting.ae2.mixin;


import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import java.util.Comparator;

import appeng.api.config.SortDir;
import appeng.api.config.SortOrder;
import appeng.api.stacks.AEKey;
import appeng.client.gui.me.common.Repo;
import appeng.menu.me.common.GridInventoryEntry;
import iskallia.vault.gear.data.AttributeGearData;
import iskallia.vault.gear.data.VaultGearData;
import iskallia.vault.init.ModItems;
import iskallia.vault.item.crystal.CrystalData;
import iskallia.vault.item.data.InscriptionData;
import lv.id.bonne.vaulthunters.jewelsorting.VaultJewelSorting;
import lv.id.bonne.vaulthunters.jewelsorting.utils.CustomVaultGearData;
import lv.id.bonne.vaulthunters.jewelsorting.utils.SortingHelper;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;


/**
 * This mixin handles custom jewel sorting order for ae2.
 */
@Mixin(Repo.class)
public abstract class MixinRepo
{
    @Inject(method = "getComparator",
        at = @At("RETURN"),
        cancellable = true,
        remap = false)
    public final void comparator(SortOrder sortOrder,
        SortDir sortDir,
        CallbackInfoReturnable<Comparator<GridInventoryEntry>> cir)
    {
        if (Screen.hasShiftDown())
        {
            return;
        }

        boolean ascending = sortDir == SortDir.ASCENDING;

        cir.setReturnValue(cir.getReturnValue().thenComparing((left, right) ->
        {
            AEKey leftWhat = left.getWhat();
            AEKey rightWhat = right.getWhat();

            if (leftWhat == null || rightWhat == null)
            {
                // Null-pointer check
                return leftWhat == null ? 1 : -1;
            }

            String leftName = leftWhat.getDisplayName().getString();
            String rightName = rightWhat.getDisplayName().getString();

            if (!leftWhat.getModId().equals(rightWhat.getModId()))
            {
                // some small cleanup. We want to sort only vault items.
                return String.CASE_INSENSITIVE_ORDER.compare(
                    leftWhat.getModId(),
                    rightWhat.getModId());
            }

            final ResourceLocation leftId = leftWhat.getId();

            int registryOrder = SortingHelper.compareRegistryNames(
                leftId,
                rightWhat.getId(),
                ascending);

            if (registryOrder != 0 || !SortingHelper.isSortable(leftId))
            {
                // Use default string comparing
                return registryOrder;
            }
            else if (leftId == ModItems.JEWEL.getRegistryName())
            {
                CompoundTag leftTag = leftWhat.toTag().getCompound("tag");
                CompoundTag rightTag = rightWhat.toTag().getCompound("tag");

                if (!leftTag.contains("clientCache") ||
                    !leftTag.getCompound("clientCache").contains(SortingHelper.EXTRA_ATTRIBUTE_INDEX) ||
                    !leftTag.getCompound("clientCache").contains(SortingHelper.EXTRA_ATTRIBUTE_VALUE) ||
                    !leftTag.getCompound("clientCache").contains(SortingHelper.EXTRA_GEAR_LEVEL) ||
                    !leftTag.getCompound("clientCache").contains(SortingHelper.EXTRA_JEWEL_SIZE) ||
                    !(leftTag.getCompound("clientCache").contains(SortingHelper.EXTRA_CACHE_VERSION) &&
                        leftTag.getCompound("clientCache").getString(SortingHelper.EXTRA_CACHE_VERSION).
                            equals(VaultJewelSorting.VAULT_MOD_VERSION)) ||
                    !rightTag.contains("clientCache") ||
                    !rightTag.getCompound("clientCache").contains(SortingHelper.EXTRA_ATTRIBUTE_INDEX) ||
                    !rightTag.getCompound("clientCache").contains(SortingHelper.EXTRA_ATTRIBUTE_VALUE) ||
                    !rightTag.getCompound("clientCache").contains(SortingHelper.EXTRA_GEAR_LEVEL) ||
                    !rightTag.getCompound("clientCache").contains(SortingHelper.EXTRA_JEWEL_SIZE) ||
                    !(rightTag.getCompound("clientCache").contains(SortingHelper.EXTRA_CACHE_VERSION) &&
                        rightTag.getCompound("clientCache").getString(SortingHelper.EXTRA_CACHE_VERSION).
                            equals(VaultJewelSorting.VAULT_MOD_VERSION)))
                {
                    // Client cache is not generated. Process everything manually.
                    VaultGearData leftData = CustomVaultGearData.read(leftTag);
                    VaultGearData rightData = CustomVaultGearData.read(rightTag);

                    return switch (sortOrder) {
                        case NAME -> SortingHelper.compareJewels(leftName,
                            leftData,
                            leftTag.getInt("freeCuts"),
                            rightName,
                            rightData,
                            rightTag.getInt("freeCuts"),
                            VaultJewelSorting.CONFIGURATION.getJewelSortingByName(),
                            ascending);
                        case AMOUNT -> SortingHelper.compareJewels(leftName,
                            leftData,
                            leftTag.getInt("freeCuts"),
                            rightName,
                            rightData,
                            rightTag.getInt("freeCuts"),
                            VaultJewelSorting.CONFIGURATION.getJewelSortingByAmount(),
                            ascending);
                        case MOD -> SortingHelper.compareJewels(leftName,
                            leftData,
                            leftTag.getInt("freeCuts"),
                            rightName,
                            rightData,
                            rightTag.getInt("freeCuts"),
                            VaultJewelSorting.CONFIGURATION.getJewelSortingByMod(),
                            ascending);
                    };
                }
                else
                {
                    return switch (sortOrder) {
                        case NAME -> SortingHelper.compareJewels(leftName,
                            leftTag.getCompound("clientCache"),
                            leftTag.getInt("freeCuts"),
                            rightName,
                            rightTag.getCompound("clientCache"),
                            rightTag.getInt("freeCuts"),
                            VaultJewelSorting.CONFIGURATION.getJewelSortingByName(),
                            ascending);
                        case AMOUNT -> SortingHelper.compareJewels(leftName,
                            leftTag.getCompound("clientCache"),
                            leftTag.getInt("freeCuts"),
                            rightName,
                            rightTag.getCompound("clientCache"),
                            rightTag.getInt("freeCuts"),
                            VaultJewelSorting.CONFIGURATION.getJewelSortingByAmount(),
                            ascending);
                        case MOD -> SortingHelper.compareJewels(leftName,
                            leftTag.getCompound("clientCache"),
                            leftTag.getInt("freeCuts"),
                            rightName,
                            rightTag.getCompound("clientCache"),
                            rightTag.getInt("freeCuts"),
                            VaultJewelSorting.CONFIGURATION.getJewelSortingByMod(),
                            ascending);
                    };
                }
            }
            else if (leftId == ModItems.INSCRIPTION.getRegistryName())
            {
                InscriptionData leftData = InscriptionData.empty();
                leftData.deserializeNBT(leftWhat.toTag().getCompound("tag").getCompound("data"));

                InscriptionData rightData = InscriptionData.empty();
                rightData.deserializeNBT(rightWhat.toTag().getCompound("tag").getCompound("data"));

                return switch (sortOrder) {
                    case NAME -> SortingHelper.compareInscriptions(leftName,
                        leftData,
                        rightName,
                        rightData,
                        VaultJewelSorting.CONFIGURATION.getInscriptionSortingByName(),
                        ascending);
                    case AMOUNT -> SortingHelper.compareInscriptions(leftName,
                        leftData,
                        rightName,
                        rightData,
                        VaultJewelSorting.CONFIGURATION.getInscriptionSortingByAmount(),
                        ascending);
                    case MOD -> SortingHelper.compareInscriptions(leftName,
                        leftData,
                        rightName,
                        rightData,
                        VaultJewelSorting.CONFIGURATION.getInscriptionSortingByMod(),
                        ascending);
                };
            }
            else if (leftId == ModItems.VAULT_CRYSTAL.getRegistryName())
            {
                CrystalData leftData = CrystalData.empty();
                leftData.readNbt(leftWhat.toTag().getCompound("tag").getCompound("CrystalData"));

                CrystalData rightData = CrystalData.empty();
                rightData.readNbt(rightWhat.toTag().getCompound("tag").getCompound("CrystalData"));

                return switch (sortOrder) {
                    case NAME -> SortingHelper.compareVaultCrystals(leftName,
                        leftData,
                        rightName,
                        rightData,
                        VaultJewelSorting.CONFIGURATION.getVaultCrystalSortingByName(),
                        ascending);
                    case AMOUNT -> SortingHelper.compareVaultCrystals(leftName,
                        leftData,
                        rightName,
                        rightData,
                        VaultJewelSorting.CONFIGURATION.getVaultCrystalSortingByAmount(),
                        ascending);
                    case MOD -> SortingHelper.compareVaultCrystals(leftName,
                        leftData,
                        rightName,
                        rightData,
                        VaultJewelSorting.CONFIGURATION.getVaultCrystalSortingByMod(),
                        ascending);
                };
            }
            else if (leftId == ModItems.TRINKET.getRegistryName())
            {
                AttributeGearData leftData = CustomVaultGearData.read(leftWhat.toTag().getCompound("tag"));
                AttributeGearData rightData = CustomVaultGearData.read(rightWhat.toTag().getCompound("tag"));

                return switch (sortOrder) {
                    case NAME -> SortingHelper.compareTrinkets(leftName,
                        leftData,
                        leftWhat.toTag().getCompound("tag"),
                        rightName,
                        rightData,
                        rightWhat.toTag().getCompound("tag"),
                        VaultJewelSorting.CONFIGURATION.getTrinketSortingByName(),
                        ascending);
                    case AMOUNT -> SortingHelper.compareTrinkets(leftName,
                        leftData,
                        leftWhat.toTag().getCompound("tag"),
                        rightName,
                        rightData,
                        rightWhat.toTag().getCompound("tag"),
                        VaultJewelSorting.CONFIGURATION.getTrinketSortingByAmount(),
                        ascending);
                    case MOD -> SortingHelper.compareTrinkets(leftName,
                        leftData,
                        leftWhat.toTag().getCompound("tag"),
                        rightName,
                        rightData,
                        rightWhat.toTag().getCompound("tag"),
                        VaultJewelSorting.CONFIGURATION.getTrinketSortingByMod(),
                        ascending);
                };
            }
            else if (SortingHelper.VAULT_CHARMS.contains(leftId))
            {
                AttributeGearData leftData = CustomVaultGearData.read(leftWhat.toTag().getCompound("tag"));
                AttributeGearData rightData = CustomVaultGearData.read(rightWhat.toTag().getCompound("tag"));

                return switch (sortOrder) {
                    case NAME -> SortingHelper.compareCharms(leftName,
                        leftData,
                        leftWhat.toTag().getCompound("tag"),
                        rightName,
                        rightData,
                        rightWhat.toTag().getCompound("tag"),
                        VaultJewelSorting.CONFIGURATION.getCharmSortingByName(),
                        ascending);
                    case AMOUNT -> SortingHelper.compareCharms(leftName,
                        leftData,
                        leftWhat.toTag().getCompound("tag"),
                        rightName,
                        rightData,
                        rightWhat.toTag().getCompound("tag"),
                        VaultJewelSorting.CONFIGURATION.getCharmSortingByAmount(),
                        ascending);
                    case MOD -> SortingHelper.compareCharms(leftName,
                        leftData,
                        leftWhat.toTag().getCompound("tag"),
                        rightName,
                        rightData,
                        rightWhat.toTag().getCompound("tag"),
                        VaultJewelSorting.CONFIGURATION.getCharmSortingByMod(),
                        ascending);
                };
            }
            else if (leftId == ModItems.VAULT_CATALYST_INFUSED.getRegistryName())
            {
                return switch (sortOrder) {
                    case NAME -> SortingHelper.compareCatalysts(leftName,
                        leftWhat.toTag().getCompound("tag"),
                        rightName,
                        rightWhat.toTag().getCompound("tag"),
                        VaultJewelSorting.CONFIGURATION.getCatalystSortingByName(),
                        ascending);
                    case AMOUNT -> SortingHelper.compareCatalysts(leftName,
                        leftWhat.toTag().getCompound("tag"),
                        rightName,
                        rightWhat.toTag().getCompound("tag"),
                        VaultJewelSorting.CONFIGURATION.getCatalystSortingByAmount(),
                        ascending);
                    case MOD -> SortingHelper.compareCatalysts(leftName,
                        leftWhat.toTag().getCompound("tag"),
                        rightName,
                        rightWhat.toTag().getCompound("tag"),
                        VaultJewelSorting.CONFIGURATION.getCatalystSortingByMod(),
                        ascending);
                };
            }
            else if (leftId == ModItems.VAULT_DOLL.getRegistryName())
            {
                return switch (sortOrder) {
                    case NAME -> SortingHelper.compareVaultDolls(leftName,
                        leftWhat.toTag().getCompound("tag"),
                        rightName,
                        rightWhat.toTag().getCompound("tag"),
                        VaultJewelSorting.CONFIGURATION.getDollSortingByName(),
                        ascending);
                    case AMOUNT -> SortingHelper.compareVaultDolls(leftName,
                        leftWhat.toTag().getCompound("tag"),
                        rightName,
                        rightWhat.toTag().getCompound("tag"),
                        VaultJewelSorting.CONFIGURATION.getDollSortingByAmount(),
                        ascending);
                    case MOD -> SortingHelper.compareVaultDolls(leftName,
                        leftWhat.toTag().getCompound("tag"),
                        rightName,
                        rightWhat.toTag().getCompound("tag"),
                        VaultJewelSorting.CONFIGURATION.getDollSortingByMod(),
                        ascending);
                };
            }
            else if (leftId == ModItems.RELIC_FRAGMENT.getRegistryName())
            {
                return SortingHelper.compareRelicFragments(
                    leftWhat.toTag().getCompound("tag"),
                    rightWhat.toTag().getCompound("tag"),
                    ascending);
            }
            else if (leftId == ModItems.RESPEC_FLASK.getRegistryName())
            {
                return SortingHelper.compareRespecFlasks(
                    leftWhat.toTag().getCompound("tag"),
                    rightWhat.toTag().getCompound("tag"),
                    ascending);
            }
            else if (leftId == ModItems.FACETED_FOCUS.getRegistryName())
            {
                return SortingHelper.compareFacedFocus(
                    leftWhat.toTag().getCompound("tag"),
                    rightWhat.toTag().getCompound("tag"),
                    ascending);
            }
            else if (leftId == ModItems.AUGMENT.getRegistryName())
            {
                return SortingHelper.compareAugments(
                    leftWhat.toTag().getCompound("tag"),
                    rightWhat.toTag().getCompound("tag"),
                    ascending);
            }
            else if (leftId == ModItems.CARD.getRegistryName())
            {
                return switch (sortOrder) {
                    case NAME -> SortingHelper.compareCards(leftName,
                        leftWhat.toTag().getCompound("tag"),
                        rightName,
                        rightWhat.toTag().getCompound("tag"),
                        VaultJewelSorting.CONFIGURATION.getCardSortingByName(),
                        ascending);
                    case AMOUNT -> SortingHelper.compareCards(leftName,
                        leftWhat.toTag().getCompound("tag"),
                        rightName,
                        rightWhat.toTag().getCompound("tag"),
                        VaultJewelSorting.CONFIGURATION.getCardSortingByAmount(),
                        ascending);
                    case MOD -> SortingHelper.compareCards(leftName,
                        leftWhat.toTag().getCompound("tag"),
                        rightName,
                        rightWhat.toTag().getCompound("tag"),
                        VaultJewelSorting.CONFIGURATION.getCardSortingByMod(),
                        ascending);
                };
            }
            else if (leftId == ModItems.CARD_DECK.getRegistryName())
            {
                return SortingHelper.compareDecks(
                    leftWhat.toTag().getCompound("tag"),
                    rightWhat.toTag().getCompound("tag"),
                    ascending);
            }
            else if (leftId == ModItems.BOOSTER_PACK.getRegistryName())
            {
                return SortingHelper.compareBoosterPacks(
                    leftWhat.toTag().getCompound("tag"),
                    rightWhat.toTag().getCompound("tag"),
                    ascending);
            }
            else if (leftId == ModItems.ANTIQUE.getRegistryName())
            {
                return SortingHelper.compareAntique(
                    leftWhat.toTag().getCompound("tag"),
                    rightWhat.toTag().getCompound("tag"),
                    ascending);
            }
            else if (leftId == ModItems.JEWEL_POUCH.getRegistryName())
            {
                return SortingHelper.comparePouches(
                    leftWhat.toTag().getCompound("tag"),
                    rightWhat.toTag().getCompound("tag"),
                    ascending);
            }
            else
            {
                VaultGearData leftData = CustomVaultGearData.read(leftWhat.toTag().getCompound("tag"));
                VaultGearData rightData = CustomVaultGearData.read(rightWhat.toTag().getCompound("tag"));

                return switch (sortOrder) {
                    case NAME -> SortingHelper.compareVaultGear(leftName,
                        leftData,
                        rightName,
                        rightData,
                        VaultJewelSorting.CONFIGURATION.getGearSortingByName(),
                        ascending);
                    case AMOUNT -> SortingHelper.compareVaultGear(leftName,
                        leftData,
                        rightName,
                        rightData,
                        VaultJewelSorting.CONFIGURATION.getGearSortingByAmount(),
                        ascending);
                    case MOD -> SortingHelper.compareVaultGear(leftName,
                        leftData,
                        rightName,
                        rightData,
                        VaultJewelSorting.CONFIGURATION.getGearSortingByMod(),
                        ascending);
                };
            }

        }));
    }
}
