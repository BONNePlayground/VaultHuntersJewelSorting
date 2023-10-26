//
// Created by BONNe
// Copyright - 2023
//


package lv.id.bonne.vaulthunters.jewelsorting.quark.mixin;


import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

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
import lv.id.bonne.vaulthunters.jewelsorting.VaultJewelSorting;
import lv.id.bonne.vaulthunters.jewelsorting.utils.SortingHelper;
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
        at = @At("RETURN"),
        cancellable = true,
        remap = false)
    private static void stackCompare(ItemStack stack1, ItemStack stack2, CallbackInfoReturnable<Integer> callbackInfoReturnable)
    {
        if (stack1 == stack2 ||
            !MixinSortingHandler.hasCustomComparison(stack1) ||
            !MixinSortingHandler.hasCustomComparison(stack2) ||
            !stack1.getItem().equals(stack2.getItem()))
        {
            // Leave to original code.
            return;
        }

        // Deal with Jewels
        if (stack1.getItem() instanceof JewelItem &&
            stack2.getItem() instanceof JewelItem)
        {
            if (!VaultJewelSorting.CONFIGURATION.getJewelSortingByName().isEmpty())
            {
                callbackInfoReturnable.setReturnValue(
                    SortingHelper.compareJewels(stack1.getDisplayName().getString(),
                        VaultGearData.read(stack1),
                        stack2.getDisplayName().getString(),
                        VaultGearData.read(stack2),
                        VaultJewelSorting.CONFIGURATION.getJewelSortingByName(),
                        true));

                callbackInfoReturnable.cancel();
            }
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
            if (!VaultJewelSorting.CONFIGURATION.getGearSortingByName().isEmpty())
            {
                callbackInfoReturnable.setReturnValue(
                    SortingHelper.compareVaultGear(stack1.getDisplayName().getString(),
                        VaultGearData.read(stack1),
                        stack2.getDisplayName().getString(),
                        VaultGearData.read(stack2),
                        VaultJewelSorting.CONFIGURATION.getGearSortingByName(),
                        true));

                callbackInfoReturnable.cancel();
            }
        }
        else if (stack1.getItem() instanceof InscriptionItem &&
            stack2.getItem() instanceof InscriptionItem)
        {
            if (!VaultJewelSorting.CONFIGURATION.getInscriptionSortingByName().isEmpty())
            {
                callbackInfoReturnable.setReturnValue(
                    SortingHelper.compareInscriptions(stack1.getDisplayName().getString(),
                        InscriptionData.from(stack1),
                        stack2.getDisplayName().getString(),
                        InscriptionData.from(stack2),
                        VaultJewelSorting.CONFIGURATION.getInscriptionSortingByName(),
                        true));

                callbackInfoReturnable.cancel();
            }
        }
        else if (stack1.getItem() instanceof VaultCrystalItem &&
            stack2.getItem() instanceof VaultCrystalItem)
        {
            if (!VaultJewelSorting.CONFIGURATION.getVaultCrystalSortingByName().isEmpty())
            {
                callbackInfoReturnable.setReturnValue(
                    SortingHelper.compareVaultCrystals(stack1.getDisplayName().getString(),
                        CrystalData.read(stack1),
                        stack2.getDisplayName().getString(),
                        CrystalData.read(stack2),
                        VaultJewelSorting.CONFIGURATION.getVaultCrystalSortingByName(),
                        true));

                callbackInfoReturnable.cancel();
            }
        }
        else if (stack1.getItem() instanceof TrinketItem &&
            stack2.getItem() instanceof TrinketItem)
        {
            if (!VaultJewelSorting.CONFIGURATION.getTrinketSortingByName().isEmpty())
            {
                callbackInfoReturnable.setReturnValue(
                    SortingHelper.compareTrinkets(stack1.getDisplayName().getString(),
                        AttributeGearData.read(stack1),
                        stack1.getTag(),
                        stack2.getDisplayName().getString(),
                        AttributeGearData.read(stack2),
                        stack2.getTag(),
                        VaultJewelSorting.CONFIGURATION.getTrinketSortingByName(),
                        true));

                callbackInfoReturnable.cancel();
            }
        }
    }


    /**
     * This method returns if stack has custom comparison.
     * @param stack Stack to check.
     * @return true if stack has custom comparison.
     */
    @Unique
    private static boolean hasCustomComparison(ItemStack stack)
    {
        return stack.getItem() instanceof VaultGearItem ||
            stack.getItem() instanceof InscriptionItem ||
            stack.getItem() instanceof VaultCrystalItem ||
            stack.getItem() instanceof TrinketItem;
    }
}
