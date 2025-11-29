package use_case.add_section;

public class AddSectionInteractor implements AddSectionInputBoundary {
    private final AddSectionDataAccessInterface dataAccess;

    public AddSectionInteractor(AddSectionDataAccessInterface dataAccess) {
        this.dataAccess = dataAccess;
    }

    @Override
    public void execute(AddSectionInputData inputData) {
        try {
            dataAccess.addSectionToSchedule(inputData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}