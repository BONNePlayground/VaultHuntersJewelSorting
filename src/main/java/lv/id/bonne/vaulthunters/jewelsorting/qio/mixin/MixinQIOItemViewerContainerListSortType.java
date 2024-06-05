//
// Created by BONNe
// Copyright - 2023
//


package lv.id.bonne.vaulthunters.jewelsorting.qio.mixin;


import com.refinedmods.refinedstorage.screen.grid.sorting.SortingDirection;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import java.util.Comparator;

import iskallia.vault.gear.data.AttributeGearData;
import iskallia.vault.gear.data.GearDataCache;
import iskallia.vault.gear.data.VaultGearData;
import iskallia.vault.gear.item.VaultGearItem;
import iskallia.vault.init.ModItems;
import iskallia.vault.item.*;
import iskallia.vault.item.crystal.CrystalData;
import iskallia.vault.item.crystal.VaultCrystalItem;
import iskallia.vault.item.data.InscriptionData;
import iskallia.vault.item.gear.CharmItem;
import iskallia.vault.item.gear.TrinketItem;
import iskallia.vault.item.tool.JewelItem;
import iskallia.vault.item.tool.ToolItem;
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

            if (registryOrder != 0)
            {
                // Use default string comparing
                return registryOrder;
            }
            else if (firstItem.getItem() instanceof JewelItem &&
                secondItem.getItem() instanceof JewelItem)
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
                        true);
                    case SIZE -> SortingHelper.compareJewels(leftName,
                        leftData,
                        rightName,
                        rightData,
                        VaultJewelSorting.CONFIGURATION.getJewelSortingByAmount(),
                        true);
                    case MOD -> SortingHelper.compareJewels(leftName,
                        leftData,
                        rightName,
                        rightData,
                        VaultJewelSorting.CONFIGURATION.getJewelSortingByMod(),
                        true);
                };
            }
            else if (firstItem.getItem() instanceof ToolItem &&
                secondItem.getItem() instanceof ToolItem)
            {
                // TODO: compare tools. Currently no.
                return 0;
            }
            else if (firstItem.getItem() instanceof VaultGearItem &&
                secondItem.getItem() instanceof VaultGearItem)
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
                        true);
                    case SIZE -> SortingHelper.compareVaultGear(leftName,
                        leftData,
                        rightName,
                        rightData,
                        VaultJewelSorting.CONFIGURATION.getGearSortingByAmount(),
                        true);
                    case MOD -> SortingHelper.compareVaultGear(leftName,
                        leftData,
                        rightName,
                        rightData,
                        VaultJewelSorting.CONFIGURATION.getGearSortingByMod(),
                        true);
                };
            }
            else if (firstItem.getItem() instanceof InscriptionItem &&
                secondItem.getItem() instanceof InscriptionItem)
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
                        true);
                    case SIZE -> SortingHelper.compareInscriptions(leftName,
                        leftData,
                        rightName,
                        rightData,
                        VaultJewelSorting.CONFIGURATION.getInscriptionSortingByAmount(),
                        true);
                    case MOD -> SortingHelper.compareInscriptions(leftName,
                        leftData,
                        rightName,
                        rightData,
                        VaultJewelSorting.CONFIGURATION.getInscriptionSortingByMod(),
                        true);
                };
            }
            else if (firstItem.getItem() instanceof VaultCrystalItem &&
                secondItem.getItem() instanceof VaultCrystalItem)
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
                        true);
                    case SIZE -> SortingHelper.compareVaultCrystals(leftName,
                        leftData,
                        rightName,
                        rightData,
                        VaultJewelSorting.CONFIGURATION.getVaultCrystalSortingByAmount(),
                        true);
                    case MOD -> SortingHelper.compareVaultCrystals(leftName,
                        leftData,
                        rightName,
                        rightData,
                        VaultJewelSorting.CONFIGURATION.getVaultCrystalSortingByMod(),
                        true);
                };
            }
            else if (firstItem.getItem() instanceof TrinketItem &&
                secondItem.getItem() instanceof TrinketItem)
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
                        true);
                    case SIZE -> SortingHelper.compareTrinkets(leftName,
                        leftData,
                        firstItem.getTag(),
                        rightName,
                        rightData,
                        secondItem.getTag(),
                        VaultJewelSorting.CONFIGURATION.getTrinketSortingByAmount(),
                        true);
                    case MOD -> SortingHelper.compareTrinkets(leftName,
                        leftData,
                        firstItem.getTag(),
                        rightName,
                        rightData,
                        secondItem.getTag(),
                        VaultJewelSorting.CONFIGURATION.getTrinketSortingByMod(),
                        true);
                };
            }
            else if (firstItem.getItem() instanceof CharmItem &&
                secondItem.getItem() instanceof CharmItem)
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
                        true);
                    case SIZE -> SortingHelper.compareCharms(leftName,
                        leftData,
                        firstItem.getTag(),
                        rightName,
                        rightData,
                        secondItem.getTag(),
                        VaultJewelSorting.CONFIGURATION.getCharmSortingByAmount(),
                        true);
                    case MOD -> SortingHelper.compareCharms(leftName,
                        leftData,
                        firstItem.getTag(),
                        rightName,
                        rightData,
                        secondItem.getTag(),
                        VaultJewelSorting.CONFIGURATION.getCharmSortingByMod(),
                        true);
                };
            }
            else if (firstItem.getItem() instanceof InfusedCatalystItem &&
                secondItem.getItem() instanceof InfusedCatalystItem)
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
                        true);
                    case SIZE -> SortingHelper.compareCatalysts(leftName,
                        firstItem.getTag(),
                        rightName,
                        secondItem.getTag(),
                        VaultJewelSorting.CONFIGURATION.getCatalystSortingByAmount(),
                        true);
                    case MOD -> SortingHelper.compareCatalysts(leftName,
                        firstItem.getTag(),
                        rightName,
                        secondItem.getTag(),
                        VaultJewelSorting.CONFIGURATION.getCatalystSortingByMod(),
                        true);
                };
            }
            else if (firstItem.getItem() instanceof VaultDollItem &&
                secondItem.getItem() instanceof VaultDollItem)
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
                        true);
                    case SIZE -> SortingHelper.compareVaultDolls(leftName,
                        firstItem.getTag(),
                        rightName,
                        secondItem.getTag(),
                        VaultJewelSorting.CONFIGURATION.getDollSortingByAmount(),
                        true);
                    case MOD -> SortingHelper.compareVaultDolls(leftName,
                        firstItem.getTag(),
                        rightName,
                        secondItem.getTag(),
                        VaultJewelSorting.CONFIGURATION.getDollSortingByMod(),
                        true);
                };
            }
            else if (firstItem.getItem() instanceof RelicFragmentItem &&
                secondItem.getItem() instanceof RelicFragmentItem)
            {
                return SortingHelper.compareRelicFragments(
                    firstItem.getTag(),
                    secondItem.getTag(),
                    true);
            }
            else if (firstItem.getItem() instanceof ItemRespecFlask &&
                secondItem.getItem() instanceof ItemRespecFlask)
            {
                return SortingHelper.compareRespecFlasks(
                    firstItem.getTag(),
                    secondItem.getTag(),
                    true);
            }
            else if (firstItem.getItem() == ModItems.FACETED_FOCUS &&
                secondItem.getItem() == ModItems.FACETED_FOCUS)
            {
                return SortingHelper.compareFacedFocus(
                    firstItem.getTag(),
                    secondItem.getTag(),
                    true);
            }
            else if (firstItem.getItem() == ModItems.AUGMENT &&
                secondItem.getItem() == ModItems.AUGMENT)
            {
                return SortingHelper.compareAugments(
                    firstItem.getTag(),
                    secondItem.getTag(),
                    true);
            }
            else
            {
                return 0;
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

            if (registryOrder != 0)
            {
                // Use default string comparing
                return registryOrder;
            }
            else if (firstItem.getItem() instanceof JewelItem &&
                secondItem.getItem() instanceof JewelItem)
            {
                String leftName = firstItem.getDisplayName().getString();
                String rightName = secondItem.getDisplayName().getString();
                VaultGearData leftData = VaultGearData.read(firstItem);
                VaultGearData rightData = VaultGearData.read(secondItem);

                return switch (instance) {
                    case NAME -> SortingHelper.compareJewels(leftName,
                        leftData,
                        rightName,
                        rightData,
                        VaultJewelSorting.CONFIGURATION.getJewelSortingByName(),
                        false);
                    case SIZE -> SortingHelper.compareJewels(leftName,
                        leftData,
                        rightName,
                        rightData,
                        VaultJewelSorting.CONFIGURATION.getJewelSortingByAmount(),
                        false);
                    case MOD -> SortingHelper.compareJewels(leftName,
                        leftData,
                        rightName,
                        rightData,
                        VaultJewelSorting.CONFIGURATION.getJewelSortingByMod(),
                        false);
                };
            }
            else if (firstItem.getItem() instanceof ToolItem &&
                secondItem.getItem() instanceof ToolItem)
            {
                // TODO: compare tools. Currently no.
                return 0;
            }
            else if (firstItem.getItem() instanceof VaultGearItem &&
                secondItem.getItem() instanceof VaultGearItem)
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
                        false);
                    case SIZE -> SortingHelper.compareVaultGear(leftName,
                        leftData,
                        rightName,
                        rightData,
                        VaultJewelSorting.CONFIGURATION.getGearSortingByAmount(),
                        false);
                    case MOD -> SortingHelper.compareVaultGear(leftName,
                        leftData,
                        rightName,
                        rightData,
                        VaultJewelSorting.CONFIGURATION.getGearSortingByMod(),
                        false);
                };
            }
            else if (firstItem.getItem() instanceof InscriptionItem &&
                secondItem.getItem() instanceof InscriptionItem)
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
                        false);
                    case SIZE -> SortingHelper.compareInscriptions(leftName,
                        leftData,
                        rightName,
                        rightData,
                        VaultJewelSorting.CONFIGURATION.getInscriptionSortingByAmount(),
                        false);
                    case MOD -> SortingHelper.compareInscriptions(leftName,
                        leftData,
                        rightName,
                        rightData,
                        VaultJewelSorting.CONFIGURATION.getInscriptionSortingByMod(),
                        false);
                };
            }
            else if (firstItem.getItem() instanceof VaultCrystalItem &&
                secondItem.getItem() instanceof VaultCrystalItem)
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
                        false);
                    case SIZE -> SortingHelper.compareVaultCrystals(leftName,
                        leftData,
                        rightName,
                        rightData,
                        VaultJewelSorting.CONFIGURATION.getVaultCrystalSortingByAmount(),
                        false);
                    case MOD -> SortingHelper.compareVaultCrystals(leftName,
                        leftData,
                        rightName,
                        rightData,
                        VaultJewelSorting.CONFIGURATION.getVaultCrystalSortingByMod(),
                        false);
                };
            }
            else if (firstItem.getItem() instanceof TrinketItem &&
                secondItem.getItem() instanceof TrinketItem)
            {
                String leftName = firstItem.getDisplayName().getString();
                String rightName = secondItem.getDisplayName().getString();
                AttributeGearData leftData = AttributeGearData.read(firstItem);
                AttributeGearData rightData = AttributeGearData.read(secondItem);

                return switch (instance) {
                    case NAME -> SortingHelper.compareTrinkets(leftName,
                        leftData,
                        firstItem.getTag(),
                        rightName,
                        rightData,
                        secondItem.getTag(),
                        VaultJewelSorting.CONFIGURATION.getTrinketSortingByName(),
                        false);
                    case SIZE -> SortingHelper.compareTrinkets(leftName,
                        leftData,
                        firstItem.getTag(),
                        rightName,
                        rightData,
                        secondItem.getTag(),
                        VaultJewelSorting.CONFIGURATION.getTrinketSortingByAmount(),
                        false);
                    case MOD -> SortingHelper.compareTrinkets(leftName,
                        leftData,
                        firstItem.getTag(),
                        rightName,
                        rightData,
                        secondItem.getTag(),
                        VaultJewelSorting.CONFIGURATION.getTrinketSortingByMod(),
                        false);
                };
            }
            else if (firstItem.getItem() instanceof CharmItem &&
                secondItem.getItem() instanceof CharmItem)
            {
                String leftName = firstItem.getDisplayName().getString();
                String rightName = secondItem.getDisplayName().getString();
                AttributeGearData leftData = AttributeGearData.read(firstItem);
                AttributeGearData rightData = AttributeGearData.read(secondItem);

                return switch (instance) {
                    case NAME -> SortingHelper.compareCharms(leftName,
                        leftData,
                        firstItem.getTag(),
                        rightName,
                        rightData,
                        secondItem.getTag(),
                        VaultJewelSorting.CONFIGURATION.getCharmSortingByName(),
                        false);
                    case SIZE -> SortingHelper.compareCharms(leftName,
                        leftData,
                        firstItem.getTag(),
                        rightName,
                        rightData,
                        secondItem.getTag(),
                        VaultJewelSorting.CONFIGURATION.getCharmSortingByAmount(),
                        false);
                    case MOD -> SortingHelper.compareCharms(leftName,
                        leftData,
                        firstItem.getTag(),
                        rightName,
                        rightData,
                        secondItem.getTag(),
                        VaultJewelSorting.CONFIGURATION.getCharmSortingByMod(),
                        false);
                };
            }
            else if (firstItem.getItem() instanceof InfusedCatalystItem &&
                secondItem.getItem() instanceof InfusedCatalystItem)
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
                        false);
                    case SIZE -> SortingHelper.compareCatalysts(leftName,
                        firstItem.getTag(),
                        rightName,
                        secondItem.getTag(),
                        VaultJewelSorting.CONFIGURATION.getCatalystSortingByAmount(),
                        false);
                    case MOD -> SortingHelper.compareCatalysts(leftName,
                        firstItem.getTag(),
                        rightName,
                        secondItem.getTag(),
                        VaultJewelSorting.CONFIGURATION.getCatalystSortingByMod(),
                        false);
                };
            }
            else if (firstItem.getItem() instanceof VaultDollItem &&
                secondItem.getItem() instanceof VaultDollItem)
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
                        false);
                    case SIZE -> SortingHelper.compareVaultDolls(leftName,
                        firstItem.getTag(),
                        rightName,
                        secondItem.getTag(),
                        VaultJewelSorting.CONFIGURATION.getDollSortingByAmount(),
                        false);
                    case MOD -> SortingHelper.compareVaultDolls(leftName,
                        firstItem.getTag(),
                        rightName,
                        secondItem.getTag(),
                        VaultJewelSorting.CONFIGURATION.getDollSortingByMod(),
                        false);
                };
            }
            else if (firstItem.getItem() instanceof RelicFragmentItem &&
                secondItem.getItem() instanceof RelicFragmentItem)
            {
                return SortingHelper.compareRelicFragments(
                    firstItem.getTag(),
                    secondItem.getTag(),
                    false);
            }
            else if (firstItem.getItem() instanceof ItemRespecFlask &&
                secondItem.getItem() instanceof ItemRespecFlask)
            {
                return SortingHelper.compareRespecFlasks(
                    firstItem.getTag(),
                    secondItem.getTag(),
                    false);
            }
            else if (firstItem.getItem() == ModItems.FACETED_FOCUS &&
                secondItem.getItem() == ModItems.FACETED_FOCUS)
            {
                return SortingHelper.compareFacedFocus(
                    firstItem.getTag(),
                    secondItem.getTag(),
                    false);
            }
            else if (firstItem.getItem() == ModItems.AUGMENT &&
                secondItem.getItem() == ModItems.AUGMENT)
            {
                return SortingHelper.compareAugments(
                    firstItem.getTag(),
                    secondItem.getTag(),
                    false);
            }
            else
            {
                return 0;
            }
        });
    }
}
