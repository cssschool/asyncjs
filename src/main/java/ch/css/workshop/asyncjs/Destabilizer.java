package ch.css.workshop.asyncjs;


import pl.setblack.badass.Politician;

import java.math.BigDecimal;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

public class Destabilizer {
   private final long standardDelay;

   private final  double failureProbability;

   private final  double stuckProbability;

   private final ThreadPoolExecutor myExecutor = createExecutor();

   //private final java.util.concurrent.ThreadLocalRandom r = new ThreadLocalRandom();

   public Destabilizer(long standardDelay, double failureProbability, double stuckProbability) {
      this.standardDelay = standardDelay;
      this.failureProbability = failureProbability;
      this.stuckProbability = stuckProbability;
   }

   public <INPUT, RESULT > Function<INPUT, CompletionStage<RESULT>> makeBlockingSlowAndUnstable(Function<INPUT,
      RESULT> func) {
      return makeAsyncSlowAndUnstable(( INPUT inp ) -> {
         final CompletableFuture<RESULT> result = new CompletableFuture<>();
         myExecutor.execute(() -> {
               result.complete(func.apply(inp));
            }
         );
         return result;
      });
   }

   public <INPUT, RESULT > Function<INPUT, CompletionStage<RESULT>> makeAsyncSlowAndUnstable(Function<INPUT,
      CompletionStage<RESULT>>
                                                                             func) {
      return (INPUT inp) -> func.apply(inp).thenApply( val -> {
         if ( standardDelay > 0) {
            Politician.beatAroundTheBush(() -> Thread.sleep(getPoissonRandom(standardDelay)));
         }
         if  (failureProbability > 0) {
            if ( ThreadLocalRandom.current().nextDouble() < failureProbability ) {
               throw new RuntimeException("just planned failure");
            }
         }
         return val;
      } );
   }



   private ThreadPoolExecutor createExecutor() {
      BlockingQueue<Runnable> queue = new ArrayBlockingQueue<Runnable>(
         10);
      ThreadPoolExecutor executorService = new ThreadPoolExecutor(2, 5, 30,
         TimeUnit.SECONDS, queue,
         new ThreadPoolExecutor.AbortPolicy());
      return executorService;
   }

   private static long getPoissonRandom(double mean) {
      final ThreadLocalRandom random = ThreadLocalRandom.current();
      double L = Math.exp(-mean);
      long k = 0;
      double p = 1.0;
      do {
         p = p * random.nextDouble();
         k++;
      } while (p > L);
      return k - 1;
   }

}
