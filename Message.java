import java.io.Serializable;

// Represents a message with content, sender, receiver, delivery status, and timestamp.
public class Message implements Serializable {

    private int id;
    private String content;
    private String sender;
    private String receiver;
    private boolean delivered;
    private long timestamp;
    // Constructor to initialize a Message object with given parameters.
    public Message(int id, String content, String sender, String receiver, long timestamp) {
        this.id = id;
        this.content = content.length() > Config.maxContentLength ? content.substring(0,Config.maxContentLength ) : content;
        this.sender = sender.length() > Config.maxSenderLength ? sender.substring(0,Config.maxSenderLength ) : sender;
        this.receiver = receiver.length() > Config.maxReceiverLength ? receiver.substring(0,Config.maxReceiverLength ) : receiver;
        this.delivered = false;
        this.timestamp = timestamp;
    }
    
    // Getter methods for accessing private fields.
    public int getId() { 
        return id; 
    }
    public String getContent() { 
        return content; 
    }
    public String getSender() { 
        return sender; 
    }
    public String getReceiver() { 
        return receiver; 
    }
    public boolean isDelivered() { 
        return delivered; 
    }
    public long getTimestamp() { 
        return timestamp; 
    }

    public void setDelivered(boolean delivered) { 
        this.delivered = delivered; 
    }
}

    
