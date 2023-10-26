//
// Created by BONNe
// Copyright - 2023
//


package lv.id.bonne.vaulthunters.jewelsorting.utils;


import java.util.function.Function;
import java.util.function.Supplier;

import iskallia.vault.core.net.ArrayBitBuffer;
import iskallia.vault.core.net.BitBuffer;
import iskallia.vault.gear.data.AttributeGearData;
import iskallia.vault.gear.data.VaultGearData;
import net.minecraft.nbt.CompoundTag;


/**
 * The type Custom vault gear data.
 */
public class CustomVaultGearData extends VaultGearData
{
    /**
     * Instantiates a new Custom vault gear data.
     */
    protected CustomVaultGearData()
    {
        super();
    }


    /**
     * Instantiates a new Custom vault gear data.
     *
     * @param bitBuffer the bit buffer
     */
    protected CustomVaultGearData(BitBuffer bitBuffer)
    {
        super(bitBuffer);
    }


    /**
     * Read vault gear data from NBT tag
     *
     * @param tag the tag
     * @return the vault gear data
     */
    public static VaultGearData read(CompoundTag tag)
    {
        return read(tag, CustomVaultGearData::new, CustomVaultGearData::new);
    }


    /**
     * This method reads VaultGearData from tag.
     * @param tag The CompoundTag that contains VaultGearData.
     * @param bufferCtor Buffer Collector.
     * @param ctor Supplier for VaultGearData.
     * @return New VaultGearData.
     * @param <T> Type of VaultGearData.
     */
    private static <T extends AttributeGearData> T read(CompoundTag tag,
        Function<BitBuffer, T> bufferCtor,
        Supplier<T> ctor)
    {
        return tag != null && tag.contains("vaultGearData", 12) ?
            bufferCtor.apply(ArrayBitBuffer.backing(tag.getLongArray("vaultGearData"), 0)) :
            ctor.get();
    }
}
