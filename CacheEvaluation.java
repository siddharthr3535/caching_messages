import java.util.Random;

public class CacheEvaluation {
    
    public void runTests() {
        System.out.println("cache performance evaluation test :\n");
        
        evaluateStrategy(Strategy.LRU);
        evaluateStrategy(Strategy.LIFO);
        evaluateStrategy(Strategy.RANDOM);
    }
    
    public static void evaluateStrategy(Strategy strategy) {
        System.out.println("testing strategy :  " + strategy );
        
        int totalMessages = 100; 

        
        Cache cache = new Cache(strategy);
        
        System.out.println("Creating and storing :  " + totalMessages + " messages...");
        for (int i = 1; i <= totalMessages; i++) {
            Message msg = MessageStorage.createMessage(i, "message " + i, 
                                                      "sid" + i, 
                                                      "shan" + i, 
                                                      System.currentTimeMillis());
            if (msg != null) {
                MessageStorage.storeMessage(msg); 
                cache.putMessageInCache(msg);       
            }
        }
        

        cache.resetMetrics();
        
        System.out.println("Performing 1000 random accesses");
        Random random = new Random(42);  
        
        for (int i = 0; i < 1000; i++) {
            int randomId = random.nextInt(totalMessages) + 1;
            Message msg = cache.getMessageFromCache(randomId);
            if (msg == null) {
                msg = MessageStorage.retrieveMessage(randomId);
                if (msg != null) {
                    cache.putMessageInCache(msg);
                }
            }
        }
        
        int hits = cache.getCacheHits();
        int misses = cache.getCacheMisses();
        double hitRatio = cache.getHitRatio();
        
        System.out.println("\noutput: ");
        System.out.println("Cache Hits:    " + hits + " per  1000 access");
        System.out.println("Cache Misses:  " + misses + " per 1000");
        System.out.println("Hit Ratio:     " + String.format("%.2f%%", hitRatio * 100));
        System.out.println("Miss Ratio:    " + String.format("%.2f%%", (1 - hitRatio) * 100));
        System.out.println("\n");
    }
}