package use_case;

import entity.Professor;

/**
 * Interface for RateMyProf data access.
 */
public interface RateMyProfGateway {
    public Professor getProfessorInfo(String profName, String profLastName);
}