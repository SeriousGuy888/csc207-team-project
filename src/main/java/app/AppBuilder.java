package app;

import java.awt.*;

import javax.swing.*;

import data_access.WorkbookDataAccessObject;
import interface_adapter.GlobalViewController;
import interface_adapter.GlobalViewModel;
import interface_adapter.GlobalViewPresenter;
import use_case.tab_actions.add_tab.AddTabInteractor;
import use_case.tab_actions.delete_tab.DeleteTabInteractor;
import use_case.tab_actions.rename_tab.RenameTabInteractor;
import use_case.tab_actions.switch_tab.SwitchTabInteractor;
import data_access.course_data.CourseDataRepository;
import view.MainPanel;

public class AppBuilder {
    private final JPanel cardPanel = new JPanel();
    private final CardLayout cardLayout = new CardLayout();

    private MainPanel mainPanel;
    private GlobalViewModel globalViewModel;

    private CourseDataRepository courseDataRepository;

    public AppBuilder() {
        cardPanel.setLayout(cardLayout);
    }

    /**
     * Initializes workbook DAO, interface adapters, view models and view.
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

    /**
     * Builds the application.
     * @return the application frame.
     */
    public JFrame build() {
        final JFrame application = new JFrame("Jason's Extravagant Timetable Builder");
        application.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        application.add(cardPanel);

        return application;
    }
}