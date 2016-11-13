var weatherService = (function () {
    var params= {
        idealTemperature : 21,
        perGradCost : 20
    };



    function getTemperature(cityId) {
        var result = new Promise(
            function (resolve, reject) {
                $.get("/services/temperature/" + cityId).done(function (data) {
                    resolve(data);
                }).fail( function() {
                    reject("oops");
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
                }).fail( function() {
                    reject("oops");
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

                var tempDiff = Math.abs(params.idealTemperature - temp);
                return  tempDiff*params.perGradCost + transp;
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
