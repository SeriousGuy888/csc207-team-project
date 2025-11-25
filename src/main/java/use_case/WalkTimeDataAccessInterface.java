package use_case;

import data_access.UofTWalkTimeDAO;

import java.io.IOException;

public interface WalkTimeDataAccessInterface {

    int getWalkTimeInSeconds(String originCode, String destinationCode) throws IOException,
            UofTWalkTimeDAO.UnknownBuildingException;
}

