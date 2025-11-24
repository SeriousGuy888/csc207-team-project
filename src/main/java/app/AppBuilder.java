package app;


import data_access.course_data.CourseDataRepository;
import view.MainPanel;
import javax.swing.*;
import java.awt.*;

public class AppBuilder {
    private final JPanel cardPanel = new JPanel();
    private final CardLayout cardLayout = new CardLayout();

    private CourseDataRepository courseDataRepository;

    public AppBuilder() {
        cardPanel.setLayout(cardLayout);
    }

    public AppBuilder addMainPanel(MainPanel mainPanel) {
        cardPanel.add(mainPanel.getRootPanel(), "main");
        cardLayout.show(cardPanel, "main");
        return this;
    }

    public AppBuilder addCourseDataRepository(CourseDataRepository repository) {
        this.courseDataRepository = repository;
        return this;
    }

    public void addLoadUseCase() {

    }

    public JFrame build() {
        final JFrame application = new JFrame("Jason's Extravagant Timetable Builder");
        application.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        application.add(cardPanel);

        return application;
    }
}