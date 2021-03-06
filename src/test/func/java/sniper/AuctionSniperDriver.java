package sniper;

import com.objogate.wl.swing.AWTEventQueueProber;
import com.objogate.wl.swing.driver.JButtonDriver;
import com.objogate.wl.swing.driver.JFrameDriver;
import com.objogate.wl.swing.driver.JTableDriver;
import com.objogate.wl.swing.driver.JTextFieldDriver;
import com.objogate.wl.swing.gesture.GesturePerformer;
import org.hamcrest.Matcher;
import sniper.ui.MainWindow;

import javax.swing.*;
import java.awt.*;

import static com.objogate.wl.swing.matcher.IterableComponentsMatcher.matching;
import static com.objogate.wl.swing.matcher.JLabelTextMatcher.withLabelText;
import static java.lang.String.valueOf;

/**
 * @author Sergey Ivanov.
 */
public class AuctionSniperDriver extends JFrameDriver {

    public AuctionSniperDriver(int timeoutMillis) {
        super(new GesturePerformer(),
                JFrameDriver.topLevelFrame(named(MainWindow.MAIN_WINDOW_NAME), showingOnScreen()),
                new AWTEventQueueProber(timeoutMillis, 100));
    }

    public void showsSniperStatus(String itemId, int lastPrice, int lastBid, String statusText) {
        final JTableDriver table = new JTableDriver(this);
        table.hasRow(matches(itemId, lastPrice, lastBid, statusText));
    }

    private Matcher<Iterable<? extends Component>> matches(String itemId, int lastPrice, int lastBid, String statusText) {
        return matching(withLabelText(itemId), withLabelText(valueOf(lastPrice)),
                withLabelText(valueOf(lastBid)), withLabelText(statusText));
    }

    public void startBiddingFor(String itemId, int stopPrice) {
        updateTextFieldValue(MainWindow.NEW_ITEM_ID_NAME, itemId);
        updateTextFieldValue(MainWindow.NEW_ITEM_STOP_PRICE_NAME, String.valueOf(stopPrice));
        bidButton().click();
    }

    private void updateTextFieldValue(String fieldName, String itemId) {
        textField(fieldName).leftClickOnComponent();
        textField(fieldName).leftClickOnComponent();
        textField(fieldName).replaceAllText(itemId);
    }

    private JButtonDriver bidButton() {
        return new JButtonDriver(this, JButton.class, named(MainWindow.JOIN_BUTTON_NAME));
    }

    private JTextFieldDriver textField(String fieldName) {
        return new JTextFieldDriver(this, JTextField.class, named(fieldName));
    }
}
