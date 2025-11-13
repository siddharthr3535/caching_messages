

import java.io.Serializable;

public class Message implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private int id;
    private String content;
    private String sender;
    private String receiver;
    private boolean delivered;
    private long timestamp;
    
    public Message(int id, String content, String sender, String receiver, long timestamp) {
        this.id = id;
        this.content = content.length() > Config.maxContentLength ? content.substring(0,Config.maxContentLength ) : content;
        this.sender = sender.length() > Config.maxSenderLength ? sender.substring(0,Config.maxSenderLength ) : sender;
        this.receiver = receiver.length() > Config.maxReceiverLength ? receiver.substring(0,Config.maxReceiverLength ) : receiver;
        this.delivered = false;
        this.timestamp = timestamp;
    }
    

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

    
