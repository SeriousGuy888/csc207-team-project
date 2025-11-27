package interface_adapter;

import entity.Meeting;

import java.util.*;

public class TimetableState {
    // 2D arrays representing the grid [TimeSlots][Days]
    // 24 half-hours (9am-9pm) x 5 days (Mon-Fri)
    private String[][][] firstSemesterTimetable;
    private String[][][] secondSemesterTimetable;
    private String error;

    public TimetableState() {
        // Initialize empty timetables for both semesters
        firstSemesterTimetable = new String[24][5][];
        secondSemesterTimetable = new String[24][5][];
        for (int i = 0; i < 24; i++) {
            for (int j = 0; j < 5; j++) {
                firstSemesterTimetable[i][j] = null;
                secondSemesterTimetable[i][j] = null;
            }
        }
    }

    public String[][][] getFirstSemesterTimetable() {
        return firstSemesterTimetable;
    }

    public String[][][] getSecondSemesterTimetable() {
        return secondSemesterTimetable;
    }

    public void setSecondSemesterTimetable(String[][][] secondSemesterTimetable) {
        this.secondSemesterTimetable = secondSemesterTimetable;
    }

    public void setFirstSemesterTimetable(String[][][] firstSemesterTimetable) {
        this.firstSemesterTimetable = firstSemesterTimetable;
    }



}
