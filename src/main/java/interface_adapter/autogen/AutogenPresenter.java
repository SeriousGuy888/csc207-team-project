package interface_adapter.autogen;

import entity.Timetable;
import entity.Workbook;
import use_case.WorkbookDataAccessInterface;
import use_case.autogen.AutogenOutputBoundary;
import use_case.autogen.AutogenOutputData;
import interface_adapter.GlobalViewPresenter;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class AutogenPresenter implements AutogenOutputBoundary {

    private final WorkbookDataAccessInterface workbookDao;
    private final GlobalViewPresenter globalViewPresenter;

    public AutogenPresenter(WorkbookDataAccessInterface workbookDao,
                            GlobalViewPresenter globalViewPresenter) {
        this.workbookDao = workbookDao;
        this.globalViewPresenter = globalViewPresenter;
    }

    @Override
    public void prepareSuccessView(AutogenOutputData outputData) {
        Workbook workbook = workbookDao.getWorkbook();
        Timetable generated = outputData.getGeneratedTimetable();

        String name = "Autogen " + (workbook.getTimetables().size() + 1);
        generated.setTimetableName(name);

        workbook.addTimetable(generated);
        workbookDao.saveWorkbook(workbook);

        // This will rebuild TimetableState list and
        // auto-select the last tab when size increased.
        globalViewPresenter.prepareSuccessView(workbook);
    }

    @Override
    public void prepareFailView(String errorMessage) {
        SwingUtilities.invokeLater(() ->
                JOptionPane.showMessageDialog(
                        null,
                        "Failed to generate timetable:\n" + errorMessage,
                        "Autogen Failed",
                        JOptionPane.ERROR_MESSAGE
                )
        );
    }
}
