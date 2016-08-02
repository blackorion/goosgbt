package sniper.ui;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import sniper.AuctionSniper;
import sniper.SniperSnapshot;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
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
    public void setsSniperValuesInColumns() throws Exception {
        AuctionSniper sniper = createFakeSniper("item id");
        SniperSnapshot bidding = sniper.getSnapshot().bidding(555, 6666);

        model.addSniper(sniper);
        model.sniperStateChanged(bidding);

        assertRowMatchesSnapshot(0, bidding);
    }

    @Test
    public void firesStateToTable() throws Exception {
        AuctionSniper sniper = createFakeSniper("item id");
        SniperSnapshot bidding = sniper.getSnapshot().bidding(555, 666);

        model.addSniper(sniper);
        model.sniperStateChanged(bidding);

        final ArgumentCaptor<TableModelEvent> captor = ArgumentCaptor.forClass(TableModelEvent.class);
        verify(listener, atLeastOnce()).tableChanged(captor.capture());
        assertThat(captor.getValue(), samePropertyValuesAs(new TableModelEvent(model, 0)));
    }

    @Test
    public void notifiesListenersWhenAddingASniper() throws Exception {
        AuctionSniper sniper = createFakeSniper("item123");

        assertThat(model.getRowCount(), is(0));

        model.addSniper(sniper);

        assertThat(model.getRowCount(), is(1));
        assertRowMatchesSnapshot(0, sniper.getSnapshot());
    }

    @Test
    public void holdsSnipersInAdditionOrder() throws Exception {
        AuctionSniper sniper = createFakeSniper("item 0");
        AuctionSniper sniper2 = createFakeSniper("item 1");

        model.addSniper(sniper);
        model.addSniper(sniper2);

        assertEquals("item 0", cellValue(0, SnipersTableModel.Column.ITEM_IDENTIFIER));
        assertEquals("item 1", cellValue(1, SnipersTableModel.Column.ITEM_IDENTIFIER));
    }

    @Test
    public void updatesCorrectRowForSniper() throws Exception {
        AuctionSniper sniper = createFakeSniper("item 0");
        AuctionSniper sniper2 = createFakeSniper("item 1");

        SniperSnapshot bidding2 = sniper2.getSnapshot().bidding(1000, 12);
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

    private AuctionSniper createFakeSniper(String itemId) throws Exception {
        AuctionSniper sniper = mock(AuctionSniper.class);
        when(sniper.getSnapshot()).thenReturn(SniperSnapshot.joining(itemId));

        return sniper;
    }

}