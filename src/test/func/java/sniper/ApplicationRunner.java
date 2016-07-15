package sniper;

import sniper.doubles.FakeAuctionServer;

import static sniper.doubles.FakeAuctionServer.XMPP_HOSTNAME;

/**
 * @author Sergey Ivanov.
 */
public class ApplicationRunner {
    public static final String SNIPER_ID = "sniper";
    public static final String SNIPER_PASSWORD = "sniper";
    private static final String STATUS_JOINING = "joining";
    private static final String STATUS_LOST = "lost";
    private AuctionSniperDriver driver;

    public void startBiddingIn(FakeAuctionServer auction) {
        runApplicationMain(auction);
        initDriver();
    }

    private void runApplicationMain(final FakeAuctionServer auction) {
        Thread thread = new Thread("Test Application") {
            @Override
            public void run() {
                try {
                    Main.main(XMPP_HOSTNAME, SNIPER_ID, SNIPER_PASSWORD, auction.getItemId());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        thread.setDaemon(true);
        thread.start();
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
