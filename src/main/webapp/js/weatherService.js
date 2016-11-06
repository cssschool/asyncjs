var weatherService = (function () {

    function getTemperature(cityId) {
        var result = new Promise(
            function (resolve, reject) {
                $.get("/services/temperature/" + cityId).done(function (data) {
                    resolve(data);
                });
            }
        );
        return result;

    }

    function getTransport(cityId) {
        var result = new Promise(
            function (resolve, reject) {
                $.get("/services/transport/" + cityId).done(function (data) {
                    resolve(data);
                });
            }
        );
        return result;
    }

    function getSatisfaction(cityId) {
        return Promise
            .all( [getTemperature(cityId), getTransport(cityId)])
            .then( function(values) {
                var temp = parseFloat(values[0]);
                var transp = parseInt(values[1],10);

                var tempDiff = Math.abs(25.5 - temp);
                return  tempDiff*50 + transp;
        });

    }

    function searchCities(search) {
        var result = new Promise(
            function (resolve, reject) {
                $.get("/services/cities/" + search).done(function (data) {
                    console.log("got:" + data);
                    resolve(JSON.parse(data));
                });
            }
        );
        return result;
    }

    return {
        searchCities: searchCities,
        getTemperature: getTemperature,
        getTransport: getTransport,
        getSatisfaction: getSatisfaction
    };

}());