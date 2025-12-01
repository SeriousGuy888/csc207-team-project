package use_case.osrm_walktime;

public interface WalkTimeService {
    /**
     * Safe method to get walking time.
     * Does not throw exceptions; returns -1 if something goes wrong.
     *
     * @param startCode Origin building code (e.g. "BA")
     * @param endCode   Destination building code (e.g. "SS")
     * @return Duration in seconds, or -1 if invalid/error.
     */
    int calculateWalkingTime(String startCode, String endCode);
}
