package use_case;

import entity.CourseCode;
import entity.CourseOffering;
import entity.Section;

import java.util.Collections;
import java.util.Map;

public class TestConstants {
    public static final String COURSE_IDENTIFIER_MAT137 = "MAT137Y1-F-20259";
    public static final String COURSE_IDENTIFIER_CSC207 = "CSC207H1-F-20259";
    public static final CourseOffering COURSE_OFFERING_MAT137 = new CourseOffering(
            COURSE_IDENTIFIER_MAT137,
            new CourseCode("MAT137Y1"),
            "Pain and Agony with Proofs",
            "yearlong course");
    public static final CourseOffering COURSE_OFFERING_CSC207 = new CourseOffering(
            COURSE_IDENTIFIER_CSC207,
            new CourseCode("CSC207Y1"),
            "Software Design",
            "clean architecture");

    public static final Section SECTION_MAT137_LEC0101 = new Section(
            COURSE_OFFERING_MAT137, "LEC0101", Section.TeachingMethod.LECTURE);
    public static final Section SECTION_MAT137_TUT0101 = new Section(
            COURSE_OFFERING_MAT137, "TUT0101", Section.TeachingMethod.TUTORIAL);

    static {
        COURSE_OFFERING_MAT137.addAvailableSection(SECTION_MAT137_LEC0101);
        COURSE_OFFERING_MAT137.addAvailableSection(SECTION_MAT137_TUT0101);
    }

    public static final Map<String, CourseOffering> AVAILABLE_COURSES = Map.of(
            COURSE_IDENTIFIER_MAT137, COURSE_OFFERING_MAT137,
            COURSE_IDENTIFIER_CSC207, COURSE_OFFERING_CSC207
    );
}
