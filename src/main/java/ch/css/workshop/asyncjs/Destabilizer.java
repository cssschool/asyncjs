package ch.css.workshop.asyncjs;

import com.google.common.base.Function;
import pl.setblack.badass.Politician;

import java.math.BigDecimal;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Destabilizer<INPUT, RESULT > {
   private final long standardDelay;

   private final  double failureProbability;

   private final  double stuckProbability;



   private final ThreadPoolExecutor myExecutor = createExecutor();


   public Destabilizer(long standardDelay, double failureProbability, double stuckProbability) {
      this.standardDelay = standardDelay;
      this.failureProbability = failureProbability;
      this.stuckProbability = stuckProbability;
   }

   public Function<INPUT, CompletionStage<RESULT>> makeSlowAndUnstable1(Function<INPUT,RESULT> func) {
      return makeSlowAndUnstable2(( INPUT inp ) -> {
         final CompletableFuture<RESULT> result = new CompletableFuture<>();
         myExecutor.execute(() -> {
               result.complete(func.apply(inp));
            }
         );
         return result;
      });
   }

   public Function<INPUT, CompletionStage<RESULT>> makeSlowAndUnstable2(Function<INPUT,CompletionStage<RESULT>> func) {
      return (INPUT inp) -> func.apply(inp).thenApply( val -> {
         Politician.beatAroundTheBush( ()->Thread.sleep(500));
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

}
