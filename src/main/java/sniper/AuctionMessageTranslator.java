package sniper;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.packet.Message;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Sergey Ivanov.
 */
public class AuctionMessageTranslator implements MessageListener {
    private final AuctionEventListener listener;

    public AuctionMessageTranslator(AuctionEventListener listener) {
        this.listener = listener;
    }

    public void processMessage(Chat chat, Message message) {
        Map<String, String> event = unpackEventFrom(message);

        String type = event.get("Event");

        if ("CLOSE".equals(type))
            listener.auctionClosed();
        else if ("PRICE".equals(type)) {
            listener.currentPrice(Integer.parseInt(event.get("CurrentPrice")), Integer.parseInt(event.get("Increment")));
        }
    }

    private Map<String, String> unpackEventFrom(Message message) {
        return Arrays.stream(message.getBody().split(";"))
                .map(el -> el.split(":"))
                .collect(Collectors.toMap(this::getKey, this::getValue));
    }

    private String getKey(String[] pair) {
        return pair[0].trim();
    }

    private String getValue(String[] pair) {
        return pair[1].trim();
    }

}
