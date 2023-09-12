package lv.id.bonne.vaultjewelsorting;

import com.mojang.logging.LogUtils;

import iskallia.vault.gear.attribute.VaultGearAttribute;
import lv.id.bonne.vaultjewelsorting.config.Configuration;
import lv.id.bonne.vaultjewelsorting.utils.AttributeHelper;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;

import org.slf4j.Logger;


/**
 * The main class for Vault Jewels Sorting mod.
 */
@Mod("vaulthunters_jewel_sorting")
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

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Configuration.GENERAL_SPEC, "vaulthunters_jewel_sorting.toml");
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
}
