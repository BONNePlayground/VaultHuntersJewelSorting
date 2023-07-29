//
// Created by BONNe
// Copyright - 2023
//


package lv.id.bonne.vaultjewelsorting.refinedstorage.mixin;


import com.refinedmods.refinedstorage.screen.grid.sorting.NameGridSorter;
import com.refinedmods.refinedstorage.screen.grid.sorting.SortingDirection;
import com.refinedmods.refinedstorage.screen.grid.stack.IGridStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import iskallia.vault.gear.data.VaultGearData;
import iskallia.vault.item.tool.JewelItem;
import lv.id.bonne.vaultjewelsorting.utils.SortingHelper;
import net.minecraft.world.item.ItemStack;


/**
 * This mixin handles custom jewel sorting order for rs.
 */
@Mixin(NameGridSorter.class)
public class MixinNameGridSorter
{
    /**
     * This method injects code at the start of compare to sort jewel items.
     * @param left The first stack item.
     * @param right The second stack item.
     * @param sortingDirection The sorting direction.
     * @param callbackInfoReturnable The callback info returnable.
     */
    @Inject(method = "compare(Lcom/refinedmods/refinedstorage/screen/grid/stack/IGridStack;Lcom/refinedmods/refinedstorage/screen/grid/stack/IGridStack;Lcom/refinedmods/refinedstorage/screen/grid/sorting/SortingDirection;)I",
        at = @At("HEAD"),
        cancellable = true,
        remap = false)
    public void compare(IGridStack left,
        IGridStack right,
        SortingDirection sortingDirection,
        CallbackInfoReturnable<Integer> callbackInfoReturnable)
    {
        String leftName = left.getName();
        String rightName = right.getName();

        // Check only equal named items.
        if (leftName.equalsIgnoreCase(rightName))
        {
            // Custom sorting is only for Jewel items.
            if (left.getIngredient() instanceof ItemStack leftStack &&
                right.getIngredient() instanceof ItemStack rightStack &&
                leftStack.getItem() instanceof JewelItem &&
                rightStack.getItem() instanceof JewelItem)
            {
                callbackInfoReturnable.setReturnValue(SortingHelper.compareJewels(
                    VaultGearData.read(leftStack),
                    VaultGearData.read(rightStack),
                    sortingDirection == SortingDirection.ASCENDING));
                callbackInfoReturnable.cancel();
            }
        }
    }
}
