//
// Created by BONNe
// Copyright - 2023
//


package lv.id.bonne.vaultjewelsorting.qio;


import lv.id.bonne.vaultjewelsorting.config.MixinConfigPlugin;
import net.minecraftforge.fml.loading.LoadingModList;


/**
 * This Mixin configuration checks if Mekanism mod is loaded.
 */
public class QIOIntegrationModConfiguration extends MixinConfigPlugin
{
    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName)
    {
        return LoadingModList.get().getModFileById("mekanism") != null;
    }
}