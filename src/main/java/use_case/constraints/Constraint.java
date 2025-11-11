package use_case.constraints;

import entity.Section;
import java.util.Set;

/**
 * A constraint defines whether a partial or complete selection of sections
 * is valid under some rule (e.g., no time conflicts, same campus, etc.).
 */
public interface Constraint {
    /**
     * @param chosen the set of currently chosen sections
     * @return true if this constraint is still satisfied given the current selection
     */
    boolean isSatisfiedBy(Set<Section> chosen);
}
