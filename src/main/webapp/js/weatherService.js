var weatherService = (function () {
    var params= {
        idealTemperature : 21,
        perGradCost : 20
    };

    $.ajaxSettings.timeout = 20000;

    function getTemperature(cityId) {
        $.get("/services/temperature/" + cityId).done(function (data) {
            console.log("temperatur is : " + data);
        });
        return 15.5;//TODO: remove hardcoded
    }

    function getTransport(cityId) {
        $.get("/services/transport/" + cityId).done(function (data) {
            console.log("transport is : " + data);
        });
        return 400;//TODO: remove hardcoded
    }

    function getSatisfaction(cityId, callback) {

        /**
         *  Write here calculation
         *  satifsaction = abs(temperatue-idealTemperature) * perGradCost + transport
         *
         */
        
        setTimeout( function() {
            var result =  420; //HARDCODED
            console.log("result=" + result);
            callback(result);
        },100);
        
    }

    function searchCities(search) {
        return $.get("/services/cities/" + search);
    }

    function setParams( p ) {
        params = p;
    }

    return {
        searchCities: searchCities,
        getTemperature: getTemperature,
        getTransport: getTransport,
        getSatisfaction: getSatisfaction,
        setParams : setParams
    };

}());
