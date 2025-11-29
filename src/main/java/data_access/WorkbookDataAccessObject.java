package data_access;

import entity.Timetable;
import entity.Workbook;
import use_case.WorkbookDataAccessInterface;

import java.util.ArrayList;

public class WorkbookDataAccessObject implements WorkbookDataAccessInterface{
    private Workbook currentWorkbook;

    public WorkbookDataAccessObject() {
        // Initialize with a default empty workbook containing 1 timetable
        this.currentWorkbook = new Workbook(new ArrayList<>());
        this.currentWorkbook.addTimetable(new Timetable());
    }

    @Override
    public Workbook getWorkbook() {
        return currentWorkbook;
    }

    @Override
    public void saveWorkbook(Workbook workbook) {
        // For in-memory, "saving" just means updating our reference.
        // If this were a file DAO, you would write the JSON to disk here.
        this.currentWorkbook = workbook;
        System.out.println("DAO: Workbook saved in memory.");
    }
}
