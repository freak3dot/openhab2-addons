/**
 * Copyright (c) 2010-2019 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.senseenergy.internal.handler;

import static org.openhab.binding.senseenergy.internal.SenseEnergyBindingConstants.*;

import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.smarthome.config.core.Configuration;
import org.eclipse.smarthome.core.library.types.HSBType;
import org.eclipse.smarthome.core.library.types.QuantityType;
import org.eclipse.smarthome.core.library.unit.SmartHomeUnits;
import org.eclipse.smarthome.core.thing.Bridge;
import org.eclipse.smarthome.core.thing.Channel;
import org.eclipse.smarthome.core.thing.ChannelUID;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.ThingStatus;
import org.eclipse.smarthome.core.thing.ThingStatusDetail;
import org.eclipse.smarthome.core.thing.binding.BaseThingHandler;
import org.eclipse.smarthome.core.types.Command;
import org.eclipse.smarthome.core.types.RefreshType;
import org.eclipse.smarthome.core.types.State;
import org.openhab.binding.senseenergy.internal.SenseEnergyConfiguration;
import org.openhab.binding.senseenergy.internal.json.SenseEnergyJsonResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link SenseEnergyReportHandler} is responsible for handling commands, which are
 * sent to one of the channels.
 *
 * @author Ryan Johnston - Initial contribution
 */
@NonNullByDefault
public class SenseEnergyReportHandler extends BaseThingHandler {
    private static final int DEFAULT_REFRESH_PERIOD = 30;
    private final Logger logger = LoggerFactory.getLogger(SenseEnergyReportHandler.class);

    @NonNullByDefault({})
    private SenseEnergyBridgeHandler bridgeHandler;

    @NonNullByDefault({})
    private ScheduledFuture<?> refreshJob;
    @NonNullByDefault({})

    public SenseEnergyReportHandler(Thing thing) {
        super(thing);
    }

    @Override
    public void initialize() {
        logger.debug("Initializing Sense Energy handler.");

        SenseEnergyConfiguration config = getConfigAs(SenseEnergyConfiguration.class);

        String errorMsg = null;

        Bridge bridge = getBridge();
        if (bridge != null) {
            bridgeHandler = (SenseEnergyBridgeHandler) bridge.getHandler();
        } else {
            errorMsg = "Invalid bridge";
        }

        if (errorMsg == null) {
            updateStatus(ThingStatus.UNKNOWN);
            startAutomaticRefresh();
        } else {
            updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.CONFIGURATION_ERROR, errorMsg);
        }
    }


    /**
     * Start the job refreshing the data
     */
    private void startAutomaticRefresh() {
        if (refreshJob == null || refreshJob.isCancelled()) {
            SenseEnergyConfiguration config = getConfigAs(SenseEnergyConfiguration.class);
            int delay = (config.refresh != null) ? config.refresh.intValue() : DEFAULT_REFRESH_PERIOD;
            refreshJob = scheduler.scheduleWithFixedDelay(() -> {
                updateChannels();
            }, 0, delay, TimeUnit.MINUTES);
        }
    }

    private void updateChannels() {
        try {
            SenseEnergyJsonResult senseEnergyData = getData();
            for (Channel channel : getThing().getChannels()) {
                updateChannel(channel.getUID(), Data);
            }
            updateStatus(ThingStatus.ONLINE);
        } catch (IOException e) {
            logger.error("Exception occurred during execution: {}", e.getMessage(), e);
            updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.COMMUNICATION_ERROR, e.getMessage());
        }
    }

    @Override
    public void dispose() {
        logger.debug("Disposing the Sense Energy handler.");

        if (refreshJob != null && !refreshJob.isCancelled()) {
            refreshJob.cancel(true);
            refreshJob = null;
        }
    }

    @Override
    public void handleCommand(ChannelUID channelUID, Command command) {
        if (command instanceof RefreshType) {
            scheduler.execute(() -> {
                updateChannels();
            });
        } else {
            logger.debug("The Sense Energy binding is read-only and can not handle command {}", command);
        }
    }

    /**
     * Update the channel from the last Sense Energy data retrieved
     *
     * @param channelUID the id identifying the channel to be updated
     * @param senseEnergyData
     *
     */
    private void updateChannel(ChannelUID channelUID, SenseEnergyJsonResult senseEnergyData) {
        switch (channelUID.getId()) {
            case FREQUENCY:
                updateState(channelUID, senseEnergyData.getFrequency());
                break;
        }
    }

    /**
     * Request new Sense Energy data from the service
     *
     * @throws IOException
     */
    private SenseEnergyJsonResult getSenseEnergyData() throws IOException {
        SenseEnergyConfiguration config = getConfigAs(SenseEnergyConfiguration.class);
        return bridgeHandler.getData(config.getLatitude(), config.getLongitude(), config.getAltitude()).getResult();
    }

}
