package sniper.xmpp;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.packet.Message;
import org.junit.Test;
import sniper.AuctionEventListener;
import sniper.xmpp.AuctionMessageTranslator;

import static org.mockito.Mockito.*;
import static sniper.AuctionEventListener.PriceSource.FromOtherBidder;
import static sniper.AuctionEventListener.PriceSource.FromSniper;

/**
 * @author Sergey Ivanov.
 */
public class AuctionMessageTranslatorTest {
    private static final Chat UNUSED_CHAT = null;
    private static final String SNIPER_ID = "sniperId";
    private final AuctionEventListener listener = mock(AuctionEventListener.class);
    private AuctionMessageTranslator translator = new AuctionMessageTranslator(SNIPER_ID, listener);

    @Test
    public void notifiesAuctionClosedWhenCloseMessageReceived() {
        Message message = new Message();
        message.setBody("SOLVersion: 1.1; Event: CLOSE;");

        translator.processMessage(UNUSED_CHAT, message);

        verify(listener).auctionClosed();
    }

    @Test
    public void notifiesBidDetailsWhenCurrentPriceMessageReceivedFromOtherBidder() {
        Message message = new Message();
        message.setBody("SOLVersion: 1.1; Event: PRICE; CurrentPrice: 192; Increment: 7; Bidder: Someone else;");

        translator.processMessage(UNUSED_CHAT, message);

        verify(listener).currentPrice(192, 7, FromOtherBidder);
    }

    @Test
    public void notifiesBidDetailsWhenCurrentPriceMessageReceivedFromSniper() {
        Message message = new Message();
        message.setBody("SOLVersion: 1.1; Event: PRICE; CurrentPrice: 234; Increment: 5; Bidder: " + SNIPER_ID + ";");

        translator.processMessage(UNUSED_CHAT, message);

        verify(listener).currentPrice(234, 5, FromSniper);
    }

}
