package sniper.xmpp;

import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import sniper.Auction;
import sniper.AuctionHouse;

/**
 * @author Sergey Ivanov.
 */
public class XMPPAuctionHouse implements AuctionHouse{
    private XMPPConnection connection;

    public XMPPAuctionHouse(XMPPConnection connection) {
        this.connection = connection;
    }

    @Override
    public Auction auctionFor(String itemId) {
        return new XMPPAuction(connection, itemId);
    }

    public static XMPPAuctionHouse connect(String host, String user, String pass) throws XMPPException {
        return new XMPPAuctionHouse(connection(host, user, pass));
    }

    private static XMPPConnection connection(String host, String user, String password) throws XMPPException {
        XMPPConnection connection = new XMPPConnection(host);
        connection.connect();
        connection.login(user, password);

        return connection;
    }

    public void disconnect() {
        connection.disconnect();
    }
}
