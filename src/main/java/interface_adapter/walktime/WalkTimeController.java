package interface_adapter.walktime;

import entity.UofTLocation;
import use_case.WalkTimeInputBoundary;
import use_case.WalkTimeInputData;

public class WalkTimeController {

    final WalkTimeInputBoundary walkTimeUseCaseInteractor;

    public WalkTimeController(WalkTimeInputBoundary walkTimeUseCaseInteractor) {
        this.walkTimeUseCaseInteractor = walkTimeUseCaseInteractor;
    }

    /**
     * Triggers the walk time calculation between two UofT Locations.
     */
    public void execute(UofTLocation location1, UofTLocation location2) {
        // 1. Extract the Building Codes from your Entity
        // This is safe because your Entity guarantees these fields exist.
        String code1 = location1.getBuildingCode();
        String code2 = location2.getBuildingCode();

        // 2. Create the Input Data (which expects Strings)
        WalkTimeInputData inputData = new WalkTimeInputData(code1, code2);

        // 3. Execute the Use Case
        walkTimeUseCaseInteractor.execute(inputData);
    }
}