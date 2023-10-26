//
// Created by BONNe
// Copyright - 2023
//


package lv.id.bonne.vaulthunters.jewelsorting.vaulthunters.mixin;


import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.function.Function;

import iskallia.vault.gear.data.GearDataCache;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;


/**
 * This mixin allows access to private methods of GearDataCache.
 */
@Mixin(GearDataCache.class)
public interface InvokerGearDataCache
{
    /**
     * This method allows to call private method queryIntCache.
     * @param key  The key of the cache.
     * @param defaultValue The default value of the cache.
     * @param cacheInit The cache init function.
     * @return The value of the cache.
     */
    @Invoker(value = "queryIntCache", remap = false)
    public Integer callQueryIntCache(String key, int defaultValue, Function<ItemStack, Integer> cacheInit);


    /**
     * This method allows to call private method queryCache.
     * @param <R> The type of the cache.
     * @param <T> The type of the cache.
     * @param key The key of the cache.
     * @param cacheRead The cache read function.
     * @param cacheWrite The cache write function.
     * @param cacheMissDefault The cache miss default value.
     * @param cacheHitTransform The cache hit transform function.
     * @param cacheInit The cache init function.
     * @return The value of the cache.
     */
    @Invoker(value = "queryCache", remap = false)
    public <R, T> T callQueryCache(String key, Function<Tag, R> cacheRead, Function<R, Tag> cacheWrite, T cacheMissDefault, Function<R, T> cacheHitTransform, Function<ItemStack, R> cacheInit);
}
