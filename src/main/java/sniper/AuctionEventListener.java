package sniper;

/**
 * @author Sergey Ivanov.
 */
public interface AuctionEventListener {
    void auctionClosed();

    void currentPrice(int price, int increment, PriceSource fromOtherBidder);

    enum PriceSource {
        FromSniper, FromOtherBidder
    }
}
