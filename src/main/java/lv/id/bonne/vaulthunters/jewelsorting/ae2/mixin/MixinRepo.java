//
// Created by BONNe
// Copyright - 2023
//


package lv.id.bonne.vaulthunters.jewelsorting.ae2.mixin;


import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import java.util.Comparator;

import appeng.api.config.SortDir;
import appeng.api.config.SortOrder;
import appeng.api.stacks.AEKey;
import appeng.client.gui.me.common.Repo;
import appeng.menu.me.common.GridInventoryEntry;
import iskallia.vault.gear.data.AttributeGearData;
import iskallia.vault.gear.data.VaultGearData;
import iskallia.vault.init.ModItems;
import iskallia.vault.item.crystal.CrystalData;
import iskallia.vault.item.data.InscriptionData;
import lv.id.bonne.vaulthunters.jewelsorting.VaultJewelSorting;
import lv.id.bonne.vaulthunters.jewelsorting.utils.CustomVaultGearData;
import lv.id.bonne.vaulthunters.jewelsorting.utils.SortingHelper;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;


/**
 * This mixin handles custom jewel sorting order for ae2.
 */
@Mixin(Repo.class)
public abstract class MixinRepo
{
    @Inject(method = "getComparator",
        at = @At("RETURN"),
        cancellable = true,
        remap = false)
    public final void comparator(SortOrder sortOrder,
        SortDir sortDir,
        CallbackInfoReturnable<Comparator<GridInventoryEntry>> cir)
    {
        if (Screen.hasShiftDown())
        {
            return;
        }

        boolean ascending = sortDir == SortDir.ASCENDING;

        cir.setReturnValue(cir.getReturnValue().thenComparing((left, right) ->
        {
            AEKey leftWhat = left.getWhat();
            AEKey rightWhat = right.getWhat();

            if (leftWhat == null || rightWhat == null)
            {
                // Null-pointer check
                return leftWhat == null ? 1 : -1;
            }

            String leftName = leftWhat.getDisplayName().getString();
            String rightName = rightWhat.getDisplayName().getString();

            if (!leftWhat.getId().equals(rightWhat.getId()) ||
                !MixinRepo.isSortable(leftWhat.getId()))
            {
                // Use default string comparing
                return ascending ?
                    String.CASE_INSENSITIVE_ORDER.compare(leftName, rightName) :
                    String.CASE_INSENSITIVE_ORDER.compare(rightName, leftName);
            }

            if (leftWhat.getId() == ModItems.JEWEL.getRegistryName())
            {
                CompoundTag leftTag = leftWhat.toTag().getCompound("tag");
                CompoundTag rightTag = rightWhat.toTag().getCompound("tag");

                if (!leftTag.contains("clientCache") ||
                    !leftTag.getCompound("clientCache").contains(SortingHelper.EXTRA_ATTRIBUTE_INDEX) ||
                    !leftTag.getCompound("clientCache").contains(SortingHelper.EXTRA_ATTRIBUTE_VALUE) ||
                    !leftTag.getCompound("clientCache").contains(SortingHelper.EXTRA_GEAR_LEVEL) ||
                    !leftTag.getCompound("clientCache").contains(SortingHelper.EXTRA_JEWEL_SIZE) ||
                    !rightTag.contains("clientCache") ||
                    !rightTag.getCompound("clientCache").contains(SortingHelper.EXTRA_ATTRIBUTE_INDEX) ||
                    !rightTag.getCompound("clientCache").contains(SortingHelper.EXTRA_ATTRIBUTE_VALUE) ||
                    !rightTag.getCompound("clientCache").contains(SortingHelper.EXTRA_GEAR_LEVEL) ||
                    !rightTag.getCompound("clientCache").contains(SortingHelper.EXTRA_JEWEL_SIZE))
                {
                    // Client cache is not generated. Process everything manually.
                    VaultGearData leftData = CustomVaultGearData.read(leftTag);
                    VaultGearData rightData = CustomVaultGearData.read(rightTag);

                    return switch (sortOrder) {
                        case NAME -> SortingHelper.compareJewels(leftName,
                            leftData,
                            rightName,
                            rightData,
                            VaultJewelSorting.CONFIGURATION.getJewelSortingByName(),
                            ascending);
                        case AMOUNT -> SortingHelper.compareJewels(leftName,
                            leftData,
                            rightName,
                            rightData,
                            VaultJewelSorting.CONFIGURATION.getJewelSortingByAmount(),
                            ascending);
                        case MOD -> SortingHelper.compareJewels(leftName,
                            leftData,
                            rightName,
                            rightData,
                            VaultJewelSorting.CONFIGURATION.getJewelSortingByMod(),
                            ascending);
                    };
                }
                else
                {
                    return switch (sortOrder) {
                        case NAME -> SortingHelper.compareJewels(leftName,
                            leftTag.getCompound("clientCache"),
                            rightName,
                            rightTag.getCompound("clientCache"),
                            VaultJewelSorting.CONFIGURATION.getJewelSortingByName(),
                            ascending);
                        case AMOUNT -> SortingHelper.compareJewels(leftName,
                            leftTag.getCompound("clientCache"),
                            rightName,
                            rightTag.getCompound("clientCache"),
                            VaultJewelSorting.CONFIGURATION.getJewelSortingByAmount(),
                            ascending);
                        case MOD -> SortingHelper.compareJewels(leftName,
                            leftTag.getCompound("clientCache"),
                            rightName,
                            rightTag.getCompound("clientCache"),
                            VaultJewelSorting.CONFIGURATION.getJewelSortingByMod(),
                            ascending);
                    };
                }
            }
            else if (leftWhat.getId() == ModItems.INSCRIPTION.getRegistryName())
            {
                InscriptionData leftData = InscriptionData.empty();
                leftData.deserializeNBT(leftWhat.toTag().getCompound("tag").getCompound("data"));

                InscriptionData rightData = InscriptionData.empty();
                rightData.deserializeNBT(rightWhat.toTag().getCompound("tag").getCompound("data"));

                return switch (sortOrder) {
                    case NAME -> SortingHelper.compareInscriptions(leftName,
                        leftData,
                        rightName,
                        rightData,
                        VaultJewelSorting.CONFIGURATION.getInscriptionSortingByName(),
                        ascending);
                    case AMOUNT -> SortingHelper.compareInscriptions(leftName,
                        leftData,
                        rightName,
                        rightData,
                        VaultJewelSorting.CONFIGURATION.getInscriptionSortingByAmount(),
                        ascending);
                    case MOD -> SortingHelper.compareInscriptions(leftName,
                        leftData,
                        rightName,
                        rightData,
                        VaultJewelSorting.CONFIGURATION.getInscriptionSortingByMod(),
                        ascending);
                };
            }
            else if (leftWhat.getId() == ModItems.VAULT_CRYSTAL.getRegistryName())
            {
                CrystalData leftData = CrystalData.empty();
                leftData.deserializeNBT(leftWhat.toTag().getCompound("tag").getCompound("CrystalData"));

                CrystalData rightData = CrystalData.empty();
                rightData.deserializeNBT(rightWhat.toTag().getCompound("tag").getCompound("CrystalData"));

                return switch (sortOrder) {
                    case NAME -> SortingHelper.compareVaultCrystals(leftName,
                        leftData,
                        rightName,
                        rightData,
                        VaultJewelSorting.CONFIGURATION.getVaultCrystalSortingByName(),
                        ascending);
                    case AMOUNT -> SortingHelper.compareVaultCrystals(leftName,
                        leftData,
                        rightName,
                        rightData,
                        VaultJewelSorting.CONFIGURATION.getVaultCrystalSortingByAmount(),
                        ascending);
                    case MOD -> SortingHelper.compareVaultCrystals(leftName,
                        leftData,
                        rightName,
                        rightData,
                        VaultJewelSorting.CONFIGURATION.getVaultCrystalSortingByMod(),
                        ascending);
                };
            }
            else if (leftWhat.getId() == ModItems.TRINKET.getRegistryName())
            {
                AttributeGearData leftData = CustomVaultGearData.read(leftWhat.toTag().getCompound("tag"));
                AttributeGearData rightData = CustomVaultGearData.read(rightWhat.toTag().getCompound("tag"));

                return switch (sortOrder) {
                    case NAME -> SortingHelper.compareTrinkets(leftName,
                        leftData,
                        leftWhat.toTag().getCompound("tag"),
                        rightName,
                        rightData,
                        rightWhat.toTag().getCompound("tag"),
                        VaultJewelSorting.CONFIGURATION.getTrinketSortingByName(),
                        ascending);
                    case AMOUNT -> SortingHelper.compareTrinkets(leftName,
                        leftData,
                        leftWhat.toTag().getCompound("tag"),
                        rightName,
                        rightData,
                        rightWhat.toTag().getCompound("tag"),
                        VaultJewelSorting.CONFIGURATION.getTrinketSortingByAmount(),
                        ascending);
                    case MOD -> SortingHelper.compareTrinkets(leftName,
                        leftData,
                        leftWhat.toTag().getCompound("tag"),
                        rightName,
                        rightData,
                        rightWhat.toTag().getCompound("tag"),
                        VaultJewelSorting.CONFIGURATION.getTrinketSortingByMod(),
                        ascending);
                };
            }
            else
            {
                VaultGearData leftData = CustomVaultGearData.read(leftWhat.toTag().getCompound("tag"));
                VaultGearData rightData = CustomVaultGearData.read(rightWhat.toTag().getCompound("tag"));

                return switch (sortOrder) {
                    case NAME -> SortingHelper.compareVaultGear(leftName,
                        leftData,
                        rightName,
                        rightData,
                        VaultJewelSorting.CONFIGURATION.getGearSortingByName(),
                        ascending);
                    case AMOUNT -> SortingHelper.compareVaultGear(leftName,
                        leftData,
                        rightName,
                        rightData,
                        VaultJewelSorting.CONFIGURATION.getGearSortingByAmount(),
                        ascending);
                    case MOD -> SortingHelper.compareVaultGear(leftName,
                        leftData,
                        rightName,
                        rightData,
                        VaultJewelSorting.CONFIGURATION.getGearSortingByMod(),
                        ascending);
                };
            }

        }));
    }


    /**
     * This method checks if item is sortable via custom sorting.
     * @param id the ResourceLocation of item.
     * @return true if item is sortable via custom sorting.
     */
    @Unique
    private static boolean isSortable(ResourceLocation id)
    {
        return id.equals(ModItems.JEWEL.getRegistryName()) ||
            id.equals(ModItems.HELMET.getRegistryName()) ||
            id.equals(ModItems.CHESTPLATE.getRegistryName()) ||
            id.equals(ModItems.LEGGINGS.getRegistryName()) ||
            id.equals(ModItems.BOOTS.getRegistryName()) ||
            id.equals(ModItems.SWORD.getRegistryName()) ||
            id.equals(ModItems.AXE.getRegistryName()) ||
            id.equals(ModItems.SHIELD.getRegistryName()) ||
            id.equals(ModItems.IDOL_BENEVOLENT.getRegistryName()) ||
            id.equals(ModItems.IDOL_OMNISCIENT.getRegistryName()) ||
            id.equals(ModItems.IDOL_TIMEKEEPER.getRegistryName()) ||
            id.equals(ModItems.IDOL_MALEVOLENCE.getRegistryName()) ||
            id.equals(ModItems.WAND.getRegistryName()) ||
            id.equals(ModItems.MAGNET.getRegistryName()) ||
            id.equals(ModItems.INSCRIPTION.getRegistryName()) ||
            id.equals(ModItems.VAULT_CRYSTAL.getRegistryName()) ||
            id.equals(ModItems.TRINKET.getRegistryName());
    }
}
