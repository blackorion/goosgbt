package sniper;

import org.junit.Test;

import static org.mockito.Mockito.*;

/**
 * @author Sergey Ivanov.
 */
public class AuctionSniperTest {

    private Auction auction = mock(Auction.class);
    private SniperListener listener = mock(SniperListener.class);
    private final AuctionSniper sniper = new AuctionSniper(auction, listener);

    @Test
    public void reportsLostWhenAuctionCloses() {
        sniper.auctionClosed();

        verify(listener).sniperLost();
    }

    @Test
    public void bidsHigherAndReportsBiddingWhenNewPriceArrives() {
        final int price = 1001;
        final int increment = 25;

        sniper.currentPrice(price, increment);

        verify(listener, atLeastOnce()).sniperBidding();
    }

}
