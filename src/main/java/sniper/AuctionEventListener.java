package sniper;

import java.util.EventListener;

/**
 * @author Sergey Ivanov.
 */
public interface AuctionEventListener extends EventListener{
    void auctionClosed();

    void currentPrice(int price, int increment, PriceSource fromOtherBidder);

    enum PriceSource {
        FromSniper, FromOtherBidder
    }
}
