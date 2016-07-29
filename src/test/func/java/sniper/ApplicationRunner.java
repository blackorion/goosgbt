package sniper;

import sniper.doubles.FakeAuctionServer;
import sniper.ui.MainWindow;

import java.util.stream.IntStream;

import static sniper.doubles.FakeAuctionServer.XMPP_HOSTNAME;
import static sniper.ui.MainWindow.STATUS_JOINING;
import static sniper.ui.MainWindow.STATUS_LOST;

/**
 * @author Sergey Ivanov.
 */
public class ApplicationRunner {
    public static final String SNIPER_ID = "sniper";
    public static final String SNIPER_PASSWORD = "sniper";
    public static final String SNIPER_XMPP_ID = "sniper@localhost/Smack";
    private AuctionSniperDriver driver;

    public void startBiddingIn(FakeAuctionServer... auctions) {
        runApplicationMain(auctions);
        initDriver();
        waitForSniperToJoin(auctions);
    }

    private void runApplicationMain(final FakeAuctionServer[] auction) {
        Thread thread = new Thread(mainAppRunnable(auction), "Test Application");

        thread.setDaemon(true);
        thread.start();
    }

    private Runnable mainAppRunnable(FakeAuctionServer[] auctions) {
        return () -> {
            try {
                Main.main(arguments(auctions));
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
    }

    private String[] arguments(FakeAuctionServer[] auctions) {
        final String[] arguments = new String[auctions.length + 3];
        arguments[0] = XMPP_HOSTNAME;
        arguments[1] = SNIPER_ID;
        arguments[2] = SNIPER_PASSWORD;

        for (int i = 0; i < auctions.length; i++)
            arguments[i + 3] = auctions[i].getItemId();

        return arguments;
    }

    private void initDriver() {
        driver = new AuctionSniperDriver(1000);
    }

    private void waitForSniperToJoin(FakeAuctionServer[] auctions) {
        for (FakeAuctionServer auction : auctions)
            driver.showsSniperStatus(auction.getItemId(), 0, 0, STATUS_JOINING);
    }

    public void stop() {
        if (driver != null)
            driver.dispose();
    }

    public void hasShownSniperIsBidding(FakeAuctionServer auction, int lastPrice, int lastBid) {
        driver.showsSniperStatus(auction.getItemId(), lastPrice, lastBid, MainWindow.STATUS_BIDDING);
    }

    public void showsSniperHasLostAuction(FakeAuctionServer auction, int price, int bid) {
        driver.showsSniperStatus(auction.getItemId(), price, bid, STATUS_LOST);
    }

    public void hasShownSniperIsWinning(FakeAuctionServer auction, int winningBid) {
        driver.showsSniperStatus(auction.getItemId(), winningBid, winningBid, MainWindow.STATUS_WINNING);
    }

    public void showsSniperHasWonAuction(FakeAuctionServer auction, int lastPrice) {
        driver.showsSniperStatus(auction.getItemId(), lastPrice, lastPrice, MainWindow.STATUS_HAS_WON);
    }
}
