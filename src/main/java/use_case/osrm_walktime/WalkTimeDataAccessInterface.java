package use_case.osrm_walktime;

public interface WalkTimeDataAccessInterface {
    int getWalkTimeInSeconds(String originCode, String destinationCode)
            throws InvalidLocationException, ApiConnectionException;

    class InvalidLocationException extends Exception {
        public InvalidLocationException(String code) { super("Unknown Building: " + code); }
    }

    class ApiConnectionException extends Exception {
        public ApiConnectionException(String msg) { super(msg); }
    }
}