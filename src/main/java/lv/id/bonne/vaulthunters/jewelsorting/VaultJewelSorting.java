package lv.id.bonne.vaulthunters.jewelsorting;


import com.mojang.logging.LogUtils;
import org.slf4j.Logger;

import iskallia.vault.gear.attribute.VaultGearAttribute;
import lv.id.bonne.vaulthunters.jewelsorting.utils.AttributeHelper;
import lv.id.bonne.vaulthunters.jewelsorting.config.Configuration;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;


/**
 * The main class for Vault Jewels Sorting mod.
 */
@Mod("vault_hunters_jewel_sorting")
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class VaultJewelSorting
{
    /**
     * The main class initialization.
     */
    public VaultJewelSorting()
    {
        MinecraftForge.EVENT_BUS.register(this);
        VaultJewelSorting.CONFIGURATION = new Configuration();

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Configuration.GENERAL_SPEC, "vault_hunters_jewel_sorting.toml");

        ModList.get().getModContainerById("the_vault").ifPresent(modContainer ->
            VAULT_MOD_VERSION = modContainer.getModInfo().getVersion().toString());
    }


    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onGearAttributeRegistry(RegistryEvent.Register<VaultGearAttribute<?>> event)
    {
        AttributeHelper.registerAttributes();
//        AttributeHelper.registerAttributes(event);
    }


    /**
     * The main configuration file.
     */
    public static Configuration CONFIGURATION;

    /**
     * The logger for this mod.
     */
    public static final Logger LOGGER = LogUtils.getLogger();

    /**
     * The Vault Mod version for caching.
     */
    public static String VAULT_MOD_VERSION = null;
}
