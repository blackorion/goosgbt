package sniper;

import sniper.doubles.FakeAuctionServer;
import sniper.ui.MainWindow;

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
        runApplicationMain();
        initDriver();
        join(auctions);
    }

    private void runApplicationMain() {
        Thread thread = new Thread(mainAppRunnable(), "Test Application");

        thread.setDaemon(true);
        thread.start();
    }

    private Runnable mainAppRunnable() {
        return () -> {
            try {
                Main.main(arguments());
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
    }

    private String[] arguments() {
        return new String[]{XMPP_HOSTNAME, SNIPER_ID, SNIPER_PASSWORD};
    }

    private void initDriver() {
        driver = new AuctionSniperDriver(1000);
    }

    private void join(FakeAuctionServer[] auctions) {
        for (FakeAuctionServer auction : auctions) {
            driver.startBiddingFor(auction.getItemId());
            driver.showsSniperStatus(auction.getItemId(), 0, 0, STATUS_JOINING);
        }
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
