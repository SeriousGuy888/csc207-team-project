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
import data_access.display_course_context.DisplayCourseDetailsDataAccessObject;
import interface_adapter.display_course_context.DisplayCourseDetailsController;
import interface_adapter.display_course_context.DisplayCourseDetailsPresenter;
import interface_adapter.display_course_context.DisplayCourseDetailsViewModel;
import use_case.display_course_context.DisplayCourseDetailsDataAccessInterface;
import use_case.display_course_context.DisplayCourseDetailsInputBoundary;
import use_case.display_course_context.DisplayCourseDetailsInteractor;
import use_case.display_course_context.DisplayCourseDetailsOutputBoundary;
import data_access.RateMyProfDataAccessObject;
import use_case.ratemyprof.RateMyProfDataAccessInterface;
import use_case.ratemyprof.RateMyProfInputBoundary;
import use_case.ratemyprof.RateMyProfInteractor;
import use_case.ratemyprof.RateMyProfOutputBoundary;
import use_case.ratemyprof.RateMyProfPresenter; // You need to define this simple class
import data_access.RateMyProfAPI;

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

    // Course Context Components
    private DisplayCourseDetailsViewModel displayCoursesViewModel;
    private DisplayCourseDetailsController displayCoursesController;

    // RMP components
    private RateMyProfInputBoundary rateMyProfInteractor;

    private MainPanel mainPanel;
    private GlobalViewModel globalViewModel;

    public AppBuilder() {
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

    public AppBuilder addDisplayCourseContextUseCase() {

        // 1. RATE MY PROF USE CASE SETUP (Dependency for Display Interactor)
        // NOTE: Replace MockRateMyProfAPI() with your real API if available
        RateMyProfAPI rmpFetcher = new RateMyProfAPI();
        RateMyProfDataAccessInterface rmpDAO = new RateMyProfDataAccessObject(rmpFetcher);

        // The Display Interactor uses the synchronous RMP method, so the RMP presenter
        // is often a minimal placeholder.
        RateMyProfOutputBoundary rmpPresenter = new RateMyProfPresenter();
        this.rateMyProfInteractor = new RateMyProfInteractor(rmpDAO, rmpPresenter);

        // 2. DISPLAY COURSE CONTEXT USE CASE SETUP
        this.displayCoursesViewModel = new DisplayCourseDetailsViewModel(); // Assume you've created this ViewModel

        // Create Presenter (implements OutputBoundary, updates Display ViewModel)
        DisplayCourseDetailsOutputBoundary displayPresenter =
                new DisplayCourseDetailsPresenter(displayCoursesViewModel); // Assume you've created this Presenter

        // Create DAO (uses the repository, which has professor name lookup)
        DisplayCourseDetailsDataAccessInterface displayDAO =
                new DisplayCourseDetailsDataAccessObject(this.courseDataRepository);

        // Create Interactor (injects DAO, Presenter, and the RMP Interactor)
        DisplayCourseDetailsInputBoundary displayInteractor =
                new DisplayCourseDetailsInteractor(displayDAO, displayPresenter, this.rateMyProfInteractor);

        // Create Controller
        this.displayCoursesController = new DisplayCourseDetailsController(displayInteractor);

        return this;
    }

    public AppBuilder addMainPanel() {
        globalViewModel = new GlobalViewModel();
        mainPanel = new MainPanel(globalViewModel);

        // Wire the SearchPanel with controller and viewModel
        SearchPanel searchPanel = mainPanel.getSearchPanel();
        searchPanel.setSearchCoursesController(searchCoursesController);
        searchPanel.setSearchCoursesViewModel(searchCoursesViewModel);

        searchPanel.setDisplayCoursesController(displayCoursesController);
        searchPanel.setDisplayCoursesViewModel(displayCoursesViewModel);

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