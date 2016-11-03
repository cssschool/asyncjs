package ch.css.workshop.asyncjs;

import pl.setblack.badass.Politician;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.concurrent.*;

public class SlowWeatherService {



   //private final Executor weatherExecutor = Executors.newFixedThreadPool(10);
   private final ThreadPoolExecutor weatherExecutor = createExecutor();

   private  BlockingQueue<Runnable> leQueue;

   private ThreadPoolExecutor  createExecutor() {
      BlockingQueue<Runnable> queue = new ArrayBlockingQueue<Runnable>(
              10);
      ThreadPoolExecutor executorService = new ThreadPoolExecutor(2, 5, 30,
              TimeUnit.SECONDS, queue,
              new ThreadPoolExecutor.AbortPolicy());
      leQueue = queue;
      return executorService;
   }

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
