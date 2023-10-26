package lv.id.bonne.vaulthunters.jewelsorting.sophisticatedcore;

import lv.id.bonne.vaulthunters.jewelsorting.config.MixinConfigPlugin;
import net.minecraftforge.fml.loading.LoadingModList;

public class SophisticatedCoreIntegrationModConfiguration extends MixinConfigPlugin
{
    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        return LoadingModList.get().getModFileById("sophisticatedcore") != null;

    }
}
