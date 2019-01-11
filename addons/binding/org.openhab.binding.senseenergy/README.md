# Sense Energy Binding

This binding is intended for use with the [Sense Home Energy Monitor](https://sense.com/). The binding uses the [Sense Home Energy Monitor API](https://community.sense.com/t/official-api/2848) for providing real time information about electricity use in your home. Although information has not been publically released about the Sense API, it has been reverse engineered to some extent. Given that, this binding may be affected by changes to the Sense API.

To use this binding:
1. Install your Sense Home Energy Monitor in you your electric panel. [How do I install Sense?](https://help.sense.com/hc/en-us/articles/360000513007-How-do-I-install-Sense-).
2. Be sure to register your sense online account. This is part of the install process.

## Binding Installation

This binding can be installed via the Add-ons section of the Paper UI. 

Go to Bindings and search for `Sense Energy`. Click on install. 

## Discovery



## Binding Configuration

The binding requires your sense email and password.

## Bridge Configuration

The bridge required configuration parameters to connect to your sense account :

| Parameter | Description                                                  |
|-----------|--------------------------------------------------------------|
| email     | Email address used to sign up for sense. Mandatory.          |
| password  | Password used with sense account. Mandatory.                 |

## Thing Configuration

The thing has a few configuration parameters :

| Parameter | Description                                                  |
|-----------|--------------------------------------------------------------|
| location  | Geo coordinates to be considered by the service.             |
| refresh   | Refresh interval in minutes. Optional.                       |

## Channels

The Sense Energy Report thing that is retrieved has these channels:

| Channel ID   | Item Type                | Description                                          |
|--------------|--------------------------|------------------------------------------------------|
| LegOneVolts  | Number:ElectricPotential | The voltage reading of the first Sense device lead.  |
| LegTwoVolts  | Number:ElectricPotential | The voltage reading of the second Sense device lead. |
| LegOneWatts  | Number:Energy            | Energy use reading of the first Sense device lead.   |
| LegTwoWatts  | Number:Energy            | Energy use reading of the first Sense device lead.   |
| Frequency    | Number:Frequency         | Electrical frequency detected by Sense.              |


## Examples

demo.things:

```xtend
Bridge openuv:openuvapi:local "OpenUV Api" [ apikey="xxxxYYYxxxx" ] {
    Thing uvreport city1 "UV In My City" [ location="52.5200066,13.4049540", refresh=10 ]
}

```

demo.items:

```xtend

```

