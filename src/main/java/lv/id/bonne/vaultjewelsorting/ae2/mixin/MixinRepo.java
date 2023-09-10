//
// Created by BONNe
// Copyright - 2023
//


package lv.id.bonne.vaultjewelsorting.ae2.mixin;


import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
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
import iskallia.vault.gear.data.VaultGearData;
import iskallia.vault.init.ModItems;
import lv.id.bonne.vaultjewelsorting.utils.CustomVaultGearData;
import lv.id.bonne.vaultjewelsorting.utils.SortingHelper;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.resources.ResourceLocation;


/**
 * This mixin handles custom jewel sorting order for ae2.
 */
@Mixin(Repo.class)
public abstract class MixinRepo
{
    @Shadow(remap = false)
    public abstract boolean isPaused();


    @Inject(method = "getComparator",
        at = @At("RETURN"),
        cancellable = true,
        remap = false)
    public final void comparator(SortOrder sortOrder,
        SortDir sortDir,
        CallbackInfoReturnable<Comparator<GridInventoryEntry>> cir)
    {
        if (!Screen.hasShiftDown() && !isPaused())
        {
            if (sortOrder.equals(SortOrder.MOD))
            {
                // Only if sorting by name and size.
                return;
            }
        }

        boolean ascending = sortDir == SortDir.ASCENDING;
        boolean bySize = sortOrder.equals(SortOrder.AMOUNT);

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
                !MixinRepo.isSortable(leftWhat.getId()) ||
                !leftName.equalsIgnoreCase(rightName))
            {
                // Use default string comparing
                return ascending ?
                    String.CASE_INSENSITIVE_ORDER.compare(leftName, rightName) :
                    String.CASE_INSENSITIVE_ORDER.compare(rightName, leftName);
            }

            VaultGearData leftData = CustomVaultGearData.read(leftWhat.toTag().getCompound("tag"));
            VaultGearData rightData = CustomVaultGearData.read(rightWhat.toTag().getCompound("tag"));

            if (leftWhat.getId() == ModItems.JEWEL.getRegistryName())
            {
                if (bySize)
                {
                    return SortingHelper.compareJewelsSize(leftData, rightData, ascending);
                }
                else
                {
                    return SortingHelper.compareJewels(leftData, rightData, ascending);
                }
            }
            else
            {
                return SortingHelper.compareVaultGear(leftData, rightData, ascending);
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
            id.equals(ModItems.MAGNET.getRegistryName());
    }
}
