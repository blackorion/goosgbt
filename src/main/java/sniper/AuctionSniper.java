package sniper;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

/**
 * @author Sergey Ivanov.
 */
public class AuctionSniper implements AuctionEventListener {
    private Auction auction;
    private SniperListener listener;
    private PriceSource lastPriceUpdateFrom = PriceSource.FromOtherBidder;
    private final Map<PriceSource, BiConsumer<Integer, Integer>> priceUpdateStrategies = createPriceUpdateStrategies();

    private Map<PriceSource, BiConsumer<Integer, Integer>> createPriceUpdateStrategies() {
        final Map<PriceSource, BiConsumer<Integer, Integer>> strategies = new HashMap<>();
        strategies.put(PriceSource.FromSniper, this::priceUpdateFromSniper);
        strategies.put(PriceSource.FromOtherBidder, this::priceUpdateFromOtherBidder);

        return strategies;
    }

    public AuctionSniper(Auction auction, SniperListener listener) {
        this.auction = auction;
        this.listener = listener;
    }

    @Override
    public void currentPrice(int price, int increment, PriceSource from) {
        lastPriceUpdateFrom = from;
        priceUpdateStrategies.get(from).accept(price, increment);
    }

    private void priceUpdateFromSniper(Integer price, Integer increment) {
        listener.sniperWinning();
    }

    private void priceUpdateFromOtherBidder(Integer price, Integer increment) {
        listener.sniperBidding();
        auction.bid(price + increment);
    }

    @Override
    public void auctionClosed() {
        if (lastPriceUpdateFrom == PriceSource.FromOtherBidder)
            listener.sniperLost();
        else
            listener.sniperWon();
    }
}
