( function (service) {

    var result = tim("resultTable", {cities: []});
    $(".results").html(result);


    $("#city").on("input", function (event) {
        console.log("have question for city:" + this.value);
        if (this.value.length > 3) {
            var inputElement = $(this);
            inputElement.attr('data-state','load');
            service.searchCities(this.value)
                .then(function (cities) {
                    var fullData = cities.map( function(city) {
                        service.getSatisfaction(city.city).then( function(data){
                            var tdElem = $(".results [data-cityname='"+city.city+"'] td.satisfaction");
                            tdElem.text(data);
                            tdElem.removeClass("spinning");
                        });
                        city.satisfaction =  "?";
                        return city;
                    });
                    var result = tim("resultTable", {cities: fullData});

                    $(".results").html(result);
                    inputElement.attr('data-state','normal');
                });
        }
    });


}(weatherService));




