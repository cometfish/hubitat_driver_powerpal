# Hubitat Driver for Powerpal energy monitor

This driver allows you to read data from the Powerpal cloud from Hubitat.

Note: It does not connect directly to the Powerpal device - therefore you still require a Bluetooth enabled device with the Powerpal app running to upload data to the Powerpal cloud

## Get your Device ID

Your device ID can be found in the Settings of the Powerpal app on your device.

## Get your Authorisation Key

You will need to set up `mitmproxy` to read the requests coming from your device to the Powerpal cloud.  
This is easiest with an iOS device: set up `mitmproxy` on a computer on the same network, set the iOS device to use the proxy server, trust the root certificate, and then open the Powerpal app.  
If you don't have an iOS device you could try this repo (I haven't tested it - please let me know if it works and I'll update this note accordingly): [https://github.com/WeekendWarrior1/powerpal_ble/tree/main/auth_extraction](https://github.com/WeekendWarrior1/powerpal_ble/tree/main/auth_extraction)

## References
Powerpal cloud API: [https://forfuncsake.github.io/post/2021/08/owning-my-own-powerpal-data/](https://forfuncsake.github.io/post/2021/08/owning-my-own-powerpal-data/)
Other API examples: [https://community.home-assistant.io/t/powerpal-smart-energy-monitor/263713/63](https://community.home-assistant.io/t/powerpal-smart-energy-monitor/263713/63)