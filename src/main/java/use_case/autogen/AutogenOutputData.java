package use_case.autogen;

import entity.Timetable;

import java.util.Set;


public class AutogenOutputData {
    private final Timetable generatedTimetable;
    public AutogenOutputData(Timetable generatedTimetable){this.generatedTimetable = generatedTimetable;}
    public Timetable getGeneratedTimetable(){return generatedTimetable;}
}
