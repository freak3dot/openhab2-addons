/**
 * Copyright (c) 2010-2019 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.senseenergy.internal.json;

/**
 * The {@link SenseEnergyJsonResponse} is the Java class used to map the JSON
 * response to the Sense request.
 *
 * @author Ryan Johnston - Initial contribution
 */
public class SenseEnergyJsonResponse {

    private SenseEnergyJsonResult result;
    private String error;

    public SenseEnergyJsonResponse() {
    }

    public SenseEnergyJsonResult getResult() {
        return result;
    }

    public String getError() {
        return error;
    }

}
