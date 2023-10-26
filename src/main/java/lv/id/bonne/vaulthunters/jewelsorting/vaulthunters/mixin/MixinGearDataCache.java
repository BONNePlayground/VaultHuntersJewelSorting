//
// Created by BONNe
// Copyright - 2023
//


package lv.id.bonne.vaulthunters.jewelsorting.vaulthunters.mixin;


import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import iskallia.vault.gear.attribute.VaultGearAttribute;
import iskallia.vault.gear.attribute.VaultGearModifier;
import iskallia.vault.gear.data.GearDataCache;
import iskallia.vault.gear.data.VaultGearData;
import iskallia.vault.init.ModGearAttributes;
import iskallia.vault.item.tool.JewelItem;
import lv.id.bonne.vaulthunters.jewelsorting.utils.AttributeHelper;
import lv.id.bonne.vaulthunters.jewelsorting.utils.SortingHelper;
import lv.id.bonne.vaulthunters.jewelsorting.utils.IExtraGearDataCache;
import net.minecraft.nbt.DoubleTag;
import net.minecraft.world.item.ItemStack;


/**
 * This mixin allows to add extra data to GearDataCache.
 */
@Mixin(value = GearDataCache.class, remap = false)
public class MixinGearDataCache implements IExtraGearDataCache
{
    /**
     * This method injects code to createCache method.
     * @param stack The stack to create cache from.
     * @param ci Callback info.
     * @param cache The cache to populate.
     */
    @Inject(method = "createCache",
        at = @At(value = "INVOKE",
            target = "Liskallia/vault/gear/data/GearDataCache;getJewelColorComponents()Ljava/util/List;"),
        locals = LocalCapture.CAPTURE_FAILSOFT)
    private static void extraCreateCache(ItemStack stack, CallbackInfo ci, GearDataCache cache)
    {
        if (stack.getItem() instanceof JewelItem)
        {
            MixinGearDataCache.populateJewelCache(cache, stack);
        }
    }


    /**
     * This method populates jewel cache.
     * @param cache The cache to populate.
     * @param itemStack The item stack to populate cache from.
     */
    private static void populateJewelCache(GearDataCache cache, ItemStack itemStack)
    {
        VaultGearData data = VaultGearData.read(itemStack);

        List<VaultGearModifier<?>> affixes = new ArrayList<>();
        affixes.addAll(data.getModifiers(VaultGearModifier.AffixType.PREFIX));
        affixes.addAll(data.getModifiers(VaultGearModifier.AffixType.SUFFIX));

        ((InvokerGearDataCache) cache).callQueryIntCache(SortingHelper.EXTRA_ATTRIBUTE_INDEX,
            -1,
            (stack) ->
            {
                if (affixes.size() == 1)
                {
                    return AttributeHelper.getAttributeIndex(affixes.get(0).getAttribute());
                }
                else
                {
                    return -1;
                }
            });

        ((InvokerGearDataCache) cache).callQueryCache(SortingHelper.EXTRA_ATTRIBUTE_VALUE,
            tag -> ((DoubleTag) tag).getAsDouble(),
            DoubleTag::valueOf,
            null,
            Function.identity(),
            (stack) ->
            {
                if (affixes.size() == 1)
                {
                    VaultGearAttribute<?> attribute = affixes.get(0).getAttribute();

                    if (AttributeHelper.isDoubleAttribute(attribute))
                    {
                        Optional<Double> value = (Optional<Double>) data.getFirstValue(attribute);
                        return value.orElse(null);
                    }
                    else if (AttributeHelper.isFloatAttribute(attribute))
                    {
                        Optional<Float> value = (Optional<Float>) data.getFirstValue(attribute);
                        return value.map(Double::valueOf).orElse(null);
                    }
                    else if (AttributeHelper.isIntegerAttribute(attribute))
                    {
                        Optional<Integer> value = (Optional<Integer>) data.getFirstValue(attribute);
                        return value.map(Double::valueOf).orElse(null);
                    }
                    else
                    {
                        return null;
                    }
                }
                else
                {
                    return null;
                }
            });


        ((InvokerGearDataCache) cache).callQueryIntCache(SortingHelper.EXTRA_JEWEL_SIZE, 0, (stack) ->
        {
            if (stack.getItem() instanceof JewelItem)
            {
                return data.getFirstValue(ModGearAttributes.JEWEL_SIZE).orElse(null);
            }
            else
            {
                return null;
            }
        });

        ((InvokerGearDataCache) cache).callQueryIntCache(SortingHelper.EXTRA_GEAR_LEVEL, 0, (stack) ->
            data.getItemLevel());
    }


    /**
     * This method returns first attribute index.
     * @return first attribute index.
     */
    @Override
    @Unique
    public Integer getExtraFirstAttributeIndex()
    {
        return ((InvokerGearDataCache) this).callQueryIntCache(SortingHelper.EXTRA_ATTRIBUTE_INDEX,
            -1,
            (stack) ->
            {
                VaultGearData data = VaultGearData.read(stack);

                List<VaultGearModifier<?>> affixes = new ArrayList<>();
                affixes.addAll(data.getModifiers(VaultGearModifier.AffixType.PREFIX));
                affixes.addAll(data.getModifiers(VaultGearModifier.AffixType.SUFFIX));

                if (affixes.size() == 1)
                {
                    return AttributeHelper.getAttributeIndex(affixes.get(0).getAttribute());
                }
                else
                {
                    return -1;
                }
            });
    }


    /**
     * This method returns first attribute value.
     * @return first attribute value.
     */
    @Override
    @Unique
    public Double getExtraFirstAttributeValue()
    {
        return ((InvokerGearDataCache) this).callQueryCache(SortingHelper.EXTRA_ATTRIBUTE_VALUE,
            tag -> ((DoubleTag) tag).getAsDouble(),
            DoubleTag::valueOf,
            null,
            Function.identity(),
            (stack) ->
            {
                VaultGearData data = VaultGearData.read(stack);

                List<VaultGearModifier<?>> affixes = new ArrayList<>();
                affixes.addAll(data.getModifiers(VaultGearModifier.AffixType.PREFIX));
                affixes.addAll(data.getModifiers(VaultGearModifier.AffixType.SUFFIX));

                if (affixes.size() == 1)
                {
                    VaultGearAttribute<?> attribute = affixes.get(0).getAttribute();

                    if (AttributeHelper.isDoubleAttribute(attribute))
                    {
                        Optional<Double> value = (Optional<Double>) data.getFirstValue(attribute);
                        return value.orElse(null);
                    }
                    else if (AttributeHelper.isFloatAttribute(attribute))
                    {
                        Optional<Float> value = (Optional<Float>) data.getFirstValue(attribute);
                        return value.map(Double::valueOf).orElse(null);
                    }
                    else if (AttributeHelper.isIntegerAttribute(attribute))
                    {
                        Optional<Integer> value = (Optional<Integer>) data.getFirstValue(attribute);
                        return value.map(Double::valueOf).orElse(null);
                    }
                    else
                    {
                        return null;
                    }
                }
                else
                {
                    return null;
                }
            });
    }


    /**
     * This method returns Jewel Size value.
     * @return Jewel Size value.
     */
    @Override
    @Unique
    public Integer getExtraJewelSize()
    {
        return ((InvokerGearDataCache) this).callQueryIntCache(SortingHelper.EXTRA_JEWEL_SIZE, 0, (stack) ->
        {
            if (stack.getItem() instanceof JewelItem)
            {
                VaultGearData data = VaultGearData.read(stack);
                return data.getFirstValue(ModGearAttributes.JEWEL_SIZE).orElse(null);
            }
            else
            {
                return null;
            }
        });
    }


    /**
     * This method returns Gear Level value.
     * @return Gear Level value.
     */
    @Override
    @Unique
    public int getExtraGearLevel()
    {
        return ((InvokerGearDataCache) this).callQueryIntCache(SortingHelper.EXTRA_GEAR_LEVEL, 0, (stack) ->
        {
            VaultGearData data = VaultGearData.read(stack);
            return data.getItemLevel();
        });
    }
}
