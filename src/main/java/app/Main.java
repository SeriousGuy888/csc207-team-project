package app;

import java.awt.*;

import javax.swing.*;

public class Main {
    /**
     * Main method for running the application.
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        final AppBuilder appBuilder = new AppBuilder();
        final JFrame application = appBuilder
                .addMainPanel()
                .build();

        application.pack();
        application.setLocationRelativeTo(null);
        application.setExtendedState(Frame.MAXIMIZED_BOTH);
        application.setVisible(true);
    }
}