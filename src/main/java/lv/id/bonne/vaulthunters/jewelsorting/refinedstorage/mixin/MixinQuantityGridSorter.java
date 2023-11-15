//
// Created by BONNe
// Copyright - 2023
//


package lv.id.bonne.vaulthunters.jewelsorting.refinedstorage.mixin;


import com.refinedmods.refinedstorage.screen.grid.sorting.QuantityGridSorter;
import com.refinedmods.refinedstorage.screen.grid.sorting.SortingDirection;
import com.refinedmods.refinedstorage.screen.grid.stack.IGridStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import iskallia.vault.gear.data.AttributeGearData;
import iskallia.vault.gear.data.GearDataCache;
import iskallia.vault.gear.data.VaultGearData;
import iskallia.vault.gear.item.VaultGearItem;
import iskallia.vault.item.InscriptionItem;
import iskallia.vault.item.VaultDollItem;
import iskallia.vault.item.crystal.CrystalData;
import iskallia.vault.item.crystal.VaultCrystalItem;
import iskallia.vault.item.data.InscriptionData;
import iskallia.vault.item.gear.CharmItem;
import iskallia.vault.item.gear.TrinketItem;
import iskallia.vault.item.tool.JewelItem;
import iskallia.vault.item.tool.ToolItem;
import lv.id.bonne.vaulthunters.jewelsorting.VaultJewelSorting;
import lv.id.bonne.vaulthunters.jewelsorting.utils.SortingHelper;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.world.item.ItemStack;


/**
 * This mixin handles custom jewel sorting order for rs.
 */
@Mixin(QuantityGridSorter.class)
public class MixinQuantityGridSorter
{
    /**
     * This method injects code at the start of compare to sort jewel items.
     * @param left The first stack item.
     * @param right The second stack item.
     * @param sortingDirection The sorting direction.
     * @param callbackInfoReturnable The callback info returnable.
     */
    @Inject(method = "compare(Lcom/refinedmods/refinedstorage/screen/grid/stack/IGridStack;Lcom/refinedmods/refinedstorage/screen/grid/stack/IGridStack;Lcom/refinedmods/refinedstorage/screen/grid/sorting/SortingDirection;)I",
        at = @At("RETURN"),
        cancellable = true,
        remap = false)
    public void compare(IGridStack left,
        IGridStack right,
        SortingDirection sortingDirection,
        CallbackInfoReturnable<Integer> callbackInfoReturnable)
    {
        if (Screen.hasShiftDown() || callbackInfoReturnable.getReturnValue() != 0)
        {
            // If shift is pressed or both names are not equal, then ignore.
            return;
        }

        if (!left.getModId().equals(right.getModId()))
        {
            // some small cleanup. We want to sort only vault items.
            callbackInfoReturnable.setReturnValue(
                String.CASE_INSENSITIVE_ORDER.compare(left.getModId(), right.getModId()));
            return;
        }

        if (left.getIngredient() instanceof ItemStack leftStack &&
            right.getIngredient() instanceof ItemStack rightStack)
        {
            // Get item registry names.

            int registryOrder = SortingHelper.compareRegistryNames(
                leftStack.getItem().getRegistryName(),
                rightStack.getItem().getRegistryName(),
                sortingDirection == SortingDirection.ASCENDING);

            if (registryOrder != 0)
            {
                // If registry order is not 0, then return it.
                callbackInfoReturnable.setReturnValue(registryOrder);
            }
            else if (leftStack.getItem() instanceof JewelItem &&
                rightStack.getItem() instanceof JewelItem)
            {
                if (!VaultJewelSorting.CONFIGURATION.getJewelSortingByAmount().isEmpty())
                {
                    callbackInfoReturnable.setReturnValue(SortingHelper.compareJewels(
                        left.getName(),
                        GearDataCache.of(leftStack),
                        right.getName(),
                        GearDataCache.of(rightStack),
                        VaultJewelSorting.CONFIGURATION.getJewelSortingByAmount(),
                        sortingDirection == SortingDirection.ASCENDING));
                }
            }
            else if (leftStack.getItem() instanceof ToolItem &&
                rightStack.getItem() instanceof ToolItem)
            {
// TODO: Compare vault tools by their type? Currently is left just to filter out from VaultGearItem
//                callbackInfoReturnable.setReturnValue(SortingHelper.compareTools(
//                    VaultGearData.read(leftStack),
//                    VaultGearData.read(rightStack),
//                    sortingDirection == SortingDirection.ASCENDING));
//                callbackInfoReturnable.cancel();
            }
            else if (leftStack.getItem() instanceof VaultGearItem &&
                rightStack.getItem() instanceof VaultGearItem)
            {
                if (!VaultJewelSorting.CONFIGURATION.getGearSortingByAmount().isEmpty())
                {
                    callbackInfoReturnable.setReturnValue(SortingHelper.compareVaultGear(
                        left.getName(),
                        VaultGearData.read(leftStack),
                        right.getName(),
                        VaultGearData.read(rightStack),
                        VaultJewelSorting.CONFIGURATION.getGearSortingByAmount(),
                        sortingDirection == SortingDirection.ASCENDING));
                }
            }
            else if (leftStack.getItem() instanceof InscriptionItem &&
                rightStack.getItem() instanceof InscriptionItem)
            {
                if (!VaultJewelSorting.CONFIGURATION.getInscriptionSortingByAmount().isEmpty())
                {
                    callbackInfoReturnable.setReturnValue(SortingHelper.compareInscriptions(left.getName(),
                        InscriptionData.from(leftStack),
                        right.getName(),
                        InscriptionData.from(rightStack),
                        VaultJewelSorting.CONFIGURATION.getInscriptionSortingByAmount(),
                        sortingDirection == SortingDirection.ASCENDING));
                }
            }
            else if (leftStack.getItem() instanceof VaultCrystalItem &&
                rightStack.getItem() instanceof VaultCrystalItem)
            {
                if (!VaultJewelSorting.CONFIGURATION.getVaultCrystalSortingByAmount().isEmpty())
                {
                    callbackInfoReturnable.setReturnValue(
                        SortingHelper.compareVaultCrystals(leftStack.getDisplayName().getString(),
                            CrystalData.read(leftStack),
                            rightStack.getDisplayName().getString(),
                            CrystalData.read(rightStack),
                            VaultJewelSorting.CONFIGURATION.getVaultCrystalSortingByAmount(),
                            sortingDirection == SortingDirection.ASCENDING));
                }
            }
            else if (leftStack.getItem() instanceof TrinketItem &&
                rightStack.getItem() instanceof TrinketItem)
            {
                if (!VaultJewelSorting.CONFIGURATION.getTrinketSortingByAmount().isEmpty())
                {
                    callbackInfoReturnable.setReturnValue(
                        SortingHelper.compareTrinkets(leftStack.getDisplayName().getString(),
                            AttributeGearData.read(leftStack),
                            leftStack.getTag(),
                            rightStack.getDisplayName().getString(),
                            AttributeGearData.read(rightStack),
                            rightStack.getTag(),
                            VaultJewelSorting.CONFIGURATION.getTrinketSortingByAmount(),
                            sortingDirection == SortingDirection.ASCENDING));
                }
            }
            else if (leftStack.getItem() instanceof CharmItem &&
                rightStack.getItem() instanceof CharmItem)
            {
                if (!VaultJewelSorting.CONFIGURATION.getCharmSortingByAmount().isEmpty())
                {
                    callbackInfoReturnable.setReturnValue(
                        SortingHelper.compareCharms(leftStack.getDisplayName().getString(),
                            AttributeGearData.read(leftStack),
                            leftStack.getTag(),
                            rightStack.getDisplayName().getString(),
                            AttributeGearData.read(rightStack),
                            rightStack.getTag(),
                            VaultJewelSorting.CONFIGURATION.getCharmSortingByAmount(),
                            sortingDirection == SortingDirection.ASCENDING));
                }
            }
            else if (leftStack.getItem() instanceof VaultDollItem &&
                rightStack.getItem() instanceof VaultDollItem)
            {
                if (!VaultJewelSorting.CONFIGURATION.getDollSortingByAmount().isEmpty())
                {
                    callbackInfoReturnable.setReturnValue(
                        SortingHelper.compareVaultDolls(leftStack.getDisplayName().getString(),
                            leftStack.getTag(),
                            rightStack.getDisplayName().getString(),
                            rightStack.getTag(),
                            VaultJewelSorting.CONFIGURATION.getDollSortingByAmount(),
                            sortingDirection == SortingDirection.ASCENDING));
                }
            }
        }
    }
}
