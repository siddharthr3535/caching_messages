public class CacheStorage {
    Message message;
    CacheStorage prev, next;
    CacheStorage(){
        this.next = null;
        this.prev = null;
    }
    CacheStorage(Message message){
        this.message = message;
    }
}
