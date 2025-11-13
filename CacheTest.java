public class CacheTest {
    
    public void runTests() {
        System.out.println("Testing Cache: \n");
        
        testCacheHitMiss();
        testLRUReplacement();
        testLIFOReplacement();
        testRandomReplacement();
        testCacheCapacity();
        testMessageSizeLimits();
        
        System.out.println("\n All tests completed.");
    }
    
    public static void testCacheHitMiss() {
        System.out.println("test 1: Cache Hit/Miss Detection - LRU");
        
        Cache cache = new Cache(Strategy.LRU);
        
        Message msg1 = new Message(1, "Test message lru", "Sid", "shan", System.currentTimeMillis());
        cache.putMessageInCache(msg1);
        
        Message retrieved = cache.getMessageFromCache(1);
        if (retrieved != null && retrieved.getId() == 1) {
            System.out.println("Message found in cache , works");
        } else {
            System.out.println("Failed, Message not in cache");
        }
        

        Message missing = cache.getMessageFromCache(999);
        if (missing == null) {
            System.out.println("works,  Non existent message returned null");
        } else {
            System.out.println("✗ Failed, returned something for an non existent message");
        }
        
        System.out.println();
    }
    
    public static void testLRUReplacement() {
        System.out.println("test 2: LRU Replacement");
        
        Cache cache = new Cache(Strategy.LRU);
        
        System.out.println("Filling cache with 16 messages");
        for (int i = 1; i <= 16; i++) {
            Message msg = new Message(i, "Message " + i, "na dhan", "yaara irundha enna", System.currentTimeMillis());
            cache.putMessageInCache(msg);
        }
        
        System.out.println("Accessing message 1");
        cache.getMessageFromCache(1);
        
        System.out.println("Adding message 17 , this should evict message 2 as it is least recently used");
        Message msg17 = new Message(17, "Message 17", "naane", "neeye", System.currentTimeMillis());
        cache.putMessageInCache(msg17);
        
        if (cache.getMessageFromCache(1) != null) {
            System.out.println("Message 1 still in cache, works");
        } else {
            System.out.println("Failed, Message 1 not in cache");
        }
        
        if (cache.getMessageFromCache(2) == null) {
            System.out.println("works, message 2 evicted ");
        } else {
            System.out.println("failed, message 2 still there");
        }
        
        if (cache.getMessageFromCache(17) != null) {
            System.out.println("Message 17 in cache, works");
        } else {
            System.out.println("failed, message 17 is not there");
        }
        
        System.out.println();
    }
    
    public static void testLIFOReplacement() {
        System.out.println("test 3: LIFO Replacement");
        
        Cache cache = new Cache(Strategy.LIFO);
        
        System.out.println("Filling cache with 16 messages");
        for (int i = 1; i <= 16; i++) {
            Message msg = new Message(i, "Message " + i, "sid", "shan", System.currentTimeMillis());
            cache.putMessageInCache(msg);
        }
        
        System.out.println("Accessing message 1 ");
        cache.getMessageFromCache(1);
        

        System.out.println("Adding message 17 , should evice  the last message 16");
        Message msg17 = new Message(17, "Message 17", "sid", "shan", System.currentTimeMillis());
        cache.putMessageInCache(msg17);
        
        if (cache.getMessageFromCache(16) == null) {
            System.out.println("works, Message 16 evicted");
        } else {
            System.out.println("failed, Message 16 should be evicted");
        }
        
        if (cache.getMessageFromCache(1) != null) {
            System.out.println("works, Message 1 still in cache");
        } else {
            System.out.println("failed,  Message 1 should still be in cache");
        }
        
        System.out.println();
    }
    
    public static void testRandomReplacement() {
        System.out.println("test 4 : Random Replacement");
        
        Cache cache = new Cache(Strategy.RANDOM);
        
        System.out.println("Filling cache with 16 messages");
        for (int i = 1; i <= 16; i++) {
            Message msg = new Message(i, "Message " + i, "me", "you", System.currentTimeMillis());
            cache.putMessageInCache(msg);
        }
        
        System.out.println("Adding 5 new messages with random eviction");
        int evictedCount = 0;
        for (int i = 17; i <= 21; i++) {
            Message msg = new Message(i, "Message " + i, "me", "you", System.currentTimeMillis());
            cache.putMessageInCache(msg);
            for (int j = 1; j <= 16; j++) {
                if (cache.getMessageFromCache(j) == null) {
                    evictedCount++;
                }
            }
        }
        
        if (evictedCount >= 5) {
            System.out.println("works, 5 were evicted");
        } else {
            System.out.println("failed, 5 messages were not evicted");
        }
        
        System.out.println();
    }
    
    public static void testCacheCapacity() {
        System.out.println("test 5: Cache Capacity");
        
        Cache cache = new Cache(Strategy.LRU);
        
        System.out.println("Adding 20 messages now that the capacity is 16.");
        for (int i = 1; i <= 20; i++) {
            Message msg = new Message(i, "Message " + i, "sid", "shan", System.currentTimeMillis());
            cache.putMessageInCache(msg);
        }
        
        int inCache = 0;
        for (int i = 1; i <= 20; i++) {
            if (cache.getMessageFromCache(i) != null) {
                inCache++;
            }
        }
        
        if (inCache <= Config.cacheCapacity) {
            System.out.println("works, expected is " + inCache + " messages in cache");
        } else {
            System.out.println("failed, capacity was exceeded.");
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
    
    
}