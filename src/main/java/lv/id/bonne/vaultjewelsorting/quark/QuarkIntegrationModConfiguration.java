//
// Created by BONNe
// Copyright - 2023
//


package lv.id.bonne.vaultjewelsorting.quark;


import lv.id.bonne.vaultjewelsorting.config.MixinConfigPlugin;
import net.minecraftforge.fml.loading.LoadingModList;


public class QuarkIntegrationModConfiguration extends MixinConfigPlugin
{
    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName)
    {
        return LoadingModList.get().getModFileById("quark") != null;
    }
}
