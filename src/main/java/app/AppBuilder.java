package app;


import interface_adapter.GlobalViewModel;
import view.MainPanel;

import javax.swing.*;
import java.awt.*;

public class AppBuilder {
    private final JPanel cardPanel = new JPanel();
    private final CardLayout cardLayout = new CardLayout();

    private MainPanel mainPanel;
    private GlobalViewModel globalViewModel;


    public AppBuilder() {
        CardLayout cardLayout = new CardLayout();
        cardPanel.setLayout(cardLayout);
    }

    public AppBuilder addMainPanel() {
        globalViewModel = new GlobalViewModel();
        mainPanel = new MainPanel(globalViewModel);

        cardPanel.add(mainPanel.getRootPanel(), "main");
        cardLayout.show(cardPanel, "main");
        return this;
    }

    public JFrame build() {
        final JFrame application = new JFrame("Jason's Extravagant Timetable Builder");
        application.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        application.add(cardPanel);

        return application;
    }
}