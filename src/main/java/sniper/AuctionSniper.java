package sniper;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

/**
 * @author Sergey Ivanov.
 */
public class AuctionSniper implements AuctionEventListener {
    private Auction auction;
    private PriceSource lastPriceUpdateFrom = PriceSource.FromOtherBidder;
    private final Map<PriceSource, BiConsumer<Integer, Integer>> priceUpdateStrategies = createPriceUpdateStrategies();
    private SniperSnapshot snapshot;
    private Announcer<SniperListener> listeners = new Announcer<>(SniperListener.class);

    public AuctionSniper(String itemId, Auction auction) {
        this.auction = auction;
        this.snapshot = SniperSnapshot.joining(itemId);
    }

    private Map<PriceSource, BiConsumer<Integer, Integer>> createPriceUpdateStrategies() {
        final Map<PriceSource, BiConsumer<Integer, Integer>> strategies = new HashMap<>();
        strategies.put(PriceSource.FromSniper, this::priceUpdateFromSniper);
        strategies.put(PriceSource.FromOtherBidder, this::priceUpdateFromOtherBidder);

        return strategies;
    }

    public SniperSnapshot getSnapshot() {
        return snapshot;
    }

    @Override
    public void currentPrice(int price, int increment, PriceSource from) {
        lastPriceUpdateFrom = from;
        priceUpdateStrategies.get(from).accept(price, increment);
        listeners.announce().sniperStateChanged(snapshot);
    }

    private void priceUpdateFromSniper(Integer price, Integer increment) {
        snapshot = snapshot.winning(price);
    }

    private void priceUpdateFromOtherBidder(Integer price, Integer increment) {
        final int bid = price + increment;
        auction.bid(bid);
        snapshot = snapshot.bidding(price, bid);
    }

    @Override
    public void auctionClosed() {
        if (lastPriceUpdateFrom == PriceSource.FromOtherBidder)
            this.snapshot = snapshot.lost();
        else
            this.snapshot = snapshot.won();

        listeners.announce().sniperStateChanged(snapshot);
    }

    public void addSniperListener(SniperListener listener) {
        this.listeners.addListener(listener);
    }
}
