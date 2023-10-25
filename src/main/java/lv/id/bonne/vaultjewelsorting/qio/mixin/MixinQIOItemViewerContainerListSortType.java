//
// Created by BONNe
// Copyright - 2023
//


package lv.id.bonne.vaultjewelsorting.qio.mixin;


import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Comparator;

import iskallia.vault.gear.data.AttributeGearData;
import iskallia.vault.gear.data.VaultGearData;
import iskallia.vault.gear.item.VaultGearItem;
import iskallia.vault.item.InscriptionItem;
import iskallia.vault.item.crystal.CrystalData;
import iskallia.vault.item.crystal.VaultCrystalItem;
import iskallia.vault.item.data.InscriptionData;
import iskallia.vault.item.gear.TrinketItem;
import iskallia.vault.item.tool.JewelItem;
import iskallia.vault.item.tool.ToolItem;
import lv.id.bonne.vaultjewelsorting.VaultJewelSorting;
import lv.id.bonne.vaultjewelsorting.utils.CustomVaultGearData;
import lv.id.bonne.vaultjewelsorting.utils.SortingHelper;
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

            // if shift is down, or sorting by mod, then skip everything.
            if (firstItem.getItem() instanceof JewelItem &&
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

            // if shift is down, or sorting by mod, then skip everything.
            if (firstItem.getItem() instanceof JewelItem &&
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
            else
            {
                return 0;
            }
        });
    }
}
