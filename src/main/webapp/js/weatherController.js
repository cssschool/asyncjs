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

    /** init */
    stdCities.forEach(c=>{
        c.satisfaction= '?';
        c.goClass = "nogo";
    });


    $(".results").html(tim("resultTable", {cities: []}));

    /**
     * This function is started on every change of "temp slider or chf coefficient"
     */
    function onChangedInputs() {
        var tempInputSource = Rx.Observable.fromEvent($("#temperature"), 'input');
        tempInputSource.subscribe( event => {
            idealTemperature = $(event.target).val();
            $("#temperature + output").text(idealTemperature);
        } );

        var costInputSource = Rx.Observable.fromEvent( $("input[name=cost]"), 'change');
        costInputSource.subscribe( any => {
            perGradCost = $("input[name=cost]:checked").val();
        });

        Rx.Observable.merge(tempInputSource, costInputSource)
            .subscribe( stdCitiesQuery);
    }

    /**
     *  This function asks for "satisfaction" and draws template
     */
    function stdCitiesQuery() {
        service.setParams( {idealTemperature:idealTemperature, perGradCost:perGradCost});
        stdCities.forEach(c=>{
                service.getSatisfaction(c.id).then(function(satisf) {
                c.satisfaction= satisf;
                c.goClass = c.satisfaction > 500 ? "nogo" : "go";
                drawCitiesTemplate(stdCities);
            });
        });
    }

    function drawCitiesTemplate(fullData) {
        var result = tim("resultTable", {cities: fullData});
        $(".results").html(result);
    }

    stdCitiesQuery();
    onChangedInputs();

}(weatherService));




