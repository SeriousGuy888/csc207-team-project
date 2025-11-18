package use_case.autogen;

import use_case.constraints.Constraint;
import entity.Section;
import java.util.*;

/**A DFS (Depth First Search) timetable generator that uses recursion + backtracking
 * to find a suitable path of generation  **/
public class DfsBacktracker {

    private final List<Variable> variables;
    private final List<Constraint> constraints;
    /**
     * @param variables The List of variables (each containing a course, and it's domain) that need to be
     *                  generated in the timetable
     * @param constraints The list of constraints that need to be met for this timetable generation
     */
    public DfsBacktracker(List<Variable> variables, List<Constraint> constraints) {
        this.variables = variables;
        this.constraints = constraints;
    }

    /**
     *
     * @return the search result containing the generated timetable (hopefully)
     * If it's a fail it means no possible combinations found (atleast using DFS given current constraints)
     */
    public PotentialTimetable search(){
        Set<Section> initial = new HashSet<>();
        //Assignment initial = new Assignment();
        return dfs(0, initial);
    }

    /**
     *
     * @param index The current index number in the variables set that's having its section assigned
     * @param assignment The assignment (generated timetable) so far
     * @return A SearchResult, if true contains a sucesfully generated timetable,
     * else false, and it backtracks and tries a different path
     */
    private PotentialTimetable dfs(int index, Set<Section> assignment){
        // This is the base case, that all variables have succesfully been assigned,
        // and we have a fully generated timetable!
        if(index == variables.size()){
            return new PotentialTimetable(true, assignment);
        }

        //Choses the variable we will try finding a suitable lecture section for
        Variable var = variables.get(index);
        for(Section candidate : var.getDomain()){ //Iterates through all possible candidates in variable's domain
            //tries a possible assignment by adding a candidate to it
            Set<Section> next = new HashSet<>();
            next.add(candidate);

            //checks if this new temporary assignment is valid
            if (isConsistent(next)){
                //if valid it checks if the rest of this path works (recursive step)
                PotentialTimetable result = dfs(index+1, next);
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
     * @return true if all constraints met, else false
     */
    private boolean isConsistent(Set<Section> assignment){
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
