package use_case.ratemyprof;

import entity.Professor;

/**
 * Interface for RateMyProf data access.
 */
public interface RateMyProfDataAccessInterface {
    /**
     * Retrieves information about a professor based on their first and last name.
     *
     * @param profFirstName the professor's first name
     * @param profLastName the professor's last name
     * @return the {@link Professor} object containing the professor's information,
     *         or {@code null} if no matching professor is found
     */
    Professor getProfessorInfo(String profFirstName, String profLastName);
}
