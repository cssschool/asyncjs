( function (service) {

    var result = tim("resultTable", {cities: []});
    $(".results").html(result);

    var cityInput= $("#city");
    var inputSource = Rx.Observable.fromEvent(cityInput, 'input');

    var throttled = inputSource.debounce(() => Rx.Observable.interval(300));

    var cityNameSource = throttled.map( event => $(event.target).val());

    var validSearchSource  = cityNameSource.filter(  cityName=> cityName.length > 3);

    var subscription = validSearchSource.subscribe(
        function (cityName) {
            console.log("looking for : " + cityName);
            cityInput.attr('data-state','load');
            service.searchCities(cityName)
                .then(function (cities) {
                    var fullData = cities.map( function(cityIn) {
                        var cityId = cityIn[0];
                        var cityData = cityIn[1];
                        service.getSatisfaction(cityId).then( function(data){
                            var tdElem = $(".results [data-cityname='"+cityId+"'] td.satisfaction");
                            tdElem.text(data);
                            tdElem.removeClass("spinning");
                        });
                        cityData.satisfaction =  "?";
                        cityData.id =  cityId;
                        return cityData;
                    });
                    var result = tim("resultTable", {cities: fullData});

                    $(".results").html(result);
                    cityInput.attr('data-state','normal');
                });
        },
        function (err) {
            console.log('Error: %s', err);
        },
        function () {
            console.log('Completed');
        });


}(weatherService));




