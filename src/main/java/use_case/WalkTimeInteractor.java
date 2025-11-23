package use_case;

import java.io.IOException;

public class WalkTimeInteractor implements WalkTimeInputBoundary {

    private final WalkTimeDataAccessInterface dataAccessObject;
    private final WalkTimeOutputBoundary outputBoundary;

    public WalkTimeInteractor(WalkTimeDataAccessInterface dataAccessObject,
                              WalkTimeOutputBoundary outputBoundary) {
        this.dataAccessObject = dataAccessObject;
        this.outputBoundary = outputBoundary;
    }

    @Override
    public void execute(WalkTimeInputData input) {
        String origin = input.getOriginCode();
        String dest = input.getDestinationCode();

        try {
            // 1. Ask DAO for the time (Main Flow)
            int seconds = dataAccessObject.getWalkTime(origin, dest);

            if (seconds == -1) {
                // 2. Handle "Unknown Building" (Alternate Flow)
                outputBoundary.prepareFailView("Walk time unavailable: Unknown building.");
            } else {
                // 3. Success! Convert to minutes
                int minutes = Math.round((float) seconds / 60);

                // Create Output Data (Success flag = true)
                WalkTimeOutputData output = new WalkTimeOutputData(minutes, false);
                outputBoundary.prepareSuccessView(output);
            }

        } catch (IOException e) {
            // 4. Handle Network/API Errors (Alternate Flow)
            outputBoundary.prepareFailView("Walk time unavailable: API Error.");
        }
    }
}