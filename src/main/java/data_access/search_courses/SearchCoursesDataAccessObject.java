package use_case.search_courses;

import java.util.HashSet;
import java.util.Set;
import java.util.Map;
import java.util.regex.Pattern;

import entity.CourseOffering;

import data_access.course_data.CourseDataRepositoryGrouped;
import java.io.IOException;

public class SearchCoursesDataAccessObject implements SearchCoursesDataAccessInterface {
    private final CourseDataRepositoryGrouped courseDataRepositoryGrouped;
     // use regex to identify if query is course code or dept code or neither
    private final Pattern COURSECODE = Pattern.compile("[A-Z]{3}\\d{3}");
    private final Pattern DEPTCODE = Pattern.compile("[A-Z]{3}");

    public SearchCoursesDataAccessObject(CourseDataRepositoryGrouped courseDataRepositoryGrouped) {  
        this.courseDataRepositoryGrouped = courseDataRepositoryGrouped; 
    }

    @Override
    public Set<CourseOffering> searchCourses(String query) {
        String normalizedQuery = query.trim().toUpperCase();

        if (COURSECODE.matcher(normalizedQuery).matches()) {
            return searchByCourseCode(normalizedQuery);
        }
        else if (DEPTCODE.matcher(normalizedQuery).matches()) {
            return searchByDept(normalizedQuery);
        } 
        throw new IOException("Please search by first three letters of the course (e.g., CSC) or specific course code (e.g., CSC207) only");        
    }

    private Set<CourseOffering> searchByCourseCode(String normalizedQuery) {
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

    private Set<CourseOffering> searchByDept(String normalizedQuery) {
        Map<String, CourseOffering> matches = courseDataRepositoryGrouped.getMatchingCourseInfo(normalizedQuery);

        if (matches != null) {
            // return only first 10 courses in that department 
            // todo: implement pagination later
            return new HashSet<>(matches.values()).subList(0, 11);
        } 
        throw new IOException("This course code does not exist or is not offering any courses this term");
    }
}
  

