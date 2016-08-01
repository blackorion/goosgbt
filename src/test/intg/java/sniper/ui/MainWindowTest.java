package sniper.ui;

import com.objogate.wl.swing.probe.ValueMatcherProbe;
import org.junit.Test;
import sniper.AuctionSniperDriver;

import static org.hamcrest.CoreMatchers.equalTo;

/**
 * @author Sergey Ivanov.
 */
public class MainWindowTest {

    private final SnipersTableModel tableModel = new SnipersTableModel();
    private final MainWindow mainWindow = new MainWindow(tableModel);
    private final AuctionSniperDriver driver = new AuctionSniperDriver(100);

    @Test
    public void makesUserRequestWhenJointButtonClicked() {
        ValueMatcherProbe<String> buttonProbe = new ValueMatcherProbe<>(equalTo("my item-id"), "join request");
        mainWindow.addUserRequestListener(buttonProbe::setReceivedValue);

        driver.startBiddingFor("my item-id");

        driver.check(buttonProbe);
    }
}