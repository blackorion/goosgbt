package sniper.ui;

import sniper.Announcer;
import sniper.SniperPortfolio;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

/**
 * @author Sergey Ivanov.
 */
public class MainWindow extends JFrame {
    public static final String MAIN_WINDOW_NAME = "Auction Sniper Main";
    public static final String NEW_ITEM_ID_NAME = "item id";
    public static final String JOIN_BUTTON_NAME = "join";
    public static final String SNIPERS_TABLE_NAME = "Snipers";
    private final Announcer<UserRequestListener> userRequests = Announcer.to(UserRequestListener.class);

    public MainWindow(SniperPortfolio portfolio) {
        super("Auction Sniper");
        setName(MAIN_WINDOW_NAME);
        fillContentPane(makeSnipersTable(portfolio), makeControls());
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void fillContentPane(JComponent... panels) {
        final Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        Arrays.stream(panels).forEach((panel) -> contentPane.add(new JScrollPane(panel), BorderLayout.CENTER));
    }

    private JTable makeSnipersTable(SniperPortfolio portfolio) {
        SnipersTableModel model = new SnipersTableModel();
        portfolio.addPortfolioListener(model);
        final JTable table = new JTable(model);
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
