package sniper;

import java.util.EventListener;

/**
 * @author Sergey Ivanov.
 */
public interface SniperListener extends EventListener {
    void sniperLost();

    void sniperWon();

    void sniperStateChanged(SniperSnapshot sniperSnapshot);
}
