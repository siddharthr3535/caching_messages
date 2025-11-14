public class CacheTest {
    
    public void runTests() {
        System.out.println("Testing Cache: \n");
        
        testCacheHitMiss();
        testLRUReplacement();
        testLIFOReplacement();
        testRandomReplacement();
        testCacheCapacity();
        testMessageSizeLimits();
        testDiskIntegration();  // ✅ New test
        
        System.out.println("\n All tests completed.");
    }
    
    public static void testCacheHitMiss() {
        System.out.println("test 1: Cache Hit/Miss Detection - LRU");
        
        MessageManager manager = new MessageManager(Strategy.LRU);  
        
        Message msg1 = manager.createMessage(1, "Test message lru", "Sid", "shan", System.currentTimeMillis());
        manager.storeMessage(msg1);  
        
        Message retrieved = manager.retrieveMessage(1); 
        if (retrieved != null && retrieved.getId() == 1) {
            System.out.println("Message found in cache , works");
        } else {
            System.out.println("Failed, Message not in cache");
        }
        
        Message missing = manager.retrieveMessage(999); 
        if (missing == null) {
            System.out.println("works,  Non existent message returned null");
        } else {
            System.out.println("✗ Failed, returned something for an non existent message");
        }
        
        System.out.println();
    }
    
    public static void testLRUReplacement() {
        System.out.println("test 2: LRU Replacement");
        
        MessageManager manager = new MessageManager(Strategy.LRU);
        
        System.out.println("Filling cache with 16 messages");
        for (int i = 1; i <= 16; i++) {
            Message msg = manager.createMessage(i, "Message " + i, "na dhan", "yaara irundha enna", System.currentTimeMillis());
            manager.storeMessage(msg);
        }
        
        manager.resetMetrics();
        
        System.out.println("Accessing message 1");
        manager.retrieveMessage(1);  
        
        System.out.println("Adding message 17 , this should evict message 2 as it is least recently used");
        Message msg17 = manager.createMessage(17, "Message 17", "naane", "neeye", System.currentTimeMillis());
        manager.storeMessage(msg17);
        
        if (manager.retrieveMessage(1) != null) {
            System.out.println("Message 1 still in cache, works");
        } else {
            System.out.println("Failed, Message 1 not in cache");
        }
        
        int missesBeforeMsg2 = manager.getCacheMisses();
        manager.retrieveMessage(2); 
        int missesAfterMsg2 = manager.getCacheMisses();
        
        if (missesAfterMsg2 > missesBeforeMsg2) {
            System.out.println("works, message 2 was evicted (cache miss occurred)");
        } else {
            System.out.println("failed, message 2 still in cache");
        }
        
        if (manager.retrieveMessage(17) != null) {
            System.out.println("Message 17 in cache, works");
        } else {
            System.out.println("failed, message 17 is not there");
        }
        
        System.out.println();
    }
    
    public static void testLIFOReplacement() {
        System.out.println("test 3: LIFO Replacement");
        
        MessageManager manager = new MessageManager(Strategy.LIFO);
        
        System.out.println("Filling cache with 16 messages");
        for (int i = 1; i <= 16; i++) {
            Message msg = manager.createMessage(i, "Message " + i, "sid", "shan", System.currentTimeMillis());
            manager.storeMessage(msg);
        }
        
        manager.resetMetrics();
        
        System.out.println("Accessing message 1 ");
        manager.retrieveMessage(1);
        
        System.out.println("Adding message 17 , should evice  the last message 16");
        Message msg17 = manager.createMessage(17, "Message 17", "sid", "shan", System.currentTimeMillis());
        manager.storeMessage(msg17);
        
        int missesBeforeMsg16 = manager.getCacheMisses();
        manager.retrieveMessage(16);
        int missesAfterMsg16 = manager.getCacheMisses();
        
        if (missesAfterMsg16 > missesBeforeMsg16) {
            System.out.println("works, Message 16 was evicted (cache miss)");
        } else {
            System.out.println("failed, Message 16 should be evicted");
        }
        
        if (manager.retrieveMessage(1) != null) {
            System.out.println("works, Message 1 still in cache");
        } else {
            System.out.println("failed,  Message 1 should still be in cache");
        }
        
        System.out.println();
    }
    
    public static void testRandomReplacement() {
        System.out.println("test 4 : Random Replacement");
        
        MessageManager manager = new MessageManager(Strategy.RANDOM);
        
        System.out.println("Filling cache with 16 messages");
        for (int i = 1; i <= 16; i++) {
            Message msg = manager.createMessage(i, "Message " + i, "me", "you", System.currentTimeMillis());
            manager.storeMessage(msg);
        }
        
        manager.resetMetrics();
        
        System.out.println("Adding 5 new messages with random eviction");
        for (int i = 17; i <= 21; i++) {
            Message msg = manager.createMessage(i, "Message " + i, "me", "you", System.currentTimeMillis());
            manager.storeMessage(msg);
        }
    
        int cacheMisses = 0;
        for (int j = 1; j <= 16; j++) {
            int missesBeforeCheck = manager.getCacheMisses();
            manager.retrieveMessage(j);
            if (manager.getCacheMisses() > missesBeforeCheck) {
                cacheMisses++;
            }
        }
        
        if (cacheMisses >= 5) {
            System.out.println("works, " + cacheMisses + " messages were evicted");
        } else {
            System.out.println("failed, only " + cacheMisses + " messages were evicted");
        }
        
        System.out.println();
    }
    
    public static void testCacheCapacity() {
        System.out.println("test 5: Cache Capacity");
        
        MessageManager manager = new MessageManager(Strategy.LRU);
        
        System.out.println("Adding 20 messages now that the capacity is 16.");
        for (int i = 1; i <= 20; i++) {
            Message msg = manager.createMessage(i, "Message " + i, "sid", "shan", System.currentTimeMillis());
            manager.storeMessage(msg);
        }
        
        manager.resetMetrics();
        
        int inCache = 0;
        for (int i = 1; i <= 20; i++) {
            int hitsBeforeCheck = manager.getCacheHits();
            manager.retrieveMessage(i);
            if (manager.getCacheHits() > hitsBeforeCheck) {
                inCache++;  
            }
        }
        
        if (inCache <= Config.cacheCapacity) {
            System.out.println("works, " + inCache + " messages in cache (capacity: " + Config.cacheCapacity + ")");
        } else {
            System.out.println("failed, capacity was exceeded: " + inCache + " > " + Config.cacheCapacity);
        }
        
        System.out.println();
    }
    
    public static void testMessageSizeLimits() {
        System.out.println("test 6: Message Size Limits");
        String longContent = "";
        for(int i = 0 ; i < Config.maxContentLength + 10 ; i++){
            longContent += "a";
        }
        Message msg = new Message(1, longContent, "Sid", "shan", System.currentTimeMillis());
        
        if (msg.getContent().length() <= Config.maxContentLength) {
            System.out.println("works, content was trimmed to  " + msg.getContent().length() + " characters");
        } else {
            System.out.println("failed , exceeds size limit");
        }
        String longSender = "";
        for(int i = 0 ; i < Config.maxSenderLength + 10 ; i++){
            longSender += "s";
        }
        Message msg2 = new Message(2, "test", longSender, "shan", System.currentTimeMillis());
        
        if (msg2.getSender().length() <= Config.maxSenderLength) {
            System.out.println("sender wsa trimmed to  " + msg2.getSender().length() + " characters");
        } else {
            System.out.println("failedd, Sender exceeds limit");
        }
        
        System.out.println();
    }
    
  // loads back from the disk on cache miss
    public static void testDiskIntegration() {
        System.out.println("test 7: Cache-Disk Integration (Load from Disk)");
        
        MessageManager manager = new MessageManager(Strategy.LRU);
        
        // Store 20 messages (cache only holds 16)
        System.out.println("Storing 20 messages (cache capacity: 16)");
        for (int i = 1; i <= 20; i++) {
            Message msg = manager.createMessage(i, "Message " + i, "sid", "shan", System.currentTimeMillis());
            manager.storeMessage(msg);
        }
        
        manager.resetMetrics();
        
        // Try to access message 1 (should be evicted from cache, but on disk)
        System.out.println("Accessing message 1 (evicted from cache)...");
        Message msg1 = manager.retrieveMessage(1);
        
        if (msg1 != null && msg1.getId() == 1) {
            System.out.println("works, Message loaded from disk and added to cache");
            System.out.println("Cache misses: " + manager.getCacheMisses());
            System.out.println("Cache hits: " + manager.getCacheHits());
        } else {
            System.out.println("failed, Could not load message from disk");
        }
        
        // Access same message again (should be cache hit now)
        System.out.println("Accessing message 1 again...");
        Message msg1Again = manager.retrieveMessage(1);
        
        if (msg1Again != null && manager.getCacheHits() > 0) {
            System.out.println("works, Message found in cache (cache hit)");
        } else {
            System.out.println("failed, Should be cache hit");
        }
        
        System.out.println();
    }
}