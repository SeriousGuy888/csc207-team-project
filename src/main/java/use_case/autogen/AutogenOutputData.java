package use_case.autogen;

import entity.Section;
import java.util.Set;


public class AutogenOutputData {
    private final  Set<Section> generatedSections;
    public AutogenOutputData(Set<Section> generatedSections){this.generatedSections = generatedSections;}
    public Set<Section> getGeneratedTimetable(){return generatedSections;}
}
