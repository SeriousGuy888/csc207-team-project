package app;

import GUI.MainPanel;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        AppBuilder appBuilder = new AppBuilder();
        MainPanel mainPanel = new MainPanel();

        appBuilder.addMainPanel(mainPanel);

        JFrame application = appBuilder.build();
        application.pack();
        application.setLocationRelativeTo(null);
        application.setExtendedState(Frame.MAXIMIZED_BOTH);
        application.setVisible(true);
    }
}