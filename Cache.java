import java.util.HashMap;
import java.util.Map;

/**
 * Cache implementation with configurable replacement strategies (LRU, LIFO, Random).
 * Uses HashMap for O(1) lookups and doubly-linked list for ordering.
 */
public class Cache {
    int id;
    int cacheSize;
    int cacheCount;
    CacheStorage head, tail;  // Dummy nodes for doubly-linked list
    int cacheHits = 0;
    int cacheMisses = 0;
    Map<Integer, CacheStorage> map;  // Maps message ID to cache node
    Strategy strategy;
    
    /**
     * Creates cache with specified replacement strategy.
     * Initializes dummy head/tail nodes for linked list.
     */
    Cache(Strategy strategy){
        map = new HashMap<>();
        cacheSize = Config.cacheCapacity;
        cacheCount = 0;
        head = new CacheStorage();
        tail = new CacheStorage();
        head.next = tail;
        tail.prev = head;
        this.strategy = strategy;
    }
    
    /**
     * Adds or updates message in cache.
     * If cache is full, evicts based on strategy before adding new message.
     */
    public void putMessageInCache(Message message){
        if(map.containsKey(message.getId())){
            // Update existing message
            CacheStorage cache = map.get(message.getId());
            cache.message = message;

            // Only reorder for LRU
            if(strategy == Strategy.LRU){
                removeBlock(cache);
                addToFront(cache);
            }
        }
        else{
            // Evict if cache is full
            if(cacheCount >= cacheSize){
                switch (strategy) {
                    case LRU:
                        LRU();
                        break;
                    case LIFO:
                        LIFO();
                        break;
                    case RANDOM:
                        RANDOM();
                        break;
                }
            }
            
            // Add new message
            CacheStorage newCache = new CacheStorage(message);
            map.put(message.getId(), newCache);
            addToFront(newCache);
        }
    }

    /**
     * Evicts a random message from cache.
     */
    private void RANDOM(){
        int size = map.size();
        int[] array = new int[size];
        for (Map.Entry<Integer, CacheStorage> entry : map.entrySet()) {
            int key = entry.getKey();
            array[--size] = key;
        }
        int randomIndex = (int)(Math.random() * array.length);
        int randomKey = array[randomIndex];
        CacheStorage cacheStorage = map.get(randomKey);
        map.remove(randomKey);
        removeBlock(cacheStorage);
    }
    
    /**
     * Evicts most recently added message (tail.prev).
     */
    private void LIFO(){
       CacheStorage cacheStorage = tail.prev;
       map.remove(cacheStorage.message.getId());
       removeBlock(cacheStorage);
    }

    /**
     * Evicts least recently used message (head.next).
     */
    private void LRU(){
        removeLeast();
    }
    
    /**
     * Retrieves message from cache by ID.
     * Returns null if not found (cache miss).
     * For LRU, moves accessed message to front.
     */
    public Message getMessageFromCache(int id){
        if(!map.containsKey(id)){
            cacheMisses++;
            return null;
        }
        
        cacheHits++;
        CacheStorage cacheStorage = map.get(id);
        
        // Only reorder for LRU strategy
        if(strategy == Strategy.LRU){
            removeBlock(cacheStorage);
            addToFront(cacheStorage);
        }
        
        return cacheStorage.message;
    }

    /**
     * Adds cache node to front of list (before tail).
     * Represents most recently used/added position.
     */
    private void addToFront(CacheStorage cache){
        cache.prev = tail.prev;
        tail.prev.next = cache;
        tail.prev = cache;
        cache.next = tail;
        cacheCount++;
    }

    /**
     * Removes cache node from linked list.
     */
    private void removeBlock(CacheStorage cache){
        CacheStorage previous = cache.prev;
        CacheStorage nextStorage = cache.next;
        nextStorage.prev = previous;
        previous.next = nextStorage;
        cacheCount--;
    }

    /**
     * Removes least recently used message (head.next).
     */
    private void removeLeast(){
        CacheStorage least = head.next;
        map.remove(least.message.getId());
        least.next.prev = head;
        head.next = least.next;
        cacheCount--;
    }
    
    // Performance metrics getters
    public int getCacheHits() { return cacheHits; }
    public int getCacheMisses() { return cacheMisses; }
    public double getHitRatio() {
        int total = cacheHits + cacheMisses;
        if (total == 0) return 0.0;
        return (double) cacheHits / total;
    }
    
    public void resetMetrics() {
        cacheHits = 0;
        cacheMisses = 0;
    }
}