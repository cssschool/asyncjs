package ch.css.workshop.asyncjs;

import ch.css.workshop.asyncjs.data.CitiesService;
import pl.setblack.badass.Politician;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.concurrent.*;

public class SlowWeatherService {
   private final ThreadPoolExecutor weatherExecutor = createExecutor();

   private final CitiesService citiesService;

   public SlowWeatherService(CitiesService citiesService) {
      this.citiesService = citiesService;
   }

   private ThreadPoolExecutor  createExecutor() {
      BlockingQueue<Runnable> queue = new ArrayBlockingQueue<Runnable>(
              10);
      ThreadPoolExecutor executorService = new ThreadPoolExecutor(2, 5, 30,
              TimeUnit.SECONDS, queue,
              new ThreadPoolExecutor.AbortPolicy());
      return executorService;
   }

   public CompletionStage<BigDecimal> getTemperature(String cityName, LocalDate date) {
      final CompletableFuture<BigDecimal> result = new CompletableFuture<>();
      weatherExecutor.execute( () -> {
         //Politician.beatAroundTheBush(()->Thread.sleep(2000));
         citiesService.getCity(cityName).onSuccess( (city)->{
               return city.map( c -> Math.abs(90 - c.latitude)/90f );
         });


         result.complete(new BigDecimal("25.5"));
         }
      );

      return  result;
   }

}
