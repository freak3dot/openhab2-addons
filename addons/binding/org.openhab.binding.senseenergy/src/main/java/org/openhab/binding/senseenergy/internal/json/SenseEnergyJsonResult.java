/**
 * Copyright (c) 2010-2019 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.senseenergy.internal.json;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.eclipse.smarthome.core.library.types.DateTimeType;
import org.eclipse.smarthome.core.library.types.DecimalType;

/**
 * The {@link SenseEnergyJsonResult} is responsible for storing
 * the "result" node from the Sense JSON response
 *
 * @author Ryan Johnston - Initial contribution
 */
public class SenseEnergyJsonResult {

    private DecimalType volts1;
    private DecimalType volts2;
    private DecimalType watts1;
    private DecimalType watts2;
    private DecimalType frequency;

    public SenseEnergyJsonResult() {
    }

    public DecimalType getLegOneVolts() {
        return volts1;
    }

    public DecimalType getLegTwoVolts() {
        return volts2;
    }

    public DecimalType getLegOneWatts() {
        return watts1;
    }

    public DecimalType getLegTwoWatts() {
        return watts2;
    }

    public DecimalType getFrequency() {
        return frequency;
    }
}
