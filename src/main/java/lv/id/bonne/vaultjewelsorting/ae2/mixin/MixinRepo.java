//
// Created by BONNe
// Copyright - 2023
//


package lv.id.bonne.vaultjewelsorting.ae2.mixin;


import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.util.ArrayList;
import java.util.Comparator;

import appeng.api.config.SortDir;
import appeng.api.config.SortOrder;
import appeng.api.stacks.AEKey;
import appeng.client.gui.me.common.Repo;
import appeng.client.gui.widgets.ISortSource;
import appeng.menu.me.common.GridInventoryEntry;
import iskallia.vault.gear.data.VaultGearData;
import iskallia.vault.init.ModItems;
import lv.id.bonne.vaultjewelsorting.utils.SortingHelper;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.world.item.ItemStack;


/**
 * This mixin handles custom jewel sorting order for ae2.
 */
@Mixin(Repo.class)
public abstract class MixinRepo
{
    @Shadow(remap = false)
    @Final
    private ArrayList<GridInventoryEntry> view;

    @Shadow(remap = false)
    public abstract boolean isPaused();

    @Shadow(remap = false)
    @Final
    private ISortSource sortSrc;


    /**
     * This method injects code at the end of updateView to resort jewel items.
     *
     * @param callbackInfo callback info
     */
    @Inject(method = "updateView()V", at = @At("TAIL"), remap = false)
    public final void updateView(CallbackInfo callbackInfo)
    {
        // do not sort if shift is pressed.
        if (!Screen.hasShiftDown() && !isPaused())
        {
            var sortOrder = this.sortSrc.getSortBy();
            var sortDir = this.sortSrc.getSortDir();

            if (!sortOrder.equals(SortOrder.NAME))
            {
                // Only if sorting by name
                return;
            }

            this.view.sort(this.getJewelComparator(sortDir == SortDir.ASCENDING));
        }
    }


    /**
     * This method returns comparator for sorting jewels or default name sorting.
     * @param ascending boolean that indicates if order is ascending or descending.
     * @return Comparator for sorting jewels or default name sorting.
     */
    @Unique
    private Comparator<GridInventoryEntry> getJewelComparator(boolean ascending)
    {
        return (left, right) ->
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
                leftWhat.getId() != ModItems.JEWEL.getRegistryName() ||
                !leftName.equalsIgnoreCase(rightName))
            {
                // Use default string comparing
                return ascending ?
                    String.CASE_INSENSITIVE_ORDER.compare(leftName, rightName) :
                    String.CASE_INSENSITIVE_ORDER.compare(rightName, leftName);
            }

            // Unfortunately, I was not able to generate VaultGearData from tag. So I had to
            // add dummy item for parser.
            // This is non-optimal solution, and will need to search a way to improve it.
            ItemStack leftItem = new ItemStack(ModItems.JEWEL);
            leftItem.getOrCreateTag().putLongArray("vaultGearData",
                leftWhat.toTag().getCompound("tag").getLongArray("vaultGearData"));

            ItemStack rightItem = new ItemStack(ModItems.JEWEL);
            rightItem.getOrCreateTag().putLongArray("vaultGearData",
                rightWhat.toTag().getCompound("tag").getLongArray("vaultGearData"));

            return SortingHelper.compareJewels(VaultGearData.read(leftItem),
                VaultGearData.read(rightItem),
                ascending);
        };
    }
}
