/**
 * Copyright (c) 2010-2019 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.senseenergy.internal;

import static org.openhab.binding.senseenergy.internal.SenseEnergyBindingConstants.*;

import java.util.Hashtable;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.smarthome.config.discovery.DiscoveryService;
import org.eclipse.smarthome.core.i18n.LocationProvider;
import org.eclipse.smarthome.core.thing.Bridge;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.ThingTypeUID;
import org.eclipse.smarthome.core.thing.binding.BaseThingHandlerFactory;
import org.eclipse.smarthome.core.thing.binding.ThingHandler;
import org.eclipse.smarthome.core.thing.binding.ThingHandlerFactory;
import org.openhab.binding.senseenergy.internal.discovery.SenseEnergyDiscoveryService;
import org.openhab.binding.senseenergy.internal.handler.SenseEnergyBridgeHandler;
import org.openhab.binding.senseenergy.internal.handler.SenseEnergyReportHandler;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * The {@link SenseEnergyHandlerFactory} is responsible for creating things and thing
 * handlers.
 *
 * @author Ryan Johnston - Initial contribution
 */
@NonNullByDefault
@Component(service = ThingHandlerFactory.class, configurationPid = "binding.senseenergy")
public class SenseEnergyHandlerFactory extends BaseThingHandlerFactory {

    private @Nullable LocationProvider locationProvider;

    @Override
    public boolean supportsThingType(ThingTypeUID thingTypeUID) {
        return SUPPORTED_THING_TYPES_UIDS.contains(thingTypeUID) || BRIDGE_THING_TYPES_UIDS.contains(thingTypeUID);
    }

    @Override
    protected @Nullable ThingHandler createHandler(Thing thing) {
        ThingTypeUID thingTypeUID = thing.getThingTypeUID();

        if (APIBRIDGE_THING_TYPE.equals(thingTypeUID)) {
            SenseEnergyBridgeHandler handler = new SenseEnergyBridgeHandler((Bridge) thing);
            registerSenseEnergyDiscoveryService(handler);
            return handler;
        } else if (LOCATION_REPORT_THING_TYPE.equals(thingTypeUID)) {
            return new SenseEnergyReportHandler(thing);
        }

        return null;
    }

    private void registerSenseEnergyDiscoveryService(SenseEnergyBridgeHandler bridgeHandler) {
        if (locationProvider != null) {
            SenseEnergyDiscoveryService discoveryService = new SenseEnergyDiscoveryService(bridgeHandler, locationProvider);
            bridgeHandler.getDiscoveryServiceRegs().put(bridgeHandler.getThing().getUID(),
                    bundleContext.registerService(DiscoveryService.class.getName(), discoveryService,
                            new Hashtable<String, Object>()));
        }
    }

}
