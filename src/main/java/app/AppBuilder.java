package app;


import interface_adapter.GlobalViewModel;
import data_access.course_data.CourseDataRepository;
import view.MainPanel;
import view.SaveDialog;

import javax.swing.*;
import java.awt.*;

public class AppBuilder {
    private final JPanel cardPanel = new JPanel();
    private final CardLayout cardLayout = new CardLayout();

    private MainPanel mainPanel;
    private GlobalViewModel globalViewModel;

    private CourseDataRepository courseDataRepository;

    public AppBuilder() {
        cardPanel.setLayout(cardLayout);
    }

    public AppBuilder addSaveWorkbookUseCase() {
        SaveDialog saveDialog = new SaveDialog();

        return this;
    }

    public AppBuilder addMainPanel() {
        globalViewModel = new GlobalViewModel();
        mainPanel = new MainPanel(globalViewModel);

        cardPanel.add(mainPanel.getRootPanel(), "main");
        cardLayout.show(cardPanel, "main");
        return this;
    }

    public AppBuilder addCourseDataRepository(CourseDataRepository repository) {
        this.courseDataRepository = repository;
        return this;
    }

    public JFrame build() {
        final JFrame application = new JFrame("Jason's Extravagant Timetable Builder");
        application.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        application.add(cardPanel);

        return application;
    }
}