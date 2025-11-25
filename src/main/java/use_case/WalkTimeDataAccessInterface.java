package use_case;

import java.io.IOException;

public interface WalkTimeDataAccessInterface {

    int getWalkTime(String originCode, String destinationCode) throws IOException;

    /**
     * Gets the walking time estimate between two UofT buildings.
     * @param originCode The code of the starting building (e.g. "BA")
     * @param destinationCode The code of the destination building (e.g. "SS")
     * @return The walk time in seconds. Returns -1 if the building code is not found.
     * @throws IOException If the external API call fails (network error).
     */
    int getWalkTimeInSeconds(String originCode, String destinationCode)
            throws IOException;
}
