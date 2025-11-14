import java.io.FileInputStream;
import java.util.Properties;

// Configuration class to load settings from config.properties file.
public class Config {
    static int messageSize;
    static int cacheCapacity;
    static int maxContentLength;
    static int maxSenderLength;
    static int maxReceiverLength;
    static{

        try{
            Properties properties = new Properties();
        FileInputStream fis = new FileInputStream("config.properties");
        properties.load(fis);
        messageSize = Integer.parseInt(properties.getProperty("message.size" , "512"));
        cacheCapacity = Integer.parseInt(properties.getProperty("cache.capacity" , "16"));
        maxContentLength = Integer.parseInt(properties.getProperty("max.content.length", "384"));
        maxSenderLength = Integer.parseInt(properties.getProperty("max.sender.length", "50"));
        maxReceiverLength = Integer.parseInt(properties.getProperty("max.receiver.length", "50"));
        fis.close();
        } catch(Exception e){
            System.out.println("error occured " + e.getMessage());
        }
        
    }
        
    
}
