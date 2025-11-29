package app;


import data_access.WorkbookDataAccessObject;
import interface_adapter.GlobalViewModel;
import interface_adapter.GlobalViewPresenter;
import use_case.tab_actions.add_tab.AddTabInteractor;
import use_case.tab_actions.delete_tab.DeleteTabInteractor;
import use_case.tab_actions.switch_tab.SwitchTabInteractor;
import view.MainPanel;

import javax.swing.*;
import java.awt.*;

public class AppBuilder {
    private final JPanel cardPanel = new JPanel();
    private final CardLayout cardLayout = new CardLayout();

    private MainPanel mainPanel;
    private GlobalViewModel globalViewModel;
    private WorkbookDataAccessObject dataAccess;

    public AppBuilder() {
        cardPanel.setLayout(cardLayout);
    }

    public AppBuilder addMainPanel() {
        // 1. Create DAO
        dataAccess = new WorkbookDataAccessObject();

        // 2. Create Panel and ViewModel
        globalViewModel = new GlobalViewModel();
        final GlobalViewPresenter presenter = new GlobalViewPresenter(globalViewModel);

        // 3. Add Interactors
        final AddTabInteractor addTabInteractor = new AddTabInteractor(dataAccess, presenter);
        final DeleteTabInteractor removeTabInteractor = new DeleteTabInteractor(dataAccess, presenter);
        final SwitchTabInteractor switchTabInteractor = new SwitchTabInteractor(presenter);

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