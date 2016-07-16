package sniper;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.packet.Message;

import static sniper.AuctionEventListener.PriceSource.FromOtherBidder;
import static sniper.AuctionEventListener.PriceSource.FromSniper;

/**
 * @author Sergey Ivanov.
 */
public class AuctionMessageTranslator implements MessageListener {
    private final String sniperId;
    private final AuctionEventListener listener;

    public AuctionMessageTranslator(String sniperId, AuctionEventListener listener) {
        this.sniperId = sniperId;
        this.listener = listener;
    }

    public void processMessage(Chat chat, Message message) {
        AuctionEvent event = AuctionEvent.from(message.getBody());

        String type = event.type();

        if ("CLOSE".equals(type))
            listener.auctionClosed();
        else if ("PRICE".equals(type)) {
            listener.currentPrice(event.currentPrice(), event.increment(), event.isFrom(sniperId));
        }
    }
}
