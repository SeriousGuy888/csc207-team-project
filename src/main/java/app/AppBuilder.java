package app;

import java.awt.*;

import javax.swing.*;

import data_access.WorkbookDataAccessObject;
import data_access.workbook_persistence.FileWorkbookDataAccessObject;
import interface_adapter.GlobalViewController;
import interface_adapter.GlobalViewModel;
import interface_adapter.GlobalViewPresenter;
import interface_adapter.save_workbook.SaveWorkbookController;
import interface_adapter.save_workbook.SaveWorkbookPresenter;
import interface_adapter.save_workbook.SaveWorkbookViewModel;
import use_case.save_workbook.SaveWorkbookInteractor;
import use_case.tab_actions.add_tab.AddTabInteractor;
import use_case.tab_actions.delete_tab.DeleteTabInteractor;
import use_case.tab_actions.rename_tab.RenameTabInteractor;
import use_case.tab_actions.switch_tab.SwitchTabInteractor;
import data_access.course_data.CourseDataRepository;
import view.MainPanel;
import view.SaveDialog;

public class AppBuilder {
    private final JPanel cardPanel = new JPanel();
    private final CardLayout cardLayout = new CardLayout();

    private MainPanel mainPanel;
    private GlobalViewModel globalViewModel;

    private CourseDataRepository courseDataRepository;

    private FileWorkbookDataAccessObject workbookPersistenceDataAccessObject;
    private SaveWorkbookViewModel saveWorkbookViewModel;
    private SaveWorkbookPresenter saveWorkbookPresenter;
    private SaveWorkbookInteractor saveWorkbookInteractor;
    private SaveWorkbookController saveWorkbookController;

    public AppBuilder() {
        cardPanel.setLayout(cardLayout);
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

        final MainPanel mainPanel = new MainPanel(globalViewModel, globalViewController);
        cardPanel.add(mainPanel.getRootPanel(), "main");
        presenter.prepareSuccessView(dataAccess.getWorkbook());
        cardLayout.show(cardPanel, "main");
        return this;
    }

    public AppBuilder addCourseDataRepository(CourseDataRepository repository) {
        this.courseDataRepository = repository;
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