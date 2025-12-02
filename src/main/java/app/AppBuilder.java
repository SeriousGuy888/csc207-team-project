package app;


import data_access.*;
import data_access.OsrmApiFetcher;
import data_access.SearchCoursesDataAccessObject;
import data_access.UofTWalkTimeDataAccessObject;
import data_access.WorkbookDataAccessObject;
import data_access.autogen.AutogenCourseDataAccess;
import data_access.course_data.CourseDataRepository;
import data_access.course_data.JsonCourseDataRepository;
import data_access.workbook_persistence.FileWorkbookDataAccessObject;
import interface_adapter.GlobalViewController;
import interface_adapter.GlobalViewModel;
import interface_adapter.GlobalViewPresenter;
import interface_adapter.autogen.AutogenController;
import interface_adapter.autogen.AutogenPresenter;
import interface_adapter.load_workbook.LoadWorkbookController;
import interface_adapter.load_workbook.LoadWorkbookPresenter;
import interface_adapter.load_workbook.LoadWorkbookViewModel;
import interface_adapter.save_workbook.SaveWorkbookController;
import interface_adapter.save_workbook.SaveWorkbookPresenter;
import interface_adapter.save_workbook.SaveWorkbookViewModel;
import interface_adapter.search_courses.SearchCoursesController;
import interface_adapter.search_courses.SearchCoursesPresenter;
import interface_adapter.search_courses.SearchCoursesViewModel;
import use_case.osrm_walktime.WalkTimeService;
import use_case.WorkbookDataAccessInterface;
import use_case.add_section.AddSectionInteractor;
import interface_adapter.add_section.AddSectionController;
import use_case.autogen.AutogenDataAccessInterface;
import use_case.autogen.AutogenInputBoundary;
import use_case.autogen.AutogenInteractor;
import use_case.autogen.AutogenOutputBoundary;
import use_case.load_workbook.LoadWorkbookInteractor;
import use_case.save_workbook.SaveWorkbookInteractor;
import use_case.search_courses.SearchCoursesDataAccessInterface;
import use_case.search_courses.SearchCoursesInputBoundary;
import use_case.search_courses.SearchCoursesInteractor;
import use_case.search_courses.SearchCoursesOutputBoundary;
import use_case.timetable_update.TimetableUpdateOutputBoundary;
import data_access.display_course_context.DisplayCourseDetailsDataAccessObject;
import interface_adapter.display_course_context.DisplayCourseDetailsController;
import interface_adapter.display_course_context.DisplayCourseDetailsPresenter;
import interface_adapter.display_course_context.DisplayCourseDetailsViewModel;

import use_case.remove_section.RemoveSectionInteractor;
import interface_adapter.remove_section.RemoveSectionController;

import use_case.display_course_context.DisplayCourseDetailsDataAccessInterface;
import use_case.display_course_context.DisplayCourseDetailsInputBoundary;
import use_case.display_course_context.DisplayCourseDetailsInteractor;
import use_case.display_course_context.DisplayCourseDetailsOutputBoundary;
import data_access.RateMyProfDataAccessObject;

import use_case.ratemyprof.RateMyProfDataAccessInterface;
import use_case.ratemyprof.RateMyProfInputBoundary;
import use_case.ratemyprof.RateMyProfInteractor;
import use_case.ratemyprof.RateMyProfOutputBoundary;
import use_case.ratemyprof.RateMyProfPresenter;
import data_access.RateMyProfAPI;

import use_case.tab_actions.add_tab.AddTabInteractor;
import use_case.tab_actions.delete_tab.DeleteTabInteractor;
import use_case.tab_actions.rename_tab.RenameTabInteractor;
import use_case.tab_actions.switch_tab.SwitchTabInteractor;
import use_case.timetable_update.TimetableUpdateOutputBoundary;
import view.LoadDialog;
import view.MainPanel;
import view.SaveDialog;
import view.SearchPanel;
import interface_adapter.locksections.LockSectionController;
import use_case.locksections.LockSectionInputBoundary;
import use_case.locksections.LockSectionInteractor;


import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class AppBuilder {
    private final JPanel cardPanel = new JPanel();
    private final CardLayout cardLayout = new CardLayout();

    private MainPanel mainPanel;

    // shared view and presenter
    private final GlobalViewModel globalViewModel = new GlobalViewModel();
    private final GlobalViewPresenter globalViewPresenter = new GlobalViewPresenter(this.globalViewModel);

    // shared object to store the workbook that the user is currently working on
    private WorkbookDataAccessObject workbookDataAccessObject;

    // Shared data access
    // todo: figure out why we can't use a shared interface here
    //  like why does it have to be a JsonCourseDataRepository and not one of the interfaces
    private JsonCourseDataRepository courseDataRepository;

    // Search courses components
    private SearchCoursesDataAccessObject searchCoursesDataAccessObject;
    private SearchCoursesViewModel searchCoursesViewModel;
    private SearchCoursesPresenter searchCoursesPresenter;
    private SearchCoursesInteractor searchCoursesInteractor;
    private SearchCoursesController searchCoursesController;

    // Course Context Components
    private DisplayCourseDetailsViewModel displayCoursesViewModel;
    private DisplayCourseDetailsController displayCoursesController;

    // RMP components
    private RateMyProfInputBoundary rateMyProfInteractor;

    // Add section components
    private AddSectionDataAccessObject addSectionDataAccessObject;
    private AddSectionInteractor addSectionInteractor;
    private AddSectionController addSectionController;

    // Remove section components
    private RemoveSectionDataAccessObject removeSectionDataAccessObject;
    private RemoveSectionInteractor removeSectionInteractor;
    private RemoveSectionController removeSectionController;

    // Save and Load Components
    private FileWorkbookDataAccessObject workbookPersistenceDataAccessObject;
    private SaveWorkbookViewModel saveWorkbookViewModel;
    private SaveWorkbookPresenter saveWorkbookPresenter;
    private SaveWorkbookInteractor saveWorkbookInteractor;
    private SaveWorkbookController saveWorkbookController;
    private AutogenController autogenController;
    private LoadWorkbookViewModel loadWorkbookViewModel;
    private LoadWorkbookPresenter loadWorkbookPresenter;
    private LoadWorkbookInteractor loadWorkbookInteractor;
    private LoadWorkbookController loadWorkbookController;

    public AppBuilder() {
        cardPanel.setLayout(cardLayout);

    }

    public AppBuilder initializeWorkbookDataAccessObject() {
        workbookDataAccessObject = new WorkbookDataAccessObject();
        return this;
    }

    public AppBuilder initializeCourseRepository() {
        this.courseDataRepository = new JsonCourseDataRepository(CourseDataFilesToLoad.RESOURCE_NAMES_FOR_TESTING);

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
        this.searchCoursesPresenter =
                new SearchCoursesPresenter(searchCoursesViewModel);

        // 3. DAO
        this.searchCoursesDataAccessObject = new SearchCoursesDataAccessObject(this.courseDataRepository);

        // 4. Create Interactor
        this.searchCoursesInteractor =
                new SearchCoursesInteractor(searchCoursesDataAccessObject, searchCoursesPresenter);

        // 5. Create Controller
        this.searchCoursesController = new SearchCoursesController(searchCoursesInteractor);

        return this;
    }

    public AppBuilder addDisplayCourseContextUseCase() {

        // 1. RATE MY PROF USE CASE SETUP (Dependency for Display Interactor)
        RateMyProfAPI rmpFetcher = new RateMyProfAPI();
        RateMyProfDataAccessInterface rmpDAO = new RateMyProfDataAccessObject(rmpFetcher);

        RateMyProfOutputBoundary rmpPresenter = new RateMyProfPresenter();
        this.rateMyProfInteractor = new RateMyProfInteractor(rmpDAO, rmpPresenter);

        // 2. DISPLAY COURSE CONTEXT USE CASE SETUP
        this.displayCoursesViewModel = new DisplayCourseDetailsViewModel();

        // Create Presenter (implements OutputBoundary, updates Display ViewModel)
        DisplayCourseDetailsOutputBoundary displayPresenter =
                new DisplayCourseDetailsPresenter(displayCoursesViewModel);

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


    /**
     * Initializes workbook DAO, interface adapters, view models and view.
     *
     * @return this builder
     */
    public AppBuilder addMainPanel() {
//        // 1. Create DAO
//        final WorkbookDataAccessObject dataAccess = new WorkbookDataAccessObject();
//
//        // 2. Create Panel and ViewModel
//        final GlobalViewModel globalViewModel = new GlobalViewModel();
//        final GlobalViewPresenter presenter = new GlobalViewPresenter(globalViewModel);

        // 3. Add Interactors
        final AddTabInteractor addTabInteractor = new AddTabInteractor(workbookDataAccessObject, globalViewPresenter);
        final DeleteTabInteractor removeTabInteractor = new DeleteTabInteractor(workbookDataAccessObject, globalViewPresenter);
        final SwitchTabInteractor switchTabInteractor = new SwitchTabInteractor(globalViewPresenter);
        final RenameTabInteractor renameTabInteractor = new RenameTabInteractor(workbookDataAccessObject, globalViewPresenter);

        // initiate walktime service.
        WalkTimeService walkTimeService;
        try {
            final OsrmApiFetcher apiFetcher = new OsrmApiFetcher();
            walkTimeService = new UofTWalkTimeDataAccessObject(apiFetcher);
        }
        catch (IOException e) {
            System.err.println("Warning: Could not load building codes. Walking times will be disabled.");

            // Fallback: Create a dummy service that always returns -1 (Error)
            // This allows the app to start even if the file is missing.
            walkTimeService = new WalkTimeService() {
                @Override
                public int calculateWalkingTime(String startCode, String endCode) {
                    return -1;
                }
            };
        }
        globalViewPresenter.setWalkTimeService(walkTimeService);

        final GlobalViewController globalViewController = new GlobalViewController(
                addTabInteractor,
                removeTabInteractor,
                switchTabInteractor,
                renameTabInteractor
        );

        mainPanel = new MainPanel(globalViewModel, globalViewController);
        cardPanel.add(mainPanel.getRootPanel(), "main");
        globalViewPresenter.prepareSuccessView(workbookDataAccessObject.getWorkbook());
        cardLayout.show(cardPanel, "main");

        // Wire the SearchPanel with controller and viewModel
        final SearchPanel searchPanel = mainPanel.getSearchPanel();
        searchPanel.setSearchCoursesController(searchCoursesController);
        searchPanel.setSearchCoursesViewModel(searchCoursesViewModel);
        searchPanel.setDisplayCoursesController(displayCoursesController);
        searchPanel.setDisplayCoursesViewModel(displayCoursesViewModel);
        searchPanel.setAddSectionController(addSectionController);
        searchPanel.setRemoveSectionController(removeSectionController);

        final AutogenDataAccessInterface autogenDao = new AutogenCourseDataAccess(courseDataRepository);
        final AutogenOutputBoundary autogenPresenter =
                new AutogenPresenter(workbookDataAccessObject, globalViewPresenter);
        final AutogenInputBoundary autogenInteractor =
                new AutogenInteractor(autogenDao, autogenPresenter);

        this.autogenController = new AutogenController(
                autogenInteractor,
                workbookDataAccessObject
        );

        mainPanel.setAutogenController(autogenController);

        LockSectionInputBoundary lockSectionInteractor =
                new LockSectionInteractor(
                        workbookDataAccessObject,
                        globalViewPresenter
                );

        LockSectionController lockSectionController =
                new LockSectionController(lockSectionInteractor);

        mainPanel.setLockSectionController(lockSectionController);

        return this;
    }

    public AppBuilder addAddSectionUseCase() {
        // Create DAO
        this.addSectionDataAccessObject = new AddSectionDataAccessObject(
                this.courseDataRepository,
                this.workbookDataAccessObject
        );

        this.addSectionInteractor = new AddSectionInteractor(
                addSectionDataAccessObject,
                globalViewPresenter
        );

        this.addSectionController = new AddSectionController(
                addSectionInteractor,
                globalViewModel);

        return this;
    }


    public AppBuilder addRemoveSectionUseCase() {
        // Create DAO
        this.removeSectionDataAccessObject = new RemoveSectionDataAccessObject(
                this.courseDataRepository,
                this.workbookDataAccessObject
        );

        this.removeSectionInteractor = new RemoveSectionInteractor(
                removeSectionDataAccessObject,
                globalViewPresenter
        );

        this.removeSectionController = new RemoveSectionController(
                removeSectionInteractor,
                globalViewModel);

        return this;
    }

    public AppBuilder addWorkbookPersistenceDataAccessObject() {
        if (courseDataRepository == null) {
            throw new IllegalStateException(
                    "Workbook Persistence Data Access Object cannot be created "
                            + " before Course Data Repository has been created."
            );
        }
        workbookPersistenceDataAccessObject = new FileWorkbookDataAccessObject(courseDataRepository);
        return this;
    }





    public AppBuilder addSaveWorkbookUseCase() {
        if (workbookDataAccessObject == null) {
            throw new IllegalStateException(
                    "Save Workbook use case cannot be initialised"
                            + " before Workbook Data Access Object has been created."
            );
        }
        if (workbookPersistenceDataAccessObject == null) {
            throw new IllegalStateException(
                    "Save Workbook use case cannot be initialised"
                            + " before Workbook Persistence Data Access Object has been created."
            );
        }

        saveWorkbookViewModel = new SaveWorkbookViewModel();
        saveWorkbookPresenter = new SaveWorkbookPresenter(saveWorkbookViewModel);
        saveWorkbookInteractor = new SaveWorkbookInteractor(
                workbookDataAccessObject,
                workbookPersistenceDataAccessObject,
                saveWorkbookPresenter);
        saveWorkbookController = new SaveWorkbookController(saveWorkbookInteractor);
        SaveDialog.createSingletonInstance(saveWorkbookViewModel, saveWorkbookController);

        return this;
    }

    public AppBuilder addLoadWorkbookUseCase() {
        if (workbookDataAccessObject == null) {
            throw new IllegalStateException(
                    "Load Workbook use case cannot be initialised"
                            + " before Workbook Data Access Object has been created."
            );
        }
        if (workbookPersistenceDataAccessObject == null) {
            throw new IllegalStateException(
                    "Load Workbook use case cannot be initialised"
                            + " before Workbook Persistence Data Access Object has been created."
            );
        }

        loadWorkbookViewModel = new LoadWorkbookViewModel();
        loadWorkbookPresenter = new LoadWorkbookPresenter(loadWorkbookViewModel);
        loadWorkbookInteractor = new LoadWorkbookInteractor(
                workbookDataAccessObject,
                workbookPersistenceDataAccessObject,
                loadWorkbookPresenter,
                globalViewPresenter);
        loadWorkbookController = new LoadWorkbookController(loadWorkbookInteractor);
        LoadDialog.createSingletonInstance(loadWorkbookViewModel, loadWorkbookController);

        return this;
    }

    /**
     * Builds the application.
     *
     * @return the application frame.
     */
    public JFrame build() {
        final JFrame application = new JFrame("Jason's Extravagant Timetable Builder");
        application.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        application.add(cardPanel);

        return application;
    }
}