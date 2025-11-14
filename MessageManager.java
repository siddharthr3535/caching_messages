
 // Manages message storage with caching.
// Integrates cache and disk storage for transparent message access.
 
public class MessageManager {
    private Cache cache;
    
    //Creates message manager with specified cache replacement strategy.

    public MessageManager(Strategy strategy) {
        this.cache = new Cache(strategy);
    }
    
    //Creates a new message.

    public Message createMessage(int id, String content, String sender, 
                                 String receiver, long timestamp) {
        return MessageStorage.createMessage(id, content, sender, receiver, timestamp);
    }
    
    //Stores message to disk and adds to cache.
      //Returns 0 on success, -1 on failure.

    public int storeMessage(Message msg) {
        if (msg == null) return -1;
        
        // Store to disk
        int result = MessageStorage.storeMessage(msg);
        
        // Also add to cache
        if (result == 0) {
            cache.putMessageInCache(msg);
        }
        
        return result;
    }
    
    
      // Retrieves message from cache first, loads from disk if cache miss.
     // Demonstrates proper page loading from disk on cache miss.
     
    public Message retrieveMessage(int id) {
        // Try cache first
        Message msg = cache.getMessageFromCache(id);
        
        // Cache miss then load from disk
        if (msg == null) {
            msg = MessageStorage.retrieveMessage(id);
            
            // Add to cache for future access
            if (msg != null) {
                cache.putMessageInCache(msg);
            }
        }
        
        return msg;
    }
    
    public int getCacheHits() { return cache.getCacheHits(); }
    public int getCacheMisses() { return cache.getCacheMisses(); }
    public double getHitRatio() { return cache.getHitRatio(); }
    public void resetMetrics() { cache.resetMetrics(); }
}