package app;


import view.GUI.MainPanel;
import javax.swing.*;
import java.awt.*;

public class AppBuilder {
    private final JPanel cardPanel = new JPanel();
    private final CardLayout cardLayout = new CardLayout();

    public AppBuilder() {
        cardPanel.setLayout(cardLayout);
    }

    public void addMainPanel(MainPanel mainPanel) {
        cardPanel.add(mainPanel.getRootPanel(), "main");
        cardLayout.show(cardPanel, "main");
    }

    public JFrame build() {
        final JFrame application = new JFrame("Jason's Extravagant Timetable Builder");
        application.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        application.add(cardPanel);

        return application;
    }
}