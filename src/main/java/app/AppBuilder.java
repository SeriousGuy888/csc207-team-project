package app;


import data_access.SearchCoursesDataAccessObject;
import data_access.WorkbookDataAccessObject;
import data_access.course_data.CourseDataRepository;
import data_access.course_data.CourseDataRepositoryGrouped;
import data_access.course_data.JsonCourseDataRepository;
import data_access.workbook_persistence.FileWorkbookDataAccessObject;
import interface_adapter.GlobalViewController;
import interface_adapter.GlobalViewModel;
import interface_adapter.GlobalViewPresenter;
import interface_adapter.save_workbook.SaveWorkbookController;
import interface_adapter.save_workbook.SaveWorkbookPresenter;
import interface_adapter.save_workbook.SaveWorkbookViewModel;
import interface_adapter.search_courses.SearchCoursesController;
import interface_adapter.search_courses.SearchCoursesPresenter;
import interface_adapter.search_courses.SearchCoursesViewModel;
import use_case.save_workbook.SaveWorkbookInteractor;
import use_case.search_courses.SearchCoursesDataAccessInterface;
import use_case.search_courses.SearchCoursesInputBoundary;
import use_case.search_courses.SearchCoursesInteractor;
import use_case.search_courses.SearchCoursesOutputBoundary;
import use_case.tab_actions.add_tab.AddTabInteractor;
import use_case.tab_actions.delete_tab.DeleteTabInteractor;
import use_case.tab_actions.rename_tab.RenameTabInteractor;
import use_case.tab_actions.switch_tab.SwitchTabInteractor;
import view.MainPanel;
import view.SaveDialog;
import view.SearchPanel;

import javax.swing.*;
import java.awt.*;

public class AppBuilder {
    private final JPanel cardPanel = new JPanel();
    private final CardLayout cardLayout = new CardLayout();

    private MainPanel mainPanel;
    private GlobalViewModel globalViewModel;

    // Shared data access
    // todo: figure out why we can't use a shared interface here
    //  like why does it have to be a JsonCourseDataRepository and not one of the interfaces
    private JsonCourseDataRepository courseDataRepository;

    // Search courses components
    private SearchCoursesViewModel searchCoursesViewModel;
    private SearchCoursesController searchCoursesController;
    private SearchCoursesDataAccessInterface searchCoursesDataAccessObject;

    private FileWorkbookDataAccessObject workbookPersistenceDataAccessObject;
    private SaveWorkbookViewModel saveWorkbookViewModel;
    private SaveWorkbookPresenter saveWorkbookPresenter;
    private SaveWorkbookInteractor saveWorkbookInteractor;
    private SaveWorkbookController saveWorkbookController;

    public AppBuilder() {
        cardPanel.setLayout(cardLayout);

    }

    public AppBuilder initializeCourseRepository() {
        this.courseDataRepository = new JsonCourseDataRepository(CourseDataFilesToLoad.RESOURCE_NAMES_FOR_TESTING);
        return this;
    }

    /**
     * Wire the search courses use case.
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

    /**
     * Initializes workbook DAO, interface adapters, view models and view.
     *
     * @return this builder
     */
    public AppBuilder addMainPanel() {
        // 1. Create DAO
        final WorkbookDataAccessObject dataAccess = new WorkbookDataAccessObject();

        // 2. Create Panel and ViewModel
        final GlobalViewModel globalViewModel = new GlobalViewModel();
        final GlobalViewPresenter presenter = new GlobalViewPresenter(globalViewModel);

        // 3. Add Interactors
        final AddTabInteractor addTabInteractor = new AddTabInteractor(dataAccess, presenter);
        final DeleteTabInteractor removeTabInteractor = new DeleteTabInteractor(dataAccess, presenter);
        final SwitchTabInteractor switchTabInteractor = new SwitchTabInteractor(presenter);
        final RenameTabInteractor renameTabInteractor = new RenameTabInteractor(dataAccess, presenter);

        final GlobalViewController globalViewController = new GlobalViewController(
                addTabInteractor,
                removeTabInteractor,
                switchTabInteractor,
                renameTabInteractor
        );

        mainPanel = new MainPanel(globalViewModel, globalViewController);
        cardPanel.add(mainPanel.getRootPanel(), "main");
        presenter.prepareSuccessView(dataAccess.getWorkbook());
        cardLayout.show(cardPanel, "main");

        // Wire the SearchPanel with controller and viewModel
        final SearchPanel searchPanel = mainPanel.getSearchPanel();
        searchPanel.setSearchCoursesController(searchCoursesController);
        searchPanel.setSearchCoursesViewModel(searchCoursesViewModel);

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
        if (workbookPersistenceDataAccessObject == null) {
            throw new IllegalStateException(
                    "Save Workbook use case cannot be initialised"
                            + " before Workbook Persistence Data Access Object has been created."
            );
        }

        saveWorkbookViewModel = new SaveWorkbookViewModel();
        saveWorkbookPresenter = new SaveWorkbookPresenter(saveWorkbookViewModel);
        saveWorkbookInteractor = new SaveWorkbookInteractor(workbookPersistenceDataAccessObject, saveWorkbookPresenter);
        saveWorkbookController = new SaveWorkbookController(saveWorkbookInteractor);
        SaveDialog.createSingletonInstance(saveWorkbookViewModel, saveWorkbookController);

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