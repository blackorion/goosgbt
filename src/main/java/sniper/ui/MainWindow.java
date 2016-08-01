package sniper.ui;

import sniper.Main;
import sniper.SniperSnapshot;
import sniper.SniperState;

import javax.swing.*;
import java.awt.*;

/**
 * @author Sergey Ivanov.
 */
public class MainWindow extends JFrame {
    public static final String STATUS_JOINING = "joining";
    public static final String STATUS_LOST = "lost";
    public static final String STATUS_BIDDING = "bidding";
    public static final String STATUS_WINNING = "winning";
    public static final String STATUS_HAS_WON = "won";
    private static final String SNIPERS_TABLE_NAME = "Snipers";

    private final SnipersTableModel snipers;

    public MainWindow(SnipersTableModel snipers) {
        super("Auction Sniper");
        this.snipers = snipers;
        setName(Main.MAIN_WINDOW_NAME);
        fillContentPane(makeSnipersTable());
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private JTable makeSnipersTable() {
        final JTable table = new JTable(snipers);
        table.setName(SNIPERS_TABLE_NAME);

        return table;
    }

    private void fillContentPane(JTable table) {
        final Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(new JScrollPane(table), BorderLayout.CENTER);
    }

    public void sniperStatusChanged(SniperSnapshot state) {
        snipers.sniperStateChanged(state);
    }
}
