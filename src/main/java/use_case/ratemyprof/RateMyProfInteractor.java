package use_case.ratemyprof;
import entity.Professor;
/**
 * The RateMyProf Interactor
 */
public class RateMyProfInteractor implements RateMyProfInputBoundary {
    private final RateMyProfDataAccessInterface rateMyProfDataAccessObject;
    private final RateMyProfOutputBoundary rateMyProfPresenter;

    public RateMyProfInteractor(RateMyProfDataAccessInterface rateMyProfDataAccessObject,
                                RateMyProfOutputBoundary rateMyProfPresenter) {
        this.rateMyProfDataAccessObject = rateMyProfDataAccessObject;
        this.rateMyProfPresenter = rateMyProfPresenter;
    }

    @Override
    public void execute(RateMyProfInputData profInputData) {
        try {
            Professor prof = rateMyProfDataAccessObject.getProfessorInfo(
                    profInputData.getFirstName(),
                    profInputData.getLastName());
            RateMyProfOutputData outputData = new RateMyProfOutputData(prof);
            rateMyProfPresenter.prepareSuccessView(outputData);
        }
        catch (RuntimeException e) {
            String errorMessage = "Failed to retrieve professor info: " + e.getMessage();
            rateMyProfPresenter.prepareFailView(errorMessage);
        }
    }

    @Override
    public RateMyProfOutputData executeSynchronous(RateMyProfInputData profInputData) {
        try {
            Professor prof = rateMyProfDataAccessObject.getProfessorInfo(
                    profInputData.getFirstName(),
                    profInputData.getLastName());

            // Success: Return the output data directly
            return new RateMyProfOutputData(prof);

        } catch (RuntimeException e) {
            // Failure: Return an OutputData DTO based on the empty/default Professor
            // This is the clean way to handle failure for inter-use-case communication
            return new RateMyProfOutputData(Professor.emptyProfessor());
        }
    }
}
