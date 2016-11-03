( function (service) {

    var result = tim("resultTable", {cities: []});
    $(".results").html(result);


    $("#city").on("input", function (event) {
        console.log("have question for city:" + this.value);
        if (this.value.length > 3) {
            service.searchCities(this.value)
                .then(function (cities) {

                    var fullData = cities.map( function(city) {
                        city.temperature = service.getTemperature(city);
                        city.transport = service.getTransport(city);
                        city.satisfaction = service.getSatisfaction(city);
                        return city;
                    });

                    var result = tim("resultTable", {cities: fullData});
                    $(".results").html(result);

                });
        }
    });


}(weatherService));




