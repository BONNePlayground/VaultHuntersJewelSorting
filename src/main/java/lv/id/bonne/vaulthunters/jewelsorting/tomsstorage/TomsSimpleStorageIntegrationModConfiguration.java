package lv.id.bonne.vaulthunters.jewelsorting.tomsstorage;

import lv.id.bonne.vaulthunters.jewelsorting.config.MixinConfigPlugin;
import net.minecraftforge.fml.loading.LoadingModList;


/**
 * This Mixin configuration checks if Tom's Simple Storage mod is loaded.
 */
public class TomsSimpleStorageIntegrationModConfiguration extends MixinConfigPlugin
{
    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName)
    {
        return LoadingModList.get().getModFileById("toms_storage") != null;
    }
}
