package sniper.ui;

import com.objogate.wl.swing.probe.ValueMatcherProbe;
import org.junit.Test;
import sniper.AuctionSniperDriver;
import sniper.Item;
import sniper.SniperPortfolio;

import static org.hamcrest.CoreMatchers.equalTo;

/**
 * @author Sergey Ivanov.
 */
public class MainWindowTest {

    private final MainWindow mainWindow = new MainWindow(new SniperPortfolio());
    private final AuctionSniperDriver driver = new AuctionSniperDriver(100);

    @Test
    public void makesUserRequestWhenJointButtonClicked() {
        ValueMatcherProbe<Item> buttonProbe = new ValueMatcherProbe<>(equalTo(new Item("my item-id", 789)), "join request");
        mainWindow.addUserRequestListener(buttonProbe::setReceivedValue);

        driver.startBiddingFor("my item-id", 789);

        driver.check(buttonProbe);
    }
}