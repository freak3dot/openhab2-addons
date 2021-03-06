/**
 * Copyright (c) 2010-2019 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.senseenergy.internal;

/**
 * The {@link SenseEnergyConfiguration} is the class used to match the
 * thing configuration.
 *
 * @author Ryan Johnston - Initial contribution
 */
public class SenseEnergyConfiguration {
    String[] elements = null;

    public String apikey;
    private String location;
    public Integer refresh;

    public String getLatitude() {
        return getElement(0);
    }

    public String getLongitude() {
        return getElement(1);
    }

    public String getAltitude() {
        return getElement(2);
    }

    private String getElement(int index) {
        if (elements == null) {
            elements = location.split(",");
        }
        if (index < elements.length) {
            return elements[index].trim();
        } else {
            return null;
        }
    }
}
