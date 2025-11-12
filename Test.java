public class Test {
    public void runTests(){
        System.out.println("Test 1: Create and store a message");
        Message msg1 = MessageStorage.createMessage(1, "vanakkam", "sid", "stinson", 1234567890L);
        if (msg1 == null) {
            System.out.println("Could not create message");
            return;
        }
        
        if (MessageStorage.storeMessage(msg1) != 0) {
            System.out.println("Could not store message!");
            return;
        }
        
        System.out.println("Message stored successfully\n");
        
        System.out.println("Test 2: Retrieve message by ID");
        Message retrieved = MessageStorage.retrieveMessage(1);
        if (retrieved == null) {
            System.out.println("Could not retrieve message");
            return;
        }
        System.out.println("Retrieved message: " + retrieved.getContent());
        
        if (retrieved.getId() == msg1.getId() &&
            retrieved.getSender().equals(msg1.getSender()) &&
            retrieved.getReceiver().equals(msg1.getReceiver()) &&
            retrieved.getContent().equals(msg1.getContent())) {
            System.out.println("Retrieved message matches original\n");
        } else {
            System.out.println("Retrieved message does not match\n");
        }
        
        System.out.println("Test 3: Store and retrieve multiple messages");
        Message msg2 = MessageStorage.createMessage(2, "rendavadhu message", "sid", "stinson", 1234567900L);
        Message msg3 = MessageStorage.createMessage(3, "moonavadhu message", "sid", "stinson", 1234567910L);
        
        MessageStorage.storeMessage(msg2);
        MessageStorage.storeMessage(msg3);
        
        Message get2 = MessageStorage.retrieveMessage(2);
        Message get3 = MessageStorage.retrieveMessage(3);
        
        if (get2 != null && get3 != null) {
            System.out.println("Multiple messages stored and retrieved\n");
        } else {
            System.out.println("Could not retrieve all messages\n");
        }
        
        System.out.println("Test 4: Retrieve non existent message");
        Message missing = MessageStorage.retrieveMessage(999);
        if (missing == null) {
            System.out.println("Correctly returned NULL for missing message\n");
        } else {
            System.out.println("returned something it should not have\n");
        }
        
        System.out.println("Test 5: Store NULL message");
        if (MessageStorage.storeMessage(null) == -1) {
            System.out.println("Correctly rejected null message\n");
        } else {
            System.out.println("failed rejected null message\n");
        }
        
        System.out.println("All tests completed");
    }
}
