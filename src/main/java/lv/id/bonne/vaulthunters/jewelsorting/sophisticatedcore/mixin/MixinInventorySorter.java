package lv.id.bonne.vaulthunters.jewelsorting.sophisticatedcore.mixin;

import iskallia.vault.gear.data.AttributeGearData;
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
import lv.id.bonne.vaulthunters.jewelsorting.utils.SortingHelper;
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
                int registryOrder = SortingHelper.compareRegistryNames(
                    first.getKey().getStack().getItem().getRegistryName(),
                    second.getKey().getStack().getItem().getRegistryName(),
                    true);

                if (registryOrder != 0)
                {
                    // Use default string comparing
                    return registryOrder;
                }
                else if (first.getKey().getStack().getItem() instanceof JewelItem &&
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
                else if (first.getKey().getStack().getItem() instanceof VaultCrystalItem &&
                    second.getKey().getStack().getItem() instanceof VaultCrystalItem)
                {
                    if (!VaultJewelSorting.CONFIGURATION.getVaultCrystalSortingByAmount().isEmpty())
                    {
                        return SortingHelper.compareVaultCrystals(
                            first.getKey().getStack().getDisplayName().getString(),
                            CrystalData.read(first.getKey().getStack()),
                            second.getKey().getStack().getDisplayName().getString(),
                            CrystalData.read(second.getKey().getStack()),
                            VaultJewelSorting.CONFIGURATION.getVaultCrystalSortingByAmount(),
                            true);
                    }
                }
                else if (first.getKey().getStack().getItem() instanceof TrinketItem &&
                    second.getKey().getStack().getItem() instanceof TrinketItem)
                {
                    if (!VaultJewelSorting.CONFIGURATION.getTrinketSortingByAmount().isEmpty())
                    {
                        return SortingHelper.compareTrinkets(first.getKey().getStack().getDisplayName().getString(),
                            AttributeGearData.read(first.getKey().getStack()),
                            first.getKey().getStack().getTag(),
                            second.getKey().getStack().getDisplayName().getString(),
                            AttributeGearData.read(second.getKey().getStack()),
                            second.getKey().getStack().getTag(),
                            VaultJewelSorting.CONFIGURATION.getTrinketSortingByAmount(),
                            true);
                    }
                }
                else if (first.getKey().getStack().getItem() instanceof CharmItem &&
                    second.getKey().getStack().getItem() instanceof CharmItem)
                {
                    if (!VaultJewelSorting.CONFIGURATION.getCharmSortingByAmount().isEmpty())
                    {
                        return SortingHelper.compareCharms(first.getKey().getStack().getDisplayName().getString(),
                            AttributeGearData.read(first.getKey().getStack()),
                            first.getKey().getStack().getTag(),
                            second.getKey().getStack().getDisplayName().getString(),
                            AttributeGearData.read(second.getKey().getStack()),
                            second.getKey().getStack().getTag(),
                            VaultJewelSorting.CONFIGURATION.getCharmSortingByAmount(),
                            true);
                    }
                }
                else if (first.getKey().getStack().getItem() instanceof InfusedCatalystItem &&
                    second.getKey().getStack().getItem() instanceof InfusedCatalystItem)
                {
                    if (!VaultJewelSorting.CONFIGURATION.getCatalystSortingByAmount().isEmpty())
                    {
                        return SortingHelper.compareCatalysts(
                            first.getKey().getStack().getDisplayName().getString(),
                            first.getKey().getStack().getTag(),
                            second.getKey().getStack().getDisplayName().getString(),
                            second.getKey().getStack().getTag(),
                            VaultJewelSorting.CONFIGURATION.getCatalystSortingByAmount(),
                            true);
                    }
                }
                else if (first.getKey().getStack().getItem() instanceof VaultDollItem &&
                    second.getKey().getStack().getItem() instanceof VaultDollItem)
                {
                    if (!VaultJewelSorting.CONFIGURATION.getDollSortingByAmount().isEmpty())
                    {
                        return SortingHelper.compareVaultDolls(first.getKey().getStack().getDisplayName().getString(),
                            first.getKey().getStack().getTag(),
                            second.getKey().getStack().getDisplayName().getString(),
                            second.getKey().getStack().getTag(),
                            VaultJewelSorting.CONFIGURATION.getDollSortingByAmount(),
                            true);
                    }
                }
                else if (first.getKey().getStack().getItem() instanceof RelicFragmentItem &&
                    second.getKey().getStack().getItem() instanceof RelicFragmentItem)
                {
                    return SortingHelper.compareRelicFragments(
                        first.getKey().getStack().getTag(),
                        second.getKey().getStack().getTag(),
                        true);
                }
                else if (first.getKey().getStack().getItem() instanceof ItemRespecFlask &&
                    second.getKey().getStack().getItem() instanceof ItemRespecFlask)
                {
                    return SortingHelper.compareRespecFlasks(
                        first.getKey().getStack().getTag(),
                        second.getKey().getStack().getTag(),
                        true);
                }
                else if (first.getKey().getStack().getItem() == ModItems.FACETED_FOCUS &&
                    second.getKey().getStack().getItem() == ModItems.FACETED_FOCUS)
                {
                    return SortingHelper.compareFacedFocus(
                        first.getKey().getStack().getTag(),
                        second.getKey().getStack().getTag(),
                        true);
                }
                else if (first.getKey().getStack().getItem() == ModItems.AUGMENT &&
                    second.getKey().getStack().getItem() == ModItems.AUGMENT)
                {
                    return SortingHelper.compareAugments(
                        first.getKey().getStack().getTag(),
                        second.getKey().getStack().getTag(),
                        true);
                }

                return 0;
            });
        }
        else
        {
            return original.thenComparing((first, second) ->
            {
                int registryOrder = SortingHelper.compareRegistryNames(
                    first.getKey().getStack().getItem().getRegistryName(),
                    second.getKey().getStack().getItem().getRegistryName(),
                    true);

                if (registryOrder != 0)
                {
                    // Use default string comparing
                    return registryOrder;
                }
                else if (first.getKey().getStack().getItem() instanceof JewelItem &&
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
                else if (first.getKey().getStack().getItem() instanceof VaultCrystalItem &&
                    second.getKey().getStack().getItem() instanceof VaultCrystalItem)
                {
                    if (!VaultJewelSorting.CONFIGURATION.getVaultCrystalSortingByName().isEmpty())
                    {
                        return SortingHelper.compareVaultCrystals(
                            first.getKey().getStack().getDisplayName().getString(),
                            CrystalData.read(first.getKey().getStack()),
                            second.getKey().getStack().getDisplayName().getString(),
                            CrystalData.read(second.getKey().getStack()),
                            VaultJewelSorting.CONFIGURATION.getVaultCrystalSortingByName(),
                            true);
                    }
                }
                else if (first.getKey().getStack().getItem() instanceof TrinketItem &&
                    second.getKey().getStack().getItem() instanceof TrinketItem)
                {
                    if (!VaultJewelSorting.CONFIGURATION.getTrinketSortingByName().isEmpty())
                    {
                        return SortingHelper.compareTrinkets(first.getKey().getStack().getDisplayName().getString(),
                            AttributeGearData.read(first.getKey().getStack()),
                            first.getKey().getStack().getTag(),
                            second.getKey().getStack().getDisplayName().getString(),
                            AttributeGearData.read(second.getKey().getStack()),
                            second.getKey().getStack().getTag(),
                            VaultJewelSorting.CONFIGURATION.getTrinketSortingByName(),
                            true);
                    }
                }
                else if (first.getKey().getStack().getItem() instanceof CharmItem &&
                    second.getKey().getStack().getItem() instanceof CharmItem)
                {
                    if (!VaultJewelSorting.CONFIGURATION.getCharmSortingByName().isEmpty())
                    {
                        return SortingHelper.compareCharms(first.getKey().getStack().getDisplayName().getString(),
                            AttributeGearData.read(first.getKey().getStack()),
                            first.getKey().getStack().getTag(),
                            second.getKey().getStack().getDisplayName().getString(),
                            AttributeGearData.read(second.getKey().getStack()),
                            second.getKey().getStack().getTag(),
                            VaultJewelSorting.CONFIGURATION.getCharmSortingByName(),
                            true);
                    }
                }
                else if (first.getKey().getStack().getItem() instanceof InfusedCatalystItem &&
                    second.getKey().getStack().getItem() instanceof InfusedCatalystItem)
                {
                    if (!VaultJewelSorting.CONFIGURATION.getCatalystSortingByName().isEmpty())
                    {
                        return SortingHelper.compareCatalysts(
                            first.getKey().getStack().getDisplayName().getString(),
                            first.getKey().getStack().getTag(),
                            second.getKey().getStack().getDisplayName().getString(),
                            second.getKey().getStack().getTag(),
                            VaultJewelSorting.CONFIGURATION.getCatalystSortingByName(),
                            true);
                    }
                }
                else if (first.getKey().getStack().getItem() instanceof VaultDollItem &&
                    second.getKey().getStack().getItem() instanceof VaultDollItem)
                {
                    if (!VaultJewelSorting.CONFIGURATION.getDollSortingByName().isEmpty())
                    {
                        return SortingHelper.compareVaultDolls(first.getKey().getStack().getDisplayName().getString(),
                            first.getKey().getStack().getTag(),
                            second.getKey().getStack().getDisplayName().getString(),
                            second.getKey().getStack().getTag(),
                            VaultJewelSorting.CONFIGURATION.getDollSortingByName(),
                            true);
                    }
                }
                else if (first.getKey().getStack().getItem() instanceof RelicFragmentItem &&
                    second.getKey().getStack().getItem() instanceof RelicFragmentItem)
                {
                    return SortingHelper.compareRelicFragments(
                        first.getKey().getStack().getTag(),
                        second.getKey().getStack().getTag(),
                        true);
                }
                else if (first.getKey().getStack().getItem() instanceof ItemRespecFlask &&
                    second.getKey().getStack().getItem() instanceof ItemRespecFlask)
                {
                    return SortingHelper.compareRespecFlasks(
                        first.getKey().getStack().getTag(),
                        second.getKey().getStack().getTag(),
                        true);
                }
                else if (first.getKey().getStack().getItem() == ModItems.FACETED_FOCUS &&
                    second.getKey().getStack().getItem() == ModItems.FACETED_FOCUS)
                {
                    return SortingHelper.compareFacedFocus(
                        first.getKey().getStack().getTag(),
                        second.getKey().getStack().getTag(),
                        true);
                }
                else if (first.getKey().getStack().getItem() == ModItems.AUGMENT &&
                    second.getKey().getStack().getItem() == ModItems.AUGMENT)
                {
                    return SortingHelper.compareAugments(
                        first.getKey().getStack().getTag(),
                        second.getKey().getStack().getTag(),
                        true);
                }

                return 0;
            });
        }
    }
}
