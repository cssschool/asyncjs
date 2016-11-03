var weatherService = (function(){

    function getTemperature(city) {
        $.get("/services/temperature/"+city).done(function(data) {
            console.log("got:" + data);
        });
        return 25.5;
    }

    function getTransport(city) {
        $.get("/services/transport/"+city).done(function(data) {
            console.log("got:" + data);
        });
        return 250;
    }

    function getSatisfaction (city) {
        return Math.abs(getTemperature(city)-25)*50 + getTransport(city);
    }

    function searchCities(search) {
        var result = new Promise(
            // The resolver function is called with the ability to resolve or
            // reject the promise
            function(resolve, reject) {
                $.get("/services/cities/"+search).done(function(data) {
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