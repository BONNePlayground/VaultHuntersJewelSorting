//
// Created by BONNe
// Copyright - 2023
//


package lv.id.bonne.vaultjewelsorting.quark.mixin;


import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import iskallia.vault.gear.data.VaultGearData;
import iskallia.vault.gear.item.VaultGearItem;
import iskallia.vault.item.tool.JewelItem;
import iskallia.vault.item.tool.ToolItem;
import lv.id.bonne.vaultjewelsorting.utils.SortingHelper;
import net.minecraft.world.item.ItemStack;
import vazkii.quark.base.handler.SortingHandler;


/**
 * This mixin handles custom jewel sorting order for quark.
 */
@Mixin(SortingHandler.class)
public class MixinSortingHandler
{
    /**
     * This method injects code at the start of stackCompare to sort jewel items.
     * @param stack1 The first stack item.
     * @param stack2 The second stack item.
     * @param callbackInfoReturnable The callback info returnable.
     */
    @Inject(method = "stackCompare(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;)I",
        at = @At("HEAD"),
        cancellable = true,
        remap = false)
    private static void stackCompare(ItemStack stack1, ItemStack stack2, CallbackInfoReturnable<Integer> callbackInfoReturnable)
    {
        if (stack1 == stack2 ||
            stack1.isEmpty() ||
            stack2.isEmpty())
        {
            // Leave to original code.
            return;
        }

        // Deal with Jewels
        if (stack1.getItem() instanceof JewelItem &&
            stack2.getItem() instanceof JewelItem)
        {
            String leftName = stack1.getDisplayName().getString();
            String rightName = stack2.getDisplayName().getString();

            if (!leftName.equalsIgnoreCase(rightName))
            {
                callbackInfoReturnable.setReturnValue(String.CASE_INSENSITIVE_ORDER.compare(leftName, rightName));
            }
            else
            {
                callbackInfoReturnable.setReturnValue(
                    SortingHelper.compareJewels(
                        VaultGearData.read(stack1),
                        VaultGearData.read(stack2),
                        true));
            }

            callbackInfoReturnable.cancel();
        }
        else if (stack1.getItem() instanceof ToolItem &&
            stack2.getItem() instanceof ToolItem)
        {
// TODO: Compare vault tools by their type? Currently is left just to filter out from VaultGearItem
//                callbackInfoReturnable.setReturnValue(SortingHelper.compareTools(
//                    VaultGearData.read(leftStack),
//                    VaultGearData.read(rightStack),
//                    sortingDirection == SortingDirection.ASCENDING));
//                callbackInfoReturnable.cancel();
        }
        else if (stack1.getItem() instanceof VaultGearItem &&
            stack2.getItem() instanceof VaultGearItem)
        {
            String leftName = stack1.getDisplayName().getString();
            String rightName = stack2.getDisplayName().getString();

            if (!leftName.equalsIgnoreCase(rightName))
            {
                callbackInfoReturnable.setReturnValue(String.CASE_INSENSITIVE_ORDER.compare(leftName, rightName));
            }
            else
            {
                callbackInfoReturnable.setReturnValue(
                    SortingHelper.compareVaultGear(
                        VaultGearData.read(stack1),
                        VaultGearData.read(stack2),
                        true));
            }

            callbackInfoReturnable.cancel();
        }
    }
}
