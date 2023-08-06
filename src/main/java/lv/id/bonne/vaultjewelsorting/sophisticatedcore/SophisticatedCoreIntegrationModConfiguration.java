package lv.id.bonne.vaultjewelsorting.sophisticatedcore;

import lv.id.bonne.vaultjewelsorting.config.MixinConfigPlugin;
import net.minecraftforge.fml.loading.LoadingModList;

public class SophisticatedCoreIntegrationModConfiguration extends MixinConfigPlugin {
    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        return LoadingModList.get().getModFileById("sophisticatedcore") != null;

    }
}
