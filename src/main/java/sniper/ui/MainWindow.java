package sniper.ui;

import sniper.Announcer;
import sniper.Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

/**
 * @author Sergey Ivanov.
 */
public class MainWindow extends JFrame {
    public static final String STATUS_JOINING = "joining";
    public static final String STATUS_LOST = "lost";
    public static final String STATUS_BIDDING = "bidding";
    public static final String STATUS_WINNING = "winning";
    public static final String STATUS_HAS_WON = "won";
    public static final String NEW_ITEM_ID_NAME = "item id";
    public static final String JOIN_BUTTON_NAME = "join";
    public static final String SNIPERS_TABLE_NAME = "Snipers";
    private final Announcer<UserRequestListener> userRequests = Announcer.to(UserRequestListener.class);

    public MainWindow(SnipersTableModel snipers) {
        super("Auction Sniper");
        setName(Main.MAIN_WINDOW_NAME);
        fillContentPane(makeSnipersTable(snipers), makeControls());
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void fillContentPane(JComponent... panels) {
        final Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        Arrays.stream(panels).forEach((panel) -> contentPane.add(new JScrollPane(panel), BorderLayout.CENTER));
    }

    private JTable makeSnipersTable(SnipersTableModel snipers) {
        final JTable table = new JTable(snipers);
        table.setName(SNIPERS_TABLE_NAME);

        return table;
    }

    private JPanel makeControls() {
        JPanel controls = new JPanel(new FlowLayout());
        JTextField itemIdField = new JTextField();
        itemIdField.setColumns(25);
        itemIdField.setName(NEW_ITEM_ID_NAME);
        controls.add(itemIdField);

        JButton joinAuctionButton = new JButton("Join Auction");
        joinAuctionButton.setName(JOIN_BUTTON_NAME);
        joinAuctionButton.addActionListener(ev -> userRequests.announce().joinAuction(itemIdField.getText()));
        controls.add(joinAuctionButton);

        return controls;
    }

    public void addUserRequestListener(UserRequestListener listener) {
        userRequests.addListener(listener);
    }
}
