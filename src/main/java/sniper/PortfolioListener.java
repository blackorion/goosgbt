package sniper;

import java.util.EventListener;

/**
 * @author Sergey Ivanov.
 */
public interface PortfolioListener extends EventListener {
    void sniperAdded(AuctionSniper sniper);
}
