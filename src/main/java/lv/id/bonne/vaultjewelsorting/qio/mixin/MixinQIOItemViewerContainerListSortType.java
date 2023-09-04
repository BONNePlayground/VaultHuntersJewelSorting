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
import iskallia.vault.item.tool.JewelItem;
import lv.id.bonne.vaultjewelsorting.utils.SortingHelper;
import mekanism.common.inventory.ISlotClickHandler;
import mekanism.common.inventory.container.QIOItemViewerContainer;
import net.minecraft.client.gui.screens.Screen;


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
            // if shift is down, or sorting by mod, then skip everything.
            if (!Screen.hasShiftDown() &&
                instance != QIOItemViewerContainer.ListSortType.MOD &&
                stack1.getItem().getStack().getItem() instanceof JewelItem &&
                stack2.getItem().getStack().getItem() instanceof JewelItem)
            {
                 if (instance == QIOItemViewerContainer.ListSortType.NAME)
                 {
                     // Use NAME, ATTRIBUTE, ATTRIBUTE_VALUE, SIZE comparing.
                     return SortingHelper.compareJewels(
                         VaultGearData.read(stack1.getItem().getStack()),
                         VaultGearData.read(stack2.getItem().getStack()),
                         true);
                 }
                 else
                 {
                     // Use NAME, ATTRIBUTE, SIZE, ATTRIBUTE_VALUE comparing.
                     return SortingHelper.compareJewelsSize(
                         VaultGearData.read(stack1.getItem().getStack()),
                         VaultGearData.read(stack2.getItem().getStack()),
                         true);
                 }
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
            // if shift is down, or sorting by mod, then skip everything.
            if (!Screen.hasShiftDown() &&
                instance != QIOItemViewerContainer.ListSortType.MOD &&
                stack1.getItem().getStack().getItem() instanceof JewelItem &&
                stack2.getItem().getStack().getItem() instanceof JewelItem)
            {
                if (instance == QIOItemViewerContainer.ListSortType.NAME)
                {
                    // Use NAME, ATTRIBUTE, ATTRIBUTE_VALUE, SIZE comparing.
                    return SortingHelper.compareJewels(
                        VaultGearData.read(stack1.getItem().getStack()),
                        VaultGearData.read(stack2.getItem().getStack()),
                        false);
                }
                else
                {
                    // Use NAME, ATTRIBUTE, SIZE, ATTRIBUTE_VALUE comparing.
                    return SortingHelper.compareJewelsSize(
                        VaultGearData.read(stack1.getItem().getStack()),
                        VaultGearData.read(stack2.getItem().getStack()),
                        false);
                }
            }
            else
            {
                return 0;
            }
        });
    }
}
