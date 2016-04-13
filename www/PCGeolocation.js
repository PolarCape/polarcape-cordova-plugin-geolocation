var pcGeolocation = {
	getLocation: function(callback,args) {
    	cordova.exec(
            	function(data){callback(data);},
            	function(err){callback(err);},
            	"PCGeolocation",
            	'getLocation',
            	[args]
    	);
	}
};

module.exports = pcGeolocation;