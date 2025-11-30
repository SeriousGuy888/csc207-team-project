package data_access;

import java.util.ArrayList;

import entity.Timetable;
import entity.Workbook;
import use_case.WorkbookDataAccessInterface;

public class WorkbookDataAccessObject implements WorkbookDataAccessInterface {

    private Workbook currentWorkbook;

    /**
     * Constructor for WorkbookDataAccessObject.
     * Adds a default timetable to the workbook.
     */
    public WorkbookDataAccessObject() {
        // Initialize with a default empty workbook containing 1 timetable
        this.currentWorkbook = new Workbook(new ArrayList<>());
        this.currentWorkbook.addTimetable(new Timetable());
    }

    /**
     * Retrieves the workbook from memory.
     * @return the workbook.
     */
    @Override
    public Workbook getWorkbook() {
        return currentWorkbook;
    }

    /**
     * Saves the workbook to memory.
     * @param workbook the workbook to save.
     */
    @Override
    public void saveWorkbook(Workbook workbook) {
        // This saves the workbook in memory.
        // If this were a file DAO, we should write workbook into JSON and save to disk here.
        this.currentWorkbook = workbook;
    }
}
