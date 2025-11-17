package use_case.autogen;


import entity.Section;
import java.util.HashSet;
import java.util.Set;

/** Current partial selection of sections during search. */
public class Assignment {
    private final Set<Section> chosen =  new HashSet<>();
    /**
     * param chosen The set of lecture/tutorial/prac sections that have been assigned to the timetable generated so far
     */

    public Assignment(){}

    public Assignment(Assignment assignment){
        this.chosen.addAll(assignment.chosen);
    }

    /** Basic setters and getter **/
    public void add(Section section){chosen.add(section);}
    public void remove(Section section){chosen.remove(section);}
    public Set<Section> getChosen() { return chosen; }

}
