package app;

import data_access.course_data.JsonCourseDataRepository;
import view.MainPanel;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        AppBuilder appBuilder = new AppBuilder();
        MainPanel mainPanel = new MainPanel();

        JFrame application = appBuilder
                .addMainPanel(mainPanel)
                .addCourseDataRepository(new JsonCourseDataRepository(CourseDataFilesToLoad.RESOURCE_NAMES_FOR_TESTING))
                .build();

        application.pack();
        application.setLocationRelativeTo(null);
        application.setExtendedState(Frame.MAXIMIZED_BOTH);
        application.setVisible(true);
    }
}