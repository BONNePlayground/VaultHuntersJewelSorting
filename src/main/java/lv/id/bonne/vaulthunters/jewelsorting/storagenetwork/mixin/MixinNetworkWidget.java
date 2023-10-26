package lv.id.bonne.vaulthunters.jewelsorting.storagenetwork.mixin;


import com.lothrazar.storagenetwork.api.IGuiNetwork;
import com.lothrazar.storagenetwork.gui.NetworkWidget;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import java.util.Comparator;

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

            // Do not sort if shift is pressed
            if (first.getItem() instanceof JewelItem &&
                second.getItem() instanceof JewelItem)
            {
                String leftName = first.getDisplayName().getString();
                String rightName = second.getDisplayName().getString();
                VaultGearData leftData = VaultGearData.read(first);
                VaultGearData rightData = VaultGearData.read(second);

                return switch (this.gui.getSort()) {
                    case NAME -> SortingHelper.compareJewels(leftName,
                        leftData,
                        rightName,
                        rightData,
                        VaultJewelSorting.CONFIGURATION.getJewelSortingByName(),
                        this.gui.getDownwards());
                    case AMOUNT -> SortingHelper.compareJewels(leftName,
                        leftData,
                        rightName,
                        rightData,
                        VaultJewelSorting.CONFIGURATION.getJewelSortingByAmount(),
                        this.gui.getDownwards());
                    case MOD -> SortingHelper.compareJewels(leftName,
                        leftData,
                        rightName,
                        rightData,
                        VaultJewelSorting.CONFIGURATION.getJewelSortingByMod(),
                        this.gui.getDownwards());
                };
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
                String leftName = first.getDisplayName().getString();
                String rightName = second.getDisplayName().getString();
                VaultGearData leftData = VaultGearData.read(first);
                VaultGearData rightData = VaultGearData.read(second);

                return switch (this.gui.getSort()) {
                    case NAME -> SortingHelper.compareVaultGear(leftName,
                        leftData,
                        rightName,
                        rightData,
                        VaultJewelSorting.CONFIGURATION.getGearSortingByName(),
                        this.gui.getDownwards());
                    case AMOUNT -> SortingHelper.compareVaultGear(leftName,
                        leftData,
                        rightName,
                        rightData,
                        VaultJewelSorting.CONFIGURATION.getGearSortingByAmount(),
                        this.gui.getDownwards());
                    case MOD -> SortingHelper.compareVaultGear(leftName,
                        leftData,
                        rightName,
                        rightData,
                        VaultJewelSorting.CONFIGURATION.getGearSortingByMod(),
                        this.gui.getDownwards());
                };
            }
            else if (first.getItem() instanceof InscriptionItem &&
                second.getItem() instanceof InscriptionItem)
            {
                String leftName = first.getDisplayName().getString();
                String rightName = second.getDisplayName().getString();
                InscriptionData leftData = InscriptionData.from(first);
                InscriptionData rightData = InscriptionData.from(second);

                return switch (this.gui.getSort()) {
                    case NAME -> SortingHelper.compareInscriptions(leftName,
                        leftData,
                        rightName,
                        rightData,
                        VaultJewelSorting.CONFIGURATION.getInscriptionSortingByName(),
                        true);
                    case AMOUNT -> SortingHelper.compareInscriptions(leftName,
                        leftData,
                        rightName,
                        rightData,
                        VaultJewelSorting.CONFIGURATION.getInscriptionSortingByAmount(),
                        true);
                    case MOD -> SortingHelper.compareInscriptions(leftName,
                        leftData,
                        rightName,
                        rightData,
                        VaultJewelSorting.CONFIGURATION.getInscriptionSortingByMod(),
                        true);
                };
            }
            else if (first.getItem() instanceof VaultCrystalItem &&
                second.getItem() instanceof VaultCrystalItem)
            {
                String leftName = first.getDisplayName().getString();
                String rightName = second.getDisplayName().getString();
                CrystalData leftData = CrystalData.read(first);
                CrystalData rightData = CrystalData.read(second);

                return switch (this.gui.getSort()) {
                    case NAME -> SortingHelper.compareVaultCrystals(leftName,
                        leftData,
                        rightName,
                        rightData,
                        VaultJewelSorting.CONFIGURATION.getVaultCrystalSortingByName(),
                        true);
                    case AMOUNT -> SortingHelper.compareVaultCrystals(leftName,
                        leftData,
                        rightName,
                        rightData,
                        VaultJewelSorting.CONFIGURATION.getVaultCrystalSortingByAmount(),
                        true);
                    case MOD -> SortingHelper.compareVaultCrystals(leftName,
                        leftData,
                        rightName,
                        rightData,
                        VaultJewelSorting.CONFIGURATION.getVaultCrystalSortingByMod(),
                        true);
                };
            }
            else if (first.getItem() instanceof TrinketItem &&
                second.getItem() instanceof TrinketItem)
            {
                return switch (this.gui.getSort()) {
                    case NAME -> SortingHelper.compareTrinkets(first.getDisplayName().getString(),
                        AttributeGearData.read(first),
                        first.getTag(),
                        second.getDisplayName().getString(),
                        AttributeGearData.read(second),
                        second.getTag(),
                        VaultJewelSorting.CONFIGURATION.getTrinketSortingByName(),
                        true);
                    case AMOUNT -> SortingHelper.compareTrinkets(first.getDisplayName().getString(),
                        AttributeGearData.read(first),
                        first.getTag(),
                        second.getDisplayName().getString(),
                        AttributeGearData.read(second),
                        second.getTag(),
                        VaultJewelSorting.CONFIGURATION.getTrinketSortingByAmount(),
                        true);
                    case MOD -> SortingHelper.compareTrinkets(first.getDisplayName().getString(),
                        AttributeGearData.read(first),
                        first.getTag(),
                        second.getDisplayName().getString(),
                        AttributeGearData.read(second),
                        second.getTag(),
                        VaultJewelSorting.CONFIGURATION.getTrinketSortingByMod(),
                        true);
                };
            }

            return 0;
        });
    }
}
