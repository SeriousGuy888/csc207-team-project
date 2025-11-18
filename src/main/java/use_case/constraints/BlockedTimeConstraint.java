package use_case.constraints;

import entity.Section;
import entity.WeeklyOccupancy;

import java.util.Set;

public class BlockedTimeConstraint implements Constraint {
    private final WeeklyOccupancy blockedTimes;

    public BlockedTimeConstraint(WeeklyOccupancy blockedTimes){
        this.blockedTimes = blockedTimes;
    }

    @Override
    public boolean isSatisfiedBy(Set<Section> chosen) {
        Section[] list = chosen.toArray(new Section[0]);
        for (int i = 0; i < list.length; i++) {
            if(list[i].getOccupiedTime().doesIntersect(blockedTimes)){
                return false;
            }
        }
        return true;
    }
}
