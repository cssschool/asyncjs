( function (service) {

    var idealTemperature = 21;
    var perGradCost = 20;

    var stdCities = [
        {id: '635499', city:"London"},
        {id: '1480982', city:"Warsaw"},
        {id: '99363', city:"Rome"},
        {id: '1636821', city:"Moscow"},
        {id: '607047', city:"Paris"},
        {id: '651950', city:"Athenes"}
    ];

    $(".results").html(tim("resultTable", {cities: []}));

    function onChangedInputs() {

        var tempInputSource = Rx.Observable.fromEvent($("#temperature"), 'input');
        tempInputSource.subscribe( event => {
            idealTemperature = $(event.target).val();
        } );

        var costInputSource = Rx.Observable.fromEvent( $("input[name=cost]"), 'change');
        costInputSource.subscribe( any => {
            gradCost = $("input[name=cost]:checked").val();
        });


        Rx.Observable.merge(tempInputSource, costInputSource)
            .subscribe( stdCitiesQuery);

    }


    function stdCitiesQuery() {
        service.setParams( {idealTemperature:idealTemperature, perGradCost:perGradCost});

        stdCities.forEach( city => forCity(city.id));
        drawCitiesTemplate(stdCities.map(c=>{c.satisfaction='?';return c;}));
    }

    function forCity(cityId) {
        var satisfactionObservable = Rx.Observable.defer(()=>{
                console.log("once again:" + cityId);
        return service.getSatisfaction(cityId);
        }).retry(30);
        satisfactionObservable.subscribe(function(data){
            var tdElem = $(".results [data-cityname='"+cityId+"'] td.satisfaction");
            tdElem.text(data);
            tdElem.removeClass("spinning");
        } , function(a) {
            console.log("on error?" + a);
        } );


    }

    function drawCitiesTemplate(fullData) {
        var result = tim("resultTable", {cities: fullData});
        $(".results").html(result);
    }


    function onInput() {

        var cityInput= $("#city");
        var inputSource = Rx.Observable.fromEvent(cityInput, 'input');

        var throttled = inputSource.debounce(() => Rx.Observable.interval(300));

        var cityNameSource = throttled.map( event => $(event.target).val());

        var validSearchSource  = cityNameSource.filter(  cityName=> cityName.length > 3);

        var citiesQuery = validSearchSource.flatMap ( cityName => {
                console.log("looking for : " + cityName);
        cityInput.attr('data-state','load');
        return service.searchCities(cityName);
    });

        var subscription = citiesQuery.subscribe(
            function (cities) {

                var fullData = cities.map( function(cityIn) {
                    var cityId = cityIn[0];
                    var cityData = cityIn[1];
                    drawCity(cityId);
                    cityData.satisfaction =  "?";
                    cityData.id =  cityId;
                    return cityData;
                });

                drawCitiesTemplate(fullData);

                cityInput.attr('data-state','normal');
            },
            function (err) {
                console.log('Error: %s', err);
            },
            function () {
                console.log('Completed');
            });

    }

    stdCitiesQuery();
    onInput();
    onChangedInputs();

}(weatherService));




