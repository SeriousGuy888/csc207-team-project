package use_case.ratemyprof;

/**
 * The RateMyProf Interactor
 */
public class RateMyProfInteractor implements RateMyProfInputBoundary {
    private final RateMyProfDataAccessInterface rateMyProfDataAccessObject;

    public RateMyProfInteractor(RateMyProfDataAccessInterface rateMyProfDataAccessObject) {
        this.rateMyProfDataAccessObject = rateMyProfDataAccessObject;
    }

    @Override
    public void execute(RateMyProfInputData profInputData) {
        rateMyProfDataAccessObject.getProfessorInfo(profInputData.getFirstName(), profInputData.getLastName());
    }
}
