//
// Created by BONNe
// Copyright - 2023
//


package lv.id.bonne.vaultjewelsorting.refinedstorage;


import lv.id.bonne.vaultjewelsorting.config.MixinConfigPlugin;
import net.minecraftforge.fml.loading.LoadingModList;


public class RefinedStorageIntegrationModConfiguration extends MixinConfigPlugin
{
    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName)
    {
        return LoadingModList.get().getModFileById("refinedstorage") != null;
    }
}
