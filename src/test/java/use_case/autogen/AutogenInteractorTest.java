package use_case.autogen;

import entity.*;
import org.junit.jupiter.api.Test;
import data_access.autogen.AutogenCourseDataAccess;
import data_access.course_data.JsonCourseDataRepository;
import java.util.List;
import java.util.Set;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class AutogenInteractorTest {

    private static final int MILLIS_PER_HOUR = 1000 * 60 * 60;

    private static final WeeklyOccupancy SUNDAY_00_01 = new WeeklyOccupancy(
            WeeklyOccupancy.DayOfTheWeek.SUNDAY,
            0,
            1 * MILLIS_PER_HOUR
    );

    private static final WeeklyOccupancy MONDAY_10_12 = new WeeklyOccupancy(
            WeeklyOccupancy.DayOfTheWeek.MONDAY,
            10 * MILLIS_PER_HOUR,
            12 * MILLIS_PER_HOUR
    );

    private static final WeeklyOccupancy MONDAY_10_11 = new WeeklyOccupancy(
            WeeklyOccupancy.DayOfTheWeek.MONDAY,
            10 * MILLIS_PER_HOUR,
            11 * MILLIS_PER_HOUR
    );

    private static final WeeklyOccupancy MONDAY_12_14 = new WeeklyOccupancy(
            WeeklyOccupancy.DayOfTheWeek.MONDAY,
            12 * MILLIS_PER_HOUR,
            14 * MILLIS_PER_HOUR
    );

    private static final WeeklyOccupancy MONDAY_14_16 = new WeeklyOccupancy(
            WeeklyOccupancy.DayOfTheWeek.MONDAY,
            14 * MILLIS_PER_HOUR,
            16 * MILLIS_PER_HOUR
    );

    private static final WeeklyOccupancy THURSDAY_13_15 = new WeeklyOccupancy(
            WeeklyOccupancy.DayOfTheWeek.THURSDAY,
            13 * MILLIS_PER_HOUR,
            15 * MILLIS_PER_HOUR
    );

    private static final Meeting.Semester FIRST_SEMESTER = Meeting.Semester.FIRST;
    private static final Meeting.Semester SECOND_SEMESTER = Meeting.Semester.SECOND;

    // ------------------------------------------------------------------------
    // Real DAO tests
    // ------------------------------------------------------------------------

    @Test
    void autogenWorksWithRealDaoForOneCourse() {
        JsonCourseDataRepository courseRepo = new JsonCourseDataRepository(
                List.of(
                        "courses/ABP.json"
                        // , "courses/mat.json", ...
                )
        );

        AutogenDataAccessInterface dao = new AutogenCourseDataAccess(courseRepo);
        TestAutogenPresenter presenter = new TestAutogenPresenter();
        AutogenInteractor interactor = new AutogenInteractor(dao, presenter);

        CourseCode selected = new CourseCode("ABP104Y1");  // change if needed

        WeeklyOccupancy nonConflictingBlockedTime = SUNDAY_00_01;

        AutogenInputData inputData = new AutogenInputData(
                Set.of(selected),
                Set.of(),
                nonConflictingBlockedTime
        );

        interactor.execute(inputData);

        System.out.println("\n*** RESULT FOR REAL DAO + INTERACTOR TEST ***");
        if (presenter.lastOutput != null) {
            printTimetable(presenter.lastOutput.getGeneratedTimetable());
        } else {
            System.out.println("(No timetable generated)");
        }
        System.out.println("*** END RESULT ***\n");

        assertNull(presenter.lastError,
                "Should not have an error when using the real DAO for a valid course");

        assertNotNull(presenter.lastOutput,
                "Should generate a timetable using the real DAO");

        Timetable timetable = presenter.lastOutput.getGeneratedTimetable();
        assertFalse(timetable.getSections().isEmpty(),
                "Generated timetable should contain at least one section");

        timetable.getSections().forEach(section ->
                assertEquals(selected, section.getCourseOffering().getCourseCode(),
                        "All sections in the timetable should be for the selected course")
        );
    }

    // ------------------------------------------------------------------------
    // Fake-DAO-based unit tests
    // ------------------------------------------------------------------------

    @Test
    void autogenWorksForTwoSimpleCourses() {
        AutogenDataAccessInterface dao = new FakeAutogenDataAccessTwoCourses();
        TestAutogenPresenter presenter = new TestAutogenPresenter();
        AutogenInteractor interactor = new AutogenInteractor(dao, presenter);

        WeeklyOccupancy nonConflictingBlockedTime = SUNDAY_00_01;

        AutogenInputData inputData = new AutogenInputData(
                Set.of(),
                Set.of(),
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

        assertNull(presenter.lastError,
                "Should not have an error for two simple courses");

        assertNotNull(presenter.lastOutput,
                "Should produce output data for two simple courses");

        assertEquals(2,
                presenter.lastOutput.getGeneratedTimetable().getSections().size(),
                "Expected 2 sections in the generated timetable");
    }

    @Test
    void autogenFailsWhenConstraintsImpossible() {
        AutogenDataAccessInterface dao = new FakeImpossibleAutogenDataAccess();
        TestAutogenPresenter presenter = new TestAutogenPresenter();
        AutogenInteractor interactor = new AutogenInteractor(dao, presenter);

        WeeklyOccupancy fullyBlockedTime = MONDAY_10_12;

        AutogenInputData inputData = new AutogenInputData(
                Set.of(),
                Set.of(),
                fullyBlockedTime
        );

        interactor.execute(inputData);

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

        WeeklyOccupancy nonConflictingBlockedTime = SUNDAY_00_01;

        AutogenInputData inputData = new AutogenInputData(
                Set.of(),
                Set.of(),
                nonConflictingBlockedTime
        );

        interactor.execute(inputData);

        System.out.println("\n*** RESULT FOR CONFLICT-AVOIDANCE TEST ***");
        if (presenter.lastOutput != null) {
            printTimetable(presenter.lastOutput.getGeneratedTimetable());
        } else {
            System.out.println("(No timetable generated)");
        }
        System.out.println("*** END RESULT ***\n");

        assertNull(presenter.lastError, "Should not have an error for resolvable conflicts");
        assertNotNull(presenter.lastOutput, "Should generate a timetable");
        Set<Section> timetable = presenter.lastOutput.getGeneratedTimetable().getSections();
        assertEquals(2, timetable.size(), "Should pick 1 section per course");

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

    @Test
    void autogenWorksForDifferentSemesters() {
        AutogenDataAccessInterface dao = new FakeAutogenDataAccessDifferentSemesters();
        TestAutogenPresenter presenter = new TestAutogenPresenter();
        AutogenInteractor interactor = new AutogenInteractor(dao, presenter);

        WeeklyOccupancy nonConflictingBlockedTime = SUNDAY_00_01;

        AutogenInputData inputData = new AutogenInputData(
                Set.of(),
                Set.of(),
                nonConflictingBlockedTime
        );

        interactor.execute(inputData);

        System.out.println("\n*** COURSES IN DIFFERENT SEMESTERS ***");
        if (presenter.lastOutput != null) {
            printTimetable(presenter.lastOutput.getGeneratedTimetable());
        } else {
            System.out.println("(No timetable generated)");
        }
        System.out.println("*** END PRINT ***\n");

        assertNull(presenter.lastError,
                "Should not have an error for two courses in diff semesters");

        assertNotNull(presenter.lastOutput,
                "Should produce output data for two simple courses");

        assertEquals(2,
                presenter.lastOutput.getGeneratedTimetable().getSections().size(),
                "Expected 2 sections in the generated timetable");
    }

    @Test
    void autogenRespectsLockedSectionsAndNullBlockedTimes() {
        // Build a single course offering with two sections
        CourseOffering csc207 = new CourseOffering(
                "CSC207H1",
                new CourseCode("CSC207H1"),
                "Software Design",
                "pain but with Java"
        );

        Section lec0101 = new Section(
                csc207,
                "LEC0101",
                Section.TeachingMethod.LECTURE
        );
        lec0101.addMeeting(new Meeting(
                new UofTLocation("BA", "2175"), FIRST_SEMESTER,
                MONDAY_10_12
        ));

        Section lec0201 = new Section(
                csc207,
                "LEC0201",
                Section.TeachingMethod.LECTURE
        );
        lec0201.addMeeting(new Meeting(
                new UofTLocation("BA", "2175"), FIRST_SEMESTER,
                THURSDAY_13_15
        ));

        csc207.addAvailableSection(lec0101);
        csc207.addAvailableSection(lec0201);

        // DAO that always returns this offering
        AutogenDataAccessInterface dao = new AutogenDataAccessInterface() {
            @Override
            public List<CourseOffering> getSelectedCourseOfferings(Set<CourseCode> selectedCourses) {
                return List.of(csc207);
            }
        };

        TestAutogenPresenter presenter = new TestAutogenPresenter();
        AutogenInteractor interactor = new AutogenInteractor(dao, presenter);

        // Lock one specific section for this course
        Set<Section> lockedSections = Set.of(lec0201);

        AutogenInputData inputData = new AutogenInputData(
                Set.of(csc207.getCourseCode()),
                lockedSections,
                null          // blockedTimes == null branch in buildConstraints
        );

        interactor.execute(inputData);

        assertNull(presenter.lastError, "Should succeed when locked section is consistent");
        assertNotNull(presenter.lastOutput, "Should generate a timetable");

        Timetable timetable = presenter.lastOutput.getGeneratedTimetable();
        Set<Section> resultSections = timetable.getSections();

        // Only the locked section should appear in the timetable
        assertEquals(1, resultSections.size(), "Exactly one section should be chosen");
        assertTrue(resultSections.contains(lec0201),
                "Generated timetable should contain the locked section");
    }

    @Test
    void autogenHandlesDaoExceptionGracefully() {
        AutogenDataAccessInterface dao = new AutogenDataAccessInterface() {
            @Override
            public List<CourseOffering> getSelectedCourseOfferings(Set<CourseCode> selectedCourses) {
                throw new RuntimeException("DAO exploded");
            }
        };

        TestAutogenPresenter presenter = new TestAutogenPresenter();
        AutogenInteractor interactor = new AutogenInteractor(dao, presenter);

        AutogenInputData inputData = new AutogenInputData(
                Set.of(),
                Set.of(),
                SUNDAY_00_01
        );

        interactor.execute(inputData);

        assertNull(presenter.lastOutput,
                "No timetable should be produced when an exception occurs");
        assertNotNull(presenter.lastError,
                "Error message should be set when an exception occurs");
        assertTrue(presenter.lastError.contains("DAO exploded"),
                "Error message should include the underlying exception message");
    }

    // ------------------------------------------------------------------------
    // Helper: print timetable
    // ------------------------------------------------------------------------
    private void printTimetable(Timetable timetable) {
        System.out.println("=========== GENERATED TIMETABLE ===========");
        for (Section s : timetable.getSections()) {
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
        public List<CourseOffering> getSelectedCourseOfferings(Set<CourseCode> selectedCourses) {
            // Course 1: MAT237
            CourseOffering mat237 = new CourseOffering(
                    "MAT237Y1",
                    new CourseCode("MAT237Y1"),
                    "Pain and Agony",
                    "two semesters of it"
            );
            Section mat237Lec0101 = new Section(mat237, "LEC0101", Section.TeachingMethod.LECTURE);
            mat237Lec0101.addMeeting(new Meeting(
                    new UofTLocation("MY", "150"), FIRST_SEMESTER,
                    THURSDAY_13_15
            ));
            mat237.addAvailableSection(mat237Lec0101);

            // Course 2: CSC207
            CourseOffering csc207 = new CourseOffering(
                    "CSC207H1",
                    new CourseCode("CSC207H1"),
                    "Software Design",
                    "pain but with Java"
            );
            Section csc207Lec0101 = new Section(csc207, "LEC0101", Section.TeachingMethod.LECTURE);
            csc207Lec0101.addMeeting(new Meeting(
                    new UofTLocation("BA", "2175"), FIRST_SEMESTER,
                    MONDAY_10_11
            ));
            csc207.addAvailableSection(csc207Lec0101);

            return List.of(mat237, csc207);
        }
    }

    private static class FakeImpossibleAutogenDataAccess implements AutogenDataAccessInterface {

        @Override
        public List<CourseOffering> getSelectedCourseOfferings(Set<CourseCode> selectedCourses) {
            CourseOffering mat237 = new CourseOffering(
                    "MAT237Y1",
                    new CourseCode("MAT237Y1"),
                    "Pain and Agony",
                    "two semesters of it"
            );
            Section lec0101 = new Section(mat237, "LEC0101", Section.TeachingMethod.LECTURE);
            lec0101.addMeeting(new Meeting(
                    new UofTLocation("MY", "150"), FIRST_SEMESTER,
                    MONDAY_10_12
            ));
            mat237.addAvailableSection(lec0101);

            return List.of(mat237);
        }
    }

    private static class FakeAutogenDataAccessWithConflicts implements AutogenDataAccessInterface {

        @Override
        public List<CourseOffering> getSelectedCourseOfferings(Set<CourseCode> selectedCourses) {
            // ----- Course 1: MAT237 with two sections -----
            CourseOffering mat237 = new CourseOffering(
                    "MAT237Y1",
                    new CourseCode("MAT237Y1"),
                    "Pain and Agony",
                    "two semesters of it"
            );

            Section mat237A1 = new Section(
                    mat237,
                    "LEC0101",
                    Section.TeachingMethod.LECTURE
            );
            mat237A1.addMeeting(new Meeting(
                    new UofTLocation("MY", "150"), FIRST_SEMESTER,
                    MONDAY_10_12
            ));

            Section mat237A2 = new Section(
                    mat237,
                    "LEC0201",
                    Section.TeachingMethod.LECTURE
            );
            mat237A2.addMeeting(new Meeting(
                    new UofTLocation("MY", "150"), FIRST_SEMESTER,
                    MONDAY_14_16
            ));

            mat237.addAvailableSection(mat237A1);
            mat237.addAvailableSection(mat237A2);

            // ----- Course 2: CSC207 with two sections -----
            CourseOffering csc207 = new CourseOffering(
                    "CSC207H1",
                    new CourseCode("CSC207H1"),
                    "Software Design",
                    "pain but with Java"
            );

            Section csc207B1 = new Section(
                    csc207,
                    "LEC0101",
                    Section.TeachingMethod.LECTURE
            );
            csc207B1.addMeeting(new Meeting(
                    new UofTLocation("BA", "2175"), FIRST_SEMESTER,
                    MONDAY_10_12
            ));

            Section csc207B2 = new Section(
                    csc207,
                    "LEC0201",
                    Section.TeachingMethod.LECTURE
            );
            csc207B2.addMeeting(new Meeting(
                    new UofTLocation("BA", "2175"), FIRST_SEMESTER,
                    MONDAY_12_14
            ));

            csc207.addAvailableSection(csc207B1);
            csc207.addAvailableSection(csc207B2);

            return List.of(mat237, csc207);
        }
    }

    private static class FakeAutogenDataAccessDifferentSemesters implements AutogenDataAccessInterface {

        @Override
        public List<CourseOffering> getSelectedCourseOfferings(Set<CourseCode> selectedCourses) {
            CourseOffering mat237 = new CourseOffering(
                    "MAT237Y1",
                    new CourseCode("MAT237Y1"),
                    "Pain and Agony",
                    "two semesters of it"
            );

            Section mat237A1 = new Section(
                    mat237,
                    "LEC0101",
                    Section.TeachingMethod.LECTURE
            );
            mat237A1.addMeeting(new Meeting(
                    new UofTLocation("MY", "150"), FIRST_SEMESTER,
                    MONDAY_10_12
            ));

            mat237.addAvailableSection(mat237A1);

            CourseOffering csc207 = new CourseOffering(
                    "CSC207H1",
                    new CourseCode("CSC207H1"),
                    "Software Design",
                    "pain but with Java"
            );

            Section csc207B2 = new Section(
                    csc207,
                    "LEC0201",
                    Section.TeachingMethod.LECTURE
            );
            csc207B2.addMeeting(new Meeting(
                    new UofTLocation("BA", "2175"), SECOND_SEMESTER,
                    MONDAY_10_12
            ));

            csc207.addAvailableSection(csc207B2);

            return List.of(mat237, csc207);
        }
    }
}
