metadata {
    definition(name: "Powerpal", namespace: "community", author: "cometfish", importUrl: "https://raw.githubusercontent.com/cometfish/hubitat_driver_powerpal/main/powerpal.groovy") {
        capability "EnergyMeter"
        capability "Polling"
        capability "Refresh"
        
        attribute "energy", "number"
        attribute "cost", "number"
        attribute "energy_total", "number"
        attribute "cost_total", "number"
        attribute "lastupdate", "date"
        attribute "lastupdate_timestamp", "number"
        
        command "poll"
        command "refresh"
    }
}

preferences {
    section("URIs") {
        input "DeviceID", "text", title: "Device ID", required: true
        input "AuthKey", "text", title: "Authorisation Key", required: true
		
        input name: "logEnable", type: "bool", title: "Enable debug logging", defaultValue: true
    }
}

def updated() {
    log.info "updated. debug logging is: ${logEnable == true}"
    
	unschedule()
    
    if (settings.DeviceID!='' && settings.AuthKey!='') {
        Random rand = new Random(now())
        def randomSeconds = rand.nextInt(60)
        def sched = "${randomSeconds} * * * * ?"
        schedule("${sched}", "poll")
    }
}

def refresh() {
    poll()
}

def poll() {
    if (logEnable) log.debug "Polling"

    def headers = [:]
	headers.put("Accept", "application/json")
	headers.put("Accept-Encoding", "gzip, deflate, br")
	headers.put("Accept-Language", "en-AU,en;q=0.9")
	headers.put("User-Agent", "Powerpal/2252 CFNetwork/1333.0.4 Darwin/21.5.0")
	headers.put("Authorization", settings.AuthKey)

    httpGet(["uri":"https://readings.powerpal.net/api/v1/device/"+settings.DeviceID, "headers": headers]) { resp ->
        if (logEnable) log.debug resp
        if (resp.success) {
            
                sendEvent(name: "lastupdate", value: new Date(), isStateChange: true)

                sendEvent(name: "energy", value: resp.data.last_reading_watt_hours *60, unit: "kWh", isStateChange: true)
                sendEvent(name: "cost", value: (resp.data.last_reading_cost *60d).round(2), unit: "\$", isStateChange: true)

                sendEvent(name: "energy_total", value: resp.data.total_watt_hours/1000, unit: "kWh", isStateChange: true)
                sendEvent(name: "cost_total", value: ((double)resp.data.total_cost).round(2), unit: "\$", isStateChange: true)
                
                sendEvent(name: "lastupdate", value: new Date((long)resp.data.last_reading_timestamp*1000L), isStateChange: true)
                sendEvent(name: "lastupdate_timestamp", value: resp.data.last_reading_timestamp, isStateChange:true)
        } else {
            log.error "Error: ${resp}"
        }
    }			
}
