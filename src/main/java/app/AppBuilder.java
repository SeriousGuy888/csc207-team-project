package app;


import javax.swing.*;
import java.awt.*;

public class AppBuilder {
    private final JPanel cardPanel = new JPanel();


    public AppBuilder() {
        CardLayout cardLayout = new CardLayout();
        cardPanel.setLayout(cardLayout);
    }


    public JFrame build() {
        final JFrame application = new JFrame("Jason's Extravagant Timetable Builder");
        application.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        application.add(cardPanel);

        return application;
    }
}