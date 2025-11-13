package use_case.autogen;

import use_case.constraints.Constraint;
import entity.Section;
import java.util.*;

public class DfsBacktracker {
    private List<Variable> variables;
    private List<Constraint> constraints;

    public DfsBacktracker(List<Variable> variables, List<Constraint> constraints) {
        this.variables = variables;
        this.constraints = constraints;
    }

}
