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
import iskallia.vault.init.ModItems;
import iskallia.vault.item.crystal.CrystalData;
import iskallia.vault.item.data.InscriptionData;
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

            int registryOrder = SortingHelper.compareRegistryNames(
                first.getItem().getRegistryName(),
                second.getItem().getRegistryName(),
                this.gui.getDownwards());

            if (registryOrder != 0 || !SortingHelper.isSortable(first.getItem().getRegistryName()))
            {
                // Use default string comparing
                return registryOrder;
            }
            else if (first.getItem() == ModItems.JEWEL)
            {
                String leftName = first.getDisplayName().getString();
                String rightName = second.getDisplayName().getString();
                VaultGearData leftData = VaultGearData.read(first);
                VaultGearData rightData = VaultGearData.read(second);

                return switch (this.gui.getSort()) {
                    case NAME -> SortingHelper.compareJewels(leftName,
                        leftData,
                        first.getOrCreateTag().getInt("freeCuts"),
                        rightName,
                        rightData,
                        second.getOrCreateTag().getInt("freeCuts"),
                        VaultJewelSorting.CONFIGURATION.getJewelSortingByName(),
                        this.gui.getDownwards());
                    case AMOUNT -> SortingHelper.compareJewels(leftName,
                        leftData,
                        first.getOrCreateTag().getInt("freeCuts"),
                        rightName,
                        rightData,
                        second.getOrCreateTag().getInt("freeCuts"),
                        VaultJewelSorting.CONFIGURATION.getJewelSortingByAmount(),
                        this.gui.getDownwards());
                    case MOD -> SortingHelper.compareJewels(leftName,
                        leftData,
                        first.getOrCreateTag().getInt("freeCuts"),
                        rightName,
                        rightData,
                        second.getOrCreateTag().getInt("freeCuts"),
                        VaultJewelSorting.CONFIGURATION.getJewelSortingByMod(),
                        this.gui.getDownwards());
                };
            }
            else if (first.getItem() == ModItems.TOOL)
            {
// TODO: Compare vault tools by their type? Currently is left just to filter out from VaultGearItem
//                callbackInfoReturnable.setReturnValue(SortingHelper.compareTools(
//                    VaultGearData.read(leftStack),
//                    VaultGearData.read(rightStack),
//                    sortingDirection == SortingDirection.ASCENDING));
//                callbackInfoReturnable.cancel();
            }
            else if (SortingHelper.VAULT_GEAR_SET.contains(first.getItem().getRegistryName()))
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
            else if (first.getItem() == ModItems.INSCRIPTION)
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
            else if (first.getItem() == ModItems.VAULT_CRYSTAL)
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
            else if (first.getItem() == ModItems.TRINKET)
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
            else if (SortingHelper.VAULT_CHARMS.contains(first.getItem().getRegistryName()))
            {
                return switch (this.gui.getSort()) {
                    case NAME -> SortingHelper.compareCharms(first.getDisplayName().getString(),
                        AttributeGearData.read(first),
                        first.getTag(),
                        second.getDisplayName().getString(),
                        AttributeGearData.read(second),
                        second.getTag(),
                        VaultJewelSorting.CONFIGURATION.getCharmSortingByName(),
                        true);
                    case AMOUNT -> SortingHelper.compareCharms(first.getDisplayName().getString(),
                        AttributeGearData.read(first),
                        first.getTag(),
                        second.getDisplayName().getString(),
                        AttributeGearData.read(second),
                        second.getTag(),
                        VaultJewelSorting.CONFIGURATION.getCharmSortingByAmount(),
                        true);
                    case MOD -> SortingHelper.compareCharms(first.getDisplayName().getString(),
                        AttributeGearData.read(first),
                        first.getTag(),
                        second.getDisplayName().getString(),
                        AttributeGearData.read(second),
                        second.getTag(),
                        VaultJewelSorting.CONFIGURATION.getCharmSortingByMod(),
                        true);
                };
            }
            else if (first.getItem() == ModItems.VAULT_CATALYST_INFUSED)
            {
                return switch (this.gui.getSort()) {
                    case NAME -> SortingHelper.compareCatalysts(first.getDisplayName().getString(),
                        first.getTag(),
                        second.getDisplayName().getString(),
                        second.getTag(),
                        VaultJewelSorting.CONFIGURATION.getCatalystSortingByName(),
                        true);
                    case AMOUNT -> SortingHelper.compareCatalysts(first.getDisplayName().getString(),
                        first.getTag(),
                        second.getDisplayName().getString(),
                        second.getTag(),
                        VaultJewelSorting.CONFIGURATION.getCatalystSortingByAmount(),
                        true);
                    case MOD -> SortingHelper.compareCatalysts(first.getDisplayName().getString(),
                        first.getTag(),
                        second.getDisplayName().getString(),
                        second.getTag(),
                        VaultJewelSorting.CONFIGURATION.getCatalystSortingByMod(),
                        true);
                };
            }
            else if (first.getItem() == ModItems.VAULT_DOLL)
            {
                return switch (this.gui.getSort()) {
                    case NAME -> SortingHelper.compareVaultDolls(first.getDisplayName().getString(),
                        first.getTag(),
                        second.getDisplayName().getString(),
                        second.getTag(),
                        VaultJewelSorting.CONFIGURATION.getDollSortingByName(),
                        true);
                    case AMOUNT -> SortingHelper.compareVaultDolls(first.getDisplayName().getString(),
                        first.getTag(),
                        second.getDisplayName().getString(),
                        second.getTag(),
                        VaultJewelSorting.CONFIGURATION.getDollSortingByAmount(),
                        true);
                    case MOD -> SortingHelper.compareVaultDolls(first.getDisplayName().getString(),
                        first.getTag(),
                        second.getDisplayName().getString(),
                        second.getTag(),
                        VaultJewelSorting.CONFIGURATION.getDollSortingByMod(),
                        true);
                };
            }
            else if (first.getItem() == ModItems.RELIC_FRAGMENT)
            {
                return SortingHelper.compareRelicFragments(first.getTag(), second.getTag(), true);
            }
            else if (first.getItem() == ModItems.RESPEC_FLASK)
            {
                return SortingHelper.compareRespecFlasks(first.getTag(), second.getTag(), true);
            }
            else if (first.getItem() == ModItems.FACETED_FOCUS)
            {
                return SortingHelper.compareFacedFocus(first.getTag(), second.getTag(), true);
            }
            else if (first.getItem() == ModItems.AUGMENT)
            {
                return SortingHelper.compareAugments(first.getTag(), second.getTag(), true);
            }
            else if (first.getItem() == ModItems.CARD)
            {
                return switch (this.gui.getSort()) {
                    case NAME -> SortingHelper.compareCards(first.getDisplayName().getString(),
                        first.getTag(),
                        second.getDisplayName().getString(),
                        second.getTag(),
                        VaultJewelSorting.CONFIGURATION.getCardSortingByName(),
                        true);
                    case AMOUNT -> SortingHelper.compareCards(first.getDisplayName().getString(),
                        first.getTag(),
                        second.getDisplayName().getString(),
                        second.getTag(),
                        VaultJewelSorting.CONFIGURATION.getCardSortingByAmount(),
                        true);
                    case MOD -> SortingHelper.compareCards(first.getDisplayName().getString(),
                        first.getTag(),
                        second.getDisplayName().getString(),
                        second.getTag(),
                        VaultJewelSorting.CONFIGURATION.getCardSortingByMod(),
                        true);
                };
            }
            else if (first.getItem() == ModItems.CARD_DECK)
            {
                return SortingHelper.compareDecks(first.getTag(), second.getTag(), true);
            }
            else if (first.getItem() == ModItems.BOOSTER_PACK)
            {
                return SortingHelper.compareBoosterPacks(first.getTag(), second.getTag(), true);
            }
            else if (first.getItem() == ModItems.ANTIQUE)
            {
                return SortingHelper.compareAntique(first.getTag(), second.getTag(), true);
            }
            else if (first.getItem() == ModItems.JEWEL_POUCH)
            {
                return SortingHelper.comparePouches(first.getTag(), second.getTag(), true);
            }

            return 0;
        });
    }
}
