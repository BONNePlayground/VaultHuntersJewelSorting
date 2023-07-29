//
// Created by BONNe
// Copyright - 2023
//

package lv.id.bonne.vaultjewelsorting.config;

import org.spongepowered.asm.mixin.Mixins;
import org.spongepowered.asm.mixin.connect.IMixinConnector;


/**
 * Connector to the mixin json files.
 */
public class VHJewelMixinConnector implements IMixinConnector
{
    /**
     * Connect to Mixin files
     */
    @Override
    public void connect() {
        Mixins.addConfiguration("mixins.vaulthunters_jewel_sorting.ae2.json");
        Mixins.addConfiguration("mixins.vaulthunters_jewel_sorting.rs.json");
        Mixins.addConfiguration("mixins.vaulthunters_jewel_sorting.quark.json");
    }
}