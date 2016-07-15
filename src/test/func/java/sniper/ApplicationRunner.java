package sniper;

import sniper.doubles.FakeAuctionServer;

import static sniper.doubles.FakeAuctionServer.XMPP_HOSTNAME;
import static sniper.ui.MainWindow.STATUS_JOINING;
import static sniper.ui.MainWindow.STATUS_LOST;

/**
 * @author Sergey Ivanov.
 */
public class ApplicationRunner {
    public static final String SNIPER_ID = "sniper";
    public static final String SNIPER_PASSWORD = "sniper";
    private AuctionSniperDriver driver;

    public void startBiddingIn(FakeAuctionServer auction) {
        runApplicationMain(auction);
        initDriver();
    }

    private void runApplicationMain(final FakeAuctionServer auction) {
        Thread thread = new Thread(mainAppRunnable(auction), "Test Application");

        thread.setDaemon(true);
        thread.start();
    }

    private Runnable mainAppRunnable(FakeAuctionServer auction) {
        return () -> {
            try {
                Main.main(XMPP_HOSTNAME, SNIPER_ID, SNIPER_PASSWORD, auction.getItemId());
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
    }

    private void initDriver() {
        driver = new AuctionSniperDriver(1000);
        driver.showsSniperStatus(STATUS_JOINING);
    }

    public void showsSniperHasLostAuction() {
        driver.showsSniperStatus(STATUS_LOST);
    }

    public void stop() {
        if (driver != null)
            driver.dispose();
    }
}