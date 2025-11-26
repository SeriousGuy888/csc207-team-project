package use_case.search_courses;

import data_access.course_data.CourseDataRepository;

public class SearchCoursesDataAccessObject implements SearchCoursesDataAccessInterface {
    private final CourseDataRepository courseDataRepository;

    // pass in shared constructor parameter
    public SearchCoursesDataAccessObject(CourseDataRepository courseDataRepository) {  
        this.courseDataRepository = courseDataRepository; 
    }

    @Override
    public List<CourseOffering> searchCourses(String query) {
        Set<String> queryCourseIds = prepareQuery(query);
    }

    private Set<String> prepareQuery(String query) {
        String normalized = query.trim().toUpperCase();

        Set<String> allCourseIds = courseDataRepository.getAllCourseOfferingIdentifiers();

        List<String> matching = new ArrayList<>();
        for (String id : identifiers) {
            if (id.toUpperCase().contains(normalized)) {
                matching.add(id);
            }
        }
        
    }
 
    
    // 3. Sort by match quality
    matching.sort((a, b) -> {
        boolean aStarts = a.toUpperCase().startsWith(normalized);
        boolean bStarts = b.toUpperCase().startsWith(normalized);
        
        if (aStarts && !bStarts) return -1;
        if (bStarts && !aStarts) return 1;
        return a.compareTo(b);
    });
    
    // 4. Get top 10 CourseOfferings
    List<CourseOffering> results = new ArrayList<>();
    for (int i = 0; i < Math.min(10, matching.size()); i++) {
        CourseOffering course = courseRepository.getCourseOffering(matching.get(i));
        if (course != null) {
            results.add(course);
        }
    }
    
    return results;
}
}