//
// Created by BONNe
// Copyright - 2023
//


package lv.id.bonne.vaulthunters.jewelsorting.quark.mixin;


import com.refinedmods.refinedstorage.screen.grid.sorting.SortingDirection;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import iskallia.vault.gear.data.AttributeGearData;
import iskallia.vault.gear.data.VaultGearData;
import iskallia.vault.init.ModItems;
import iskallia.vault.item.crystal.CrystalData;
import iskallia.vault.item.data.InscriptionData;
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
            !stack1.getItem().equals(stack2.getItem()) ||
            !SortingHelper.isSortable(stack1.getItem().getRegistryName()))
        {
            // Leave to original code.
            return;
        }

        // Deal with Jewels
        if (stack1.getItem() == ModItems.JEWEL)
        {
            if (!VaultJewelSorting.CONFIGURATION.getJewelSortingByName().isEmpty())
            {
                callbackInfoReturnable.setReturnValue(
                    SortingHelper.compareJewels(stack1.getDisplayName().getString(),
                        VaultGearData.read(stack1),
                        stack1.getOrCreateTag().getInt("freeCuts"),
                        stack2.getDisplayName().getString(),
                        VaultGearData.read(stack2),
                        stack2.getOrCreateTag().getInt("freeCuts"),
                        VaultJewelSorting.CONFIGURATION.getJewelSortingByName(),
                        true));

                callbackInfoReturnable.cancel();
            }
        }
        else if (stack1.getItem() == ModItems.TOOL)
        {
// TODO: Compare vault tools by their type? Currently is left just to filter out from VaultGearItem
//                callbackInfoReturnable.setReturnValue(SortingHelper.compareTools(
//                    VaultGearData.read(leftStack),
//                    VaultGearData.read(rightStack),
//                    sortingDirection == SortingDirection.ASCENDING));
//                callbackInfoReturnable.cancel();
        }
        else if (SortingHelper.VAULT_GEAR_SET.contains(stack1.getItem().getRegistryName()))
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
        else if (stack1.getItem() == ModItems.INSCRIPTION)
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
        else if (stack1.getItem() == ModItems.VAULT_CATALYST_INFUSED)
        {
            if (!VaultJewelSorting.CONFIGURATION.getCatalystSortingByName().isEmpty())
            {
                callbackInfoReturnable.setReturnValue(
                    SortingHelper.compareCatalysts(stack1.getDisplayName().getString(),
                        stack1.getTag(),
                        stack2.getDisplayName().getString(),
                        stack2.getTag(),
                        VaultJewelSorting.CONFIGURATION.getCatalystSortingByName(),
                        true));

                callbackInfoReturnable.cancel();
            }
        }
        else if (stack1.getItem() == ModItems.VAULT_CRYSTAL)
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
        else if (stack1.getItem() == ModItems.TRINKET)
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
        else if (SortingHelper.VAULT_CHARMS.contains(stack1.getItem().getRegistryName()))
        {
            if (!VaultJewelSorting.CONFIGURATION.getCharmSortingByName().isEmpty())
            {
                callbackInfoReturnable.setReturnValue(
                    SortingHelper.compareCharms(stack1.getDisplayName().getString(),
                        AttributeGearData.read(stack1),
                        stack1.getTag(),
                        stack2.getDisplayName().getString(),
                        AttributeGearData.read(stack2),
                        stack2.getTag(),
                        VaultJewelSorting.CONFIGURATION.getCharmSortingByName(),
                        true));

                callbackInfoReturnable.cancel();
            }
        }
        else if (stack1.getItem() == ModItems.VAULT_DOLL)
        {
            if (!VaultJewelSorting.CONFIGURATION.getDollSortingByName().isEmpty())
            {
                callbackInfoReturnable.setReturnValue(
                    SortingHelper.compareVaultDolls(stack1.getDisplayName().getString(),
                        stack1.getTag(),
                        stack2.getDisplayName().getString(),
                        stack2.getTag(),
                        VaultJewelSorting.CONFIGURATION.getDollSortingByName(),
                        true));

                callbackInfoReturnable.cancel();
            }
        }
        else if (stack1.getItem() == ModItems.RELIC_FRAGMENT)
        {
            callbackInfoReturnable.setReturnValue(
                SortingHelper.compareRelicFragments(stack1.getTag(),
                    stack2.getTag(),
                    true));

            callbackInfoReturnable.cancel();
        }
        else if (stack1.getItem() == ModItems.RESPEC_FLASK)
        {
            callbackInfoReturnable.setReturnValue(
                SortingHelper.compareRespecFlasks(stack1.getTag(),
                    stack2.getTag(),
                    true));

            callbackInfoReturnable.cancel();
        }
        else if (stack1.getItem() == ModItems.FACETED_FOCUS)
        {
            callbackInfoReturnable.setReturnValue(
                SortingHelper.compareFacedFocus(stack1.getTag(),
                    stack2.getTag(),
                    true));

            callbackInfoReturnable.cancel();
        }
        else if (stack1.getItem() == ModItems.AUGMENT)
        {
            callbackInfoReturnable.setReturnValue(
                SortingHelper.compareAugments(stack1.getTag(),
                    stack2.getTag(),
                    true));

            callbackInfoReturnable.cancel();
        }
        else if (stack1.getItem() == ModItems.BOOSTER_PACK)
        {
            callbackInfoReturnable.setReturnValue(
                SortingHelper.compareBoosterPacks(stack1.getTag(),
                    stack2.getTag(),
                    true));

            callbackInfoReturnable.cancel();
        }
        else if (stack1.getItem() == ModItems.CARD_DECK)
        {
            callbackInfoReturnable.setReturnValue(
                SortingHelper.compareDecks(stack1.getTag(),
                    stack2.getTag(),
                    true));

            callbackInfoReturnable.cancel();
        }
        else if (stack1.getItem() == ModItems.CARD)
        {
            callbackInfoReturnable.setReturnValue(
                SortingHelper.compareCards(stack1.getDisplayName().getString(),
                    stack1.getTag(),
                    stack2.getDisplayName().getString(),
                    stack2.getTag(),
                    VaultJewelSorting.CONFIGURATION.getCardSortingByName(),
                    true));

            callbackInfoReturnable.cancel();
        }
        else if (stack1.getItem() == ModItems.ANTIQUE)
        {
            callbackInfoReturnable.setReturnValue(
                SortingHelper.compareAntique(stack1.getTag(),
                    stack2.getTag(),
                    true));

            callbackInfoReturnable.cancel();
        }
        else if (stack1.getItem() == ModItems.JEWEL_POUCH)
        {
            callbackInfoReturnable.setReturnValue(
                SortingHelper.comparePouches(stack1.getTag(),
                    stack2.getTag(),
                    true));

            callbackInfoReturnable.cancel();
        }
    }
}
