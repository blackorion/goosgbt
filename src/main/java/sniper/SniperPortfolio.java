package sniper;

import sniper.ui.SniperCollector;

/**
 * @author Sergey Ivanov.
 */
public class SniperPortfolio implements SniperCollector {
    private final Announcer<PortfolioListener> listeners = new Announcer<>(PortfolioListener.class);

    public void addPortfolioListener(PortfolioListener listener) {
        listeners.addListener(listener);
    }

    @Override
    public void addSniper(AuctionSniper sniper) {
        listeners.announce().sniperAdded(sniper);
    }
}
