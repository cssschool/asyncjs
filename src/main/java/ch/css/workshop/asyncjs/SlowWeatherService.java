package ch.css.workshop.asyncjs;

import ch.css.workshop.asyncjs.data.CitiesService;
import ch.css.workshop.asyncjs.data.CityData;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.concurrent.*;

public class SlowWeatherService {
    private final ThreadPoolExecutor weatherExecutor = createExecutor();

    private final CitiesService citiesService;

    public SlowWeatherService(CitiesService citiesService) {
        this.citiesService = citiesService;
    }

    private ThreadPoolExecutor createExecutor() {
        BlockingQueue<Runnable> queue = new ArrayBlockingQueue<Runnable>(
                10);
        ThreadPoolExecutor executorService = new ThreadPoolExecutor(2, 5, 30,
                TimeUnit.SECONDS, queue,
                new ThreadPoolExecutor.AbortPolicy());
        return executorService;
    }

    private BigDecimal getTemperatureForCity(final CityData city) {
        System.out.println( "Temp for  City:"+ city.city+" lat:" + city.latitude);
        final double distToEq = Math.abs(90 - city.latitude) / 90.0;
        final double distSQ = Math.pow(distToEq, 0.3);
        final double temp = distSQ*60.0-30.0;

        return new BigDecimal(temp).setScale(2, RoundingMode.HALF_UP);
    }

    public CompletionStage<BigDecimal> getTemperature(Long cityId, LocalDate date) {
        final CompletableFuture<BigDecimal> result = new CompletableFuture<>();
        weatherExecutor.execute(() -> {
                    citiesService.getCity(cityId).onSuccess((city) -> {
                        city
                                .map(this::getTemperatureForCity)
                                .forEach(temp -> result.complete(temp));
                    });
                }
        );
        return result;
    }

}
