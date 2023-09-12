package lv.id.bonne.vaultjewelsorting.sophisticatedcore.mixin;

import iskallia.vault.gear.data.VaultGearData;
import iskallia.vault.gear.item.VaultGearItem;
import iskallia.vault.item.InscriptionItem;
import iskallia.vault.item.data.InscriptionData;
import iskallia.vault.item.tool.JewelItem;
import iskallia.vault.item.tool.ToolItem;
import lv.id.bonne.vaultjewelsorting.VaultJewelSorting;
import lv.id.bonne.vaultjewelsorting.utils.SortingHelper;
import net.p3pp3rf1y.sophisticatedcore.inventory.ItemStackKey;
import net.p3pp3rf1y.sophisticatedcore.util.InventorySorter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.Comparator;
import java.util.Map;

@Mixin(value = InventorySorter.class, remap = false)
public class MixinInventorySorter
{
    @ModifyArg(method = "sortHandler", at = @At(value = "INVOKE", target = "Ljava/util/List;sort(Ljava/util/Comparator;)V"))
    private static Comparator<Map.Entry<ItemStackKey, Integer>> compareJewels(Comparator<Map.Entry<ItemStackKey, Integer>> original)
    {
        if (original == InventorySorter.BY_COUNT)
        {
            return original.thenComparing((first, second) ->
            {
                if (first.getKey().getStack().getItem() instanceof JewelItem &&
                    second.getKey().getStack().getItem() instanceof JewelItem)
                {
                    return SortingHelper.compareJewels(
                        first.getKey().getStack().getDisplayName().getString(),
                        VaultGearData.read(first.getKey().getStack()),
                        second.getKey().getStack().getDisplayName().getString(),
                        VaultGearData.read(second.getKey().getStack()),
                        VaultJewelSorting.CONFIGURATION.getJewelSortingByAmount(),
                        true);
                }
                else if (first.getKey().getStack().getItem() instanceof ToolItem &&
                    second.getKey().getStack().getItem() instanceof ToolItem)
                {
// TODO: Compare vault tools by their type? Currently is left just to filter out from VaultGearItem
//                return SortingHelper.compareTools(
//                    VaultGearData.read(leftStack),
//                    VaultGearData.read(rightStack),
//                    true));
                }
                else if (first.getKey().getStack().getItem() instanceof VaultGearItem &&
                    second.getKey().getStack().getItem() instanceof VaultGearItem)
                {
                    return SortingHelper.compareVaultGear(
                        first.getKey().getStack().getDisplayName().getString(),
                        VaultGearData.read(first.getKey().getStack()),
                        second.getKey().getStack().getDisplayName().getString(),
                        VaultGearData.read(second.getKey().getStack()),
                        VaultJewelSorting.CONFIGURATION.getGearSortingByAmount(),
                        true);
                }
                else if (first.getKey().getStack().getItem() instanceof InscriptionItem &&
                    second.getKey().getStack().getItem() instanceof InscriptionItem)
                {
                    return SortingHelper.compareInscriptions(
                        first.getKey().getStack().getDisplayName().getString(),
                        InscriptionData.from(first.getKey().getStack()),
                        second.getKey().getStack().getDisplayName().getString(),
                        InscriptionData.from(second.getKey().getStack()),
                        VaultJewelSorting.CONFIGURATION.getInscriptionSortingByAmount(),
                        true);
                }

                return 0;
            });
        }
        else
        {
            return original.thenComparing((first, second) ->
            {
                if (first.getKey().getStack().getItem() instanceof JewelItem &&
                    second.getKey().getStack().getItem() instanceof JewelItem)
                {
                    return SortingHelper.compareJewels(
                        first.getKey().getStack().getDisplayName().getString(),
                        VaultGearData.read(first.getKey().getStack()),
                        second.getKey().getStack().getDisplayName().getString(),
                        VaultGearData.read(second.getKey().getStack()),
                        VaultJewelSorting.CONFIGURATION.getJewelSortingByName(),
                        true);
                }
                else if (first.getKey().getStack().getItem() instanceof ToolItem &&
                    second.getKey().getStack().getItem() instanceof ToolItem)
                {
// TODO: Compare vault tools by their type? Currently is left just to filter out from VaultGearItem
//                return SortingHelper.compareTools(
//                    VaultGearData.read(leftStack),
//                    VaultGearData.read(rightStack),
//                    true));
                }
                else if (first.getKey().getStack().getItem() instanceof VaultGearItem &&
                    second.getKey().getStack().getItem() instanceof VaultGearItem)
                {
                    return SortingHelper.compareVaultGear(
                        first.getKey().getStack().getDisplayName().getString(),
                        VaultGearData.read(first.getKey().getStack()),
                        second.getKey().getStack().getDisplayName().getString(),
                        VaultGearData.read(second.getKey().getStack()),
                        VaultJewelSorting.CONFIGURATION.getGearSortingByName(),
                        true);
                }
                else if (first.getKey().getStack().getItem() instanceof InscriptionItem &&
                    second.getKey().getStack().getItem() instanceof InscriptionItem)
                {
                    return SortingHelper.compareInscriptions(
                        first.getKey().getStack().getDisplayName().getString(),
                        InscriptionData.from(first.getKey().getStack()),
                        second.getKey().getStack().getDisplayName().getString(),
                        InscriptionData.from(second.getKey().getStack()),
                        VaultJewelSorting.CONFIGURATION.getInscriptionSortingByName(),
                        true);
                }

                return 0;
            });
        }
    }
}
