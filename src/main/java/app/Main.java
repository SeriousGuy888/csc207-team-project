package app;

import data_access.course_data.JsonCourseDataRepository;
import view.MainPanel;

import javax.swing.*;
import java.awt.*;

public class Main {
    /**
     * Main method for running the application.
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        final AppBuilder appBuilder = new AppBuilder();
        final JFrame application = appBuilder
                .addMainPanel()
                .addCourseDataRepository(new JsonCourseDataRepository(CourseDataFilesToLoad.RESOURCE_NAMES_FOR_TESTING))
                .addWorkbookPersistenceDataAccessObject()
                .addSaveWorkbookUseCase()
                .build();

        application.pack();
        application.setLocationRelativeTo(null);
        application.setExtendedState(Frame.MAXIMIZED_BOTH);
        application.setVisible(true);
    }
}