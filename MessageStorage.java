import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Handles persistent storage of messages to disk.
 * Messages are stored as binary files in messages/ directory.
 */
class MessageStorage {
    private static final String MESSAGES_DIR = "messages";
    
    /**
     * Creates a new message with given parameters.
     * Returns null if creation fails.
     */
    public static Message createMessage(int id, String content, String sender, 
                                       String receiver, long timestamp) {
        try {
            return new Message(id, content, sender, receiver, timestamp);
        } catch (Exception e) {
            System.out.println("Failed to create message: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Stores message to disk as binary file msg_id.bin.
     * Creates messages/ directory if it doesn't exist.
     * Returns 0 on success, -1 on failure.
     */
    public static int storeMessage(Message msg) {
        if (msg == null) {
            System.out.println("Cannot store null message");
            return -1;
        }
        
        try {
            Files.createDirectories(Paths.get(MESSAGES_DIR));
            
            String filename = String.format("%s/msg_%d.bin", MESSAGES_DIR, msg.getId());
            
            try (FileOutputStream fos = new FileOutputStream(filename);
                 ObjectOutputStream oos = new ObjectOutputStream(fos)) {
                oos.writeObject(msg);
            }
            
            return 0;
        } catch (IOException e) {
            System.out.println("Failed to store message: " + e.getMessage());
            return -1;
        }
    }
    
    /**
     * Retrieves message from disk by ID.
     * Returns null if file not found or read fails.
     */
    public static Message retrieveMessage(int id) {
        String filename = String.format("%s/msg_%d.bin", MESSAGES_DIR, id);
        
        try (FileInputStream fis = new FileInputStream(filename);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            return (Message) ois.readObject();
        } catch (FileNotFoundException e) {
            System.out.println("Failed to open file for reading");
            return null;
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Failed to read message from file");
            return null;
        }
    }
}