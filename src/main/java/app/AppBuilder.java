package app;


import data_access.SearchCoursesDataAccessObject;
import interface_adapter.GlobalViewModel;
import view.MainPanel;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

import data_access.course_data.JsonCourseDataRepository;
import interface_adapter.search_courses.SearchCoursesController;
import interface_adapter.search_courses.SearchCoursesPresenter;
import interface_adapter.search_courses.SearchCoursesViewModel;
import use_case.search_courses.SearchCoursesDataAccessInterface;
import use_case.search_courses.SearchCoursesInteractor;
import use_case.search_courses.SearchCoursesInputBoundary;
import use_case.search_courses.SearchCoursesOutputBoundary;
import view.MainPanel;
import view.SearchPanel;

public class AppBuilder {
    private final JPanel cardPanel = new JPanel();
    private final CardLayout cardLayout = new CardLayout();

    // Shared data access
    private JsonCourseDataRepository courseDataRepository;

    // Search courses components
    private SearchCoursesViewModel searchCoursesViewModel;
    private SearchCoursesController searchCoursesController;
    private SearchCoursesDataAccessInterface searchCoursesDataAccessObject;

    private MainPanel mainPanel;
    private GlobalViewModel globalViewModel;

    public AppBuilder() {
        CardLayout cardLayout = new CardLayout();
        cardPanel.setLayout(cardLayout);

    }

    public AppBuilder initializeCourseRepository() {
        this.courseDataRepository= new JsonCourseDataRepository(
                Arrays.asList(
                        "courses/ABP.json",
                        "courses/ACM.json",
                        "courses/ACT.json",
                        "courses/AER.json"
                )
        );
        return this;
    }

    /**
     * Wire the search courses use case
     *
     */
    public AppBuilder addSearchCoursesUseCase() {
        // 1. Create ViewModel (holds state for the View)
        this.searchCoursesViewModel = new SearchCoursesViewModel();

        // 2. Create Presenter (implements OutputBoundary, updates ViewModel)
        SearchCoursesOutputBoundary searchCoursesPresenter =
                new SearchCoursesPresenter(searchCoursesViewModel);

        // 3. DAO
        this.searchCoursesDataAccessObject = new SearchCoursesDataAccessObject(this.courseDataRepository);

        // 4. Create Interactor (implements InputBoundary, contains business logic)
        SearchCoursesInputBoundary searchCoursesInteractor =
                new SearchCoursesInteractor(searchCoursesDataAccessObject, searchCoursesPresenter);

        // 5. Create Controller (receives input from View, calls Interactor)
        this.searchCoursesController = new SearchCoursesController(searchCoursesInteractor);

        return this;
    }

    public AppBuilder addMainPanel() {
        globalViewModel = new GlobalViewModel();
        mainPanel = new MainPanel(globalViewModel);

        // Wire the SearchPanel with controller and viewModel
        SearchPanel searchPanel = mainPanel.getSearchPanel();
        searchPanel.setSearchCoursesController(searchCoursesController);
        searchPanel.setSearchCoursesViewModel(searchCoursesViewModel);

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