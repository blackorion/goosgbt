package sniper.ui;

import sniper.*;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class SnipersTableModel extends AbstractTableModel implements SniperListener, PortfolioListener {
    public static final String STATUS_JOINING = "joining";
    public static final String STATUS_LOST = "lost";
    public static final String STATUS_BIDDING = "bidding";
    public static final String STATUS_WINNING = "winning";
    public static final String STATUS_HAS_WON = "won";

    private List<SniperSnapshot> snapshots = new ArrayList<>();
    private final List<AuctionSniper> snipers = new ArrayList<>();

    @Override
    public int getRowCount() {
        return snapshots.size();
    }

    @Override
    public int getColumnCount() {
        return Column.values().length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        SniperSnapshot snapshot = snapshots.get(rowIndex);

        switch (Column.at(columnIndex)) {
            case ITEM_IDENTIFIER:
                return snapshot.itemId;
            case LAST_PRICE:
                return snapshot.price;
            case LAST_BID:
                return snapshot.bid;
            case SNIPER_STATE:
                return snapshot.state.toString();
            default:
                throw new IllegalArgumentException("No column at " + columnIndex);
        }
    }

    @Override
    public void sniperStateChanged(SniperSnapshot snapshot) {
        int row = rowMatching(snapshot);
        snapshots.set(row, snapshot);
        fireTableRowsUpdated(0, 0);
    }

    private int rowMatching(SniperSnapshot snapshot) {
        for (int i = 0; i < snapshots.size(); i++) {
            if (snapshot.isForSameItemAs(snapshots.get(i)))
                return i;
        }

        throw new IllegalArgumentException("Cannot find match for " + snapshot);
    }

    private void addSniperSnapshot(SniperSnapshot snapshot) {
        snapshots.add(snapshot);
        int row = snapshots.size() - 1;
        fireTableRowsInserted(row, row);
    }

    @Override
    public void sniperAdded(AuctionSniper sniper) {
        snipers.add(sniper);
        addSniperSnapshot(sniper.getSnapshot());
        sniper.addSniperListener(new SwingThreadSniperListener(this));
    }

    public enum Column {
        ITEM_IDENTIFIER,
        LAST_PRICE,
        LAST_BID,
        SNIPER_STATE;

        public static Column at(int offset) {
            return values()[offset];
        }
    }
}
