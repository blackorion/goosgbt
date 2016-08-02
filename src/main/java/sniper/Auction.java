package sniper;

/**
 * @author Sergey Ivanov.
 */
public interface Auction {
    void bid(int amount);

    void join();

    void addAuctionEventListener(AuctionEventListener listener);
}
