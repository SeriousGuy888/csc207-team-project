package app;

import data_access.course_data.JsonCourseDataRepository;
import view.MainPanel;

import javax.swing.*;
import java.awt.*;

public class Main {
    /**
     * Main method for running the application.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        AppBuilder appBuilder = new AppBuilder();
        JFrame application = appBuilder
                .initializeCourseRepository()
                .addSearchCoursesUseCase()
                .addDisplayCourseContextUseCase()
                .addMainPanel()
                .build();

        application.pack();
        application.setLocationRelativeTo(null);
        application.setExtendedState(Frame.MAXIMIZED_BOTH);
        application.setVisible(true);
    }
}