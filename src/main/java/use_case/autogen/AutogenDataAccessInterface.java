package use_case.autogen;
import entity.CourseVariable;
import  java.util.List;
import entity.constraints.Constraint;
import java.util.Set;

public interface AutogenDataAccessInterface {
    List<CourseVariable> getVariablesFor(AutogenInputData inputData);
    List<Constraint> getConstraintsFor(AutogenInputData inputData);
}
