package lv.id.bonne.vaulthunters.jewelsorting.vaulthunters;

import lv.id.bonne.vaulthunters.jewelsorting.config.MixinConfigPlugin;
import net.minecraftforge.fml.loading.LoadingModList;


/**
 * This Mixin configuration checks if VaultHunters mod is loaded.
 */
public class VaultHuntersIntegrationModConfiguration extends MixinConfigPlugin
{
    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName)
    {
        return LoadingModList.get().getModFileById("the_vault") != null;
    }
}
