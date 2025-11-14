import java.util.Random;


 // Evaluates cache performance for different replacement strategies.
 // Measures cache hits, misses, and hit ratios over 1000 random accesses.

public class CacheEvaluation {
    
    public void runTests() {
        System.out.println("=== CACHE PERFORMANCE EVALUATION ===\n");
        
        evaluateStrategy(Strategy.LRU);
        evaluateStrategy(Strategy.LIFO);
        evaluateStrategy(Strategy.RANDOM);
    }
    

     //Evaluates a specific cache replacement strategy.
     // Creates 100 messages, performs 1000 random accesses, and reports metrics.
     
    public static void evaluateStrategy(Strategy strategy) {
        System.out.println("Testing Strategy: " + strategy);
        System.out.println("=====================================");
        
        int TOTAL_MESSAGES = 100;
        int ACCESS_COUNT = 1000;
        
        MessageManager manager = new MessageManager(strategy); 
        

        System.out.println("Creating and storing " + TOTAL_MESSAGES + " messages...");
        for (int i = 1; i <= TOTAL_MESSAGES; i++) {
            Message msg = manager.createMessage(i, "message " + i, 
                                               "sid" + i, 
                                               "shan" + i, 
                                               System.currentTimeMillis());
            if (msg != null) {
                manager.storeMessage(msg);  
            }
        }
        
        manager.resetMetrics();
        
        // Perform 1000 random accesses
        System.out.println("Performing " + ACCESS_COUNT + " random accesses...");
        Random random = new Random(42);  
        
        for (int i = 0; i < ACCESS_COUNT; i++) {
            int randomId = random.nextInt(TOTAL_MESSAGES) + 1;
            
            Message msg = manager.retrieveMessage(randomId);
        }
        
        // results
        int hits = manager.getCacheHits();
        int misses = manager.getCacheMisses();
        double hitRatio = manager.getHitRatio();
        
        System.out.println("\nRESULTS:");
        System.out.println("--------");
        System.out.println("Cache Hits:    " + hits + " / 1000 accesses");
        System.out.println("Cache Misses:  " + misses + " / 1000 accesses");
        System.out.println("Hit Ratio:     " + String.format("%.2f%%", hitRatio * 100));
        System.out.println("Miss Ratio:    " + String.format("%.2f%%", (1 - hitRatio) * 100));
        System.out.println("\n");
    }
}