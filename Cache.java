import java.util.HashMap;
import java.util.Map;

public class Cache {
    int id;
    int cacheSize;
    int cacheCount;
    CacheStorage head, tail;
    Map<Integer, CacheStorage> map ;
    Strategy strategy;
    Cache(Strategy strategy){
        map= new HashMap<>();
        cacheSize = Config.cacheCapacity;
        cacheCount = 0;
        head = new CacheStorage();
        tail = new CacheStorage();
        head.next = tail;
        tail.prev = head;
        this.strategy = strategy;
    }
    public void putMessageInCache(Message message){
        if(map.containsKey(message.getId())){
            CacheStorage cache = map.get(message.getId());
            cache.message = message;

            if(strategy == Strategy.LRU){
                removeBlock(cache);
           
                addToFront(cache);
            }
            

        }
        else{
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
            CacheStorage newCache = new CacheStorage(message);
            map.put(message.getId(), newCache);
            addToFront(newCache);
        }
    }

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
    private void LIFO(){
       CacheStorage cacheStorage = tail.prev;
         map.remove(cacheStorage.message.getId());
            removeBlock(cacheStorage);
    }

    private void LRU(){
        removeLeast();
    }
    public Message getMessageFromCache(int id){
        if(!map.containsKey(id)){
            return null;
        }

        CacheStorage cacheStorage = map.get(id);
        if(strategy == Strategy.LRU){
            removeBlock(cacheStorage);
        addToFront(cacheStorage);
        }
        
        return cacheStorage.message;
    }

    private void addToFront(CacheStorage cache){
        cache.prev = tail.prev;
        tail.prev.next = cache;
        tail.prev = cache;
        cache.next = tail;
        cacheCount++;
    }

    private void removeBlock(CacheStorage cache){
        CacheStorage previous = cache.prev;
        CacheStorage nextStorage = cache.next;
        nextStorage.prev = previous;
        previous.next = nextStorage;
        cacheCount--;
    }

    private void removeLeast(){
        CacheStorage least = head.next;
        map.remove(least.message.getId());
        least.next.prev = head;
        head.next = least.next;
        cacheCount--;
        
    }
}
