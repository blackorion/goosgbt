package sniper;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;
import static sniper.AuctionEventListener.PriceSource.FromOtherBidder;
import static sniper.AuctionEventListener.PriceSource.FromSniper;

/**
 * @author Sergey Ivanov.
 */
public class AuctionSniperTest {

    private static final String ITEM_ID = "itemId";
    private Auction auction = mock(Auction.class);
    private SniperListener listener = mock(SniperListener.class);
    private final AuctionSniper sniper = new AuctionSniper(new Item(ITEM_ID, 1234), auction);

    @Before
    public void setUp() throws Exception {
        sniper.addSniperListener(listener);
    }

    @Test
    public void reportsLostWhenAuctionClosesImmediately() {
        sniper.auctionClosed();

        verify(listener).sniperStateChanged(new SniperSnapshot(ITEM_ID, 0, 0, SniperState.LOST));
    }

    @Test
    public void reportsLostIfAuctionClosesWhenBidding() {
        sniper.currentPrice(123, 45, FromOtherBidder);
        sniper.auctionClosed();

        verify(listener, atLeastOnce()).sniperStateChanged(new SniperSnapshot(ITEM_ID, 123, 168, SniperState.LOST));
    }

    @Test
    public void bidsHigherAndReportsBiddingWhenNewPriceArrives() {
        final int price = 1001;
        final int increment = 25;
        final int bid = price + increment;

        sniper.currentPrice(price, increment, FromOtherBidder);

        verify(listener, atLeastOnce()).sniperStateChanged(new SniperSnapshot(ITEM_ID, price, bid, SniperState.BIDDING));
    }

    @Test
    public void reportsIsWinningWhenCurrentPriceComesFromSniper() {
        sniper.currentPrice(100, 23, AuctionEventListener.PriceSource.FromOtherBidder);
        sniper.currentPrice(123, 45, AuctionEventListener.PriceSource.FromSniper);

        verify(listener, atLeastOnce()).sniperStateChanged(new SniperSnapshot(ITEM_ID, 123, 123, SniperState.WINNING));
    }

    @Test
    public void noBidsWhenCurrentPriceFromSniper() {
        sniper.currentPrice(123, 4, AuctionEventListener.PriceSource.FromSniper);

        verify(auction, never()).bid(anyInt());
    }

    @Test
    public void reportsWonIfAuctionClosesWhenWinning() {
        sniper.currentPrice(123, 4, FromSniper);
        sniper.auctionClosed();

        verify(listener, atLeastOnce()).sniperStateChanged(new SniperSnapshot(ITEM_ID, 123, 0, SniperState.WON));
    }

    @Test
    public void doesNotBidAndReportsLosingIfSubsequentPriceIsAboveStopPrice() throws Exception {
        sniper.currentPrice(123, 45, FromOtherBidder);
        sniper.currentPrice(2345, 25, FromOtherBidder);

        int bid = 123 + 45;
        verify(listener, atLeastOnce()).sniperStateChanged(new SniperSnapshot(ITEM_ID, 2345, bid, SniperState.LOSING));
    }
}
