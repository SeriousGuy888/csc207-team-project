package use_case.constraints;


import entity.Section;
import java.util.Set;


public class TimeConflictConstraint implements Constraint {
    /**
     *
     * @param chosen the set of currently chosen sections
     * @return true if no time intersections between all chosen sesctions else false
     */
    @Override
    public boolean isSatisfiedBy(Set<Section> chosen) {
        Section[] list = chosen.toArray(new Section[0]);
        for (int i = 0; i < list.length; i++) {
            for (int j = i + 1; j < list.length; j++) {
                if (list[i].getOccupiedTime().doesIntersect(list[j].getOccupiedTime())) {
                    return false;
                }
            }
        }
        return true;
    }
}
