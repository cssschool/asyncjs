package ch.css.workshop.asyncjs;

import pl.setblack.badass.Politician;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class SlowWeatherService {

   private final Executor weatherExecutor = Executors.newFixedThreadPool(10);


   public CompletionStage<BigDecimal> getTemperature(String cityName, LocalDate date) {
      final CompletableFuture<BigDecimal> result = new CompletableFuture<>();

      weatherExecutor.execute( () -> {
         Politician.beatAroundTheBush(()->Thread.sleep(2000));
         result.complete(new BigDecimal("25.5"));
         }
      );

      return  result;
   }

}
