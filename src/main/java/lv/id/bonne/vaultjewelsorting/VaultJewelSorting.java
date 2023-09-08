package lv.id.bonne.vaultjewelsorting;

import com.mojang.logging.LogUtils;

import iskallia.vault.gear.attribute.VaultGearAttribute;
import iskallia.vault.init.ModGearAttributes;
import lv.id.bonne.vaultjewelsorting.utils.AttributeHelper;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import org.slf4j.Logger;


// The value here should match an entry in the META-INF/mods.toml file
@Mod("vaulthunters_jewel_sorting")
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class VaultJewelSorting
{
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();


    public VaultJewelSorting()
    {
        MinecraftForge.EVENT_BUS.register(this);
    }


    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onGearAttributeRegistry(RegistryEvent.Register<VaultGearAttribute<?>> event)
    {
        AttributeHelper.registerAttributes();
        AttributeHelper.registerPollTypes();
//        AttributeHelper.registerAttributes(event);
    }
}
