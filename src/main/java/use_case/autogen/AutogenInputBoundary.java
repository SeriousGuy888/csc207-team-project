package use_case.autogen;

/**
 * Input boundary interface for the Autogen use case.
 * Defines the method required to initiate timetable generation.
 */
public interface AutogenInputBoundary {

    /**
     * Executes the Autogen use case using the provided input data.
     *
     * @param inputData the data needed to generate the timetable
     */
    void execute(AutogenInputData inputData);
}
