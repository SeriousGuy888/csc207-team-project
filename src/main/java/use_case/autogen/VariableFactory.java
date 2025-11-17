// src/main/java/use_case/autogen/VariableFactory.java  (or inside an interactor)
package use_case.autogen;

import entity.CourseOffering;
import entity.Section;

import java.util.*;

public class VariableFactory {

    /**
     * @param offerings   the courses the user wants in their timetable
     * @param lockedSections sections the user has explicitly locked/pinned
     */
    public static List<Variable> buildVariables(List<CourseOffering> offerings,
                                                Set<Section> lockedSections) {
        List<Variable> variables = new ArrayList<>();

        for (CourseOffering offering : offerings) {
            Set<Section> allSections = offering.getAvailableSections();

            // find if any of the locked sections belongs to this offering
            Section lockedForThisCourse = null;
            for (Section s : lockedSections) {
                if (s.getCourseOffering().equals(offering)) {
                    lockedForThisCourse = s;
                    break;
                }
            }

            Set<Section> domain;
            if (lockedForThisCourse != null) {
                // locked: only this section is allowed
                domain = Set.of(lockedForThisCourse);
            } else {
                // no lock: all sections are allowed
                domain = allSections;
            }

            variables.add(new Variable(offering, domain));
        }

        return variables;
    }
}
