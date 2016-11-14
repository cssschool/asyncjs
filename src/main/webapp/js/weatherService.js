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

    function getSatisfaction(cityId) {
        var temp = getTemperature(cityId);
        var transp = getTransport(cityId);
        var tempDiff = Math.abs(params.idealTemperature - temp);

        var result =   (tempDiff*params.perGradCost + transp).toFixed(2);
        console.log("result=" + result);
        return result;
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
