package net.quedoom.giant_potato.zapi.jade;

import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaCommonRegistration;
import snownee.jade.api.IWailaPlugin;

public class GiantPotatoJadePlugin implements IWailaPlugin {

    //TODO Register data providers
    @Override
    public void register(IWailaCommonRegistration registration) {
        IWailaPlugin.super.register(registration);
    }

    //TODO Register component providers, icon providersm and config options
    @Override
    public void registerClient(IWailaClientRegistration registration) {
        IWailaPlugin.super.registerClient(registration);
    }
}
