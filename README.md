#polarcape-cordova-plugin-geolocation

Cordova geolocation plugin

#Install

#Usage

	pcGeolocation.getLocation(callback,args);

#Example
```javascript	
	                          var nativePopUpMessages = {
                                'message': translations['Plugins.geolocation.title'],
                                'title': translations['Plugins.geolocation.message'],
                                'ok': translations['Plugins.geolocation.ok'],
                                'cancel': translations['Plugins.geolocation.cancel']
                            };
                            pcGeolocation.getLocation(
                                    function (data) {
                                        logDebug('GPS results: ' + JSON.stringify(data));
                                        if (data.error === false) {
                                            successfullyLoadedLocation(data);
                                        } else if (data.error === true) {
                                            errorLoadingLocation();
                                        }
                                    }, nativePopUpMessages);

