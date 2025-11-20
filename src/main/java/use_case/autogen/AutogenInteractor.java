package use_case.autogen;

import entity.CourseVariable;
import entity.constraints.Constraint;
import entity.Section;
import java.util.*;
import use_case.autogen.PotentialTimetable;
import entity.Timetable;

/**A DFS (Depth First Search) timetable generator that uses recursion + backtracking
 * to find a suitable path of generation  **/
public class AutogenInteractor implements AutogenInputBoundary {

    private final AutogenDataAccessInterface dataAccess;
    private final AutogenOutputBoundary presenter;

    public AutogenInteractor(AutogenDataAccessInterface dataAccess,
                             AutogenOutputBoundary presenter) {
        this.dataAccess = dataAccess;
        this.presenter = presenter;
    }

    @Override
    public void execute(AutogenInputData inputData) {
        try{
            List<CourseVariable> variables = dataAccess.getVariablesFor(inputData);
            List<Constraint> constraints = dataAccess.getConstraintsFor(inputData);

            PotentialTimetable result = generateTimetable(variables, constraints);

            if (result.getSuccess()){
                AutogenOutputData outputData = new AutogenOutputData(result.getTimetable());
                presenter.prepareSuccessView(outputData);
            }
            else{
                presenter.prepareFailView("No valid timetable could be generated " +
                        "with the given courses and constraints.");
            }




        } catch (Exception e) {
            presenter.prepareFailView("An error occurred while generating the timetable: "
                    + e.getMessage());
        }

    }

    /**
     *
     * @return the search result containing the generated timetable (hopefully)
     * If it's a fail it means no possible combinations found (atleast using DFS given current constraints)
     */
    public PotentialTimetable generateTimetable(List<CourseVariable> variables, List<Constraint> constraints) {
        Set<Section> initial = new HashSet<>();
        return timetableSearch(0, variables, constraints, initial);
    }

    /**
     *
     * @param index The current index number in the variables set that's having its section assigned
     * @param assignment The assignment of sections (generated timetable) so far
     * @param variables All course variables for the DFS (one per course)
     * @param constraints The constraints that need to be satisfied
     * @return A PotentialTimetable, if true contains a sucesfully generated timetable,
     * else false, and it backtracks and tries a different path
     */
    private PotentialTimetable timetableSearch(int index, List<CourseVariable> variables, List<Constraint>
            constraints, Set<Section> assignment) {
        // This is the base case, that all variables have succesfully been assigned,
        // and we have a fully generated timetable!
        if(index == variables.size()){
            return new PotentialTimetable(true, assignment);
        }
        //Choses the variable we will try finding a suitable lecture section for
        CourseVariable var = variables.get(index);
        for(Section candidate : var.getDomain()){ //Iterates through all possible candidates in variable's domain
            //tries a possible assignment by adding a candidate to it
            Set<Section> next = new HashSet<>();
            next.add(candidate);

            //checks if this new temporary assignment is valid
            if (isConsistent(next, constraints)){
                //if valid it checks if the rest of this path works (recursive step)
                PotentialTimetable result = timetableSearch(index+1, variables, constraints, next);
                //Valid path found then it succesfully returns this result so far
                if (result.success){
                    return result;
                }
            }
        }
        return new PotentialTimetable(false, Set.of()); //if no valid path found then returns false here so new-
        //-path can be tried in the earlier recursive call

    }


    /** Checks if constraints are met, using the classes in the constraints package **/
    /**
     *
     * @param assignment The timetable assinment that is being checked for consistency
     * @param constraints The constraints that need to be satisfied
     * @return true if all constraints met, else false
     */
    private boolean isConsistent(Set<Section> assignment,  List<Constraint> constraints) {
        for(Constraint c : constraints){
            //Iterates through all constraints, if a single one is not met function returns false
            if(!c.isSatisfiedBy(assignment)){
                return false;
            }
        }
        //Returns true by default if all constraints met
        return true;
    }
}
