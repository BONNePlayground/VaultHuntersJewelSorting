//
// Created by BONNe
// Copyright - 2023
//


package lv.id.bonne.vaultjewelsorting.ae2;


import lv.id.bonne.vaultjewelsorting.config.MixinConfigPlugin;
import net.minecraftforge.fml.loading.LoadingModList;


public class AE2IntegrationModConfiguration extends MixinConfigPlugin
{
    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName)
    {
        return LoadingModList.get().getModFileById("ae2") != null;
    }
}
