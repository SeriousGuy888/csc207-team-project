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
            String normalized = courseCode.toString().trim().toUpperCase();

            // Skip obviously invalid codes
            if (normalized.length() < 3) {
                continue;
            }

            String deptCode = normalized.substring(0, 3);

            Map<String, CourseOffering> matches =
                    courseDataRepositoryGrouped.getMatchingCourseInfo(deptCode);

            if (matches == null) {
                continue;
            }


            for (Map.Entry<String, CourseOffering> entry : matches.entrySet()) {
                if (entry.getKey().toUpperCase().startsWith(normalized)) {
                    result.add(entry.getValue());
                }
            }
        }

        return result;
    }
}
