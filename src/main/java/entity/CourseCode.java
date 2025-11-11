package entity;

import java.util.Arrays;
import java.util.Optional;

/**
 * Represents course codes for easy equivalence checking.
 */
public class CourseCode {
    // Based on specification at https://artsci.calendar.utoronto.ca/understanding-courses

    /**
     * First three uppercase letters of the course code,
     * indicating the program/subject area of the course.
     */
    private final String designator;

    /**
     * The three numbers in the course code indicating
     * exactly which course in this subject area this is.
     */
    private final String number;

    /**
     * Is this course worth 0.5 credits or 1.0 credits?
     * <p>
     * (Doesn't always mean this. Attested by SLC099Y1, for example,
     * a zero credit course, which uses "Y" to indicate yearlongness.)
     */
    private final CourseCodeWeight weight;

    /**
     * Which campus this course is taught at.
     */
    private final CourseCodeCampus campus;

    /**
     * @param designator must be three letters.
     * @param number     must be three numerals.
     * @param weight     the weight of this course (H/Y)
     * @param campus     the campus of this course
     */
    public CourseCode(String designator, String number, CourseCodeWeight weight, CourseCodeCampus campus) {
        this.designator = designator.toUpperCase();
        this.number = number;
        this.weight = weight;
        this.campus = campus;
    }

    /**
     * @param courseCodeString Eight character course code in the format MAT137Y1.
     */
    public CourseCode(String courseCodeString) {
        if (courseCodeString.length() != 8) {
            throw new IllegalArgumentException("String provided must be exactly 8 characters long.");
        }

        designator = courseCodeString.substring(0, 3).toUpperCase();
        number = courseCodeString.substring(3, 6);

        char weightChar = courseCodeString.toUpperCase().charAt(6);
        if (weightChar == CourseCodeWeight.FULL_CREDIT.getLetter()) {
            weight = CourseCodeWeight.FULL_CREDIT;
        } else if (weightChar == CourseCodeWeight.HALF_CREDIT.getLetter()) {
            weight = CourseCodeWeight.HALF_CREDIT;
        } else {
            throw new IllegalArgumentException("Invalid weight indicator specified.");
        }

        char campusChar = courseCodeString.toUpperCase().charAt(7);
        Optional<CourseCodeCampus> matchingCampus = Arrays
                .stream(CourseCodeCampus.values())
                .filter(campus -> campus.getNumber() == campusChar)
                .findFirst();
        if (matchingCampus.isEmpty()) {
            throw new IllegalArgumentException("Invalid campus indicator specified.");
        }
        campus = matchingCampus.get();
    }

    /**
     * @return The first six characters of a course code, e.g. MAT137.
     */
    public String getAbbreviatedCourseCode() {
        return designator + number;
    }

    @Override
    public String toString() {
        return designator + number + weight.getLetter() + campus.getNumber();
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof CourseCode)) {
            return false;
        }

        CourseCode otherCourseCode = (CourseCode) other;

        return (otherCourseCode.designator.equals(this.designator) &&
                otherCourseCode.number.equals(this.number) &&
                otherCourseCode.weight == this.weight &&
                otherCourseCode.campus == this.campus
        );
    }

    public enum CourseCodeWeight {
        HALF_CREDIT('H'),
        FULL_CREDIT('Y');

        private final char letter;

        CourseCodeWeight(char letter) {
            this.letter = letter;
        }

        public char getLetter() {
            return letter;
        }
    }

    public enum CourseCodeCampus {
        OFF_CAMPUS('0'),
        SAINT_GEORGE('1'),
        SCARBOROUGH('3'),
        MISSISSAUGA('5');

        private final char number;

        CourseCodeCampus(char number) {
            this.number = number;
        }

        public char getNumber() {
            return number;
        }
    }
}
