package sniper.xmpp;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import sniper.ApplicationRunner;
import sniper.Auction;
import sniper.AuctionEventListener;
import sniper.doubles.FakeAuctionServer;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertTrue;
import static sniper.doubles.FakeAuctionServer.XMPP_HOSTNAME;

/**
 * @author Sergey Ivanov.
 */
public class XMPPAuctionHouseTest {
    private XMPPAuctionHouse auctionHouse;

    private FakeAuctionServer auctionServer = new FakeAuctionServer("item-54321");

    @Before
    public void openConnection() throws Exception {
        auctionServer.startSellingItem();
        auctionHouse = XMPPAuctionHouse.connect(XMPP_HOSTNAME, "sniper", "sniper");
    }

    @After
    public void closeConnection() throws Exception {
        auctionHouse.disconnect();
        auctionServer.stop();
    }

    @Test
    public void receivesEventsFromAuctionServerAfterJoining() throws Exception {
        CountDownLatch auctionWasClosed = new CountDownLatch(1);

        Auction auction = auctionHouse.auctionFor(auctionServer.getItemId());
        auction.addAuctionEventListener(auctionClosedListener(auctionWasClosed));

        auction.join();
        auctionServer.hasReceivedJoinRequestFrom(ApplicationRunner.SNIPER_XMPP_ID);
        auctionServer.announceClosed();

        assertTrue(auctionWasClosed.await(2, TimeUnit.SECONDS));
    }

    private AuctionEventListener auctionClosedListener(CountDownLatch auctionWasClosed) {
        return new AuctionEventListener() {
            @Override
            public void auctionClosed() {
                auctionWasClosed.countDown();
            }

            @Override
            public void currentPrice(int price, int increment, PriceSource fromOtherBidder) {
                // not implemented
            }
        };
    }
}