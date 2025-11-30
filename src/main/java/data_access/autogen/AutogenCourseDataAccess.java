package data_access.autogen;

import data_access.course_data.CourseDataRepositoryGrouped;
import entity.CourseCode;
import entity.CourseOffering;
import use_case.autogen.AutogenDataAccessInterface;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AutogenCourseDataAccess implements AutogenDataAccessInterface {

    private final CourseDataRepositoryGrouped courseDataRepositoryGrouped;

    public AutogenCourseDataAccess(CourseDataRepositoryGrouped courseDataRepositoryGrouped) {
        this.courseDataRepositoryGrouped = courseDataRepositoryGrouped;
    }

    @Override
    public List<CourseOffering> getSelectedCourseOfferings(Set<CourseCode> selectedCourses) {
        List<CourseOffering> result = new ArrayList<>();

        for (CourseCode courseCode : selectedCourses) {
            // 🚨 If your CourseCode has a getter like getCode(), use that instead of toString().
            String normalized = courseCode.toString().trim().toUpperCase();

            // Skip obviously invalid codes
            if (normalized.length() < 3) {
                continue;
            }

            // First 3 letters = dept code, same as in JsonCourseDataRepository keys (CSC, MAT, etc.)
            String deptCode = normalized.substring(0, 3);

            Map<String, CourseOffering> matches =
                    courseDataRepositoryGrouped.getMatchingCourseInfo(deptCode);

            if (matches == null) {
                // No courses for this dept in the JSON; skip or throw depending on your design.
                continue;
            }

            // Same idea as SearchCoursesDataAccessObject.searchByCourseCode:
            // look at all offerings in that dept and keep those whose key starts with the course code.
            for (Map.Entry<String, CourseOffering> entry : matches.entrySet()) {
                if (entry.getKey().toUpperCase().startsWith(normalized)) {
                    result.add(entry.getValue());
                }
            }
        }

        return result;
    }
}
