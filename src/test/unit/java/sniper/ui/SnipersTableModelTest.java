package sniper.ui;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import sniper.SniperSnapshot;
import sniper.SniperState;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.samePropertyValuesAs;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Created by orion on 16.07.16.
 */
public class SnipersTableModelTest {

    private TableModelListener listener = mock(TableModelListener.class);
    private final SnipersTableModel model = new SnipersTableModel();

    @Before
    public void attachModelListener() throws Exception {
        model.addTableModelListener(listener);
    }

    @Test
    public void hasEnoughColumns() {
        assertThat(model.getColumnCount(), equalTo(SnipersTableModel.Column.values().length));
    }

    @Test
    public void setsSniperValuesInColumns() {
        SniperSnapshot joining = SniperSnapshot.joining("item id");
        SniperSnapshot bidding = joining.bidding(555, 6666);

        model.addSniper(joining);
        model.sniperStateChanged(bidding);

        assertRowMatchesSnapshot(0, bidding);
    }

    @Test
    public void firesStateToTable() {
        SniperSnapshot joining = SniperSnapshot.joining("item id");
        SniperSnapshot bidding = joining.bidding(555, 666);

        model.addSniper(joining);
        model.sniperStateChanged(bidding);

        final ArgumentCaptor<TableModelEvent> captor = ArgumentCaptor.forClass(TableModelEvent.class);
        verify(listener).tableChanged(captor.capture());
        assertThat(captor.getValue(), samePropertyValuesAs(new TableModelEvent(model, 0)));
    }

    @Test
    public void notifiesListenersWhenAddingASniper() {
        SniperSnapshot joining = SniperSnapshot.joining("item123");

        assertThat(model.getRowCount(), is(0));

        model.addSniper(joining);

        assertThat(model.getRowCount(), is(1));
        assertRowMatchesSnapshot(0, joining);
    }

    @Test
    public void holdsSnipersInAdditionOrder() {
        model.addSniper(SniperSnapshot.joining("item 0"));
        model.addSniper(SniperSnapshot.joining("item 1"));

        assertEquals("item 0", cellValue(0, SnipersTableModel.Column.ITEM_IDENTIFIER));
        assertEquals("item 1", cellValue(1, SnipersTableModel.Column.ITEM_IDENTIFIER));
    }

    @Test
    public void updatesCorrectRowForSniper() {
        SniperSnapshot sniper = SniperSnapshot.joining("item 0");
        SniperSnapshot sniper2 = SniperSnapshot.joining("item 1");
        SniperSnapshot bidding2 = sniper2.bidding(1000, 12);
        model.addSniper(sniper);
        model.addSniper(sniper2);

        model.sniperStateChanged(bidding2);

        assertRowMatchesSnapshot(1, bidding2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void throwsIllegalArgumentIfNoExistingSniperForAndUpdate() {
        model.sniperStateChanged(SniperSnapshot.joining("nonexistent item"));
    }

    private void assertRowMatchesSnapshot(int rowIndex, SniperSnapshot snapshot) {
        assertColumnEquals(snapshot.itemId, rowIndex, SnipersTableModel.Column.ITEM_IDENTIFIER);
        assertColumnEquals(snapshot.price, rowIndex, SnipersTableModel.Column.LAST_PRICE);
        assertColumnEquals(snapshot.bid, rowIndex, SnipersTableModel.Column.LAST_BID);
        assertColumnEquals(snapshot.state.toString(), rowIndex, SnipersTableModel.Column.SNIPER_STATE);
    }

    private void assertColumnEquals(Object expected, int row, SnipersTableModel.Column column) {
        assertEquals(expected, cellValue(row, column));
    }

    private Object cellValue(int rowIndex, SnipersTableModel.Column column) {
        return model.getValueAt(rowIndex, column.ordinal());
    }

}