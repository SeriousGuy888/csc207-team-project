package data_access;

import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import entity.CourseOffering;

import data_access.course_data.CourseDataRepositoryGrouped;
import use_case.search_courses.SearchCoursesDataAccessInterface;

public class SearchCoursesDataAccessObject implements SearchCoursesDataAccessInterface {
    private final CourseDataRepositoryGrouped courseDataRepositoryGrouped;
    // use regex to identify if query is course code or dept code or neither
    private final Pattern FULLCOURSECODE = Pattern.compile("[A-Z]{3}[A-Z0-9]{3}[A-Z]{1}\\d{1}");
    private final Pattern FULLALTCOURSECODE = Pattern.compile("[A-Z]{4}[A-Z0-9]{3}[A-Z]{1}\\d{1}");
    private final Pattern COURSECODE = Pattern.compile("[A-Z]{3}\\d{3}");
    private final Pattern DEPTCODE = Pattern.compile("[A-Z]{3}");

    public SearchCoursesDataAccessObject(CourseDataRepositoryGrouped courseDataRepositoryGrouped) {
        this.courseDataRepositoryGrouped = courseDataRepositoryGrouped;
    }

    @Override
    public Set<CourseOffering> searchCourses(String query) throws IOException {
        String normalizedQuery = query.trim().toUpperCase();

        if (COURSECODE.matcher(normalizedQuery).matches()
                || FULLCOURSECODE.matcher(normalizedQuery).matches()
                || FULLALTCOURSECODE.matcher(normalizedQuery).matches()) {
            return searchByCourseCode(normalizedQuery);
        } else if (DEPTCODE.matcher(normalizedQuery).matches()) {
            return searchByDept(normalizedQuery);
        }
        throw new IOException("Valid Searches: 1) Department Code (e.g. CSC) 2) Course Code (e.g. CSC108) 3) Full Course Code (e.g. CSC108H1)");
    }

    private Set<CourseOffering> searchByCourseCode(String normalizedQuery) throws IOException {
        String deptCode = normalizedQuery.substring(0, 3);
        Map<String, CourseOffering> matches = courseDataRepositoryGrouped.getMatchingCourseInfo(deptCode);

        if (matches != null) {
            Set<CourseOffering> matchedCourses = new HashSet<>();
            for (Map.Entry<String, CourseOffering> entry : matches.entrySet()) {
                if (entry.getKey().startsWith(normalizedQuery)) {
                    matchedCourses.add(entry.getValue());
                }
            }
            return matchedCourses;
        }
        throw new IOException("This course code does not exist or is not offering any courses this term");
    }

    private Set<CourseOffering> searchByDept(String normalizedQuery) throws IOException {
        Map<String, CourseOffering> matches = courseDataRepositoryGrouped.getMatchingCourseInfo(normalizedQuery);

        if (matches != null) {
            List<CourseOffering> list = matches.values().stream()
                    .sorted(Comparator.comparing(course -> {
                        String code = course.getCourseCode().toString();
                        String numPart = code.substring(3, 6);

                        if (numPart.matches("\\d{3}")) {
                            int num = Integer.parseInt(numPart);
                            return String.format("%04d", num);
                        } else {
                            return "9999" + numPart;
                        }
                    }))
                    .collect(Collectors.toList());

            if (!list.isEmpty()) {
                // LinkedHashSet preserves insertion order, so insert in sorted order
                LinkedHashSet<CourseOffering> result = new LinkedHashSet<>();
                for (CourseOffering course : list) {
                    result.add(course);
                }
                return result;
            }
        }
        throw new IOException("This course code does not exist or is not offering any courses this term");
    }
}


