package use_case.autogen;

import entity.CourseCode;
import entity.CourseOffering;
import entity.Meeting;
import entity.Section;
import entity.UofTLocation;
import entity.WeeklyOccupancy;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class AutogenInteractorTest {

    // ------------------------------------------------------------------------
    // Tests
    // ------------------------------------------------------------------------

    @Test
    void autogenWorksForTwoSimpleCourses() {
        AutogenDataAccessInterface dao = new FakeAutogenDataAccessTwoCourses();
        TestAutogenPresenter presenter = new TestAutogenPresenter();
        AutogenInteractor interactor = new AutogenInteractor(dao, presenter);

        // Block a time that doesn't intersect with any section (e.g., Sunday 00:00–01:00)
        WeeklyOccupancy nonConflictingBlockedTime = new WeeklyOccupancy(
                WeeklyOccupancy.DayOfTheWeek.SUNDAY,
                0,
                1000 * 60 * 60
        );

        AutogenInputData inputData = new AutogenInputData(
                Set.of(),          // locked sections
                Set.of(),          // (whatever your second Set is)
                nonConflictingBlockedTime
        );

        interactor.execute(inputData);

        System.out.println("\n*** ALWAYS PRINTING GENERATED TIMETABLE ***");
        if (presenter.lastOutput != null) {
            printTimetable(presenter.lastOutput.getGeneratedTimetable());
        } else {
            System.out.println("(No timetable generated)");
        }
        System.out.println("*** END PRINT ***\n");

        // Assertions happen *after* printing, so printing always runs
        assertNull(presenter.lastError,
                "Should not have an error for two simple courses");

        assertNotNull(presenter.lastOutput,
                "Should produce output data for two simple courses");

        assertEquals(2,
                presenter.lastOutput.getGeneratedTimetable().size(),
                "Expected 2 sections in the generated timetable");
    }


    @Test
    void autogenFailsWhenConstraintsImpossible() {
        AutogenDataAccessInterface dao = new FakeImpossibleAutogenDataAccess();
        TestAutogenPresenter presenter = new TestAutogenPresenter();
        AutogenInteractor interactor = new AutogenInteractor(dao, presenter);

        // The only section in the fake DAO will be Monday 10:00–12:00.
        // Here we block exactly that time so BlockedTimeConstraint makes it impossible.
        WeeklyOccupancy fullyBlockedTime = new WeeklyOccupancy(
                WeeklyOccupancy.DayOfTheWeek.MONDAY,
                1000 * 60 * 60 * 10,   // 10:00
                1000 * 60 * 60 * 12    // 12:00
        );

        AutogenInputData inputData = new AutogenInputData(
                Set.of(),
                Set.of(),
                fullyBlockedTime
        );

        interactor.execute(inputData);

        System.out.println("\n*** ALWAYS PRINTING RESULT ***");
        if (presenter.lastOutput != null) {
            printTimetable(presenter.lastOutput.getGeneratedTimetable());
        } else {
            System.out.println("(No timetable generated)");
        }
        System.out.println("*** END PRINT ***\n");

        assertNotNull(presenter.lastError,
                "Should give an error if constraints are impossible due to blocked times");

        assertNull(presenter.lastOutput,
                "Should not generate a timetable when constraints fail");
    }


    @Test
    void autogenAvoidsConflictingSections() {
        AutogenDataAccessInterface dao = new FakeAutogenDataAccessWithConflicts();
        TestAutogenPresenter presenter = new TestAutogenPresenter();
        AutogenInteractor interactor = new AutogenInteractor(dao, presenter);

        // Again, block a time that doesn't intersect any of the test sections,
        // so only the TimeConflictConstraint matters here.
        WeeklyOccupancy nonConflictingBlockedTime = new WeeklyOccupancy(
                WeeklyOccupancy.DayOfTheWeek.SUNDAY,
                0,
                1000 * 60 * 60
        );

        AutogenInputData inputData = new AutogenInputData(
                Set.of(),
                Set.of(),
                nonConflictingBlockedTime
        );

        interactor.execute(inputData);

        // Always print what we got
        System.out.println("\n*** RESULT FOR CONFLICT-AVOIDANCE TEST ***");
        if (presenter.lastOutput != null) {
            printTimetable(presenter.lastOutput.getGeneratedTimetable());
        } else {
            System.out.println("(No timetable generated)");
        }
        System.out.println("*** END RESULT ***\n");

        // Assertions
        assertNull(presenter.lastError, "Should not have an error for resolvable conflicts");
        assertNotNull(presenter.lastOutput, "Should generate a timetable");
        Set<Section> timetable = presenter.lastOutput.getGeneratedTimetable();
        assertEquals(2, timetable.size(), "Should pick 1 section per course");

        // ✅ Check that no two sections overlap in time,
        //    using Meeting.getTime().doesIntersect(...) directly.
        Section[] sections = timetable.toArray(new Section[0]);
        for (int i = 0; i < sections.length; i++) {
            for (int j = i + 1; j < sections.length; j++) {
                for (Meeting m1 : sections[i].getMeetings()) {
                    for (Meeting m2 : sections[j].getMeetings()) {
                        boolean intersects = m1.getTime().doesIntersect(m2.getTime());
                        assertFalse(intersects,
                                "Sections should not have overlapping meeting times:\n" +
                                        sections[i].getCourseOffering().getCourseCode() + " " + sections[i].getSectionName() +
                                        " vs " +
                                        sections[j].getCourseOffering().getCourseCode() + " " + sections[j].getSectionName());
                    }
                }
            }
        }
    }

    // ------------------------------------------------------------------------
    // Helper: print timetable
    // ------------------------------------------------------------------------
    private void printTimetable(Set<Section> timetable) {
        System.out.println("=========== GENERATED TIMETABLE ===========");
        for (Section s : timetable) {
            System.out.println(
                    s.getCourseOffering().getCourseCode() + " " +
                            s.getSectionName() + " (" + s.getTeachingMethod() + ")"
            );
            for (Meeting m : s.getMeetings()) {
                System.out.println("    Meeting at " + m.getLocation()
                        + " | WeeklyOccupancy object: " + m.getTime());
            }
        }
        System.out.println("===========================================");
    }


    // ------------------------------------------------------------------------
    // Presenter
    // ------------------------------------------------------------------------
    private static class TestAutogenPresenter implements AutogenOutputBoundary {
        AutogenOutputData lastOutput;
        String lastError;

        @Override
        public void prepareSuccessView(AutogenOutputData outputData) {
            this.lastOutput = outputData;
            this.lastError = null;
        }

        @Override
        public void prepareFailView(String errorMessage) {
            this.lastError = errorMessage;
            this.lastOutput = null;
        }
    }


    // ------------------------------------------------------------------------
    // Fake DAOs
    // ------------------------------------------------------------------------

    private static class FakeAutogenDataAccessTwoCourses implements AutogenDataAccessInterface {

        @Override
        public List<CourseOffering> getSelectedCourseOfferings(AutogenInputData inputData) {
            // Course 1: MAT237
            CourseOffering mat237 = new CourseOffering(
                    new CourseCode("MAT237Y1"),
                    "Pain and Agony",
                    "two semesters of it"
            );
            Section mat237Lec0101 = new Section(mat237, "LEC0101", Section.TeachingMethod.LECTURE);
            mat237Lec0101.addMeeting(new Meeting(
                    new UofTLocation("MY", "150"),
                    new WeeklyOccupancy(
                            WeeklyOccupancy.DayOfTheWeek.THURSDAY,
                            1000 * 60 * 60 * 13,   // 13:00
                            1000 * 60 * 60 * 15    // 15:00
                    )
            ));
            mat237.addAvailableSection(mat237Lec0101);

            // Course 2: CSC207
            CourseOffering csc207 = new CourseOffering(
                    new CourseCode("CSC207H1"),
                    "Software Design",
                    "pain but with Java"
            );
            Section csc207Lec0101 = new Section(csc207, "LEC0101", Section.TeachingMethod.LECTURE);
            csc207Lec0101.addMeeting(new Meeting(
                    new UofTLocation("BA", "2175"),
                    new WeeklyOccupancy(
                            WeeklyOccupancy.DayOfTheWeek.MONDAY,
                            1000 * 60 * 60 * 10,   // 10:00
                            1000 * 60 * 60 * 11    // 11:00
                    )
            ));
            csc207.addAvailableSection(csc207Lec0101);

            return List.of(mat237, csc207);
        }
    }


    private static class FakeImpossibleAutogenDataAccess implements AutogenDataAccessInterface {

        @Override
        public List<CourseOffering> getSelectedCourseOfferings(AutogenInputData inputData) {
            // Single course with a single section that is fully blocked by the input's blockedTimes
            CourseOffering mat237 = new CourseOffering(
                    new CourseCode("MAT237Y1"),
                    "Pain and Agony",
                    "two semesters of it"
            );
            Section lec0101 = new Section(mat237, "LEC0101", Section.TeachingMethod.LECTURE);
            lec0101.addMeeting(new Meeting(
                    new UofTLocation("MY", "150"),
                    new WeeklyOccupancy(
                            WeeklyOccupancy.DayOfTheWeek.MONDAY,
                            1000 * 60 * 60 * 10,   // 10:00
                            1000 * 60 * 60 * 12    // 12:00
                    )
            ));
            mat237.addAvailableSection(lec0101);

            return List.of(mat237);
        }
    }


    /**
     * Fake DAO with multiple courses whose some sections conflict in time.
     * The real TimeConflictConstraint in the interactor should force the DFS
     * to choose a non-conflicting combination.
     */
    private static class FakeAutogenDataAccessWithConflicts implements AutogenDataAccessInterface {

        @Override
        public List<CourseOffering> getSelectedCourseOfferings(AutogenInputData inputData) {
            // ----- Course 1: MAT237 with two sections -----
            CourseOffering mat237 = new CourseOffering(
                    new CourseCode("MAT237Y1"),
                    "Pain and Agony",
                    "two semesters of it"
            );

            // Section A1: Monday 10:00–12:00
            Section mat237A1 = new Section(
                    mat237,
                    "LEC0101",
                    Section.TeachingMethod.LECTURE
            );
            mat237A1.addMeeting(new Meeting(
                    new UofTLocation("MY", "150"),
                    new WeeklyOccupancy(
                            WeeklyOccupancy.DayOfTheWeek.MONDAY,
                            1000 * 60 * 60 * 10,   // 10:00
                            1000 * 60 * 60 * 12    // 12:00
                    )
            ));

            // Section A2: Monday 14:00–16:00
            Section mat237A2 = new Section(
                    mat237,
                    "LEC0201",
                    Section.TeachingMethod.LECTURE
            );
            mat237A2.addMeeting(new Meeting(
                    new UofTLocation("MY", "150"),
                    new WeeklyOccupancy(
                            WeeklyOccupancy.DayOfTheWeek.MONDAY,
                            1000 * 60 * 60 * 14,   // 14:00
                            1000 * 60 * 60 * 16    // 16:00
                    )
            ));

            mat237.addAvailableSection(mat237A1);
            mat237.addAvailableSection(mat237A2);

            // ----- Course 2: CSC207 with two sections -----
            CourseOffering csc207 = new CourseOffering(
                    new CourseCode("CSC207H1"),
                    "Software Design",
                    "pain but with Java"
            );

            // Section B1: Monday 10:00–12:00 (conflicts with A1)
            Section csc207B1 = new Section(
                    csc207,
                    "LEC0101",
                    Section.TeachingMethod.LECTURE
            );
            csc207B1.addMeeting(new Meeting(
                    new UofTLocation("BA", "2175"),
                    new WeeklyOccupancy(
                            WeeklyOccupancy.DayOfTheWeek.MONDAY,
                            1000 * 60 * 60 * 10,   // 10:00
                            1000 * 60 * 60 * 12    // 12:00
                    )
            ));

            // Section B2: Monday 12:00–14:00 (non-conflicting with A1 and A2)
            Section csc207B2 = new Section(
                    csc207,
                    "LEC0201",
                    Section.TeachingMethod.LECTURE
            );
            csc207B2.addMeeting(new Meeting(
                    new UofTLocation("BA", "2175"),
                    new WeeklyOccupancy(
                            WeeklyOccupancy.DayOfTheWeek.MONDAY,
                            1000 * 60 * 60 * 12,   // 12:00
                            1000 * 60 * 60 * 14    // 14:00
                    )
            ));

            csc207.addAvailableSection(csc207B1);
            csc207.addAvailableSection(csc207B2);

            // Return both offerings; the interactor will build CourseVariables from them.
            return List.of(mat237, csc207);
        }
    }
}
