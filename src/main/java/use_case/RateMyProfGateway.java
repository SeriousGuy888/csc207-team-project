package use_case;

/**
 * Interface for RateMyProf data access.
 */
public interface RateMyProfGateway {
    public String getProfessorInfo(String profName, String profLastName);
}