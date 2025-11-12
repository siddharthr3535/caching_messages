import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

class MessageStorage {
    
    
    public static Message createMessage(int id, String content, String sender, 
                                       String receiver, long timestamp) {
        try {
            return new Message(id, content, sender, receiver, timestamp);
        } catch (Exception e) {
            System.out.println("Failed to create message: " + e.getMessage());
            return null;
        }
    }
    
    public static int storeMessage(Message msg) {
        if (msg == null) {
            System.out.println("Cannot store null message");
            return -1;
        }
        
        try {
            Files.createDirectories(Paths.get("messages"));
            
            String filename = String.format("%s/msg_%d.bin", "messages", msg.getId());
            
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
    
    public static Message retrieveMessage(int id) {
        String filename = String.format("%s/msg_%d.bin", "messages", id);
        
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