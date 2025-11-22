package use_case;

import entity.*;

import java.util.List;

public class TestConstants {
    public static final CourseOffering COURSE_OFFERING_MAT137 = new CourseOffering(
            "MAT237Y1-F-20259",
            new CourseCode("MAT137Y1"),
            "Pain and Agony",
            "two semesters of it"
    );
    public static final Timetable TIMETABLE_MAT137 = new Timetable();
    public static final Workbook WORKBOOK_MAT137 = new Workbook(List.of(TIMETABLE_MAT137));

    static {
        Section lec0101 = new Section(COURSE_OFFERING_MAT137, "LEC0101", Section.TeachingMethod.LECTURE);
        Section tut0101 = new Section(COURSE_OFFERING_MAT137, "TUT0101", Section.TeachingMethod.TUTORIAL);
        lec0101.addMeeting(new Meeting(
                new UofTLocation("MY", "150"),
                new WeeklyOccupancy(WeeklyOccupancy.DayOfTheWeek.THURSDAY,
                        1000 * 60 * 60 * 13,
                        1000 * 60 * 60 * 15)
        ));
        tut0101.addMeeting(new Meeting(
                new UofTLocation("MS", "4279"),
                new WeeklyOccupancy(WeeklyOccupancy.DayOfTheWeek.WEDNESDAY,
                        1000 * 60 * 60 * 11,
                        1000 * 60 * 60 * 12)
        ));
        COURSE_OFFERING_MAT137.addAvailableSection(lec0101);
        COURSE_OFFERING_MAT137.addAvailableSection(tut0101);

        TIMETABLE_MAT137.addSection(lec0101);
        TIMETABLE_MAT137.addSection(tut0101);
    }
}
