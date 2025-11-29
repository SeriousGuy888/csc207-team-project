package use_case.osrm_walktime;

public interface RouteFetcher {
    // Define a custom exception so your Use Case doesn't depend on "IOException"
    class RouteFetchException extends Exception {
        public RouteFetchException(String message) {
            super(message);
        }
    }

    /**
     * Returns the walking duration in seconds between two points.
     * @param startLon Longitude of start point
     * @param startLat Latitude of start point
     * @param endLon Longitude of end point
     * @param endLat Latitude of end point
     * @return Duration in seconds
     */
    double getRouteDuration(double startLon, double startLat, double endLon, double endLat) throws RouteFetchException;
}
