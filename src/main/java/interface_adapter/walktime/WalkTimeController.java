package interface_adapter.walktime;

import entity.UofTLocation;
import use_case.osrm_walktime.WalkTimeInputBoundary;
import use_case.osrm_walktime.WalkTimeInputData;

public class WalkTimeController {

    final WalkTimeInputBoundary walkTimeUseCaseInteractor;

    public WalkTimeController(WalkTimeInputBoundary walkTimeUseCaseInteractor) {
        this.walkTimeUseCaseInteractor = walkTimeUseCaseInteractor;
    }

    /**
     * Triggers the walk time calculation between two UofT Locations.
     */
    public void execute(UofTLocation location1, UofTLocation location2) {
        String code1 = location1.getBuildingCode();
        String code2 = location2.getBuildingCode();

        WalkTimeInputData inputData = new WalkTimeInputData(code1, code2);

        walkTimeUseCaseInteractor.execute(inputData);
    }
}