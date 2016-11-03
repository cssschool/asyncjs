var weatherService = (function () {

    function getTemperature(city) {
        var result = new Promise(
            function (resolve, reject) {
                $.get("/services/temperature/" + city).done(function (data) {
                    resolve(data);
                });
            }
        );
        return result;

    }

    function getTransport(city) {
        var result = new Promise(
            function (resolve, reject) {
                $.get("/services/transport/" + city).done(function (data) {
                    resolve(data);
                });
            }
        );
        return result;
    }

    function getSatisfaction(city) {
        return Promise
            .all( [getTemperature(city), getTransport(city)])
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