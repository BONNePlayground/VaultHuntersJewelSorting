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

import iskallia.vault.gear.data.VaultGearData;
import iskallia.vault.gear.item.VaultGearItem;
import iskallia.vault.item.tool.JewelItem;
import iskallia.vault.item.tool.ToolItem;
import lv.id.bonne.vaultjewelsorting.VaultJewelSorting;
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
            else
            {
                return 0;
            }
        });
    }
}
