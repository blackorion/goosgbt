package sniper;

import sniper.ui.MainWindow;

import javax.swing.*;

/**
 * @author Sergey Ivanov.
 */
public class Main {
    public static final String MAIN_WINDOW_NAME = "Auction Sniper Main";
    public static final String SNIPER_STATUS_NAME = "sniper status";

    private MainWindow ui;

    public static void main(String... args) throws Exception {
        new Main();
    }

    public Main() throws Exception {
        startUserInterface();
    }

    private void startUserInterface() throws Exception {
        SwingUtilities.invokeAndWait(() -> ui = new MainWindow());
    }
}
