package ch.css.workshop.asyncjs;


import com.sun.corba.se.spi.orbutil.threadpool.ThreadPool;
import pl.setblack.badass.Politician;

import java.math.BigDecimal;
import java.util.Random;
import java.util.concurrent.*;
import java.util.function.Function;

public class Destabilizer {
   private final long standardDelay;

   private final  double failureProbability;

   private final  double stuckProbability;

   private final ThreadPoolExecutor executor = createExecutor();

   public Destabilizer(long standardDelay, double failureProbability, double stuckProbability) {
      this.standardDelay = standardDelay;
      this.failureProbability = failureProbability;
      this.stuckProbability = stuckProbability;
   }



   public <INPUT, RESULT > Function<INPUT, CompletionStage<RESULT>> makeSlowAndUnstable(Function<INPUT,
      CompletionStage<RESULT>>
                                                                             func) {
      return (INPUT inp) -> func.apply(inp).thenComposeAsync( val -> {
         final CompletableFuture<RESULT> result = new CompletableFuture<>();

         if ( standardDelay > 0) {
            Politician.beatAroundTheBush(() -> Thread.sleep(getPoissonRandom(standardDelay)));
         }
         if  (failureProbability > 0) {
            final double rnd = ThreadLocalRandom.current().nextDouble();
            if ( rnd < failureProbability ) {
               throw new RuntimeException("just planned failure: " + rnd);
            }
         }
         final double rnd2 = ThreadLocalRandom.current().nextDouble();
         if  (!(stuckProbability > 0 && rnd2 < stuckProbability )) {
            result.complete(val);
         } else {
            System.out.println("stuck:"+ rnd2);
         }
         return result;
      }, executor );
   }



   private ThreadPoolExecutor createExecutor() {
      BlockingQueue<Runnable> queue = new ArrayBlockingQueue<>(
         20);
      ThreadPoolExecutor executorService = new ThreadPoolExecutor(100, 1050, 30,
         TimeUnit.SECONDS, queue,
         new ThreadPoolExecutor.CallerRunsPolicy());
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
