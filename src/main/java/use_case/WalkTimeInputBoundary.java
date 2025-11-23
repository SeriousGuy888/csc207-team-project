package use_case;

public interface WalkTimeInputBoundary {
    /**
     * Executes the walk time calculation logic.
     * @param walkTimeInputData The data packet containing origin and destination codes.
     */
    void execute(WalkTimeInputData walkTimeInputData);
}
