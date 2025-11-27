package use_case.ratemyprof;
import entity.Professor;
/**
 * The RateMyProf Interactor
 */
public class RateMyProfInteractor implements RateMyProfInputBoundary {
    private final RateMyProfDataAccessInterface rateMyProfDataAccessObject;
    private final RateMyProfOutputBoundary rateMyProfOutputBoundary;

    public RateMyProfInteractor(RateMyProfDataAccessInterface rateMyProfDataAccessObject,
                                RateMyProfOutputBoundary rateMyProfOutputBoundary) {
        this.rateMyProfDataAccessObject = rateMyProfDataAccessObject;
        this.rateMyProfOutputBoundary = rateMyProfOutputBoundary;
    }

    @Override
    public void execute(RateMyProfInputData profInputData) {
        try {
            Professor prof = rateMyProfDataAccessObject.getProfessorInfo(
                    profInputData.getFirstName(),
                    profInputData.getLastName());
            RateMyProfOutputData outputData = new RateMyProfOutputData(prof);
            rateMyProfOutputBoundary.prepareSuccessView(outputData);
        }
        catch (RuntimeException e) {
            String errorMessage = "Failed to retrieve professor info: " + e.getMessage();
            rateMyProfOutputBoundary.prepareFailView(errorMessage);
        }
    }
}
