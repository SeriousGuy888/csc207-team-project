package use_case.autogen;

import entity.Timetable;

public class AutogenOutputData {
    private final   Timetable generatedTimetable;
    public AutogenOutputData(Timetable generatedTimetable){this.generatedTimetable = generatedTimetable;}
    public Timetable getGeneratedTimetable(){return generatedTimetable;}
}
