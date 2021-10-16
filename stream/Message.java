package stream;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Message {
    private int hour;
    private int minute;
    private String message;
    private String sender;
    private String roomName;

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
     * 
     * 
     */
    public void saveMessage() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("hour", hour);
        jsonObject.put("minute", minute);
        jsonObject.put("sender", sender);
        jsonObject.put("content", message);
        JSONParser parser = new JSONParser();

        try (Reader reader = new FileReader("../data/conversation.json")) {
            JSONObject listeRoom = (JSONObject) parser.parse(reader);
            JSONObject messagesRoom = (JSONObject) listeRoom.get(roomName);
            if(messagesRoom != null){
                messagesRoom.put("message", jsonObject);
            } else {
                listeRoom.put(roomName, jsonObject);
            }
        } catch (IOException e) {
            Logger.error("Message_saveMessage", e.getMessage());
        } catch (ParseException e) {
            Logger.error("Message_saveMessage", e.getMessage());
        }
    }
}
