//
// Created by BONNe
// Copyright - 2023
//


package lv.id.bonne.vaulthunters.jewelsorting.qio.mixin;


import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import java.util.Comparator;

import iskallia.vault.gear.data.AttributeGearData;
import iskallia.vault.gear.data.GearDataCache;
import iskallia.vault.gear.data.VaultGearData;
import iskallia.vault.init.ModItems;
import iskallia.vault.item.crystal.CrystalData;
import iskallia.vault.item.data.InscriptionData;
import lv.id.bonne.vaulthunters.jewelsorting.VaultJewelSorting;
import lv.id.bonne.vaulthunters.jewelsorting.utils.IExtraGearDataCache;
import lv.id.bonne.vaulthunters.jewelsorting.utils.SortingHelper;
import mekanism.common.inventory.ISlotClickHandler;
import mekanism.common.inventory.container.QIOItemViewerContainer;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.world.item.ItemStack;


/**
 * This mixin handles custom jewel sorting order for Mekanism QIO.
 */
@Mixin(targets = "mekanism.common.inventory.container.QIOItemViewerContainer$ListSortType", remap = false)
public class MixinQIOItemViewerContainerListSortType
{
    /**
     * This shadow field stores ascendingComparator.
     */
    @Shadow
    @Final
    private Comparator<ISlotClickHandler.IScrollableSlot> ascendingComparator;

    /**
     * This shadow field stores descendingComparator.
     */
    @Shadow
    @Final
    private Comparator<ISlotClickHandler.IScrollableSlot> descendingComparator;


    /**
     * This method redirects ascendingComparator to a comparator that sorts jewels.
     * @param instance Instance of ListSortType
     * @return new comparator that also handles jewels in ascending order
     */
    @Redirect(method = "sort", at = @At(value = "FIELD", target = "Lmekanism/common/inventory/container/QIOItemViewerContainer$ListSortType;ascendingComparator:Ljava/util/Comparator;", opcode = Opcodes.GETFIELD))
    private Comparator<ISlotClickHandler.IScrollableSlot> redirectAscending(QIOItemViewerContainer.ListSortType instance)
    {
        return this.ascendingComparator.thenComparing((stack1, stack2) ->
        {
            if (Screen.hasShiftDown())
            {
                return 0;
            }

            ItemStack firstItem = stack1.getItem().getStack();
            ItemStack secondItem = stack2.getItem().getStack();

            if (!stack1.getModID().equals(stack2.getModID()))
            {
                // some small cleanup. We want to sort only vault items.
                return String.CASE_INSENSITIVE_ORDER.compare(
                    stack1.getModID(),
                    stack2.getModID());
            }

            int registryOrder = SortingHelper.compareRegistryNames(
                firstItem.getItem().getRegistryName(),
                secondItem.getItem().getRegistryName(),
                true);

            if (registryOrder != 0 || !SortingHelper.isSortable(firstItem.getItem().getRegistryName()))
            {
                // Use default string comparing
                return registryOrder;
            }
            else
            {
                return compareItems(firstItem, secondItem, instance, true);
            }
        });
    }


    /**
     * This method redirects descendingComparator to a comparator that sorts jewels.
     * @param instance Instance of ListSortType
     * @return new comparator that also handles jewels in descending order
     */
    @Redirect(method = "sort", at = @At(value = "FIELD", target = "Lmekanism/common/inventory/container/QIOItemViewerContainer$ListSortType;descendingComparator:Ljava/util/Comparator;", opcode = Opcodes.GETFIELD))
    private Comparator<ISlotClickHandler.IScrollableSlot> redirectDescending(QIOItemViewerContainer.ListSortType instance)
    {
        return this.descendingComparator.thenComparing((stack1, stack2) ->
        {
            if (Screen.hasShiftDown())
            {
                return 0;
            }

            ItemStack firstItem = stack1.getItem().getStack();
            ItemStack secondItem = stack2.getItem().getStack();

            if (!stack1.getModID().equals(stack2.getModID()))
            {
                // some small cleanup. We want to sort only vault items.
                return String.CASE_INSENSITIVE_ORDER.compare(
                    stack2.getModID(),
                    stack1.getModID());
            }

            int registryOrder = SortingHelper.compareRegistryNames(
                firstItem.getItem().getRegistryName(),
                secondItem.getItem().getRegistryName(),
                false);

            if (registryOrder != 0 || !SortingHelper.isSortable(firstItem.getItem().getRegistryName()))
            {
                // Use default string comparing
                return registryOrder;
            }
            else
            {
                return compareItems(firstItem, secondItem, instance, false);
            }
        });
    }


    /**
     * This method compares two given items that have custom ordering.
     * @param firstItem First item to be compared
     * @param secondItem Second item to be compared
     * @param instance Sorting instance
     * @param ascending order of items
     * @return order of items.
     */
    @Unique
    private static int compareItems(ItemStack firstItem,
        ItemStack secondItem,
        QIOItemViewerContainer.ListSortType instance,
        boolean ascending)
    {
        if (firstItem.getItem() == ModItems.JEWEL)
        {
            String leftName = firstItem.getDisplayName().getString();
            String rightName = secondItem.getDisplayName().getString();
            GearDataCache leftData = GearDataCache.of(firstItem);
            GearDataCache rightData = GearDataCache.of(secondItem);

            // Update item cache if vault versions mismatch.
            if (((IExtraGearDataCache) leftData).isInvalidCache())
            {
                GearDataCache.removeCache(firstItem);
                GearDataCache.createCache(firstItem);
                leftData = GearDataCache.of(firstItem);
            }

            // Update item cache if vault versions mismatch.
            if (((IExtraGearDataCache) rightData).isInvalidCache())
            {
                GearDataCache.removeCache(secondItem);
                GearDataCache.createCache(secondItem);
                rightData = GearDataCache.of(secondItem);
            }

            return switch (instance) {
                case NAME -> SortingHelper.compareJewels(leftName,
                    leftData,
                    rightName,
                    rightData,
                    VaultJewelSorting.CONFIGURATION.getJewelSortingByName(),
                    ascending);
                case SIZE -> SortingHelper.compareJewels(leftName,
                    leftData,
                    rightName,
                    rightData,
                    VaultJewelSorting.CONFIGURATION.getJewelSortingByAmount(),
                    ascending);
                case MOD -> SortingHelper.compareJewels(leftName,
                    leftData,
                    rightName,
                    rightData,
                    VaultJewelSorting.CONFIGURATION.getJewelSortingByMod(),
                    ascending);
            };
        }
        else if (firstItem.getItem() == ModItems.TOOL)
        {
            // TODO: compare tools. Currently no.
            return 0;
        }
        else if (SortingHelper.VAULT_GEAR_SET.contains(firstItem.getItem().getRegistryName()))
        {
            String leftName = firstItem.getDisplayName().getString();
            String rightName = secondItem.getDisplayName().getString();
            VaultGearData leftData = VaultGearData.read(firstItem);
            VaultGearData rightData = VaultGearData.read(secondItem);

            return switch (instance) {
                case NAME -> SortingHelper.compareVaultGear(leftName,
                    leftData,
                    rightName,
                    rightData,
                    VaultJewelSorting.CONFIGURATION.getGearSortingByName(),
                    ascending);
                case SIZE -> SortingHelper.compareVaultGear(leftName,
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
        else if (firstItem.getItem() == ModItems.INSCRIPTION)
        {
            String leftName = firstItem.getDisplayName().getString();
            String rightName = secondItem.getDisplayName().getString();
            InscriptionData leftData = InscriptionData.from(firstItem);
            InscriptionData rightData = InscriptionData.from(secondItem);

            return switch (instance) {
                case NAME -> SortingHelper.compareInscriptions(leftName,
                    leftData,
                    rightName,
                    rightData,
                    VaultJewelSorting.CONFIGURATION.getInscriptionSortingByName(),
                    ascending);
                case SIZE -> SortingHelper.compareInscriptions(leftName,
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
        else if (firstItem.getItem() == ModItems.VAULT_CRYSTAL)
        {
            String leftName = firstItem.getDisplayName().getString();
            String rightName = secondItem.getDisplayName().getString();
            CrystalData leftData = CrystalData.read(firstItem);
            CrystalData rightData = CrystalData.read(secondItem);

            return switch (instance) {
                case NAME -> SortingHelper.compareVaultCrystals(leftName,
                    leftData,
                    rightName,
                    rightData,
                    VaultJewelSorting.CONFIGURATION.getVaultCrystalSortingByName(),
                    ascending);
                case SIZE -> SortingHelper.compareVaultCrystals(leftName,
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
        else if (firstItem.getItem() == ModItems.TRINKET)
        {
            String leftName = firstItem.getDisplayName().getString();
            String rightName = secondItem.getDisplayName().getString();
            AttributeGearData leftData = AttributeGearData.read(firstItem);
            AttributeGearData rightData = AttributeGearData.read(secondItem);

            return switch (instance)
            {
                case NAME -> SortingHelper.compareTrinkets(leftName,
                    leftData,
                    firstItem.getTag(),
                    rightName,
                    rightData,
                    secondItem.getTag(),
                    VaultJewelSorting.CONFIGURATION.getTrinketSortingByName(),
                    ascending);
                case SIZE -> SortingHelper.compareTrinkets(leftName,
                    leftData,
                    firstItem.getTag(),
                    rightName,
                    rightData,
                    secondItem.getTag(),
                    VaultJewelSorting.CONFIGURATION.getTrinketSortingByAmount(),
                    ascending);
                case MOD -> SortingHelper.compareTrinkets(leftName,
                    leftData,
                    firstItem.getTag(),
                    rightName,
                    rightData,
                    secondItem.getTag(),
                    VaultJewelSorting.CONFIGURATION.getTrinketSortingByMod(),
                    ascending);
            };
        }
        else if (SortingHelper.VAULT_CHARMS.contains(firstItem.getItem().getRegistryName()))
        {
            String leftName = firstItem.getDisplayName().getString();
            String rightName = secondItem.getDisplayName().getString();
            AttributeGearData leftData = AttributeGearData.read(firstItem);
            AttributeGearData rightData = AttributeGearData.read(secondItem);

            return switch (instance)
            {
                case NAME -> SortingHelper.compareCharms(leftName,
                    leftData,
                    firstItem.getTag(),
                    rightName,
                    rightData,
                    secondItem.getTag(),
                    VaultJewelSorting.CONFIGURATION.getCharmSortingByName(),
                    ascending);
                case SIZE -> SortingHelper.compareCharms(leftName,
                    leftData,
                    firstItem.getTag(),
                    rightName,
                    rightData,
                    secondItem.getTag(),
                    VaultJewelSorting.CONFIGURATION.getCharmSortingByAmount(),
                    ascending);
                case MOD -> SortingHelper.compareCharms(leftName,
                    leftData,
                    firstItem.getTag(),
                    rightName,
                    rightData,
                    secondItem.getTag(),
                    VaultJewelSorting.CONFIGURATION.getCharmSortingByMod(),
                    ascending);
            };
        }
        else if (firstItem.getItem() == ModItems.VAULT_CATALYST_INFUSED)
        {
            String leftName = firstItem.getDisplayName().getString();
            String rightName = secondItem.getDisplayName().getString();

            return switch (instance)
            {
                case NAME -> SortingHelper.compareCatalysts(leftName,
                    firstItem.getTag(),
                    rightName,
                    secondItem.getTag(),
                    VaultJewelSorting.CONFIGURATION.getCatalystSortingByName(),
                    ascending);
                case SIZE -> SortingHelper.compareCatalysts(leftName,
                    firstItem.getTag(),
                    rightName,
                    secondItem.getTag(),
                    VaultJewelSorting.CONFIGURATION.getCatalystSortingByAmount(),
                    ascending);
                case MOD -> SortingHelper.compareCatalysts(leftName,
                    firstItem.getTag(),
                    rightName,
                    secondItem.getTag(),
                    VaultJewelSorting.CONFIGURATION.getCatalystSortingByMod(),
                    ascending);
            };
        }
        else if (firstItem.getItem() == ModItems.VAULT_DOLL)
        {
            String leftName = firstItem.getDisplayName().getString();
            String rightName = secondItem.getDisplayName().getString();

            return switch (instance)
            {
                case NAME -> SortingHelper.compareVaultDolls(leftName,
                    firstItem.getTag(),
                    rightName,
                    secondItem.getTag(),
                    VaultJewelSorting.CONFIGURATION.getDollSortingByName(),
                    ascending);
                case SIZE -> SortingHelper.compareVaultDolls(leftName,
                    firstItem.getTag(),
                    rightName,
                    secondItem.getTag(),
                    VaultJewelSorting.CONFIGURATION.getDollSortingByAmount(),
                    ascending);
                case MOD -> SortingHelper.compareVaultDolls(leftName,
                    firstItem.getTag(),
                    rightName,
                    secondItem.getTag(),
                    VaultJewelSorting.CONFIGURATION.getDollSortingByMod(),
                    ascending);
            };
        }
        else if (firstItem.getItem() == ModItems.RELIC_FRAGMENT)
        {
            return SortingHelper.compareRelicFragments(
                firstItem.getTag(),
                secondItem.getTag(),
                ascending);
        }
        else if (firstItem.getItem() == ModItems.RESPEC_FLASK)
        {
            return SortingHelper.compareRespecFlasks(
                firstItem.getTag(),
                secondItem.getTag(),
                ascending);
        }
        else if (firstItem.getItem() == ModItems.FACETED_FOCUS)
        {
            return SortingHelper.compareFacedFocus(
                firstItem.getTag(),
                secondItem.getTag(),
                ascending);
        }
        else if (firstItem.getItem() == ModItems.AUGMENT)
        {
            return SortingHelper.compareAugments(
                firstItem.getTag(),
                secondItem.getTag(),
                ascending);
        }
        else
        {
            return 0;
        }
    }
}
