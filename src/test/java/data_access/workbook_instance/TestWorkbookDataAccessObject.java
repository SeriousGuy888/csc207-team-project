package data_access.workbook_instance;

import entity.Workbook;
import use_case.WorkbookDataAccessInterface;

import java.util.ArrayList;

public class TestWorkbookDataAccessObject implements WorkbookDataAccessInterface {
    private Workbook currentWorkbook;

    public TestWorkbookDataAccessObject(Workbook startingWorkbook) {
        this.currentWorkbook = startingWorkbook;
    }

    public TestWorkbookDataAccessObject() {
        reset();
    }

    public void reset() {
        currentWorkbook = new Workbook(new ArrayList<>());
    }

    @Override
    public Workbook getWorkbook() {
        return currentWorkbook;
    }

    @Override
    public void saveWorkbook(Workbook workbook) {
        currentWorkbook = workbook;
    }
}
