package lv.id.bonne.vaultjewelsorting.sophisticatedcore.mixin;

import iskallia.vault.gear.data.VaultGearData;
import iskallia.vault.gear.item.VaultGearItem;
import iskallia.vault.item.tool.JewelItem;
import iskallia.vault.item.tool.ToolItem;
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
        return original.thenComparing((first, second) ->
        {
            if (first.getKey().getStack().getItem() instanceof JewelItem &&
                second.getKey().getStack().getItem() instanceof JewelItem)
            {
                return SortingHelper.compareJewels(
                    VaultGearData.read(first.getKey().getStack()),
                    VaultGearData.read(second.getKey().getStack()),
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
                    VaultGearData.read(first.getKey().getStack()),
                    VaultGearData.read(second.getKey().getStack()),
                    true);
            }

            return 0;
        });
    }
}
