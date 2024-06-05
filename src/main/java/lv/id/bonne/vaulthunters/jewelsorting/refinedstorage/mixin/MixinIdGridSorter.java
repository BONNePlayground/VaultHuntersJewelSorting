//
// Created by BONNe
// Copyright - 2023
//


package lv.id.bonne.vaulthunters.jewelsorting.refinedstorage.mixin;


import com.refinedmods.refinedstorage.screen.grid.sorting.IdGridSorter;
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
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.world.item.ItemStack;


/**
 * This mixin handles custom jewel sorting order for rs.
 */
@Mixin(IdGridSorter.class)
public class MixinIdGridSorter
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

        if (left.getIngredient() instanceof ItemStack leftStack &&
            right.getIngredient() instanceof ItemStack rightStack)
        {
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
                if (!VaultJewelSorting.CONFIGURATION.getJewelSortingByMod().isEmpty())
                {
                    GearDataCache leftData = GearDataCache.of(leftStack);
                    GearDataCache rightData = GearDataCache.of(rightStack);

                    // Update item cache if vault versions mismatch.
                    if (((IExtraGearDataCache) leftData).isInvalidCache())
                    {
                        GearDataCache.removeCache(leftStack);
                        GearDataCache.createCache(leftStack);
                    }

                    // Update item cache if vault versions mismatch.
                    if (((IExtraGearDataCache) rightData).isInvalidCache())
                    {
                        GearDataCache.removeCache(rightStack);
                        GearDataCache.createCache(rightStack);
                    }

                    callbackInfoReturnable.setReturnValue(SortingHelper.compareJewels(left.getName(),
                        GearDataCache.of(leftStack),
                        right.getName(),
                        GearDataCache.of(rightStack),
                        VaultJewelSorting.CONFIGURATION.getJewelSortingByMod(),
                        sortingDirection == SortingDirection.ASCENDING));
                    callbackInfoReturnable.cancel();
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
                if (!VaultJewelSorting.CONFIGURATION.getGearSortingByMod().isEmpty())
                {
                    callbackInfoReturnable.setReturnValue(SortingHelper.compareVaultGear(left.getName(),
                        VaultGearData.read(leftStack),
                        right.getName(),
                        VaultGearData.read(rightStack),
                        VaultJewelSorting.CONFIGURATION.getGearSortingByMod(),
                        sortingDirection == SortingDirection.ASCENDING));
                    callbackInfoReturnable.cancel();
                }
            }
            else if (leftStack.getItem() instanceof InscriptionItem &&
                rightStack.getItem() instanceof InscriptionItem)
            {
                if (!VaultJewelSorting.CONFIGURATION.getInscriptionSortingByMod().isEmpty())
                {
                    callbackInfoReturnable.setReturnValue(SortingHelper.compareInscriptions(left.getName(),
                        InscriptionData.from(leftStack),
                        right.getName(),
                        InscriptionData.from(rightStack),
                        VaultJewelSorting.CONFIGURATION.getInscriptionSortingByMod(),
                        sortingDirection == SortingDirection.ASCENDING));
                    callbackInfoReturnable.cancel();
                }
            }
            else if (leftStack.getItem() instanceof VaultCrystalItem &&
                rightStack.getItem() instanceof VaultCrystalItem)
            {
                if (!VaultJewelSorting.CONFIGURATION.getVaultCrystalSortingByMod().isEmpty())
                {
                    callbackInfoReturnable.setReturnValue(
                        SortingHelper.compareVaultCrystals(leftStack.getDisplayName().getString(),
                            CrystalData.read(leftStack),
                            rightStack.getDisplayName().getString(),
                            CrystalData.read(rightStack),
                            VaultJewelSorting.CONFIGURATION.getVaultCrystalSortingByMod(),
                            sortingDirection == SortingDirection.ASCENDING));
                    callbackInfoReturnable.cancel();
                }
            }
            else if (leftStack.getItem() instanceof TrinketItem &&
                rightStack.getItem() instanceof TrinketItem)
            {
                if (!VaultJewelSorting.CONFIGURATION.getTrinketSortingByMod().isEmpty())
                {
                    callbackInfoReturnable.setReturnValue(
                        SortingHelper.compareTrinkets(leftStack.getDisplayName().getString(),
                            AttributeGearData.read(leftStack),
                            leftStack.getTag(),
                            rightStack.getDisplayName().getString(),
                            AttributeGearData.read(rightStack),
                            rightStack.getTag(),
                            VaultJewelSorting.CONFIGURATION.getTrinketSortingByMod(),
                            sortingDirection == SortingDirection.ASCENDING));
                    callbackInfoReturnable.cancel();
                }
            }
            else if (leftStack.getItem() instanceof CharmItem &&
                rightStack.getItem() instanceof CharmItem)
            {
                if (!VaultJewelSorting.CONFIGURATION.getCharmSortingByMod().isEmpty())
                {
                    callbackInfoReturnable.setReturnValue(
                        SortingHelper.compareCharms(leftStack.getDisplayName().getString(),
                            AttributeGearData.read(leftStack),
                            leftStack.getTag(),
                            rightStack.getDisplayName().getString(),
                            AttributeGearData.read(rightStack),
                            rightStack.getTag(),
                            VaultJewelSorting.CONFIGURATION.getCharmSortingByMod(),
                            sortingDirection == SortingDirection.ASCENDING));
                    callbackInfoReturnable.cancel();
                }
            }
            else if (leftStack.getItem() instanceof InfusedCatalystItem &&
                rightStack.getItem() instanceof InfusedCatalystItem)
            {
                if (!VaultJewelSorting.CONFIGURATION.getCatalystSortingByMod().isEmpty())
                {
                    callbackInfoReturnable.setReturnValue(
                        SortingHelper.compareCatalysts(leftStack.getDisplayName().getString(),
                            leftStack.getTag(),
                            rightStack.getDisplayName().getString(),
                            rightStack.getTag(),
                            VaultJewelSorting.CONFIGURATION.getCatalystSortingByMod(),
                            sortingDirection == SortingDirection.ASCENDING));
                }
            }
            else if (leftStack.getItem() instanceof VaultDollItem &&
                rightStack.getItem() instanceof VaultDollItem)
            {
                if (!VaultJewelSorting.CONFIGURATION.getDollSortingByMod().isEmpty())
                {
                    callbackInfoReturnable.setReturnValue(
                        SortingHelper.compareVaultDolls(leftStack.getDisplayName().getString(),
                            leftStack.getTag(),
                            rightStack.getDisplayName().getString(),
                            rightStack.getTag(),
                            VaultJewelSorting.CONFIGURATION.getDollSortingByMod(),
                            sortingDirection == SortingDirection.ASCENDING));
                }
            }
            else if (leftStack.getItem() instanceof RelicFragmentItem &&
                rightStack.getItem() instanceof RelicFragmentItem)
            {
                callbackInfoReturnable.setReturnValue(
                    SortingHelper.compareRelicFragments(
                        leftStack.getTag(),
                        rightStack.getTag(),
                        sortingDirection == SortingDirection.ASCENDING));
            }
            else if (leftStack.getItem() instanceof ItemRespecFlask &&
                rightStack.getItem() instanceof ItemRespecFlask)
            {
                callbackInfoReturnable.setReturnValue(
                    SortingHelper.compareRespecFlasks(
                        leftStack.getTag(),
                        rightStack.getTag(),
                        sortingDirection == SortingDirection.ASCENDING));
            }
            else if (leftStack.getItem() == ModItems.FACETED_FOCUS &&
                rightStack.getItem() == ModItems.FACETED_FOCUS)
            {
                callbackInfoReturnable.setReturnValue(
                    SortingHelper.compareFacedFocus(
                        leftStack.getTag(),
                        rightStack.getTag(),
                        sortingDirection == SortingDirection.ASCENDING));
            }
            else if (leftStack.getItem() == ModItems.AUGMENT &&
                rightStack.getItem() == ModItems.AUGMENT)
            {
                callbackInfoReturnable.setReturnValue(
                    SortingHelper.compareAugments(
                        leftStack.getTag(),
                        rightStack.getTag(),
                        sortingDirection == SortingDirection.ASCENDING));
            }
        }
    }
}
