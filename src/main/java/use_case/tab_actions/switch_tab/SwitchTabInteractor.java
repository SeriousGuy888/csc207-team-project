package use_case.tab_actions.switch_tab;

public class SwitchTabInteractor implements SwitchTabInputBoundary {
    private final SwitchTabOutputBoundary presenter;

    public SwitchTabInteractor(SwitchTabOutputBoundary presenter) {
        this.presenter = presenter;
    }

    @Override
    public void execute(int newIndex) {
        presenter.prepareSuccessView(newIndex);
    }
}
