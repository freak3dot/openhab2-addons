/**
 * Copyright (c) 2010-2019 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.senseenergy.internal.handler;

import static org.openhab.binding.senseenergy.internal.SenseEnergyBindingConstants.BASE_URL;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.smarthome.config.core.Configuration;
import org.eclipse.smarthome.core.library.types.DecimalType;
import org.eclipse.smarthome.core.thing.Bridge;
import org.eclipse.smarthome.core.thing.ChannelUID;
import org.eclipse.smarthome.core.thing.ThingStatus;
import org.eclipse.smarthome.core.thing.ThingStatusDetail;
import org.eclipse.smarthome.core.thing.ThingUID;
import org.eclipse.smarthome.core.thing.binding.BaseBridgeHandler;
import org.eclipse.smarthome.core.types.Command;
import org.eclipse.smarthome.io.net.http.HttpUtil;
import org.openhab.binding.senseenergy.internal.SenseEnergyBindingConstants;
import org.openhab.binding.senseenergy.internal.json.SenseEnergyJsonResponse;
import org.osgi.framework.ServiceRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;

/**
 * {@link SenseEnergyBridgeHandler} is the handler for Sense API and connects it
 * to the webservice.
 *
 * @author Ryan Johnston - Initial contribution
 *
 */
@NonNullByDefault
public class SenseEnergyBridgeHandler extends BaseBridgeHandler {
    private final Logger logger = LoggerFactory.getLogger(SenseEnergyBridgeHandler.class);
    private static final int REQUEST_TIMEOUT = (int) TimeUnit.SECONDS.toMillis(30);

    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(DecimalType.class,
                    (JsonDeserializer<DecimalType>) (json, type, jsonDeserializationContext) -> DecimalType
                            .valueOf(json.getAsJsonPrimitive().getAsString()))
            .registerTypeAdapter(ZonedDateTime.class,
                    (JsonDeserializer<ZonedDateTime>) (json, type, jsonDeserializationContext) -> ZonedDateTime
                            .parse(json.getAsJsonPrimitive().getAsString()))
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();

    private Map<ThingUID, @Nullable ServiceRegistration<?>> discoveryServiceRegs = new HashMap<>();
    private final Properties header = new Properties();

    public SenseEnergyBridgeHandler(Bridge bridge) {
        super(bridge);
    }

    @Override
    public void initialize() {
        String error = "";
        String errorDetail = null;

        logger.debug("Initializing Sense Energy API bridge handler.");
        Configuration config = getThing().getConfiguration();
        header.put("Sense-Client-Version", "1.3.7-9be446d");
        header.put("Host", "api.sense.com")
        header.put("User-Agent", "okhttp/3.4.1")
        // header.put("x-access-token", config.get(SenseEnergyBindingConstants.APIKEY));

        // Check if an api key has been provided during the bridge creation
        if (StringUtils.trimToNull((String) config.get(SenseEnergyBindingConstants.APIKEY)) == null) {
            error += " Parameter 'apikey' must be configured.";
        } else {
            // Check if the provided api key is valid for use with the Sense service
            try {
                // Run the HTTP request and get the JSON response
                SenseEnergyJsonResponse response = getData("0", "0", null);
                if ("Invalid account credentials".equalsIgnoreCase(response.getError())) {
                    error = "Invalid account credentials";
                    errorDetail = response.getError();
                } else {
                    updateStatus(ThingStatus.ONLINE);
                }
            } catch (IOException | IllegalArgumentException e) {
                error = "Error running Sense API request";
                errorDetail = e.getMessage();
            }
        }

        // Updates the thing status accordingly
        if (!"".equals(error)) {
            error = error.trim();
            logger.debug("Disabling thing '{}': Error '{}': {}", getThing().getUID(), error, errorDetail);
            updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.CONFIGURATION_ERROR, error);
        }
    }

    @Override
    public void handleCommand(ChannelUID channelUID, Command command) {
        // not needed
    }

    public Map<ThingUID, @Nullable ServiceRegistration<?>> getDiscoveryServiceRegs() {
        return discoveryServiceRegs;
    }

    public void setDiscoveryServiceRegs(Map<ThingUID, @Nullable ServiceRegistration<?>> discoveryServiceRegs) {
        this.discoveryServiceRegs = discoveryServiceRegs;
    }

    @Override
    public void handleRemoval() {
        // removes the old registration service associated to the bridge, if existing
        ServiceRegistration<?> dis = getDiscoveryServiceRegs().get(this.getThing().getUID());
        if (dis != null) {
            dis.unregister();
        }
        super.handleRemoval();
    }

    public SenseEnergyJsonResponse getData()
            throws IOException {
        StringBuilder urlBuilder = new StringBuilder(BASE_URL).append("&lng=");


        String jsonData = HttpUtil.executeUrl("GET", urlBuilder.toString(), header, null, null, REQUEST_TIMEOUT);
        logger.debug("URL = {}", jsonData);

        return gson.fromJson(jsonData, SenseEnergyJsonResponse.class);
    }

}
