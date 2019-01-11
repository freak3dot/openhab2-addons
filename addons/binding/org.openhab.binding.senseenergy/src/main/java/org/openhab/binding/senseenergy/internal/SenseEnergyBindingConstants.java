/**
 * Copyright (c) 2010-2019 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.senseenergy.internal;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.smarthome.core.thing.ThingTypeUID;

/**
 * The {@link SenseEnergyBindingConstants} class defines common constants, which are
 * used across the whole binding.
 *
 * @author Ryan Johnston - Initial contribution
 */
@NonNullByDefault
public class SenseEnergyBindingConstants {
    public static final String BASE_URL = "https://api.sense.com/apiservice/api/v1/";
    public static final String BINDING_ID = "senseenergy";
    public static final String LOCAL = "local";

    public static final String LOCATION = "location";
    public static final String APIKEY = "apikey";

    // List of Bridge Type UIDs
    public static final ThingTypeUID APIBRIDGE_THING_TYPE = new ThingTypeUID(BINDING_ID, "senseenergyapi");

    // List of Things Type UIDs
    public static final ThingTypeUID LOCATION_REPORT_THING_TYPE = new ThingTypeUID(BINDING_ID, "sensereport");

    // List of all Channel id's
    public static final String LEGONEVOLTS = "LegOneVolts";
    public static final String LEGTWOVOLTS = "LegTwoVolts";
    public static final String LEGONEWATTS = "LegOneWatts";
    public static final String LEGTWOWATTS = "LegTwoWatts";
    public static final String FREQUENCY = "Frequency";

    public static final String PROPERTY_INDEX = "Index";

    public static final Set<ThingTypeUID> BRIDGE_THING_TYPES_UIDS = Collections.singleton(APIBRIDGE_THING_TYPE);
    public static final Set<ThingTypeUID> SUPPORTED_THING_TYPES_UIDS = new HashSet<>(
            Arrays.asList(LOCATION_REPORT_THING_TYPE));
}
