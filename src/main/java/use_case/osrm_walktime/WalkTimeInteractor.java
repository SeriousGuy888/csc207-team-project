package use_case.osrm_walktime;

import use_case.osrm_walktime.WalkTimeDataAccessInterface;
import use_case.osrm_walktime.WalkTimeDataAccessInterface.ApiConnectionException;
import use_case.osrm_walktime.WalkTimeDataAccessInterface.InvalidLocationException;

public class WalkTimeInteractor implements WalkTimeInputBoundary {

    // Dependencies
    private final WalkTimeDataAccessInterface dataAccessObject;
    private final WalkTimeOutputBoundary presenter;

    public WalkTimeInteractor(WalkTimeDataAccessInterface dataAccessObject,
                              WalkTimeOutputBoundary presenter) {
        this.dataAccessObject = dataAccessObject;
        this.presenter = presenter;
    }

    @Override
    public void execute(WalkTimeInputData inputData) {
        String start = inputData.getStartCode();
        String end = inputData.getEndCode();

        try {
            int duration = dataAccessObject.getWalkTimeInSeconds(start, end);

            WalkTimeOutputData outputData = new WalkTimeOutputData(duration, false);
            presenter.prepareSuccessView(outputData);

        } catch (InvalidLocationException e) {
            presenter.prepareFailView("Invalid Building: " + e.getMessage());

        } catch (ApiConnectionException e) {
            presenter.prepareFailView("Network Error: " + e.getMessage());
        }
    }
}