package stream;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Message extends Thread{
    private int hour;
    private int minute;
    private String message;
    private String sender;
    private String roomName;
    private static String rootDataFile = "./data/conversation.json";

    public Message(LocalDateTime date, String message, String sender, String roomName) {
        this.hour = date.getHour();
        this.minute = date.getMinute();
        this.roomName = roomName;
        this.message = message;
        this.sender = sender;
    }

    public String getMessage() {
        String outputLine = hour + "h" + minute + "-  " + sender + " : " + message;
        return outputLine;
    }

    /**
     * Persist the message in a JSON file
     * Use a mutex to write and read alone
     * Thread only to persist the message
     */
    public void run() {
        // format du message à persister
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("hour", hour);
        jsonObject.put("minute", minute);
        jsonObject.put("sender", sender);
        jsonObject.put("content", message);
        Logger.debug("Message_saveMessage", "Trying to save: " +message);

        JSONParser parser = new JSONParser();
        try {
            // mutex ecriture
            EchoServerMultiThreaded.mutexConversation.acquire();
            try (Reader reader = new FileReader(Message.rootDataFile)) {
                JSONObject listeRoom = (JSONObject) parser.parse(reader);
                JSONObject room = (JSONObject) listeRoom.get(roomName);
                if(room == null){
                    room = new JSONObject();
                    JSONArray messages = new JSONArray();
                    room.put("messages",messages);
                }
                JSONArray messagesRoom = (JSONArray) room.get("messages");
                messagesRoom.add(jsonObject);
                room.put("messages",messagesRoom);
                listeRoom.put(roomName, room);
                FileWriter file = new FileWriter(Message.rootDataFile);
                file.write(listeRoom.toJSONString());
                file.flush();
                file.close();
                Logger.debug("Message_saveMessage", "Message saved");
                // libération du mutex
                EchoServerMultiThreaded.mutexConversation.release();
            } catch (IOException e) {
                Logger.error("Message_saveMessage", e.getMessage());
            } catch (ParseException e) {
                Logger.error("Message_saveMessage", e.getMessage());
            }    
        } catch (InterruptedException e1) {
            Logger.warning("Message_saveMessage", "Fail mutex "+ e1.getMessage());
        }
    }

    public static List<String> getHistoricConversation(String roomName){
        List<String> output = new ArrayList<String>();

        try (Reader reader = new FileReader(Message.rootDataFile)) {
            JSONParser parser = new JSONParser();
            JSONObject listeRoom = (JSONObject) parser.parse(reader);
            JSONObject room = (JSONObject) listeRoom.get(roomName);
            if(room != null){
                JSONArray messagesRoom = (JSONArray) room.get("messages");
                if(messagesRoom != null){
                    int len = messagesRoom.size();
                    for(int i = 0; i<len;i++){
                        JSONObject message = (JSONObject) messagesRoom.get(i);
                        output.add(message.get("hour") + "h" + message.get("minute") + "-  " + message.get("sender") + " : " + message.get("content"));
                    }
                }
            }
            Logger.debug("Message_getHistoricConversation", "Old messages loaded");
        } catch (IOException e) {
            Logger.error("Message_getHistoricConversation", e.getMessage());
        } catch (ParseException e) {
            Logger.error("Message_getHistoricConversation", e.getMessage());
        }    
        return output;
    }
}
