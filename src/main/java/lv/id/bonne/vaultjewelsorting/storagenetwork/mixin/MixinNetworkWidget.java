package lv.id.bonne.vaultjewelsorting.storagenetwork.mixin;


import com.lothrazar.storagenetwork.api.EnumSortType;
import com.lothrazar.storagenetwork.api.IGuiNetwork;
import com.lothrazar.storagenetwork.gui.NetworkWidget;
import com.refinedmods.refinedstorage.screen.grid.sorting.SortingDirection;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import java.util.Comparator;

import iskallia.vault.gear.data.VaultGearData;
import iskallia.vault.gear.item.VaultGearItem;
import iskallia.vault.item.tool.JewelItem;
import iskallia.vault.item.tool.ToolItem;
import lv.id.bonne.vaultjewelsorting.utils.SortingHelper;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.world.item.ItemStack;


/**
 * This mixin is used to sort jewels in Simple Storage Network mod.
 */
@Mixin(value = NetworkWidget.class, remap = false)
public class MixinNetworkWidget
{
    /**
     * The gui that is used to sort jewels.
     */
    @Shadow
    @Final
    private IGuiNetwork gui;


    /**
     * This method modifies the sortStackWrappers method to sort jewels.
     * It is done by modifying second parameter in Collections#sort call inside sortStackWrappers method.
     * @param original The original comparator.
     * @return The modified comparator that sorts jewels.
     */
    @ModifyArg(method = "sortStackWrappers", at = @At(value = "INVOKE", target = "Ljava/util/Collections;sort(Ljava/util/List;Ljava/util/Comparator;)V"), index = 1)
    private Comparator<ItemStack> sortStackWrappersJewelCompare(Comparator<ItemStack> original)
    {
        return original.thenComparing((first, second) ->
        {
            if (Screen.hasShiftDown())
            {
                // Fast exit on shift+click.
                return 0;
            }

            int returnValue = 0;

            // Do not sort if shift is pressed
            if (first.getItem() instanceof JewelItem &&
                second.getItem() instanceof JewelItem)
            {
                switch (this.gui.getSort())
                {
                    case AMOUNT -> {
                        returnValue = SortingHelper.compareJewelsSize(
                            VaultGearData.read(first),
                            VaultGearData.read(second),
                            this.gui.getDownwards());
                    }
                    case NAME -> {
                        returnValue = SortingHelper.compareJewels(
                            VaultGearData.read(first),
                            VaultGearData.read(second),
                            this.gui.getDownwards());
                    }
                }
            }
            else if (first.getItem() instanceof ToolItem &&
                second.getItem() instanceof ToolItem)
            {
// TODO: Compare vault tools by their type? Currently is left just to filter out from VaultGearItem
//                callbackInfoReturnable.setReturnValue(SortingHelper.compareTools(
//                    VaultGearData.read(leftStack),
//                    VaultGearData.read(rightStack),
//                    sortingDirection == SortingDirection.ASCENDING));
//                callbackInfoReturnable.cancel();
            }
            else if (first.getItem() instanceof VaultGearItem &&
                second.getItem() instanceof VaultGearItem)
            {
                returnValue = SortingHelper.compareVaultGear(
                    VaultGearData.read(first),
                    VaultGearData.read(second),
                    this.gui.getDownwards());
            }

            return returnValue;
        });
    }
}
